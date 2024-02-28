package edu.pdx.cs410J.benlutz;

import edu.pdx.cs410J.AbstractAppointmentBook;
import edu.pdx.cs410J.AppointmentBookParser;
import edu.pdx.cs410J.ParserException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class XmlParser implements AppointmentBookParser<AppointmentBook> {
    private final InputStream xml;

    public XmlParser(InputStream xml) {
        this.xml = xml;
    }

    @Override
    public AppointmentBook parse() throws ParserException {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xml);
            doc.getDocumentElement().normalize();

            String owner = doc.getDocumentElement().getElementsByTagName("owner").item(0).getTextContent();
            AppointmentBook book = new AppointmentBook(owner);

            NodeList appts = doc.getElementsByTagName("appt");
            for (int i = 0; i < appts.getLength(); i++) {
                Node apptNode = appts.item(i);
                if (apptNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element apptElement = (Element) apptNode;

                    String description = apptElement.getElementsByTagName("description").item(0).getTextContent();
                    ZonedDateTime beginTime = parseDateTime(apptElement, "begin");
                    ZonedDateTime endTime = parseDateTime(apptElement, "end");

                    Appointment appointment = new Appointment(description, beginTime, endTime);
                    book.addAppointment(appointment);
                }
            }
            return book;
        } catch (Exception | invalidOwnerException e) {
            throw new ParserException("Error parsing XML", e);
        } catch (invalidDescriptionException e) {
            throw new RuntimeException(e);
        }
    }

    private ZonedDateTime parseDateTime(Element parentElement, String tagName) {
        Element dateTimeElement = (Element) parentElement.getElementsByTagName(tagName).item(0);
        Element dateElement = (Element) dateTimeElement.getElementsByTagName("date").item(0);
        Element timeElement = (Element) dateTimeElement.getElementsByTagName("time").item(0);

        int day = Integer.parseInt(dateElement.getAttribute("day"));
        int month = Integer.parseInt(dateElement.getAttribute("month"));
        int year = Integer.parseInt(dateElement.getAttribute("year"));
        int hour = Integer.parseInt(timeElement.getAttribute("hour"));
        int minute = Integer.parseInt(timeElement.getAttribute("minute"));
        ZoneId zone = ZoneId.of(timeElement.getAttribute("time-zone"));

        return ZonedDateTime.of(year, month, day, hour, minute, 0, 0, zone);
    }
}

