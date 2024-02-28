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

/**
 * The {@code XmlDumper} class implements the {@code AppointmentBookDumper} interface,
 * enabling {@code AppointmentBook} instances to be written in XML format. This
 * class generates XML documents that adhere to a specific DTD and writes them to a provided
 * {@code Writer}, such as a file writer.
 */
public class XmlDumper implements AppointmentBookDumper<AppointmentBook> {
    private final Writer writer;

    /**
     * Constructs a new {@code XmlDumper} instance that will write to the given writer.
     *
     * @param writer The {@code Writer} where the XML document is to be written.
     */
    public XmlDumper(Writer writer) {
        this.writer = writer;
    }

    /**
     * Serializes an {@code AppointmentBook} into XML and writes it to the specified {@code Writer}.
     *
     * @param book The {@code AppointmentBook} to be serialized into XML.
     * @throws IOException If an I/O error occurs during the writing process.
     */
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

    /**
     * Creates and returns an XML {@code Element} representing a date and time, including timezone,
     * for either the start or end of an appointment. This method structures the date and time
     * according to the requirements of the DTD.
     *
     * @param doc       The XML {@code Document} to which the element will belong.
     * @param tag       The tag name for the element, typically "begin" or "end".
     * @param dateTime  The {@code ZonedDateTime} representing the date and time to be formatted.
     * @return An XML {@code Element} representing the specified date and time.
     */
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

