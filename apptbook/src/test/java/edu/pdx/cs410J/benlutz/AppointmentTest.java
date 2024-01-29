package edu.pdx.cs410J.benlutz;

import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AppointmentTest {

  /**
   * This unit test will need to be modified (likely deleted) as you implement
   * your project.
  void getBeginTimeStringNeedsToBeImplemented() {
    Appointment appointment = new Appointment();
    assertThrows(UnsupportedOperationException.class, appointment::getBeginTimeString);
  }
   */

  @Test
  void descriptionWithSomeTextIsThatText() throws invalidDescriptionException {
    //String description = "A fancy description";
    var appointmentWithDescription = new Appointment("A fancy description", "doesn't matter", "doesn't matter");

    assertThat(appointmentWithDescription.getDescription(), equalTo("A fancy description"));
  }

  @Test
  void nullDescriptionThrowsException() {
    try {
      new Appointment(null, "doesn't matter", "doesn't matter");
    } catch (invalidDescriptionException ex){
      //We expect this exception
    }
  }

  /**
   * This unit test will need to be modified (likely deleted) as you implement
   * your project.
   */
  @Test
  void initiallyAllAppointmentsHaveTheSameDescription() throws invalidDescriptionException {
    Appointment appointment = new Appointment("A fancy description", "doesn't matter", "doesn't matter");
    assertThat(appointment.getDescription(), containsString("A fancy description"));
  }

  @Test
  void forProject1ItIsOkayIfGetBeginTimeReturnsNull() throws invalidDescriptionException {
    Appointment appointment = new Appointment("A fancy description", "doesn't matter", "doesn't matter");
    assertThat(appointment.getBeginTime(), is(nullValue()));
  }

}
