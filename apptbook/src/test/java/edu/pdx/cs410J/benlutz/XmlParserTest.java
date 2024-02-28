package edu.pdx.cs410J.benlutz;

import edu.pdx.cs410J.ParserException;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

public class XmlParserTest {

    @Test
    public void testParseEmptyAppointmentBook() throws ParserException {
        String xml = "<appointmentBook><owner>Owner Name</owner></appointmentBook>";
        ByteArrayInputStream xmlStream = new ByteArrayInputStream(xml.getBytes());
        XmlParser parser = new XmlParser(xmlStream);

        AppointmentBook book = parser.parse();
        assertEquals("Owner Name", book.getOwnerName(), "Owner name should match");
        assertTrue(book.getAppointments().isEmpty(), "Appointment book should be empty");
    }

    @Test
    public void testParseSingleAppointment() throws ParserException {
        String xml = "<appointmentBook><owner>Owner Name</owner>"
                + "<appointment><description>Test Appointment</description>"
                + "<beginTime>01/01/2024 10:00 AM</beginTime>"
                + "<endTime>01/01/2024 11:00 AM</endTime></appointment></appointmentBook>";
        ByteArrayInputStream xmlStream = new ByteArrayInputStream(xml.getBytes());
        XmlParser parser = new XmlParser(xmlStream);

        AppointmentBook book = parser.parse();
        assertEquals(1, book.getAppointments().size(), "Appointment book should have one appointment");

        Appointment appointment = book.getAppointments().iterator().next();
        assertEquals("Test Appointment", appointment.getDescription(), "Appointment description should match");
    }

    @Test
    public void testParseMultipleAppointments() throws ParserException {
        String xml = "<appointmentBook><owner>Owner Name</owner>"
                + "<appointment><description>First Appointment</description>"
                + "<beginTime>01/01/2024 10:00 AM</beginTime>"
                + "<endTime>01/01/2024 11:00 AM</endTime></appointment>"
                + "<appointment><description>Second Appointment</description>"
                + "<beginTime>01/02/2024 10:00 AM</beginTime>"
                + "<endTime>01/02/2024 11:00 AM</endTime></appointment></appointmentBook>";
        ByteArrayInputStream xmlStream = new ByteArrayInputStream(xml.getBytes());
        XmlParser parser = new XmlParser(xmlStream);

        AppointmentBook book = parser.parse();
        assertEquals(2, book.getAppointments().size(), "Appointment book should have two appointments");
    }

    @Test
    public void cantParseInvalidXmlFormat() {
        String invalidXml = "<appointmentBook><owner>Owner Name</owner>"
                + "<appointment><description>Test Appointment</description>"
                + "<beginTime>01/01/2024 10:00 AM</beginTime>" // Missing endTime tag
                + "</appointment></appointmentBook>";

        InputStream xmlStream = new ByteArrayInputStream(invalidXml.getBytes());
        XmlParser parser = new XmlParser(xmlStream);

        assertThrows(ParserException.class, parser::parse);
    }
}
