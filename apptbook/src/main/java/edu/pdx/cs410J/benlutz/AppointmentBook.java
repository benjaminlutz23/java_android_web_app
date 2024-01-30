package edu.pdx.cs410J.benlutz;

import edu.pdx.cs410J.AbstractAppointmentBook;

import java.util.ArrayList;
import java.util.Collection;

public class AppointmentBook extends AbstractAppointmentBook<Appointment> {
  private final String owner;
  private final Collection<Appointment> appointments;

  public AppointmentBook(String owner) {
    this.owner = owner;
    this.appointments = new ArrayList<>();
  }

  @Override
  public String getOwnerName() {
    return this.owner;
  }

  @Override
  public Collection<Appointment> getAppointments() {
    return this.appointments;
  }

  @Override
  public void addAppointment(Appointment appt) {
    this.appointments.add(appt);
  }
}
