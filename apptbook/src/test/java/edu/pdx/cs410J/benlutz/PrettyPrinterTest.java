package edu.pdx.cs410J.benlutz;

import edu.pdx.cs410J.ParserException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.Duration;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class PrettyPrinterTest {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("M/d/yyyy h:mm a VV");


    @Test
    void appointmentBookOwnerIsPrettyPrintedInTextFormat() throws invalidOwnerException {
        // Create an appointment book with a specific owner
        String owner = "Test Appointment Book";
        AppointmentBook book = new AppointmentBook(owner);

        // Dump the appointment book to text format
        StringWriter sw = new StringWriter();
        PrettyPrinter prettyPrinter = new PrettyPrinter(sw);
        prettyPrinter.dump(book);

        // Verify that the owner's name is correctly dumped
        String text = sw.toString();
        assertThat(text, containsString(owner));
    }

    @Test
    void appointmentDetailsArePrettyPrintedInTextFormat() throws invalidOwnerException, invalidDescriptionException {
        // Create an appointment with specific details
        ZonedDateTime beginTime = ZonedDateTime.now();
        ZonedDateTime endTime = beginTime.plusHours(1);
        Appointment appointment = new Appointment("Test Description", beginTime, endTime);
        AppointmentBook book = new AppointmentBook("Test Appointment Book");
        book.addAppointment(appointment);

        // Dump the appointment book to text format
        StringWriter sw = new StringWriter();
        PrettyPrinter prettyPrinter = new PrettyPrinter(sw);
        prettyPrinter.dump(book);

        // Verify that the appointment details are correctly dumped
        String text = sw.toString();
        String expectedAppointmentString = String.format(
                "Appointment Book for: Test Appointment Book%n" +
                        "Appointments:%n" +
                        "---------------%n" +
                        "Description: %s%n" +
                        "From: %s%n" +
                        "To:   %s (%d minutes)%n%n", // Note the extra newline character at the end
                appointment.getDescription(),
                DATE_TIME_FORMATTER.format(beginTime),
                DATE_TIME_FORMATTER.format(endTime),
                Duration.between(beginTime, endTime).toMinutes() // Calculate duration as in PrettyPrinter
        );
        assertThat(text, containsString(expectedAppointmentString));
    }
}
