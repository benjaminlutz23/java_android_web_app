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

            // Create the root element and set DTD references using AppointmentBookXmlHelper
            Element rootElement = doc.createElement("appointmentBook");
            doc.appendChild(rootElement);
            doc.setXmlStandalone(true); // Optional: Indicates that the document is standalone

            // Add owner element
            Element ownerElement = doc.createElement("owner");
            ownerElement.appendChild(doc.createTextNode(book.getOwnerName()));
            rootElement.appendChild(ownerElement);

            // Iterate over appointments and add them to the document
            for (Appointment appointment : book.getAppointments()) {
                Element appointmentElement = doc.createElement("appointment");
                rootElement.appendChild(appointmentElement);

                // Description
                Element descElement = doc.createElement("description");
                descElement.appendChild(doc.createTextNode(appointment.getDescription()));
                appointmentElement.appendChild(descElement);

                // BeginTime
                Element beginElement = doc.createElement("beginTime");
                beginElement.appendChild(doc.createTextNode(appointment.getBeginTimeString()));
                appointmentElement.appendChild(beginElement);

                // EndTime
                Element endElement = doc.createElement("endTime");
                endElement.appendChild(doc.createTextNode(appointment.getEndTimeString()));
                appointmentElement.appendChild(endElement);

                // Other elements can be added here as per the DTD
            }

            // Use a Transformer to convert DOM to XML
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, AppointmentBookXmlHelper.SYSTEM_ID);
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);

            StreamResult result = new StreamResult(writer);
            transformer.transform(source, result);

        } catch (Exception e) {
            throw new IOException("Exception while dumping Appointment Book to XML", e);
        }
    }
}
