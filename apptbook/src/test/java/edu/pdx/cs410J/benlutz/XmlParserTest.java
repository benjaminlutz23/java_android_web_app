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
    public void testParseSingleAppointment() throws ParserException, invalidOwnerException, invalidDescriptionException {
        String xml = "<apptbook>" +
                "<owner>Owner Name</owner>" +
                "<appt>" +
                "<description>Test Appointment</description>" +
                "<begin>" +
                "<date day=\"1\" month=\"1\" year=\"2024\"/>" +
                "<time hour=\"10\" minute=\"0\" time-zone=\"America/New_York\"/>" +
                "</begin>" +
                "<end>" +
                "<date day=\"1\" month=\"1\" year=\"2024\"/>" +
                "<time hour=\"11\" minute=\"0\" time-zone=\"America/New_York\"/>" +
                "</end>" +
                "</appt>" +
                "</apptbook>";
        ByteArrayInputStream xmlStream = new ByteArrayInputStream(xml.getBytes());
        XmlParser parser = new XmlParser(xmlStream);

        AppointmentBook book = parser.parse();
        assertEquals(1, book.getAppointments().size(), "Appointment book should have one appointment");

        Appointment appointment = book.getAppointments().iterator().next();
        assertEquals("Test Appointment", appointment.getDescription(), "Appointment description should match");
    }

    @Test
    public void testParseMultipleAppointments() throws ParserException, invalidOwnerException, invalidDescriptionException {
        String xml = "<apptbook>" +
                "<owner>Owner Name</owner>" +
                "<appt>" +
                "<description>First Appointment</description>" +
                "<begin>" +
                "<date day=\"1\" month=\"1\" year=\"2024\"/>" +
                "<time hour=\"10\" minute=\"0\" time-zone=\"America/New_York\"/>" +
                "</begin>" +
                "<end>" +
                "<date day=\"1\" month=\"1\" year=\"2024\"/>" +
                "<time hour=\"11\" minute=\"0\" time-zone=\"America/New_York\"/>" +
                "</end>" +
                "</appt>" +
                "<appt>" +
                "<description>Second Appointment</description>" +
                "<begin>" +
                "<date day=\"2\" month=\"1\" year=\"2024\"/>" +
                "<time hour=\"10\" minute=\"0\" time-zone=\"America/New_York\"/>" +
                "</begin>" +
                "<end>" +
                "<date day=\"2\" month=\"1\" year=\"2024\"/>" +
                "<time hour=\"11\" minute=\"0\" time-zone=\"America/New_York\"/>" +
                "</end>" +
                "</appt>" +
                "</apptbook>";
        ByteArrayInputStream xmlStream = new ByteArrayInputStream(xml.getBytes());
        XmlParser parser = new XmlParser(xmlStream);

        AppointmentBook book = parser.parse();
        assertEquals(2, book.getAppointments().size(), "Appointment book should have two appointments");
    }

    @Test
    public void cantParseInvalidXmlFormat() {
        String invalidXml = "<apptbook>" +
                "<owner>Owner Name</owner>" +
                "<appt>" +
                "<description>Test Appointment</description>" +
                "<begin>" +
                "<date day=\"1\" month=\"1\" year=\"2024\"/>" +
                "<time hour=\"10\" minute=\"0\" time-zone=\"America/New_York\"/>" +
                "</begin>" + // Missing endTime tag
                "</appt>" +
                "</apptbook>";

        InputStream xmlStream = new ByteArrayInputStream(invalidXml.getBytes());
        XmlParser parser = new XmlParser(xmlStream);

        assertThrows(ParserException.class, parser::parse);
    }

}
