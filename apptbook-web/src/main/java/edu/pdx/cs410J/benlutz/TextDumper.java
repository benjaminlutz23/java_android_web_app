package edu.pdx.cs410J.benlutz;

import edu.pdx.cs410J.AppointmentBookDumper;

import java.io.PrintWriter;
import java.io.Writer;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

/**
 * A skeletal implementation of the <code>TextDumper</code> class for Project 2.
 */
public class TextDumper implements AppointmentBookDumper<AppointmentBook> {
  private final Writer writer;
  private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("M/d/yyyy h:mm a VV");

  /**
   * Constructs a new TextDumper that writes to a specified {@link Writer}
   *
   * @param writer The writer to which the appointment book data will be written
   */
  public TextDumper(Writer writer) {
    this.writer = writer;
  }

  /**
   * Dumps an {@link AppointmentBook} to a text format using the writer provided
   * <p>
   * This method writes the owner's name and appointment details to the writer
   *
   * @param book The appointment book to be dumped
   */
  @Override
  public void dump(AppointmentBook book) {
    try (
            PrintWriter pw = new PrintWriter(this.writer)
    ) {
      pw.println(book.getOwnerName());

      // Iterate through each appointment and write its details
      Collection<Appointment> appointments = book.getAppointments();
      for (Appointment appointment : appointments) {
        String formattedBeginTime = DATE_TIME_FORMATTER.format(appointment.getBeginTime());
        String formattedEndTime = DATE_TIME_FORMATTER.format(appointment.getEndTime());
        pw.printf("%s, %s, %s%n", appointment.getDescription(), formattedBeginTime, formattedEndTime);
      }

      pw.flush();
    }
  }
}
