package edu.pdx.cs410J.benlutz;

import com.google.common.annotations.VisibleForTesting;

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
            throw new IllegalArgumentException("Too many arguments.");
        }
        argCounter++;
      }
    }


    Appointment appointment = new Appointment(args[0], args[1], args[2]);  // Refer to one of Dave's classes so that we can be sure it is on the classpath
    System.err.println("Missing command line arguments");
    for (String arg : args) {
      System.out.println(arg);
    }
  }

}