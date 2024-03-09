package edu.pdx.cs410J.benlutz;

import edu.pdx.cs410J.ParserException;
import edu.pdx.cs410J.web.HttpRequestHelper.RestException;
import org.junit.jupiter.api.MethodOrderer.MethodName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.HttpURLConnection;

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
    client.removeAllAppointmentBook();
  }

  @Test
  void test1EmptyServerContainsNoDictionaryEntries() throws IOException, ParserException {
    AppointmentBookRestClient client = newAppointmentBookRestClient();
    try {
      client.getAllDictionaryEntries();
    } catch (RestException ex) {
      assertThat(ex.getHttpStatusCode(), equalTo(HttpServletResponse.SC_NOT_FOUND));
    }
  }

  @Test
  void test2CreateNewAppointment() throws IOException, ParserException {
    AppointmentBookRestClient client = newAppointmentBookRestClient();
    String testOwner = "TEST OWNER";
    String testDescription = "TEST DESCRIPTION";
    client.addDictionaryEntry(testOwner, testDescription);

    String definition = client.getDefinition(testOwner);
    assertThat(definition, equalTo(testDescription));
  }

  @Test
  void test4NonExistentAppointmentBookThrowsException() {
    AppointmentBookRestClient client = newAppointmentBookRestClient();
    String emptyString = "";

    RestException ex =
      assertThrows(RestException.class, () -> client.addDictionaryEntry(emptyString, emptyString));
    assertThat(ex.getHttpStatusCode(), equalTo(HttpURLConnection.HTTP_PRECON_FAILED));
    assertThat(ex.getMessage(), equalTo(Messages.missingRequiredParameter(AppointmentBookServlet.OWNER_PARAMETER)));  }

}
