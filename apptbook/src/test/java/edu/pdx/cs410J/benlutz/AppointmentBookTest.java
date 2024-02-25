package edu.pdx.cs410J.benlutz;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.time.format.DateTimeFormatter;
import java.util.SortedSet;
import java.util.TreeSet;


import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.*;

public class AppointmentBookTest {
    private AppointmentBook book;
    private Appointment appointment;


    private ZonedDateTime createZonedDateTime(String date, String time) {
        LocalDateTime localDateTime = LocalDateTime.parse(date + "T" + time);
        return localDateTime.atZone(ZoneId.systemDefault());
    }

    @BeforeEach
    public void setUp() throws invalidDescriptionException, invalidOwnerException {
        book = new AppointmentBook("Owner");
        ZonedDateTime begin = createZonedDateTime("2000-11-30", "07:00");
        ZonedDateTime end = createZonedDateTime("2000-11-30", "09:00");
        appointment = new Appointment("Description", begin, end);
    }

    @Test
    public void getOwnerNameReturnsCorrectOwnerName() {
        assertEquals("Owner", book.getOwnerName());
    }

    @Test
    public void emptyOwnerNameFieldPrintsErrorToStandardError() {
        assertThrows(invalidOwnerException.class, () ->
                new AppointmentBook("")
        );
    }

    @Test
    public void initiallyGetAppointmentsReturnsEmptyCollection() {
        Collection<Appointment> appointments = book.getAppointments();
        assertTrue(appointments.isEmpty());
    }

    @Test
    public void addAppointmentAddsAppointmentToBook() {
        book.addAppointment(appointment);
        Collection<Appointment> appointments = book.getAppointments();
        assertEquals(1, appointments.size());
        assertTrue(appointments.contains(appointment));
    }

    @Test
    public void getAppointmentsReturnsCollectionWithAddedAppointment() {
        book.addAppointment(appointment);
        Collection<Appointment> appointments = book.getAppointments();
        assertFalse(appointments.isEmpty());
        assertTrue(appointments.contains(appointment));
    }

    @Test
    public void appointmentsAreSortedCorrectly() throws invalidDescriptionException, invalidOwnerException {
        AppointmentBook book = new AppointmentBook("Test Owner");

        DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("M/d/yyyy h:mm a VV");

        // Creating appointments with various start/end times and descriptions
        Appointment appt1 = new Appointment("Meeting",
                ZonedDateTime.parse("01/01/2023 10:00 AM America/Los_Angeles", DATE_TIME_FORMAT),
                ZonedDateTime.parse("01/01/2023 11:00 AM America/Los_Angeles", DATE_TIME_FORMAT));

        Appointment appt2 = new Appointment("Appointment",
                ZonedDateTime.parse("01/01/2023 9:00 AM America/Los_Angeles", DATE_TIME_FORMAT),
                ZonedDateTime.parse("01/01/2023 10:00 AM America/Los_Angeles", DATE_TIME_FORMAT));

        Appointment appt3 = new Appointment("Another Meeting",
                ZonedDateTime.parse("01/01/2023 10:00 AM America/Los_Angeles", DATE_TIME_FORMAT),
                ZonedDateTime.parse("01/01/2023 12:00 PM America/Los_Angeles", DATE_TIME_FORMAT));

        Appointment appt4 = new Appointment("Follow-up",
                ZonedDateTime.parse("01/01/2023 10:00 AM America/Los_Angeles", DATE_TIME_FORMAT),
                ZonedDateTime.parse("01/01/2023 11:00 AM America/Los_Angeles", DATE_TIME_FORMAT));

        // Adding appointments out of order
        book.addAppointment(appt1);
        book.addAppointment(appt2);
        book.addAppointment(appt3);
        book.addAppointment(appt4);

        // Retrieve the sorted appointments
        SortedSet<Appointment> sortedAppointments = new TreeSet<>(book.getAppointments());

        // Verify the order
        assertEquals(sortedAppointments.first(), appt2, "First appointment should be the earliest");
        assertEquals(sortedAppointments.last(), appt3, "Last appointment should be the one with the latest end time");

        // Convert to array to check specific order
        Appointment[] appointmentsArray = sortedAppointments.toArray(new Appointment[0]);
        assertTrue(appointmentsArray[1].equals(appt1) || appointmentsArray[1].equals(appt4),
                "Second or third should be 'Meeting' or 'Follow-up' due to the same start time but sorted lexicographically by their descriptions");
        assertTrue(appointmentsArray[2].equals(appt1) || appointmentsArray[2].equals(appt4),
                "Second or third should be 'Meeting' or 'Follow-up' due to the same start time but sorted lexicographically by their descriptions");
    }

}
