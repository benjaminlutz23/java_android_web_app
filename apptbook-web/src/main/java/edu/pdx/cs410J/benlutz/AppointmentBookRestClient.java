package edu.pdx.cs410J.benlutz;

<<<<<<< HEAD
import edu.pdx.cs410J.web.HttpRequestHelper;

import java.io.IOException;
=======
import com.google.common.annotations.VisibleForTesting;
import edu.pdx.cs410J.ParserException;
import edu.pdx.cs410J.web.HttpRequestHelper;

import java.io.IOException;
import java.io.StringReader;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
>>>>>>> Fresh-Start
import java.util.HashMap;
import java.util.Map;

import static edu.pdx.cs410J.web.HttpRequestHelper.Response;
<<<<<<< HEAD
import static java.net.HttpURLConnection.HTTP_OK;

/**
 * A helper class for accessing the REST client to manage appointment books.
=======
import static edu.pdx.cs410J.web.HttpRequestHelper.RestException;
import static java.net.HttpURLConnection.HTTP_OK;

/**
 * Provides REST client functionalities for managing {@link AppointmentBook} objects.
 * This class enables operations such as retrieving, adding, and searching for appointments
 * through HTTP requests to a server.
>>>>>>> Fresh-Start
 */
public class AppointmentBookRestClient {
  private static final String WEB_APP = "apptbook";
  private static final String SERVLET = "appointments";
<<<<<<< HEAD
=======
  private static final DateTimeFormatter DATE_TIME_FORMAT = new DateTimeFormatterBuilder()
          .parseCaseInsensitive()
          .appendPattern("M/d/yyyy h:mm a VV")
          .toFormatter();
>>>>>>> Fresh-Start

  private final HttpRequestHelper http;

  /**
<<<<<<< HEAD
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
=======
   * Constructs an {@link AppointmentBookRestClient} for accessing the appointment book REST service.
   *
   * @param hostName The hostname of the server providing the REST service.
   * @param port     The port on which the server is listening.
   */
  public AppointmentBookRestClient(String hostName, int port) {
    this(new HttpRequestHelper(String.format("http://%s:%d/%s/%s", hostName, port, WEB_APP, SERVLET)));
  }

  @VisibleForTesting
  AppointmentBookRestClient(HttpRequestHelper http) {
    this.http = http;
  }

  /**
   * Retrieves an {@link AppointmentBook} for a given owner from the server.
   *
   * @param owner The owner whose appointment book is to be retrieved.
   * @return The {@link AppointmentBook} of the specified owner.
   * @throws IOException      If an I/O error occurs during the request.
   * @throws ParserException If parsing the server's response fails.
   */
  public AppointmentBook getAppointmentBook(String owner) throws IOException, ParserException {
    Response response = http.get(Map.of(AppointmentBookServlet.OWNER_PARAMETER, owner));
    throwExceptionIfNotOkayHttpStatus(response);
    String content = response.getContent();

    TextParser parser = new TextParser(new StringReader(content));
    return parser.parse();
  }

  /**
   * Retrieves appointments between specified begin and end times for a given owner from the server.
   *
   * @param owner The owner whose appointments are to be retrieved.
   * @param begin The start time of the interval for which appointments are sought.
   * @param end   The end time of the interval for which appointments are sought.
   * @return An {@link AppointmentBook} containing appointments within the specified interval.
   * @throws IOException      If an I/O error occurs during the request.
   * @throws ParserException If parsing the server's response fails.
   */
  public AppointmentBook getAppointmentsBetween(String owner, ZonedDateTime begin, ZonedDateTime end) throws IOException, ParserException, invalidOwnerException {
    Map<String, String> parameters = new HashMap<>();
    parameters.put(AppointmentBookServlet.OWNER_PARAMETER, owner);

    // Only add begin and end parameters if they are not null
    if (begin != null && end != null) {
      parameters.put(AppointmentBookServlet.BEGIN_PARAMETER, begin.format(DATE_TIME_FORMAT));
      parameters.put(AppointmentBookServlet.END_PARAMETER, end.format(DATE_TIME_FORMAT));
    }

    Response response = http.get(parameters);
    throwExceptionIfNotOkayHttpStatus(response);
    String content = response.getContent();

    TextParser parser = new TextParser(new StringReader(content));

    return parser.parse();
  }

  /**
   * Adds an appointment to the appointment book of a specified owner on the server.
   *
   * @param owner       The owner of the appointment book to which the appointment will be added.
   * @param appointment The appointment to be added.
   * @throws IOException If an I/O error occurs during the request.
   */
  public void addAppointment(String owner, Appointment appointment) throws IOException {
    String description = appointment.getDescription();
    ZonedDateTime beginTime = appointment.getBeginTime();
    ZonedDateTime endTime = appointment.getEndTime();
    Response response = postToMyURL(Map.of(
            AppointmentBookServlet.OWNER_PARAMETER, owner,
            AppointmentBookServlet.DESCRIPTION_PARAMETER, description,
            AppointmentBookServlet.BEGIN_PARAMETER, beginTime.format(DATE_TIME_FORMAT),
            AppointmentBookServlet.END_PARAMETER, endTime.format(DATE_TIME_FORMAT)
    ));
    throwExceptionIfNotOkayHttpStatus(response);
  }

  /**
   * Sends a POST request with the given dictionary entries to the server.
   * This method is primarily used for testing and internal purposes.
   *
   * @param dictionaryEntries The entries to be sent in the POST request.
   * @return The response from the server.
   * @throws IOException If an I/O error occurs during the request.
   */
  @VisibleForTesting
  Response postToMyURL(Map<String, String> dictionaryEntries) throws IOException {
    return http.post(dictionaryEntries);
  }

  /**
   * Removes all appointment books from the server. This functionality is intended
   * for testing and should be used with caution.
   *
   * @throws IOException If an I/O error occurs during the request.
   */
  public void removeAllAppointmentBooks() throws IOException {
    Response response = http.delete(Map.of());
    throwExceptionIfNotOkayHttpStatus(response);
  }

  /**
   * Checks the HTTP status code of the server's response and throws an exception if it is not OK (200).
   *
   * @param response The response from the server.
   */
  private void throwExceptionIfNotOkayHttpStatus(Response response) {
    int code = response.getHttpStatusCode();
    if (code != HTTP_OK) {
      String message = response.getContent();
      throw new RestException(code, message);
>>>>>>> Fresh-Start
    }
  }
}
