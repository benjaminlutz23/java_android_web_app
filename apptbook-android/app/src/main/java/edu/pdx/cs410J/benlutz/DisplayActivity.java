package edu.pdx.cs410J.benlutz;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileReader;
import java.io.StringWriter;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DisplayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        String ownerName = getIntent().getStringExtra("ownerName");
        String startTimeString = getIntent().getStringExtra("startTime");
        String endTimeString = getIntent().getStringExtra("endTime");

        AppointmentBook book = loadAppointmentBook(ownerName);

        if (book != null && !startTimeString.isEmpty() && !endTimeString.isEmpty()) {
            ZonedDateTime startTime = ZonedDateTime.parse(startTimeString, DateTimeFormatter.ofPattern("M/d/yyyy h:mm a VV"));
            ZonedDateTime endTime = ZonedDateTime.parse(endTimeString, DateTimeFormatter.ofPattern("M/d/yyyy h:mm a VV"));
            try {
                book = filterAppointments(book, startTime, endTime);
            } catch (invalidOwnerException e) {
                throw new RuntimeException(e);
            }
        }

        if (book != null) {
            prettyPrintAppointmentBook(book);
        }

        findViewById(R.id.done).setOnClickListener(view -> finish());
    }


    private void prettyPrintAppointmentBook(AppointmentBook book) {
        StringWriter writer = new StringWriter();
        PrettyPrinter printer = new PrettyPrinter(writer);
        printer.dump(book);

        String prettyPrintedText = writer.toString();
        TextView textView = findViewById(R.id.appointmentBookTextView);
        textView.setText(prettyPrintedText);
    }

    private AppointmentBook loadAppointmentBook(String ownerName) {
        File file = new File(getFilesDir(), ownerName + ".txt");
        if (!file.exists()) {
            return null;
        }

        try (FileReader reader = new FileReader(file)) {
            TextParser parser = new TextParser(reader);
            return parser.parse();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private AppointmentBook filterAppointments(AppointmentBook book, ZonedDateTime start, ZonedDateTime end) throws invalidOwnerException {
        AppointmentBook filteredBook = new AppointmentBook(book.getOwnerName());
        for (Appointment appointment : book.getAppointments()) {
            if (appointment.getBeginTime().compareTo(start) >= 0 && appointment.getEndTime().compareTo(end) <= 0) {
                filteredBook.addAppointment(appointment);
            }
        }
        return filteredBook;
    }
}


