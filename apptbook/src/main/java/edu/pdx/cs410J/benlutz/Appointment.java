package edu.pdx.cs410J.benlutz;

import edu.pdx.cs410J.AbstractAppointment;

import java.util.Objects;

public class Appointment extends AbstractAppointment {

  private String description;
  private String beginTimeString;
  private String endTimeString;

  public Appointment() {

  }

  public Appointment(String description, String beginDate, String beginTime, String endDate, String endTime) throws invalidDescriptionException, invalidDateFormatException, invalidTimeFormatException {
    if (Objects.equals(description, "")){
      throw new invalidDescriptionException("Cannot be empty");
    }

    if (beginDate.length() > 10 || endDate.length() > 10 || beginDate.length() < 8 || endDate.length() < 8) {
      throw new invalidDateFormatException();
    }

    if (beginTime.length() > 5 || endTime.length() > 5 || beginTime.length() < 4 || endTime.length() < 4) {
      throw new invalidTimeFormatException();
    }

    /*
    System.out.println("What the arguments are inside the constructor:");
    System.out.println(description);
    System.out.println(beginDate);
    System.out.println(beginTime);
    System.out.println(endDate);
    System.out.println(endTime);
    */

    // Concatenate begin and end time strings
    this.beginTimeString = beginDate + " " + beginTime;
    this.endTimeString = endDate + " " + endTime;
    this.description = description;
  }

  @Override
  public String getBeginTimeString() {
    return this.beginTimeString;
  }

  @Override
  public String getEndTimeString() {
    return this.endTimeString;
  }

  @Override
  public String getDescription() {
    return this.description;
  }
}
