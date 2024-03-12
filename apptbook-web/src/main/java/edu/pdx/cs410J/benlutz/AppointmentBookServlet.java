package edu.pdx.cs410J.benlutz;

import com.google.common.annotations.VisibleForTesting;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * This servlet ultimately provides a REST API for working with an
 * <code>AppointmentBook</code>.  However, in its current state, it is an example
 * of how to use HTTP and Java servlets to store simple appointmentBooks of words
 * and their definitions.
 */
public class AppointmentBookServlet extends HttpServlet
{
    static final String OWNER_PARAMETER = "owner";
    static final String DESCRIPTION_PARAMETER = "description";
    static final String BEGIN_PARAMETER = "begin time";
    static final String END_PARAMETER = "end time";

    private final Map<String, AppointmentBook> appointmentBooks = new HashMap<>();
    private static final DateTimeFormatter DATE_TIME_FORMAT = new DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .appendPattern("M/d/yyyy h:mm a VV")
            .toFormatter();

    /**
     * Handles an HTTP GET request from a client by writing the definition of the
     * word specified in the "word" HTTP parameter to the HTTP response.  If the
     * "word" parameter is not specified, all of the entries in the appointmentBooks
     * are written to the HTTP response.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");

        String owner = getParameter(OWNER_PARAMETER, request);
        String beginTimeString = getParameter(BEGIN_PARAMETER, request);
        String endTimeString = getParameter(END_PARAMETER, request);

        if (owner == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing required parameter: " + OWNER_PARAMETER);
            return;
        }

        AppointmentBook book = appointmentBooks.get(owner);
        if (book == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "No appointment book found for owner: " + owner);
            return;
        }

        PrintWriter pw = response.getWriter();
        TextDumper dumper = new TextDumper(pw);

        if (beginTimeString != null && endTimeString != null) {
            // Both begin and end time parameters are provided, filter appointments
            try {
                ZonedDateTime beginTime = ZonedDateTime.parse(beginTimeString, DATE_TIME_FORMAT);
                ZonedDateTime endTime = ZonedDateTime.parse(endTimeString, DATE_TIME_FORMAT);

                AppointmentBook filteredBook = new AppointmentBook(owner);
                for (Appointment appointment : book.getAppointments()) {
                    if (appointment.getBeginTime().isBefore(endTime) && appointment.getEndTime().isAfter(beginTime)) {
                        filteredBook.addAppointment(appointment);
                    }
                }

                if (filteredBook.getAppointments().isEmpty()) {
                    pw.println("No appointments found between the specified times for owner: " + owner);
                } else {
                    dumper.dump(filteredBook);
                }
            } catch (DateTimeParseException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid date/time format for begin or end time");
                return;
            } catch (invalidOwnerException e) {
                throw new RuntimeException(e);
            }
        } else {
            // No begin and end time parameters are provided, return all appointments for the owner
            dumper.dump(book);
        }

        pw.flush();
        response.setStatus(HttpServletResponse.SC_OK);
    }

    /**
     * Handles an HTTP POST request by storing the appointmentBooks entry for the
     * "word" and "definition" request parameters.  It writes the appointmentBooks
     * entry to the HTTP response.
     */
    @Override
    protected void doPost( HttpServletRequest request, HttpServletResponse response ) throws IOException
    {
        response.setContentType( "text/plain" );

        String owner = getParameter(OWNER_PARAMETER, request );
        if (owner == null) {
            missingRequiredParameter(response, OWNER_PARAMETER);
            return;
        }

        String description = getParameter(DESCRIPTION_PARAMETER, request );
        if ( description == null) {
            missingRequiredParameter( response, DESCRIPTION_PARAMETER);
            return;
        }

        String beginTimeString = getParameter(BEGIN_PARAMETER, request);
        if (beginTimeString == null) {
            missingRequiredParameter(response, BEGIN_PARAMETER);
            return;
        }

        String endTimeString = getParameter(END_PARAMETER, request);
        if (endTimeString == null) {
            missingRequiredParameter(response, END_PARAMETER);
            return;
        }

        ZonedDateTime beginTime;
        ZonedDateTime endTime;
        try {
            beginTime = ZonedDateTime.parse(beginTimeString, DATE_TIME_FORMAT);
            endTime = ZonedDateTime.parse(endTimeString, DATE_TIME_FORMAT);
        } catch (DateTimeParseException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Failed to parse date/time: " + e.getMessage());
            return;
        }
        addAppointmentToBook(owner, description, beginTime, endTime);

        PrintWriter pw = response.getWriter();
        pw.println(Messages.definedWordAs(owner, description));
        pw.flush();

        response.setStatus( HttpServletResponse.SC_OK);
    }

    /**
     * Adds a given appointment to the owner's appointment book. If the appointment book
     * does not exist for the given owner, it is created. Handles exceptions related to
     * invalid appointment descriptions or owner names.
     *
     * @param owner       The owner of the appointment book.
     * @param description The description of the appointment.
     * @param beginTime   The ZonedDateTime object representing the start time of the appointment.
     * @param endTime     The ZonedDateTime object representing the end time of the appointment.
     */
    private void addAppointmentToBook(String owner, String description, ZonedDateTime beginTime, ZonedDateTime endTime) {
        try {
            AppointmentBook appointmentBook = this.appointmentBooks.computeIfAbsent(owner, k -> {
                try {
                    return new AppointmentBook(k);
                } catch (invalidOwnerException e) {
                    // Handle the invalid owner case here
                    // For example, log the error and/or throw a runtime exception
                    throw new RuntimeException("Invalid owner: " + owner, e);
                }
            });
            appointmentBook.addAppointment(new Appointment(description, beginTime, endTime));
        } catch (RuntimeException | invalidDescriptionException e) {
            // Handle or log the exception as needed
            System.err.println("Exception when adding an appointment: " + e.getMessage());
        }
    }


    /**
     * Handles an HTTP DELETE request by removing all appointmentBooks entries.  This
     * behavior is exposed for testing purposes only.  It's probably not
     * something that you'd want a real application to expose.
     */
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");

        this.appointmentBooks.clear();

        PrintWriter pw = response.getWriter();
        pw.println(Messages.allAppointmentBooksDeleted());
        pw.flush();

        response.setStatus(HttpServletResponse.SC_OK);

    }

    /**
     * Writes an error message about a missing parameter to the HTTP response.
     *
     * The text of the error message is created by {@link Messages#missingRequiredParameter(String)}
     */
    private void missingRequiredParameter( HttpServletResponse response, String parameterName )
        throws IOException
    {
        String message = Messages.missingRequiredParameter(parameterName);
        response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, message);
    }

    /**
     * Writes the definition of the given word to the HTTP response.
     *
     * The text of the message is formatted with {@link TextDumper}
     */
    private void writeAppointmentBook(String owner, HttpServletResponse response) throws IOException {
        AppointmentBook book = this.appointmentBooks.get(owner);

        if (book == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);

        } else {
            PrintWriter pw = response.getWriter();

            TextDumper dumper = new TextDumper(pw);
            dumper.dump(book);

            response.setStatus(HttpServletResponse.SC_OK);
        }
    }

    /**
     * Returns the value of the HTTP request parameter with the given name.
     *
     * @return <code>null</code> if the value of the parameter is
     *         <code>null</code> or is the empty string
     */
    private String getParameter(String name, HttpServletRequest request) {
      String value = request.getParameter(name);
      if (value == null || value.isEmpty()) {
        return null;

      } else {
        return value;
      }
    }

    @VisibleForTesting
    AppointmentBook getAppointmentBook(String word) {
        return this.appointmentBooks.get(word);
    }
}
