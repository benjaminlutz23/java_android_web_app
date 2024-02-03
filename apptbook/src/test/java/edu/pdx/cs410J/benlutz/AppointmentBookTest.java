package edu.pdx.cs410J.benlutz;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collection;

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

}
