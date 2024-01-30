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

  // Test that beginDate is no more than 10 characters

  // Test that beginDate is no less than 8 characters

  // Test that endDate is no more than 10 characters

  // Test that endDate is no less than 8 characters

  // Test that beginTime is no more than 5 characters

  // Test that beginTime is no less than 4 characters

  // Test that endTime is no more than 5 characters

  // Test that endTime is no less than 4 characters

  @Test
  void forProject1ItIsOkayIfGetBeginTimeReturnsNull() throws invalidDescriptionException {
    Appointment appointment = new Appointment("A fancy description", "doesn't matter", "doesn't matter");
    assertThat(appointment.getBeginTime(), is(nullValue()));
  }

}
