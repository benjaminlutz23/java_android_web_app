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
