package edu.pdx.cs410J.benlutz;

import edu.pdx.cs410J.AbstractAppointment;

public class Appointment extends AbstractAppointment {

  private String description;
  private String beginTimeString;
  private String endTimeString;

  public Appointment() {

  }

  public Appointment(String description, String beginDate, String beginTime, String endDate, String endTime) throws invalidDescriptionException, invalidDateFormatException, invalidTimeFormatException {
    if (description == null){
      throw new invalidDescriptionException("Cannot be empty");
    }

    if (beginDate.length() > 10 || endDate.length() > 10) {
      throw new invalidDateFormatException("Date length too long");
    }

    if (beginDate.length() < 8 || endDate.length() < 8) {
      throw new invalidDateFormatException("Date length too short");
    }

    if (beginTime.length() > 5 || endTime.length() > 5) {
      throw new invalidTimeFormatException("Time length too many characters");
    }

    if (beginTime.length() < 4 || endTime.length() < 4) {
      throw new invalidTimeFormatException("Time length to few characters");
    }

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
