package edu.pdx.cs410J.benlutz;

import edu.pdx.cs410J.AbstractAppointmentBook;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents an appointment book that holds a collection of {@link Appointment} objects.
 * This class extends {@link AbstractAppointmentBook} and provides functionality
 * for managing appointments for a specific owner
 */
public class AppointmentBook extends AbstractAppointmentBook<Appointment> {
  /**
   * The name of the owner of the appointment book.
   */
  private final String owner;

  /**
   * A collection of appointments in the appointment book.
   */
  private final Collection<Appointment> appointments;

  /**
   * Constructs a new AppointmentBook with the specified owner's name
   *
   * @param owner The name of the owner of the appointment book
   * @throws invalidOwnerException if the owner name is empty
   */
  public AppointmentBook(String owner) throws invalidOwnerException {
    if (Objects.equals(owner, "")) {
      throw new invalidOwnerException();
    }

    this.owner = owner;
    this.appointments = new ArrayList<>();
  }

  /**
   * Retrieves the name of the owner of this appointment book
   *
   * @return The name of the owner
   */
  @Override
  public String getOwnerName() {
    return this.owner;
  }

  /**
   * Retrieves a collection of all appointments in this appointment book
   *
   * @return A collection of {@link Appointment} objects
   */
  @Override
  public Collection<Appointment> getAppointments() {
    return this.appointments;
  }

  /**
   * Adds an appointment to the appointment book
   *
   * @param appt The {@link Appointment} to be added to the book
   */
  @Override
  public void addAppointment(Appointment appt) {
    this.appointments.add(appt);
  }
}
