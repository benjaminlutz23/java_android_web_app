package edu.pdx.cs410J.benlutz;

import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

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
  void descriptionWithSomeTextIsThatText() throws invalidDescriptionException, invalidDateFormatException, invalidTimeFormatException {
    //String description = "A fancy description";
    var appointmentWithDescription = new Appointment("A fancy description", "11/30/2000", "07:00", "11/30/2000", "09:00");

    assertThat(appointmentWithDescription.getDescription(), equalTo("A fancy description"));
  }

  @Test
  void emptyDescriptionThrowsException() {
    assertThrows(invalidDescriptionException.class, () ->
            new Appointment("", "doesn't", "matter", "doesn't", "matter")
    );
  }

  @Test
  void beginDateIsNoMoreThan10Characters() {
    assertThrows(invalidDateFormatException.class, () ->
            new Appointment("description", "11/30/20001", "07:00", "11/30/2000", "09:00")
    );
  }


  // Test that beginDate is no less than 8 characters
  @Test
  void beginDateIsNoLessThan8Characters() {
    assertThrows(invalidDateFormatException.class, () ->
            new Appointment("description", "1/1/202", "07:00", "01/01/2021", "09:00")
    );
  }

  // Test that endDate is no more than 10 characters
  @Test
  void endDateIsNoMoreThan10Characters() {
    assertThrows(invalidDateFormatException.class, () ->
            new Appointment("description", "01/01/2021", "07:00", "11/30/20001", "09:00")
    );
  }

  // Test that endDate is no less than 8 characters
  @Test
  void endDateIsNoLessThan8Characters() {
    assertThrows(invalidDateFormatException.class, () ->
            new Appointment("description", "01/01/2021", "07:00", "1/1/202", "09:00")
    );
  }

  // Test that beginTime is no more than 5 characters
  @Test
  void beginTimeIsNoMoreThan5Characters() {
    assertThrows(invalidTimeFormatException.class, () ->
            new Appointment("description", "01/01/2021", "07:000", "01/01/2021", "09:00")
    );
  }

  // Test that beginTime is no less than 4 characters
  @Test
  void beginTimeIsNoLessThan4Characters() {
    assertThrows(invalidTimeFormatException.class, () ->
            new Appointment("description", "01/01/2021", "7:0", "01/01/2021", "09:00")
    );
  }

  // Test that endTime is no more than 5 characters
  @Test
  void endTimeIsNoMoreThan5Characters() {
    assertThrows(invalidTimeFormatException.class, () ->
            new Appointment("description", "01/01/2021", "07:00", "01/01/2021", "09:000")
    );
  }

  // Test that endTime is no less than 4 characters
  @Test
  void endTimeIsNoLessThan4Characters() {
    assertThrows(invalidTimeFormatException.class, () ->
            new Appointment("description", "01/01/2021", "07:00", "01/01/2021", "9:0")
    );
  }

  @Test
  void forProject1ItIsOkayIfGetBeginTimeReturnsNull() throws invalidDescriptionException, invalidDateFormatException, invalidTimeFormatException {
    Appointment appointment = new Appointment("description", "01/01/2021", "07:00", "01/01/2021", "9:00");
    assertThat(appointment.getBeginTime(), is(nullValue()));
  }

}
