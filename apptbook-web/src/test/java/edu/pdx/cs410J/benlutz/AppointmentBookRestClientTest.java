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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyMap;
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
    HttpRequestHelper.Response mockedResponse = mock(HttpRequestHelper.Response.class);

    // Setup mock to return expected text content and HTTP status code
    when(mockedResponse.getContent()).thenReturn(appointmentBookAsText(appointmentBook));
    when(mockedResponse.getHttpStatusCode()).thenReturn(200); // Simulate OK response

    // Configure http.get() to return the mocked response
    when(http.get(anyMap())).thenReturn(mockedResponse);

    AppointmentBookRestClient client = new AppointmentBookRestClient(http);

    // Execute test
    AppointmentBook retrievedAppointmentBook = client.getAppointmentBook(owner);
    assertNotNull(retrievedAppointmentBook);
    assertEquals(owner, retrievedAppointmentBook.getOwnerName());
    assertEquals(description, retrievedAppointmentBook.getAppointments().iterator().next().getDescription());
  }

  private String appointmentBookAsText(AppointmentBook appointmentBook) {
    StringWriter writer = new StringWriter();
    new TextDumper(writer).dump(appointmentBook);
    return writer.toString();
  }

}
