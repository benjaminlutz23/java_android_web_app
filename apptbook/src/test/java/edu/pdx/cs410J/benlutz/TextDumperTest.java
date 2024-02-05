package edu.pdx.cs410J.benlutz;

import edu.pdx.cs410J.ParserException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class TextDumperTest {

  private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");

  @Test
  void appointmentBookOwnerAndAppointmentsAreDumpedInTextFormat() throws IOException, invalidOwnerException, invalidDescriptionException {
    String owner = "Test Appointment Book";
    ZonedDateTime beginTime = ZonedDateTime.now();
    ZonedDateTime endTime = beginTime.plusHours(1);
    Appointment appointment = new Appointment("Test Description", beginTime, endTime);
    AppointmentBook book = new AppointmentBook(owner);
    book.addAppointment(appointment);

    StringWriter sw = new StringWriter();
    TextDumper dumper = new TextDumper(sw);
    dumper.dump(book);

    String text = sw.toString();
    assertThat(text, containsString(owner));
    String expectedAppointmentString = String.format("%s, %s, %s",
            appointment.getDescription(),
            DATE_TIME_FORMATTER.format(beginTime),
            DATE_TIME_FORMATTER.format(endTime));
    assertThat(text, containsString(expectedAppointmentString));
  }


  // Do this one after writing the text parser class
  @Test
  void canParseTextWrittenByTextDumper(@TempDir File tempDir) throws IOException, ParserException, invalidOwnerException {
    String owner = "Test Appointment Book";
    AppointmentBook book = new AppointmentBook(owner);

    File textFile = new File(tempDir, "apptbook.txt");
    TextDumper dumper = new TextDumper(new FileWriter(textFile));
    dumper.dump(book);

    TextParser parser = new TextParser(new FileReader(textFile));
    AppointmentBook read = parser.parse();
    assertThat(read.getOwnerName(), equalTo(owner));
  }

}
