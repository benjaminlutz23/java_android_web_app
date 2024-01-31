package edu.pdx.cs410J.benlutz;

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

    @BeforeEach
    public void setUp() throws invalidDescriptionException, invalidDateFormatException, invalidTimeFormatException, invalidOwnerException {
        book = new AppointmentBook("Owner");
        appointment = new Appointment("Description", "07/12/2021", "12:00", "07/12/2021", "13:00");
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
