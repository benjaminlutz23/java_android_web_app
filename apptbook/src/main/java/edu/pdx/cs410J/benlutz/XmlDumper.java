package edu.pdx.cs410J.benlutz;

import edu.pdx.cs410J.AppointmentBookDumper;

import java.io.OutputStream;
import java.io.PrintWriter;

public class XmlDumper implements AppointmentBookDumper<AppointmentBook> {
    private final PrintWriter writer;

    public XmlDumper(OutputStream os) {
        this.writer = new PrintWriter(os);
    }
}
