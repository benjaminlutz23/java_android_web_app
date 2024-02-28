package edu.pdx.cs410J.benlutz;

import edu.pdx.cs410J.AppointmentBookParser;
import edu.pdx.cs410J.ParserException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.time.LocalDateTime;
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

            NodeList nList = doc.getElementsByTagName("appointment");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a");
            ZoneId zoneId = ZoneId.systemDefault(); // Use the system's default timezone or specify one like ZoneId.of("America/New_York")

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    String description = eElement.getElementsByTagName("description").item(0).getTextContent();
                    String beginTimeString = eElement.getElementsByTagName("beginTime").item(0).getTextContent();
                    String endTimeString = eElement.getElementsByTagName("endTime").item(0).getTextContent();

                    LocalDateTime beginLocalDateTime = LocalDateTime.parse(beginTimeString, formatter);
                    LocalDateTime endLocalDateTime = LocalDateTime.parse(endTimeString, formatter);
                    ZonedDateTime beginTime = ZonedDateTime.of(beginLocalDateTime, zoneId);
                    ZonedDateTime endTime = ZonedDateTime.of(endLocalDateTime, zoneId);

                    Appointment appointment = new Appointment(description, beginTime, endTime);
                    book.addAppointment(appointment);
                }
            }
            return book;
        } catch (Exception | invalidDescriptionException e) {
            throw new ParserException("Error parsing XML", e);
        } catch (invalidOwnerException e) {
            throw new RuntimeException(e);
        }
    }
}
