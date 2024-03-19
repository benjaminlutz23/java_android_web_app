package edu.pdx.cs410J.benlutz;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Arrays;
import java.util.TimeZone;
import java.util.stream.Collectors;

import edu.pdx.cs410J.benlutz.databinding.FragmentSecondBinding;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;

    private ZonedDateTime beginTime;
    private ZonedDateTime endTime;
    private String ownerName;
    private String description;

    private static final DateTimeFormatter DATE_TIME_FORMAT = new DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .appendPattern("M/d/yyyy h:mm a VV")
            .toFormatter();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String[] timeZones = TimeZone.getAvailableIDs();
        Arrays.sort(timeZones);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, timeZones);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.timeZoneSpinner.setAdapter(adapter);

        binding.startTime.setOnClickListener(v -> showDateTimePicker(true));
        binding.endTime.setOnClickListener(v -> showDateTimePicker(false));
        binding.saveButton.setOnClickListener(v -> saveData());
        binding.backButton.setOnClickListener(v -> NavHostFragment.findNavController(SecondFragment.this).popBackStack());
    }

    private void showDateTimePicker(boolean isStartTime) {
        LocalDateTime now = LocalDateTime.now();
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, year, month, dayOfMonth) -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), (view1, hourOfDay, minute) -> {
                LocalDateTime selectedDateTime = LocalDateTime.of(year, month + 1, dayOfMonth, hourOfDay, minute);
                String selectedTimeZone = binding.timeZoneSpinner.getSelectedItem().toString();
                ZonedDateTime zonedDateTime = ZonedDateTime.of(selectedDateTime, ZoneId.of(selectedTimeZone));

                if (isStartTime) {
                    beginTime = zonedDateTime;
                    binding.startTime.setText(DATE_TIME_FORMAT.format(zonedDateTime));
                } else {
                    endTime = zonedDateTime;
                    binding.endTime.setText(DATE_TIME_FORMAT.format(zonedDateTime));
                }
            }, now.getHour(), now.getMinute(), false);
            timePickerDialog.show();
        }, now.getYear(), now.getMonthValue() - 1, now.getDayOfMonth());
        datePickerDialog.show();
    }

    private void saveData() {
        ownerName = binding.ownerName.getText().toString();
        description = binding.description.getText().toString();

        // Use the existing ZonedDateTime objects if they have been set, otherwise parse from text
        if (beginTime == null) {
            String startTimeString = binding.startTime.getText().toString();
            beginTime = ZonedDateTime.parse(startTimeString, DATE_TIME_FORMAT);
        }

        if (endTime == null) {
            String endTimeString = binding.endTime.getText().toString();
            endTime = ZonedDateTime.parse(endTimeString, DATE_TIME_FORMAT);
        }

        try {
            Appointment appointment = new Appointment(description, beginTime, endTime);
            AppointmentBook appointmentBook = new AppointmentBook(ownerName);
            appointmentBook.addAppointment(appointment);

            // Display the appointment and appointment book details for confirmation
            showDialogWithData(appointmentBook);
        } catch (Exception | invalidDescriptionException e) {
            // Handle any exceptions, such as invalid input or creation errors
            new AlertDialog.Builder(getContext())
                    .setTitle("Error")
                    .setMessage(e.getMessage())
                    .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                    .create()
                    .show();
        } catch (invalidOwnerException e) {
            throw new RuntimeException(e);
        }
    }

    private void showDialogWithData(AppointmentBook appointmentBook) {
        // Assuming AppointmentBook and Appointment classes have appropriate toString methods or similar
        new AlertDialog.Builder(getContext())
                .setTitle("Appointment Book Summary")
                .setMessage("Owner Name: " + appointmentBook.getOwnerName() + "\n" +
                        "Appointment Details: " + appointmentBook.getAppointments().stream()
                        .map(Appointment::display) // Assuming Appointment has a display method
                        .collect(Collectors.joining("\n")))
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}


