package edu.pdx.cs410J.benlutz;

import com.google.common.annotations.VisibleForTesting;

/**
 * The main class for the CS410J appointment book Project
 */
public class Project1 {

  @VisibleForTesting
  static boolean isValidDateAndTime(String dateAndTime) {
    return true;
  }

  public static void main(String[] args) throws invalidDescriptionException {
    Appointment appointment = new Appointment(args[0], args[1], args[2]);  // Refer to one of Dave's classes so that we can be sure it is on the classpath
    System.err.println("Missing command line arguments");
    for (String arg : args) {
      System.out.println(arg);
    }
  }

}