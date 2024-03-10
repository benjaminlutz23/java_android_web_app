package edu.pdx.cs410J.benlutz;

import edu.pdx.cs410J.ParserException;

import java.io.*;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;

/**
 * The main class that parses the command line and communicates with the
 * Appointment Book server using REST.
 */
public class Project5 {

    public static final String MISSING_ARGS = "Missing command line arguments";
    private static final DateTimeFormatter DATE_TIME_FORMAT = new DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .appendPattern("M/d/yyyy h:mm a VV")
            .toFormatter();

    public static void main(String... args) {
        if (args.length == 0) {
            usage(MISSING_ARGS);
            return;
        }

        boolean printFlag = false;
        boolean searchFlag = false;
        String hostName = null;
        String portString = null;
        String owner = null;
        String description = null;
        String beginDateTimeString = null;
        String endDateTimeString = null;
        ZonedDateTime beginDateTime = null;
        ZonedDateTime endDateTime = null;

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
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
                    if (i + 1 < args.length) {
                        hostName = args[++i];
                    } else {
                        error("Error: -host requires a hostname");
                        return;
                    }
                    break;
                case "-port":
                    if (i + 1 < args.length) {
                        portString = args[++i];
                    } else {
                        error("Error: -port requires a port number");
                        return;
                    }
                    break;
                default:
                    if (owner == null) {
                        owner = args[i];
                    } else if (description == null && !searchFlag) {
                        description = args[i];
                    } else if (beginDateTimeString == null) {
                        beginDateTimeString = args[i];
                        if (i + 2 < args.length) {
                            beginDateTimeString += " " + args[++i] + " " + args[++i];
                        } else {
                            error("Incomplete date/time for the beginning of the appointment");
                            return;
                        }
                    } else if (endDateTimeString == null) {
                        endDateTimeString = args[i];
                        if (i + 2 < args.length) {
                            endDateTimeString += " " + args[++i] + " " + args[++i];
                        } else {
                            error("Incomplete date/time for the end of the appointment");
                            return;
                        }
                    } else {
                        error("Unexpected argument: " + args[i]);
                        return;
                    }
                    break;
            }
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

        if (beginDateTimeString != null && endDateTimeString != null) {
            try {
                beginDateTime = ZonedDateTime.parse(beginDateTimeString, DATE_TIME_FORMAT);
                endDateTime = ZonedDateTime.parse(endDateTimeString, DATE_TIME_FORMAT);
            } catch (DateTimeParseException e) {
                System.err.println("Error parsing date and time: " + e.getMessage());
                return;
            }
        }

        AppointmentBookRestClient client = new AppointmentBookRestClient(hostName, port);

        String message = "hello";
        try {
            if (searchFlag) {
                if (beginDateTime != null && endDateTime != null) {
                    // Search for appointments between two times
                    AppointmentBook book = client.getAppointmentsBetween(owner, beginDateTime, endDateTime);

                    // Pretty print the result
                    PrintWriter writer = null;
                    writer = new PrintWriter(System.out, true);
                    PrettyPrinter prettyPrinter = new PrettyPrinter(writer);
                    prettyPrinter.dump(book);

                } else {
                    System.err.println("Error: Search requires both begin and end date/time");
                    return;
                }
            } else {
                // Add an appointment or other actions
                if (description != null && beginDateTime != null && endDateTime != null) {
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
        } catch (invalidDescriptionException e) {
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