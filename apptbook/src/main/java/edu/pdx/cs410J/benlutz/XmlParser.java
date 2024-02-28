package edu.pdx.cs410J.benlutz;

import edu.pdx.cs410J.AbstractAppointmentBook;
import edu.pdx.cs410J.AppointmentBookParser;
import edu.pdx.cs410J.ParserException;

import java.io.InputStream;

public class XmlParser implements AppointmentBookParser {
    private final InputStream xml;


    public XmlParser(InputStream xml) {
        this.xml = xml;
    }

    @Override
    public AbstractAppointmentBook parse() throws ParserException {
        return null;
    }
}
