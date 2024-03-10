package edu.pdx.cs410J.benlutz;

import com.google.common.annotations.VisibleForTesting;
import edu.pdx.cs410J.ParserException;
import edu.pdx.cs410J.web.HttpRequestHelper;

import java.io.IOException;
import java.io.StringReader;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static edu.pdx.cs410J.web.HttpRequestHelper.Response;
import static edu.pdx.cs410J.web.HttpRequestHelper.RestException;
import static java.net.HttpURLConnection.HTTP_OK;

public class AppointmentBookRestClient {
  private static final String WEB_APP = "apptbook";
  private static final String SERVLET = "appointments";
  private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("M/d/yyyy h:mm a VV");

  private final HttpRequestHelper http;

  public AppointmentBookRestClient(String hostName, int port) {
    this(new HttpRequestHelper(String.format("http://%s:%d/%s/%s", hostName, port, WEB_APP, SERVLET)));
  }

  @VisibleForTesting
  AppointmentBookRestClient(HttpRequestHelper http) {
    this.http = http;
  }

  public AppointmentBook getAppointmentBook(String owner) throws IOException, ParserException {
    Response response = http.get(Map.of(AppointmentBookServlet.OWNER_PARAMETER, owner));
    throwExceptionIfNotOkayHttpStatus(response);
    String content = response.getContent();

    TextParser parser = new TextParser(new StringReader(content));
    return parser.parse();
  }

  public AppointmentBook getAppointmentsBetween(String owner, ZonedDateTime begin, ZonedDateTime end) throws IOException, ParserException {
    Map<String, String> parameters = Map.of(
            AppointmentBookServlet.OWNER_PARAMETER, owner,
            AppointmentBookServlet.BEGIN_PARAMETER, begin.format(DATE_TIME_FORMAT),
            AppointmentBookServlet.END_PARAMETER, end.format(DATE_TIME_FORMAT)
    );

    Response response = http.get(parameters);
    throwExceptionIfNotOkayHttpStatus(response);
    String content = response.getContent();

    TextParser parser = new TextParser(new StringReader(content));
    return parser.parse();
  }

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

  @VisibleForTesting
  Response postToMyURL(Map<String, String> dictionaryEntries) throws IOException {
    return http.post(dictionaryEntries);
  }

  public void removeAllAppointmentBooks() throws IOException {
    Response response = http.delete(Map.of());
    throwExceptionIfNotOkayHttpStatus(response);
  }

  private void throwExceptionIfNotOkayHttpStatus(Response response) {
    int code = response.getHttpStatusCode();
    if (code != HTTP_OK) {
      String message = response.getContent();
      throw new RestException(code, message);
    }
  }
}
