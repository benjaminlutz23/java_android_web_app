package edu.pdx.cs410J.benlutz;

import edu.pdx.cs410J.AbstractAppointment;

import java.util.Objects;
import java.text.ParseException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.time.ZoneId;


/**
 * Represents an appointment with a specific description, start time, and end time.
 * This class extends {@link AbstractAppointment} and provides detailed information
 * about an appointment including its description and timing
 */
public class Appointment extends AbstractAppointment {

  private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");
  /**
   * The description of the appointment.
   */
  private final String description;

  private final ZonedDateTime beginTime;
  private final ZonedDateTime endTime;


  /**
   * Constructs an Appointment with a description, begin date and time, and end date and time
   *
   * @param description   Description of the appointment
   * @param beginTime     Time when the appointment begins in 24-hour format mm/dd/yyyy hh:mm
   * @param endTime       Time when the appointment ends in 24-hour format mm/dd/yyyy hh:mm
   * @throws invalidDescriptionException if the description is empty
   */
  public Appointment(String description, ZonedDateTime beginTime, ZonedDateTime endTime) throws invalidDescriptionException {
    this.description = description;
    this.beginTime = beginTime;
    this.endTime = endTime;

    if (Objects.equals(description, "")){
      throw new invalidDescriptionException("Cannot be empty");
    }
  }

  @Override
  public String getBeginTimeString() {
    return null;
  }

  @Override
  public String getEndTimeString() {
    return null;
  }

  /**
   * Retrieves the start time of the appointment
   *
   * @return A string representing the beginning date and time of the appointment
   */
  @Override
  public ZonedDateTime getBeginTime() {
    return this.beginTime;
  }

  /**
   * Retrieves the end time of the appointment
   *
   * @return A string representing the ending date and time of the appointment
   */
  @Override
  public ZonedDateTime getEndTime() {
    return this.endTime;
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

  public final String display() {
    return this.getDescription() + " from " +
            this.getBeginTime() + " until " + this.getEndTime();
  }
}

