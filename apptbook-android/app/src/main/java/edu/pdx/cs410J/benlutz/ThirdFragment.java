package edu.pdx.cs410J.benlutz;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import java.io.File;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.TimeZone;

import edu.pdx.cs410J.benlutz.databinding.FragmentThirdBinding;

public class ThirdFragment extends Fragment {

    private FragmentThirdBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentThirdBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.backButton.setOnClickListener(v ->
                NavHostFragment.findNavController(ThirdFragment.this)
                        .navigate(R.id.action_ThirdFragment_to_FirstFragment));

        binding.button.setOnClickListener(v -> searchAppointmentBook());

        String[] timeZones = TimeZone.getAvailableIDs();
        Arrays.sort(timeZones);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, timeZones);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.timeZoneSpinner.setAdapter(adapter);

        binding.startText.setOnClickListener(v -> showDateTimePicker(true));
        binding.endText.setOnClickListener(v -> showDateTimePicker(false));
    }

    private void showDateTimePicker(boolean isStartTime) {
        LocalDateTime now = LocalDateTime.now();
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, year, month, dayOfMonth) -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), (view1, hourOfDay, minute) -> {
                LocalDateTime selectedDateTime = LocalDateTime.of(year, month + 1, dayOfMonth, hourOfDay, minute);
                String selectedTimeZone = binding.timeZoneSpinner.getSelectedItem().toString();
                ZonedDateTime zonedDateTime = ZonedDateTime.of(selectedDateTime, ZoneId.of(selectedTimeZone));
                String formattedDateTime = DateTimeFormatter.ofPattern("M/d/yyyy h:mm a VV").format(zonedDateTime);

                if (isStartTime) {
                    binding.startText.setText(formattedDateTime);
                } else {
                    binding.endText.setText(formattedDateTime);
                }
            }, now.getHour(), now.getMinute(), false);
            timePickerDialog.show();
        }, now.getYear(), now.getMonthValue() - 1, now.getDayOfMonth());
        datePickerDialog.show();
    }

    private void searchAppointmentBook() {
        String ownerName = binding.ownerText.getText().toString().trim();
        if (ownerName.isEmpty()) {
            Toast.makeText(getContext(), "Owner name is required", Toast.LENGTH_SHORT).show();
            return;
        }

        AppointmentBook book = loadAppointmentBook(ownerName);
        if (book != null) {
            Intent intent = new Intent(getActivity(), DisplayActivity.class);
            intent.putExtra("ownerName", ownerName);
            startActivity(intent);
        } else {
            Toast.makeText(getContext(), "No appointment book found for " + ownerName, Toast.LENGTH_SHORT).show();
        }
    }

    private AppointmentBook loadAppointmentBook(String ownerName) {
        File file = new File(getContext().getFilesDir(), ownerName + ".txt");
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

