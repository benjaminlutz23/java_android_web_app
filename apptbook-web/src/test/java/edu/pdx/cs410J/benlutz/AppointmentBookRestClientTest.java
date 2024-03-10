package edu.pdx.cs410J.benlutz;

import edu.pdx.cs410J.ParserException;
import edu.pdx.cs410J.web.HttpRequestHelper;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.time.ZonedDateTime;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AppointmentBookRestClientTest {
  @Test
  void getAllDictionaryEntriesPerformsHttpGetWithNoParameters() throws ParserException, IOException, invalidOwnerException, invalidDescriptionException {
    String owner = "TEST OWNER";
    AppointmentBook appointmentBook = new AppointmentBook(owner);
    String description = "TEST DESCRIPTION";
    ZonedDateTime beginTime = ZonedDateTime.parse("2024-10-19T18:00:00-07:00[America/Los_Angeles]");
    ZonedDateTime endTime = ZonedDateTime.parse("2024-10-19T21:30:00-07:00[America/Los_Angeles]");

    appointmentBook.addAppointment(new Appointment(description, beginTime, endTime));

    HttpRequestHelper http = mock(HttpRequestHelper.class);
    when(http.get(eq(Map.of()))).thenReturn(appointmentBookAsText(appointmentBook));

    AppointmentBookRestClient client = new AppointmentBookRestClient(http);

    AppointmentBook appointmentBook2 = client.getAppointmentBook(owner);
    assertThat(appointmentBook2.getOwnerName(), equalTo(owner));
    assertThat(appointmentBook2.getAppointments().iterator().next().getDescription(), equalTo(description));
  }

  private HttpRequestHelper.Response appointmentBookAsText(AppointmentBook appointmentBook) {
    StringWriter writer = new StringWriter();
    new TextDumper(writer).dump(appointmentBook);

    return new HttpRequestHelper.Response(writer.toString());
  }
}
