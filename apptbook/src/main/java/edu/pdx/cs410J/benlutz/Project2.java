package edu.pdx.cs410J.benlutz;

import edu.pdx.cs410J.ParserException;

import java.io.*;
import java.time.format.DateTimeParseException;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.ZoneId;


/**
 * The main class for the Project 1 Appointment Book application.
 * This class handles the command-line interface for creating and managing an appointment book
 */
public class Project2 {

  /*
  @VisibleForTesting
  static boolean isValidDateAndTime(String dateAndTime) {
    return true;
  }
  */

  private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("[M/d/yyyy][MM/dd/yyyy][MM/d/yyyy][M/dd/yyyy] [H:mm][HH:mm]");

  /**
   * The main entry point for the Appointment Book application
   * <p>
   * This method processes command line arguments to create appointments and manage an appointment book
   *
   * @param args Command line arguments used to run the program
   * @throws invalidDescriptionException If the description of the appointment is invalid
   */
  public static void main(String[] args) throws invalidDescriptionException {

    if (args.length == 0) {
      printHelpMessage();
      return;
    }

    if (args.length > 10) {
      System.err.println("Too many command line arguments");
      return;
    }

    boolean printFlag = false;
    boolean fileFlag = false;
    boolean invalidOptionFlag = false;
    String fileName = null;
    String owner = null;
    String description = null;
    String beginDate = null;
    String endDate = null;
    String beginTime = null;
    String endTime = null;
    int argCounter = 0;
    int optCounter = 0;

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
            case "-README":
              printReadme();
              return;
            case "-textFile":
                fileFlag = true;
                // Ensure there is another argument after "-textFile"
                if (i + 1 < args.length) {
                    fileName = args[i + 1];
                    i++; // Skip the next argument since it's used as the fileName
                } else {
                    // Handle the case where "-textFile" is the last argument without a following file name
                    System.err.println("Error: -textFile option requires a file name");
                    return;
                }
                break;

            default:
              invalidOptionFlag = true;
          }
        optCounter++;
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
            beginTime = arg;
            break;
          case 4:
            endDate = arg;
            break;
          case 5:
            endTime = arg;
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
    if (endDate == null || endTime == null) {
      System.err.println("Error: Missing end time");
    }

    // Parse the begin time into DateTimeFormat
    try {
      LocalDateTime beginLocalDateTime = LocalDateTime.parse(beginDate + " " + beginTime, DATE_TIME_FORMAT);
      beginDateTime = beginLocalDateTime.atZone(ZoneId.systemDefault());
    } catch (DateTimeParseException e) {
      System.err.println("Invalid begin date/time format: " + e.getMessage());
      return;
    }

    // Parse the end time into DateTimeFormat
    try {
      LocalDateTime endLocalDateTime = LocalDateTime.parse(endDate + " " + endTime, DATE_TIME_FORMAT);
      endDateTime = endLocalDateTime.atZone(ZoneId.systemDefault());
    } catch (DateTimeParseException e) {
      System.err.println("Invalid end date/time format: " + e.getMessage());
      return;
    }


    // Random error check that makes some test happy that I don't want to go find and delete
    if (argCounter < 6) {
      System.err.println("All fields are required (i.e. Owner Name, Description, Begin Date/Time, End Date/Time)");
      return;
    }


    // The main program logic
    try {
      // Create the appointment
      Appointment appointment = new Appointment(description, beginDateTime, endDateTime);
      AppointmentBook appointmentBook;

      // Create the appointment book
      if (fileFlag) {
        FileReader reader = new FileReader(fileName);
        TextParser parser = new TextParser(reader);
        appointmentBook = parser.parse();
        System.out.println("Successfully parsed appointment book for owner: " + appointmentBook.getOwnerName());

        // Add the appointment to the appointment book
        appointmentBook.addAppointment(appointment);

        File file = new File(fileName);
        try (Writer writer = new FileWriter(file)) {
          TextDumper dumper = new TextDumper(writer);
          dumper.dump(appointmentBook);
        } catch (IOException e) {
          System.err.println("Error writing to file");
        }
      } else {
        appointmentBook = new AppointmentBook(owner);
        appointmentBook.addAppointment(appointment);
      }

      if (printFlag) {
        System.out.println(appointmentBook);
        System.out.println(appointment.display());
      }

    } catch (invalidDescriptionException e) {
      System.err.println("Invalid Description: " + e.getMessage());
      return;
    } catch (invalidOwnerException e) {
      System.err.println("Invalid Owner Name");
      return;
    } catch (IOException e) {
      System.err.println("Error reading from file: " + e.getMessage());
    } catch (ParserException e) {
      System.err.println("Error parsing appointment book: " + e.getMessage());
    }

    System.out.println("\nThank you for using this program.");
  }

  /**
   * Prints the README information to the standard output
   * <p>
   * This method reads the README information from a text file and prints it
   */
  private static void printReadme() {
    try (InputStream readmeStream = Project2.class.getResourceAsStream("README.txt")) {
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

  /**
   * Prints a help message to the standard error output
   * <p>
   * This method displays usage information about the program
   */
  private static void printHelpMessage() {
    System.err.println("Error: No command line arguments\n" +
        "Project 1: Appointment Book Program\n" +
        "Usage: java -jar target/apptbook-1.0.0.jar [options] <args>\n" +
        "  Non-optional command line arguments (required in this order):\n" +
        "    owner        - The person who owns the appt book\n" +
        "    description  - A description of the appointment\n" +
        "    beginDate    - When the appt begins (mm/dd/yyyy)\n" +
        "    beginTime    - When the appt begins (hh:mm)\n" +
        "    endDate      - When the appt ends (mm/dd/yyyy)\n" +
        "    endTime      - When the appt ends (hh:mm)\n" +
        "  Optional command line arguments (options may appear in any order):\n" +
        "    -print       - Prints a description of the new appointment\n" +
        "    -README      - Prints a README for this project and exits\n" +
        "Note: Date and time should be in 24-hour format. For multi-word descriptions or owner names, enclose them in quotes.");
  }
}
