package edu.pdx.cs410J.benlutz;

import edu.pdx.cs410J.AbstractAppointment;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.Objects;


/**
 * Represents an appointment with a specific description, start time, and end time.
 * This class extends {@link AbstractAppointment} and provides detailed information
 * about an appointment including its description and timing
 */
public class Appointment extends AbstractAppointment implements Comparable<Appointment>{

  private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).withLocale(Locale.US);
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
    if (Objects.equals(description, "")){
      throw new invalidDescriptionException("Cannot be empty");
    }

    this.description = description;
    this.beginTime = beginTime;
    this.endTime = endTime;
  }

  /**
   * Retrieves the start time of the appointment
   *
   * @return null because we're not using it
   */
  @Override
  public String getBeginTimeString() {
    return DATE_TIME_FORMAT.format(this.beginTime);
  }

  /**
   * Retrieves the end time of the appointment
   *
   * @return null because we're not using it
   */
  @Override
  public String getEndTimeString() {
    return DATE_TIME_FORMAT.format(this.endTime);
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


  /**
   * Display function since I can't override the toString method in the parent class
   *
   * @return A string with the description, the begin time, and the end time
   */
  public final String display() {
    return this.getDescription() + " from " +
            this.getBeginTime() + " until " + this.getEndTime();
  }

  /**
   * Compares this appointment to another based on start time, end time, and description.
   *
   * @param other the other appointment to compare to
   * @return comparison result as per {@link Comparable#compareTo}
   */
  @Override
  public int compareTo(Appointment other) {
    int beginTimeComparison = this.beginTime.compareTo(other.beginTime);
    if (beginTimeComparison != 0) return beginTimeComparison;

    int endTimeComparison = this.endTime.compareTo(other.endTime);
    if (endTimeComparison != 0) return endTimeComparison;

    return this.description.compareToIgnoreCase(other.description);
  }
}

