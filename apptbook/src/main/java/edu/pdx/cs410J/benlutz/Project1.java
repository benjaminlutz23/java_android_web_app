package edu.pdx.cs410J.benlutz;

import com.google.common.annotations.VisibleForTesting;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.ZoneId;


/**
 * The main class for the Project 1 Appointment Book application.
 * This class handles the command-line interface for creating and managing an appointment book
 */
public class Project1 {

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

    if (args.length > 8) {
      System.err.println("Too many command line arguments");
      return;
    }

    boolean printFlag = false;
    boolean invalidOptionFlag = false;
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


    for (String arg : args) {
      if (arg.startsWith("-")) {
        if (arg.equals("-print")) {
          printFlag = true;
        } else if (arg.equals("-README")) {
          printReadme();
          return;
        } else {
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

    if (invalidOptionFlag) {
      System.err.println("Error: Invalid command line option");
      return;
    }

    if (endDate == null || endTime == null) {
      System.err.println("Error: Missing end time");
    }

    try {
      LocalDateTime beginLocalDateTime = LocalDateTime.parse(beginDate + " " + beginTime, DATE_TIME_FORMAT);
      beginDateTime = beginLocalDateTime.atZone(ZoneId.systemDefault());
    } catch (DateTimeParseException e) {
      System.err.println("Invalid begin date/time format: " + e.getMessage());
      return;
    }

    try {
      LocalDateTime endLocalDateTime = LocalDateTime.parse(endDate + " " + endTime, DATE_TIME_FORMAT);
      endDateTime = endLocalDateTime.atZone(ZoneId.systemDefault());
    } catch (DateTimeParseException e) {
      System.err.println("Invalid end date/time format: " + e.getMessage());
      return;
    }



    if (argCounter < 6) {
      System.err.println("All fields are required (i.e. Owner Name, Description, Begin Date/Time, End Date/Time)");
      return;
    }


    try {
      // Create the appointment
      Appointment appointment = new Appointment(description, beginDateTime, endDateTime);

      // Create the appointment book
      AppointmentBook appointmentBook = new AppointmentBook(owner);

      // Add the appointment to the appointment book
      appointmentBook.addAppointment(appointment);

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

      System.out.println("\nThank you for using this program.");
  }

  /**
   * Prints the README information to the standard output
   * <p>
   * This method reads the README information from a text file and prints it
   */
  private static void printReadme() {
    try (InputStream readmeStream = Project1.class.getResourceAsStream("README.txt")) {
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