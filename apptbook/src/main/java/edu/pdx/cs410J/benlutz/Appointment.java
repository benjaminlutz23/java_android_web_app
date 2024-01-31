package edu.pdx.cs410J.benlutz;

import edu.pdx.cs410J.AbstractAppointment;

import java.util.Objects;


/**
 * Represents an appointment with a specific description, start time, and end time.
 * This class extends {@link AbstractAppointment} and provides detailed information
 * about an appointment including its description and timing
 */
public class Appointment extends AbstractAppointment {

  /**
   * The description of the appointment.
   */
  private final String description;

  /**
   * The starting time of the appointment in string format.
   */
  private final String beginTimeString;

  /**
   * The ending time of the appointment in string format.
   */
  private final String endTimeString;


  /**
   * Constructs an Appointment with a description, begin date and time, and end date and time
   *
   * @param description   Description of the appointment
   * @param beginDate     Date when the appointment begins in the format mm/dd/yyyy
   * @param beginTime     Time when the appointment begins in 24-hour format hh:mm
   * @param endDate       Date when the appointment ends in the format mm/dd/yyyy
   * @param endTime       Time when the appointment ends in 24-hour format hh:mm
   * @throws invalidDescriptionException if the description is empty
   * @throws invalidDateFormatException  if the date format is invalid or does not match the expected length
   * @throws invalidTimeFormatException  if the time format is invalid or does not match the expected length
   */
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

    // Concatenate begin and end time strings
    this.beginTimeString = beginDate + " " + beginTime;
    this.endTimeString = endDate + " " + endTime;
    this.description = description;
  }

  /**
   * Retrieves the start time of the appointment
   *
   * @return A string representing the beginning date and time of the appointment
   */
  @Override
  public String getBeginTimeString() {
    return this.beginTimeString;
  }

  /**
   * Retrieves the end time of the appointment
   *
   * @return A string representing the ending date and time of the appointment
   */
  @Override
  public String getEndTimeString() {
    return this.endTimeString;
  }

  /**
   * Retrieves the description of the appointment
   *
   * @return The description of the appointment
   */
  @Override
  public String getDescription() {
    return this.description;
  }
}
