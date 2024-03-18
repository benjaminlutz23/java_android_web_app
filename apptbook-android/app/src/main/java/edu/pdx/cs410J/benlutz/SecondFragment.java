package edu.pdx.cs410J.benlutz;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

import edu.pdx.cs410J.benlutz.databinding.FragmentSecondBinding;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.backButton.setOnClickListener(v -> NavHostFragment.findNavController(SecondFragment.this).popBackStack());
        setupTimeZoneSpinner();
        setupDateTimePicker(binding.startTime);
        setupDateTimePicker(binding.endTime);

        binding.saveButton.setOnClickListener(v -> {
            try {
                saveAppointment();
                // Optionally show a success message
                Toast.makeText(getContext(), "Appointment saved successfully", Toast.LENGTH_SHORT).show();
            } catch (invalidDescriptionException | invalidOwnerException e) {
                // Display the custom exception messages in an AlertDialog
                new AlertDialog.Builder(requireContext())
                        .setTitle("Error")
                        .setMessage("Error: " + e.getMessage())
                        .setPositiveButton(android.R.string.ok, null)
                        .show();
            } catch (Exception e) {
                // Display other exception messages in an AlertDialog
                new AlertDialog.Builder(requireContext())
                        .setTitle("Error")
                        .setMessage("Error saving appointment: " + e.getMessage())
                        .setPositiveButton(android.R.string.ok, null)
                        .show();
            }
        });
    }

    private void setupTimeZoneSpinner() {
        Spinner timeZoneSpinner = binding.timeZoneSpinner;
        String[] timeZones = TimeZone.getAvailableIDs();
        Arrays.sort(timeZones);
        ArrayAdapter<String> timeZoneAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, timeZones);
        timeZoneAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeZoneSpinner.setAdapter(timeZoneAdapter);
    }

    private void setupDateTimePicker(EditText editText) {
        editText.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), (view, year, month, dayOfMonth) -> {
                TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(), (view1, hourOfDay, minute) -> {
                    LocalDateTime localDateTime = LocalDateTime.of(year, month + 1, dayOfMonth, hourOfDay, minute);
                    editText.setText(localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
                timePickerDialog.show();
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });
    }

    private void saveAppointment() throws IOException, invalidDescriptionException, invalidOwnerException {
        String ownerName = binding.ownerName.getText().toString().trim();
        if (ownerName.isEmpty()) {
            throw new invalidOwnerException();
        }

        String description = binding.description.getText().toString().trim();
        if (description.isEmpty()) {
            throw new invalidDescriptionException("Description cannot be empty.");
        }

        String startTimeString = binding.startTime.getText().toString().trim();
        String endTimeString = binding.endTime.getText().toString().trim();
        if (startTimeString.isEmpty() || endTimeString.isEmpty()) {
            throw new IllegalArgumentException("Start time and end time cannot be empty.");
        }

        // Get the selected time zone from the spinner
        String selectedTimeZone = (String) binding.timeZoneSpinner.getSelectedItem();
        ZoneId zoneId = ZoneId.of(selectedTimeZone);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        ZonedDateTime startTime;
        ZonedDateTime endTime;

        try {
            startTime = ZonedDateTime.of(LocalDateTime.parse(startTimeString, formatter), zoneId);
            endTime = ZonedDateTime.of(LocalDateTime.parse(endTimeString, formatter), zoneId);

            // Show the parsed date and time in a dialog
            new AlertDialog.Builder(requireContext())
                    .setTitle("Parsed Date and Time")
                    .setMessage("Start Time: " + startTime + "\nEnd Time: " + endTime)
                    .setPositiveButton(android.R.string.ok, null)
                    .show();

        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Please use 'yyyy-MM-dd HH:mm'.");
        }

        Appointment appointment = new Appointment(description, startTime, endTime);
        AppointmentBook book;

        // Try loading existing book or create a new one if it doesn't exist
        try {
            book = loadAppointmentBook(ownerName);
        } catch (IOException | ClassNotFoundException e) {
            book = new AppointmentBook(ownerName);
        }

        book.addAppointment(appointment);

        // Serialize the appointment book
        Gson gson = new Gson();
        String json = gson.toJson(book);

        // Save the JSON to internal storage
        try (FileOutputStream fos = requireContext().openFileOutput(ownerName + ".json", Context.MODE_PRIVATE)) {
            fos.write(json.getBytes());
        }
    }



    private AppointmentBook loadAppointmentBook(String ownerName) throws IOException, ClassNotFoundException {
        FileInputStream fis = requireContext().openFileInput(ownerName + ".json");
        InputStreamReader isr = new InputStreamReader(fis);
        Gson gson = new Gson();

        // The type of data we expect to be returned
        Type appointmentBookType = new TypeToken<AppointmentBook>() {}.getType();
        AppointmentBook book = gson.fromJson(isr, appointmentBookType);

        isr.close();
        fis.close();

        return book;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

