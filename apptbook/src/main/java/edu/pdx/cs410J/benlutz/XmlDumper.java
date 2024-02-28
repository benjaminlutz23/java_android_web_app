package edu.pdx.cs410J.benlutz;

import edu.pdx.cs410J.AppointmentBookDumper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.Writer;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class XmlDumper implements AppointmentBookDumper<AppointmentBook> {
    private final Writer writer;

    public XmlDumper(Writer writer) {
        this.writer = writer;
    }

    @Override
    public void dump(AppointmentBook book) throws IOException {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();

            Element rootElement = doc.createElement("apptbook");
            doc.appendChild(rootElement);

            Element ownerElement = doc.createElement("owner");
            ownerElement.appendChild(doc.createTextNode(book.getOwnerName()));
            rootElement.appendChild(ownerElement);

            for (Appointment appointment : book.getAppointments()) {
                Element apptElement = doc.createElement("appt");
                rootElement.appendChild(apptElement);

                Element beginElement = createDateTimeElement(doc, "begin", appointment.getBeginTime());
                apptElement.appendChild(beginElement);

                Element endElement = createDateTimeElement(doc, "end", appointment.getEndTime());
                apptElement.appendChild(endElement);

                Element descElement = doc.createElement("description");
                descElement.appendChild(doc.createTextNode(appointment.getDescription()));
                apptElement.appendChild(descElement);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "http://www.cs.pdx.edu/~whitlock/dtds/apptbook.dtd");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "us-ascii");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(writer);
            transformer.transform(source, result);

        } catch (Exception e) {
            throw new IOException("Exception while dumping Appointment Book to XML", e);
        }
    }

    private Element createDateTimeElement(Document doc, String tag, ZonedDateTime dateTime) {
        Element dateTimeElement = doc.createElement(tag);

        Element dateElement = doc.createElement("date");
        dateElement.setAttribute("day", String.valueOf(dateTime.getDayOfMonth()));
        dateElement.setAttribute("month", String.valueOf(dateTime.getMonthValue()));
        dateElement.setAttribute("year", String.valueOf(dateTime.getYear()));
        dateTimeElement.appendChild(dateElement);

        Element timeElement = doc.createElement("time");
        timeElement.setAttribute("hour", String.valueOf(dateTime.getHour()));
        timeElement.setAttribute("minute", String.valueOf(dateTime.getMinute()));
        timeElement.setAttribute("time-zone", dateTime.getZone().toString());
        dateTimeElement.appendChild(timeElement);

        return dateTimeElement;
    }
}

