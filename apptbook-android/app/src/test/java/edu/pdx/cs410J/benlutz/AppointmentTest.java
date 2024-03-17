package edu.pdx.cs410J.benlutz;

import org.checkerframework.checker.units.qual.A;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertThrows;

public class AppointmentTest {

  /**
   * This unit test will need to be modified (likely deleted) as you implement
   * your project.
  void getBeginTimeStringNeedsToBeImplemented() {
    Appointment appointment = new Appointment();
    assertThrows(UnsupportedOperationException.class, appointment::getBeginTimeString);
  }
   */

  private ZonedDateTime createZonedDateTime(String date, String time) {
    LocalDateTime localDateTime = LocalDateTime.parse(date + "T" + time);
    return localDateTime.atZone(ZoneId.systemDefault());
  }

  @Test
  public void descriptionWithSomeTextIsThatText() throws Exception, invalidDescriptionException {
    ZonedDateTime begin = createZonedDateTime("2000-11-30", "07:00");
    ZonedDateTime end = createZonedDateTime("2000-11-30", "09:00");
    Appointment appointment = new Appointment("A fancy description", begin, end);

    assertThat(appointment.getDescription(), equalTo("A fancy description"));
  }

  @Test
  public void emptyDescriptionThrowsException() {
    ZonedDateTime begin = createZonedDateTime("2000-11-30", "07:00");
    ZonedDateTime end = createZonedDateTime("2000-11-30", "09:00");
    assertThrows(invalidDescriptionException.class, () ->
            new Appointment("", begin, end)
    );
  }

  // Removed tests related to date and time string lengths
  // as these validations are no longer necessary with ZonedDateTime

  @Test
  public void forProject1ItIsOkayIfGetBeginTimeReturnsNonNull() throws Exception, invalidDescriptionException {
    ZonedDateTime begin = createZonedDateTime("2021-01-01", "07:00");
    ZonedDateTime end = createZonedDateTime("2021-01-01", "09:00");
    Appointment appointment = new Appointment("description", begin, end);

    assertThat(appointment.getBeginTime(), is(notNullValue()));
  }

}
