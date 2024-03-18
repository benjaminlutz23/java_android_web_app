package edu.pdx.cs410J.benlutz;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.gson.Gson;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Calendar;
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
                Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(getContext(), "Error saving appointment: " + e.getMessage(), Toast.LENGTH_LONG).show();
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

    private void saveAppointment() throws invalidDescriptionException, invalidOwnerException {
        String ownerName = binding.ownerName.getText().toString();
        String description = binding.description.getText().toString();
        String start = binding.startTime.getText().toString();
        String end = binding.endTime.getText().toString();
        String timeZone = binding.timeZoneSpinner.getSelectedItem().toString();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        ZonedDateTime startTime = ZonedDateTime.of(LocalDateTime.parse(start, formatter), ZoneId.of(timeZone));
        ZonedDateTime endTime = ZonedDateTime.of(LocalDateTime.parse(end, formatter), ZoneId.of(timeZone));

        Appointment appointment = new Appointment(description, startTime, endTime);
        AppointmentBook appointmentBook = new AppointmentBook(ownerName);
        appointmentBook.addAppointment(appointment);

        // Using GSON to store as JSON
        Gson gson = new Gson();
        String json = gson.toJson(appointmentBook);

        try (FileOutputStream fos = requireContext().openFileOutput("appointmentBook.json", Context.MODE_PRIVATE)) {
            fos.write(json.getBytes());
        } catch (IOException e) {
            System.err.println("Error writing to storage");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

