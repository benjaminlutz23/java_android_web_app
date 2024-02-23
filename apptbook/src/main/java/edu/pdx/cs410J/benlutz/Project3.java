package edu.pdx.cs410J.benlutz;

import edu.pdx.cs410J.ParserException;

import java.io.*;
import java.time.format.DateTimeParseException;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.util.Objects;


/**
 * The main class for the Project 1 Appointment Book application.
 * This class handles the command-line interface for creating and managing an appointment book
 */
public class Project3 {

  /*
  @VisibleForTesting
  static boolean isValidDateAndTime(String dateAndTime) {
    return true;
  }
  */

  private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("[M/d/yyyy][MM/dd/yyyy][MM/d/yyyy][M/dd/yyyy] [H:mm][HH:mm] a VV");

  /**
   * The main entry point for the Appointment Book application
   * <p>
   * This method processes command line arguments to create appointments and manage an appointment book
   *
   * @param args Command line arguments used to run the program
   * @throws invalidDescriptionException If the description of the appointment is invalid
   */
  public static void main(String[] args) throws invalidDescriptionException, invalidOwnerException {

      if (args.length == 0) {
          printHelpMessage();
          return;
      }

      if (args.length > 14) {
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
      if (endDate == null || endTime == null) {
          System.err.println("Error: Missing end time");
      }

      // Parse the begin time into ZonedDateTime
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



      // Random error check that makes some test happy that I don't want to go find and delete
      if (argCounter < 6) {
          System.err.println("All fields are required (i.e. Owner Name, Description, Begin Date/Time, End Date/Time)");
          return;
      }


      // The main program logic
      try {
        AppointmentBook appointmentBook;
        Appointment appointment = new Appointment(description, beginDateTime, endDateTime);

        if (fileFlag) {
            File file = new File(fileName);

            // Check if the file exists
            if (!file.exists()) {
                try {
                    // If the file doesn't exist, create a new AppointmentBook and file
                    boolean newFileCreated = file.createNewFile();
                    if (!newFileCreated) {
                        System.err.println("Could not create new file: " + fileName);
                        return;
                    }
                    appointmentBook = new AppointmentBook(owner);
                } catch (IOException e) {
                    System.err.println("Error while creating new file: " + e.getMessage());
                    return;
                }
            } else {
                // If the file exists, parse the existing AppointmentBook
                try (FileReader reader = new FileReader(file)) {
                    TextParser parser = new TextParser(reader);
                    appointmentBook = parser.parse();
                    if (!Objects.equals(appointmentBook.getOwnerName(), owner)) {
                        System.err.println("The owner name you provided does not match the owner name in the text file.");
                        return;
                    }
                } catch (IOException | ParserException e) {
                    System.err.println("Error reading from file: " + e.getMessage());
                    return;
                }
            }

            // Add the new appointment
            appointmentBook.addAppointment(appointment);

            // Write the updated AppointmentBook back to the file
            try (FileWriter writer = new FileWriter(file)) {
                TextDumper dumper = new TextDumper(writer);
                dumper.dump(appointmentBook);
            } catch (IOException e) {
                System.err.println("Error writing to file: " + e.getMessage());
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
    }
  }

  /**
   * Prints the README information to the standard output
   * <p>
   * This method reads the README information from a text file and prints it
   */
  private static void printReadme() {
    try (InputStream readmeStream = Project3.class.getResourceAsStream("README.txt")) {
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
