<<<<<<< HEAD
//package edu.pdx.cs410J.benlutz;
//
//import org.junit.jupiter.api.MethodOrderer.MethodName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestMethodOrder;
//
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.hamcrest.Matchers.containsString;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//
///**
// * Integration test that tests the REST calls made by {@link AppointmentBookRestClient}
// */
//@TestMethodOrder(MethodName.class)
//class AppointmentBookRestClientIT {
//  private static final String HOSTNAME = "localhost";
//  private static final String PORT = System.getProperty("http.port", "8080");
//
//  private AppointmentBookRestClient newAppointmentBookRestClient() {
//    int port = Integer.parseInt(PORT);
//    return new AppointmentBookRestClient(HOSTNAME, port);
//  }
//
////  @Test
////  void test0RemoveAllAppointmentBooks() throws IOException {
////    AppointmentBookRestClient client = newAppointmentBookRestClient();
////    client.removeAllAppointmentBooks();
////  }
//
//  @Test
//  void test1EmptyServerContainsNoAppointmentBooks() {
//    AppointmentBookRestClient client = newAppointmentBookRestClient();
//    String owner = "OwnerWithNoAppointments";
//    Exception exception = assertThrows(IOException.class, () -> client.searchAppointments(owner, "", ""));
//    assertThat(exception.getMessage(), containsString(String.valueOf(HttpServletResponse.SC_NOT_FOUND)));
//  }
//
//  @Test
//  void test2CreateNewAppointment() throws IOException {
//    AppointmentBookRestClient client = newAppointmentBookRestClient();
//    String owner = "Test Owner";
//    String description = "Test Description";
//    String begin = "1/1/2020 10:00 AM";
//    String end = "1/1/2020 11:00 AM";
//    client.addAppointment(owner, description, begin, end);
//
//    String appointments = client.searchAppointments(owner, begin, end);
//    assertThat(appointments, containsString(description));
//  }
//
//  @Test
//  void test4AddAppointmentToNonExistentOwnerCreatesAppointmentBook() throws IOException {
//    AppointmentBookRestClient client = newAppointmentBookRestClient();
//    String owner = "NewOwner";
//    String description = "New Description";
//    String begin = "1/2/2020 10:00 AM";
//    String end = "1/2/2020 11:00 AM";
//    client.addAppointment(owner, description, begin, end);
//
//    String result = client.searchAppointments(owner, begin, end);
//    assertThat(result, containsString(description));
//  }
//}
=======
package edu.pdx.cs410J.benlutz;

import edu.pdx.cs410J.ParserException;
import edu.pdx.cs410J.web.HttpRequestHelper.RestException;
import org.junit.jupiter.api.MethodOrderer.MethodName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.time.ZonedDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Integration test that tests the REST calls made by {@link AppointmentBookRestClient}
 */
@TestMethodOrder(MethodName.class)
class AppointmentBookRestClientIT {
  private static final String HOSTNAME = "localhost";
  private static final String PORT = System.getProperty("http.port", "8080");

  private AppointmentBookRestClient newAppointmentBookRestClient() {
    int port = Integer.parseInt(PORT);
    return new AppointmentBookRestClient(HOSTNAME, port);
  }

  @Test
  void test0RemoveAllDictionaryEntries() throws IOException {
    AppointmentBookRestClient client = newAppointmentBookRestClient();
    client.removeAllAppointmentBooks();
  }

  @Test
  void test2CreateNewAppointment() throws IOException, ParserException, invalidDescriptionException {
    AppointmentBookRestClient client = newAppointmentBookRestClient();
    String testOwner = "TEST OWNER";
    String testDescription = "TEST DESCRIPTION";

    // Example begin and end times
    ZonedDateTime beginTime = ZonedDateTime.parse("2024-10-19T18:00:00-07:00[America/Los_Angeles]");
    ZonedDateTime endTime = ZonedDateTime.parse("2024-10-19T21:30:00-07:00[America/Los_Angeles]");

    client.addAppointment(testOwner, new Appointment(testDescription, beginTime, endTime));

    AppointmentBook book = client.getAppointmentBook(testOwner);
    assertThat(book.getOwnerName(), equalTo(testOwner));
    Appointment appointment = book.getAppointments().iterator().next();
    assertThat(appointment.getDescription(), equalTo(testDescription));
    // Verify begin and end times as well
    assertThat(appointment.getBeginTime(), equalTo(beginTime));
    assertThat(appointment.getEndTime(), equalTo(endTime));
  }

  @Test
  public void nonExistentAppointmentBookThrowsException() {
    // Assuming there's a running server at localhost:8080
    String nonExistentOwner = "NonExistentOwner";
    AppointmentBookRestClient client = new AppointmentBookRestClient("localhost", 8080);

    // Act & Assert
    assertThrows(RestException.class, () -> client.getAppointmentBook(nonExistentOwner),
            "Expected getAppointmentBook to throw, but it didn't");
  }
}


>>>>>>> Fresh-Start
