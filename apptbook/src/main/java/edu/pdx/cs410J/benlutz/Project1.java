package edu.pdx.cs410J.benlutz;

import com.google.common.annotations.VisibleForTesting;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

/**
 * The main class for the CS410J appointment book Project
 */
public class Project1 {

  @VisibleForTesting
  static boolean isValidDateAndTime(String dateAndTime) {
    return true;
  }

  public static void main(String[] args) throws invalidDescriptionException {

    if (args.length == 0) {
      System.err.println("Missing command line arguments");
      return;
    }

    if (args.length > 8) {
      System.err.println("Too many command line arguments");
    }

    boolean printFlag = false;
    boolean readmeFlag = false;
    String owner = null;
    String description = null;
    String beginDate = null;
    String endDate = null;
    String beginTime = null;
    String endTime = null;
    int argCounter = 0;

    /*
    if (args.length == 0) {
      printHelpMessage();
      return;
    }
     */

    for (String arg : args) {
      if (arg.startsWith("-")) {
        if (arg.equals("-print")) {
          printFlag = true;
        } else if (arg.equals("-README")) {
          readmeFlag = true;
          break; // No need to parse further if README is requested
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
            beginTime = arg;
            break;
          case 4:
            endDate = arg;
            break;
          case 5:
            endTime = arg;
            break;
          default:
            //throw new IllegalArgumentException("Too many arguments.");
        }
        argCounter++;
      }
    }

    if (readmeFlag) {
      printReadme();
      return;
    }

    if (args.length < 6) {
      System.err.println("All fields are required (i.e. Owner Name, Description, Begin Date/Time, End Date/Time)");
    }


    Appointment appointment = new Appointment(args[0], args[1], args[2]);  // Refer to one of Dave's classes so that we can be sure it is on the classpath
    System.err.println("Missing command line arguments");
    for (String arg : args) {
      System.out.println(arg);
    }
  }

  private static void printReadme() {
    try (InputStream readmeStream = Project1.class.getResourceAsStream("README.txt");
         BufferedReader reader = new BufferedReader(new InputStreamReader(readmeStream))) {

      if (readmeStream == null) {
        System.err.println("README file not found");
        return;
      }

      String line;
      while ((line = reader.readLine()) != null) {
        System.out.println(line);
      }

    } catch (IOException e) {
      System.err.println("Error reading README file: " + e.getMessage());
    }
  }

}