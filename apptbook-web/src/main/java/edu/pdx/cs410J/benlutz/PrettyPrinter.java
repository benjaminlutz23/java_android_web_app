package edu.pdx.cs410J.benlutz;

<<<<<<< HEAD
=======
import com.google.common.annotations.VisibleForTesting;
>>>>>>> Fresh-Start
import edu.pdx.cs410J.AppointmentBookDumper;

import java.io.PrintWriter;
import java.io.Writer;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

/**
 * The {@code PrettyPrinter} class implements the {@code AppointmentBookDumper} interface,
 * and it's purpose is to pretty-print the details of an {@code AppointmentBook} to a specified writer.
 * This includes printing a readable summary of each appointment within the book, alongside
 * the owner's name. Each appointment's duration in minutes is calculated and displayed,
 * enhancing readability and usefulness of the output.
 */

public class PrettyPrinter implements AppointmentBookDumper<AppointmentBook> {
    private final Writer writer;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("M/d/yyyy h:mm a VV");

    /**
     * Constructs a new TextDumper that writes to a specified {@link Writer}
     *
     * @param writer The writer to which the appointment book data will be written
     */
    public PrettyPrinter(Writer writer) {
        this.writer = writer;
    }

<<<<<<< HEAD
=======
    @VisibleForTesting
    public static String formatAppointmentDescription(String owner, String description) {
        return owner + " " + description;
    }

>>>>>>> Fresh-Start
    /**
     * Dumps an {@link AppointmentBook} to a text format using the writer provided
     * <p>
     * This method writes the owner's name and appointment details to the writer
     *
     * @param book The appointment book to be dumped
     */
    @Override
    public void dump(AppointmentBook book) {
        try (PrintWriter pw = new PrintWriter(this.writer)) {
            pw.println("Appointment Book for: " + book.getOwnerName());
            pw.println("Appointments:");
            pw.println("---------------");

            Collection<Appointment> appointments = book.getAppointments();
            for (Appointment appointment : appointments) {
                ZonedDateTime beginTime = appointment.getBeginTime();
                ZonedDateTime endTime = appointment.getEndTime();
                String formattedBeginTime = DATE_TIME_FORMATTER.format(beginTime);
                String formattedEndTime = DATE_TIME_FORMATTER.format(endTime);

                long duration = Duration.between(beginTime, endTime).toMinutes();
                String durationString = String.format("(%d minutes)", duration);

                pw.println("Description: " + appointment.getDescription());
                pw.println("From: " + formattedBeginTime);
                pw.println("To:   " + formattedEndTime + " " + durationString);
                pw.println(); // Adds an empty line between appointments for readability
            }

            pw.flush();
        }
    }
}