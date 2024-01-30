package edu.pdx.cs410J.benlutz;

import edu.pdx.cs410J.AbstractAppointment;

public class Appointment extends AbstractAppointment {

  private String description;
  private String begin;
  private String end;

  public Appointment() {

  }

  public Appointment(String description, String begin, String end) throws invalidDescriptionException {
    if (description == null){
      throw new invalidDescriptionException();
    }
    this.description = description;
    this.begin = begin;
    this.end = end;
  }

  @Override
  public String getBeginTimeString() {
    return this.begin;
  }

  @Override
  public String getEndTimeString() {
    return this.end;
  }

  @Override
  public String getDescription() {
    return this.description;
  }
}
