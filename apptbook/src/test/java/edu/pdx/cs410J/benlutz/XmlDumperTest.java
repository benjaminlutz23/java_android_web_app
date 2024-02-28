package edu.pdx.cs410J.benlutz;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class XmlDumperTest {
    @Test
    public void dumpEmptyAppointmentBook() throws IOException, invalidOwnerException {
        StringWriter writer = new StringWriter();
        AppointmentBook emptyBook = new AppointmentBook("Owner Name");
        XmlDumper dumper = new XmlDumper(writer);

        dumper.dump(emptyBook);
        String xmlOutput = writer.toString();

        assertThat(xmlOutput, containsString("<owner>Owner Name</owner>"));
        assertThat(xmlOutput, not(containsString("<appointment>")));
    }

    @Test
    public void dumpSingleAppointment() throws IOException, invalidOwnerException, invalidDescriptionException {
        StringWriter writer = new StringWriter();
        AppointmentBook book = new AppointmentBook("Owner Name");

        ZonedDateTime beginTime = ZonedDateTime.parse("2023-07-21T09:00:00-07:00[America/Los_Angeles]");
        ZonedDateTime endTime = ZonedDateTime.parse("2023-07-21T10:00:00-07:00[America/Los_Angeles]");
        Appointment appointment = new Appointment("Test Appointment", beginTime, endTime);
        book.addAppointment(appointment);

        XmlDumper dumper = new XmlDumper(writer);
        dumper.dump(book);
        String xmlOutput = writer.toString();

        // Verify that the XML includes the appointment details
        assertThat(xmlOutput, containsString("<description>Test Appointment</description>"));
        assertThat(xmlOutput, containsString("<beginTime>" + appointment.getBeginTimeString() + "</beginTime>"));
        assertThat(xmlOutput, containsString("<endTime>" + appointment.getEndTimeString() + "</endTime>"));
    }

    @Test
    void dumpMultipleAppointments() throws IOException, invalidDescriptionException, invalidOwnerException {
        StringWriter writer = new StringWriter();
        XmlDumper dumper = new XmlDumper(writer);

        AppointmentBook book = new AppointmentBook("Owner");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy h:mm a").withZone(ZoneId.systemDefault());

        ZonedDateTime beginTime1 = ZonedDateTime.parse("01/01/2024 10:00 AM", formatter);
        ZonedDateTime endTime1 = ZonedDateTime.parse("01/01/2024 11:00 AM", formatter);
        book.addAppointment(new Appointment("Meet with Chris", beginTime1, endTime1));

        ZonedDateTime beginTime2 = ZonedDateTime.parse("01/02/2024 12:00 PM", formatter);
        ZonedDateTime endTime2 = ZonedDateTime.parse("01/02/2024 1:00 PM", formatter);
        book.addAppointment(new Appointment("Lunch with Sam", beginTime2, endTime2));

        dumper.dump(book);

        String xmlOutput = writer.toString();
        assertTrue(xmlOutput.contains("Meet with Chris") && xmlOutput.contains("Lunch with Sam"), "Both appointments should be present in the XML");
    }

    @Test
    void dumpAppointmentWithSpecialCharacters() throws IOException, invalidOwnerException, invalidDescriptionException {
        StringWriter writer = new StringWriter();
        XmlDumper dumper = new XmlDumper(writer);

        AppointmentBook book = new AppointmentBook("Owner");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy h:mm a").withZone(ZoneId.systemDefault());

        ZonedDateTime beginTime = ZonedDateTime.parse("01/03/2024 10:00 AM", formatter);
        ZonedDateTime endTime = ZonedDateTime.parse("01/03/2024 11:00 AM", formatter);
        book.addAppointment(new Appointment("Discussion & Decision <Important>", beginTime, endTime));

        dumper.dump(book);

        String xmlOutput = writer.toString();
        assertTrue(xmlOutput.contains("Discussion &amp; Decision &lt;Important&gt;"), "Special characters should be escaped in the XML output");
    }

    @Test
    void dumpIncludesCorrectDtdReference() throws IOException, invalidOwnerException, invalidDescriptionException {
        StringWriter writer = new StringWriter();
        XmlDumper dumper = new XmlDumper(writer);

        AppointmentBook book = new AppointmentBook("Owner");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy h:mm a").withZone(ZoneId.systemDefault());

        ZonedDateTime beginTime = ZonedDateTime.parse("01/04/2024 10:00 AM", formatter);
        ZonedDateTime endTime = ZonedDateTime.parse("01/04/2024 11:00 AM", formatter);
        book.addAppointment(new Appointment("General Meeting", beginTime, endTime));

        dumper.dump(book);

        String xmlOutput = writer.toString();
        assertTrue(xmlOutput.contains("<!DOCTYPE appointmentBook SYSTEM \"http://www.cs.pdx.edu/~whitlock/dtds/apptbook.dtd\">"), "XML output should include the correct DTD reference");
    }

}
