package edu.pdx.cs410J.benlutz;

import edu.pdx.cs410J.web.HttpRequestHelper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static edu.pdx.cs410J.web.HttpRequestHelper.Response;
import static java.net.HttpURLConnection.HTTP_OK;

/**
 * A helper class for accessing the REST client to manage appointment books.
 */
public class AppointmentBookRestClient {
  private static final String WEB_APP = "apptbook";
  private static final String SERVLET = "appointments";

  private final HttpRequestHelper http;

  /**
   * Creates a client to the appointment book REST service running on the given host and port.
   *
   * @param hostName The name of the host
   * @param port     The port
   */
  public AppointmentBookRestClient(String hostName, int port) {
    this.http = new HttpRequestHelper(String.format("http://%s:%d/%s/%s", hostName, port, WEB_APP, SERVLET));
  }

  /**
   * Posts a new appointment to the server.
   *
   * @param owner       The owner of the appointment book
   * @param description The description of the appointment
   * @param begin       The start time of the appointment
   * @param end         The end time of the appointment
   * @throws IOException If there is an issue with the network communication
   */
  public void addAppointment(String owner, String description, String begin, String end) throws IOException {
    Map<String, String> params = new HashMap<>();
    params.put(AppointmentBookServlet.OWNER_PARAMETER, owner);
    params.put(AppointmentBookServlet.DESCRIPTION_PARAMETER, description);
    params.put(AppointmentBookServlet.BEGIN_PARAMETER, begin);
    params.put(AppointmentBookServlet.END_PARAMETER, end);

    Response response = http.post(params);
    checkResponseCode(HTTP_OK, response);
  }

  /**
   * Searches for appointments within the given date range.
   *
   * @param owner The owner of the appointment book
   * @param begin The start time of the range
   * @param end   The end time of the range
   * @return The server's response as a String
   * @throws IOException If there is an issue with the network communication
   */
  public String searchAppointments(String owner, String begin, String end) throws IOException {
    Map<String, String> params = new HashMap<>();
    params.put(AppointmentBookServlet.OWNER_PARAMETER, owner);
    params.put(AppointmentBookServlet.BEGIN_PARAMETER, begin);
    params.put(AppointmentBookServlet.END_PARAMETER, end);

    Response response = http.get(params);
    checkResponseCode(HTTP_OK, response);
    return response.getContent();
  }

  private void checkResponseCode(int expected, Response response) {
    if (response.getHttpStatusCode() != expected) {
      throw new HttpRequestHelper.RestException(response.getHttpStatusCode(), response.getContent());
    }
  }
}
