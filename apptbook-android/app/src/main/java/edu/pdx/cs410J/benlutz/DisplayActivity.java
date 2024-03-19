package edu.pdx.cs410J.benlutz;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileReader;
import java.io.StringWriter;

public class DisplayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        String ownerName = getIntent().getStringExtra("ownerName");
        AppointmentBook book = loadAppointmentBook(ownerName);

        if (book != null) {
            prettyPrintAppointmentBook(book);
        }
    }

    private void prettyPrintAppointmentBook(AppointmentBook book) {
        StringWriter writer = new StringWriter();
        PrettyPrinter printer = new PrettyPrinter(writer);
        printer.dump(book);

        TextView textView = new TextView(this);
        textView.setText(writer.toString());
        setContentView(textView);
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
}
