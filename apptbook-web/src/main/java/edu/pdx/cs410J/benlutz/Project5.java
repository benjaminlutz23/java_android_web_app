package edu.pdx.cs410J.benlutz;

import edu.pdx.cs410J.ParserException;

import java.io.*;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.Objects;

/**
 * The main class that parses the command line and communicates with the
 * Appointment Book server using REST.
 */
public class Project5 {

    public static final String MISSING_ARGS = "Error: No command line arguments";
    private static final DateTimeFormatter DATE_TIME_FORMAT = new DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .appendPattern("M/d/yyyy h:mm a VV")
            .toFormatter();

    public static void main(String... args) {
        if (args.length == 0) {
            usage(MISSING_ARGS);
            return;
        }

        if (args.length > 16) {
            System.err.println("Too many command line arguments");
            return;
        }


        boolean printFlag = false;
        boolean searchFlag = false;
        boolean invalidOptionFlag = false;
        String hostName = null;
        String portString = null;
        String owner = null;
        String description = null;
        String beginDate = null;
        String endDate = null;
        String beginTime = null;
        String endTime = null;
        String beginZoneID = null;
        String endZoneID = null;
        int argCounter = 0;

        ZonedDateTime beginDateTime = null;
        ZonedDateTime endDateTime = null;


        // Command line parsing logic
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.startsWith("-")) {
                switch (arg) {
                    case "-print":
                        printFlag = true;
                        break;
                    case "-search":
                        searchFlag = true;
                        break;
                    case "-README":
                        printReadme();
                        return;
                    case "-host":
                        // Ensure there is another argument after "-host"
                        if (i + 1 < args.length) {
                            hostName = args[i + 1];
                            i++; // Skip the next argument since it's used as the fileName
                        } else {
                            // Handle the case where "-textFile" is the last argument without a following file name
                            System.err.println("Error: -host option requires an argument");
                            return;
                        }
                        break;
                    case "-port":
                        // Ensure there is another argument after "-port"
                        if (i + 1 < args.length) {
                            portString = args[i + 1];
                            i++; // Skip the next argument since it's used as the string
                        } else {
                            // Handle the case where "-xmlFile" is the last argument without a following file name
                            System.err.println("Error: -port option requires an argument");
                            return;
                        }
                        break;
                    default:
                        invalidOptionFlag = true;
                }
            } else {
                switch (argCounter) {
                    case 0:
                        owner = arg;
                        break;
                    case 1:
                        description = arg;
                        break;
                    case 2:
                        beginDate = arg;
                        break;
                    case 3:
                        beginTime = args[i] + " " + args[i+1];
                        i++;
                        break;
                    case 4:
                        beginZoneID = arg;
                        break;
                    case 5:
                        endDate = arg;
                        break;
                    case 6:
                        endTime = args[i] + " " + args[i+1];
                        i++;
                        break;
                    case 7:
                        endZoneID = arg;
                        break;
                    default:
                        System.err.println("Too many arguments.");
                }
                argCounter++;
            }
        }

        // Check for invalid options
        if (invalidOptionFlag) {
            System.err.println("Error: Invalid command line option");
            return;
        }

        // Check for missing end time
        if (!searchFlag && (endDate == null || endTime == null)) {
            System.err.println("Error: Missing end time");
        }


        if (hostName == null) {
            usage( MISSING_ARGS );
            return;

        } else if ( portString == null) {
            usage( "Missing port" );
            return;
        }

        int port;
        try {
            port = Integer.parseInt( portString );

        } catch (NumberFormatException ex) {
            usage("Port \"" + portString + "\" must be an integer");
            return;
        }

        // Parse the begin time into ZonedDateTime
        boolean nonNullDateTime = beginDate != null && endDate != null && beginTime != null && endTime != null && beginZoneID != null && endZoneID != null;

        if (!searchFlag || (searchFlag && nonNullDateTime)) {
            try {
                beginDateTime = ZonedDateTime.parse(beginDate + " " + beginTime + " " + beginZoneID, DATE_TIME_FORMAT);
            } catch (DateTimeParseException e) {
                System.err.println("Invalid begin date/time format: " + e.getMessage());
                return;
            }

            // Parse the end time into ZonedDateTime
            try {
                endDateTime = ZonedDateTime.parse(endDate + " " + endTime + " " + endZoneID, DATE_TIME_FORMAT);
            } catch (DateTimeParseException e) {
                System.err.println("Invalid end date/time format: " + e.getMessage());
                return;
            }

            // Check if the begin time is after the end time
            if (beginDateTime.isAfter(endDateTime)) {
                System.err.println("Error: The begin time must be before the end time.");
                return; // Exit the program
            }
        }

        AppointmentBookRestClient client = new AppointmentBookRestClient(hostName, port);

        String message = "";
        try {
            if (searchFlag) {
                // Search for appointments between two times
                AppointmentBook book = client.getAppointmentsBetween(owner, beginDateTime, endDateTime);
                AppointmentBook filteredBook = new AppointmentBook(owner);

                // Pretty print the result
                PrintWriter writer = null;
                writer = new PrintWriter(System.out, true);
                PrettyPrinter prettyPrinter = new PrettyPrinter(writer);

                if (nonNullDateTime) {
                    for (Appointment appointment : book.getAppointments()) {
                        // Check if the entire appointment is between the begin and end times
                        if (!appointment.getBeginTime().isBefore(beginDateTime) && !appointment.getEndTime().isAfter(endDateTime)) {
                            filteredBook.addAppointment(appointment);
                        }
                    }
                    prettyPrinter.dump(filteredBook);
                }
                else {
                    prettyPrinter.dump(book);
                }
            } else {
                // Add an appointment or other actions
                if (description != null) {
                    Appointment appointment = new Appointment(description, beginDateTime, endDateTime);
                    client.addAppointment(owner, appointment);
                    if (printFlag) {
                        // Print the appointment details (implement as needed)
                        System.out.println(appointment);
                    }
                } else {
                    System.err.println("Error: Missing required arguments for adding an appointment");
                    return;
                }
            }
        } catch (IOException | ParserException e) {
            System.err.println("Error communicating with server: " + e.getMessage());
            return;
        } catch (invalidDescriptionException | invalidOwnerException e) {
            throw new RuntimeException(e);
        }
        System.out.println(message);
    }

    /**
     * Prints the README information to the standard output
     * <p>
     * This method reads the README information from a text file and prints it
     */
    private static void printReadme() {
        try (InputStream readmeStream = Project5.class.getResourceAsStream("README.txt")) {
            assert readmeStream != null;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(readmeStream))) {

                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }

            }
        } catch (IOException e) {
            System.err.println("Error reading README file: " + e.getMessage());
        }
    }

    private static void error( String message )
    {
        PrintStream err = System.err;
        err.println("** " + message);
    }

    /**
     * Prints usage information for this program and exits
     * @param message An error message to print
     */
    private static void usage( String message )
    {
        PrintStream err = System.err;
        err.println("** " + message);
        err.println();
        err.println("usage: java Project5 host port [word] [definition]");
        err.println("  host         Host of web server");
        err.println("  port         Port of web server");
        err.println("  word         Word in dictionary");
        err.println("  definition   Definition of word");
        err.println();
        err.println("This simple program posts words and their definitions");
        err.println("to the server.");
        err.println("If no definition is specified, then the word's definition");
        err.println("is printed.");
        err.println("If no word is specified, all dictionary entries are printed");
        err.println();
    }
}