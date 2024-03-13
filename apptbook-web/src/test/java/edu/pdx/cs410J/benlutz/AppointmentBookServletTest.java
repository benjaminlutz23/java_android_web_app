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
<<<<<<< HEAD
import static org.mockito.Mockito.*;

/**
 * A unit test for the {@link AppointmentBookServlet}. It uses Mockito to
 * provide mock HTTP requests and responses.
=======
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

/**
 * A unit test for the {@link AppointmentBookServlet}.  It uses mockito to
 * provide mock http requests and responses.
>>>>>>> Fresh-Start
 */
public class AppointmentBookServletTest {

  @Test
<<<<<<< HEAD
  void initiallyServletContainsNoAppointmentBooks() throws ServletException, IOException {
    AppointmentBookServlet servlet = new AppointmentBookServlet();

    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getParameter(AppointmentBookServlet.OWNER_PARAMETER)).thenReturn("TEST OWNER");
=======
  void initiallyServletContainsNoAppointmentBooks() throws IOException {
    AppointmentBookServlet servlet = new AppointmentBookServlet();

    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    PrintWriter pw = mock(PrintWriter.class);

    when(response.getWriter()).thenReturn(pw);

    servlet.doGet(request, response);

    verify(pw, never()).println(anyString());
    verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing required parameter: owner");
  }


  @Test
  void addOneAppointmentToAppointmentBook() throws IOException {
    AppointmentBookServlet servlet = new AppointmentBookServlet();

    String owner = "TEST OWNER";
    String description = "TEST DESCRIPTION";
    String beginTime = "01/01/2023 12:00 PM America/Los_Angeles";
    String endTime = "01/01/2023 01:00 PM America/Los_Angeles";

    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getParameter(AppointmentBookServlet.OWNER_PARAMETER)).thenReturn(owner);
    when(request.getParameter(AppointmentBookServlet.DESCRIPTION_PARAMETER)).thenReturn(description);
    when(request.getParameter(AppointmentBookServlet.BEGIN_PARAMETER)).thenReturn(beginTime);
    when(request.getParameter(AppointmentBookServlet.END_PARAMETER)).thenReturn(endTime);
>>>>>>> Fresh-Start

    HttpServletResponse response = mock(HttpServletResponse.class);
    StringWriter stringWriter = new StringWriter();
    PrintWriter pw = new PrintWriter(stringWriter);

    when(response.getWriter()).thenReturn(pw);

<<<<<<< HEAD
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

=======
    servlet.doPost(request, response);

    String expectedMessage = Messages.definedWordAs(owner, description);
    assertThat(stringWriter.toString(), containsString(expectedMessage));

    verify(response).setStatus(HttpServletResponse.SC_OK);

    AppointmentBook book = servlet.getAppointmentBook(owner);
    assertNotNull(book);
    assertEquals(1, book.getAppointments().size());
    Appointment appointment = book.getAppointments().iterator().next();
    assertEquals(description, appointment.getDescription());
  }


}
>>>>>>> Fresh-Start
