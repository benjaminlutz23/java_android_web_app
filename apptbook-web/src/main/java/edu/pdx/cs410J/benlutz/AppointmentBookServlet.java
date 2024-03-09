package edu.pdx.cs410J.benlutz;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class AppointmentBookServlet extends HttpServlet {

    static final String OWNER_PARAMETER = "owner";
    static final String DESCRIPTION_PARAMETER = "description";
    static final String BEGIN_PARAMETER = "begin";
    static final String END_PARAMETER = "end";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("M/d/yyyy h:mm a VV");

    private final Map<String, AppointmentBook> appointmentBooks = new HashMap<>();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String owner = request.getParameter(OWNER_PARAMETER);
        String begin = request.getParameter(BEGIN_PARAMETER);
        String end = request.getParameter(END_PARAMETER);

        AppointmentBook book = appointmentBooks.get(owner);
        if (book == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "No appointment book for owner: " + owner);
            return;
        }

        PrintWriter pw = response.getWriter();
        TextDumper dumper = new TextDumper(pw);

        // If begin and end parameters are provided, filter appointments within the range.
        if (begin != null && end != null) {
            AppointmentBook filteredBook = null;
            try {
                filteredBook = new AppointmentBook(owner);
            } catch (invalidOwnerException e) {
                throw new RuntimeException(e);
            }
            ZonedDateTime beginTime = ZonedDateTime.parse(begin, DATE_TIME_FORMATTER);
            ZonedDateTime endTime = ZonedDateTime.parse(end, DATE_TIME_FORMATTER);

            for (Appointment appointment : book.getAppointments()) {
                if (!appointment.getBeginTime().isBefore(beginTime) && !appointment.getEndTime().isAfter(endTime)) {
                    filteredBook.addAppointment(appointment);
                }
            }

            dumper.dump(filteredBook);
        } else {
            // Dump the entire book if no range is specified
            dumper.dump(book);
        }

        response.setContentType("text/plain");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String owner = request.getParameter(OWNER_PARAMETER);
        String description = request.getParameter(DESCRIPTION_PARAMETER);
        String begin = request.getParameter(BEGIN_PARAMETER);
        String end = request.getParameter(END_PARAMETER);

        ZonedDateTime beginTime = ZonedDateTime.parse(begin, DATE_TIME_FORMATTER);
        ZonedDateTime endTime = ZonedDateTime.parse(end, DATE_TIME_FORMATTER);
        Appointment appointment = null;
        try {
            appointment = new Appointment(description, beginTime, endTime);
        } catch (invalidDescriptionException e) {
            throw new RuntimeException(e);
        }

        AppointmentBook book = null;
        try {
            book = appointmentBooks.getOrDefault(owner, new AppointmentBook(owner));
        } catch (invalidOwnerException e) {
            throw new RuntimeException(e);
        }
        book.addAppointment(appointment);
        appointmentBooks.put(owner, book);

        PrintWriter pw = response.getWriter();
        pw.println("Appointment added for " + owner);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
