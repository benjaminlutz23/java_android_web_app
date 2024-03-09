package edu.pdx.cs410J.benlutz;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

/**
 * A unit test for the {@link AppointmentBookServlet}. It uses Mockito to
 * provide mock HTTP requests and responses.
 */
public class AppointmentBookServletTest {

  @Test
  void initiallyServletContainsNoAppointmentBooks() throws ServletException, IOException {
    AppointmentBookServlet servlet = new AppointmentBookServlet();

    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getParameter(AppointmentBookServlet.OWNER_PARAMETER)).thenReturn("TEST OWNER");

    HttpServletResponse response = mock(HttpServletResponse.class);
    StringWriter stringWriter = new StringWriter();
    PrintWriter pw = new PrintWriter(stringWriter);

    when(response.getWriter()).thenReturn(pw);

    servlet.doGet(request, response);

    verify(response).sendError(HttpServletResponse.SC_NOT_FOUND, "No appointment book for owner: TEST OWNER");
  }

  @Test
  void addOneAppointmentToAppointmentBook() throws ServletException, IOException {
    AppointmentBookServlet servlet = new AppointmentBookServlet();

    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);

    when(request.getParameter(AppointmentBookServlet.OWNER_PARAMETER)).thenReturn("Dave");
    when(request.getParameter(AppointmentBookServlet.DESCRIPTION_PARAMETER)).thenReturn("Teach Java Class");
    when(request.getParameter(AppointmentBookServlet.BEGIN_PARAMETER)).thenReturn("10/19/2024 6:00 PM America/Los_Angeles");
    when(request.getParameter(AppointmentBookServlet.END_PARAMETER)).thenReturn("10/19/2024 9:30 PM America/Los_Angeles");

    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    when(response.getWriter()).thenReturn(pw);

    servlet.doPost(request, response);

    ArgumentCaptor<Integer> statusCodeCaptor = ArgumentCaptor.forClass(Integer.class);
    verify(response).setStatus(statusCodeCaptor.capture());

    assertThat(statusCodeCaptor.getValue(), equalTo(HttpServletResponse.SC_OK));
    assertThat(sw.toString(), containsString("Appointment added for Dave"));

    // Verify doGet with owner name returns the appointment added
    sw = new StringWriter(); // Reset StringWriter to capture doGet response
    pw = new PrintWriter(sw);
    when(response.getWriter()).thenReturn(pw);

    servlet.doGet(request, response);
    assertThat(sw.toString(), containsString("Teach Java Class"));
  }
}

