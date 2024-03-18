package edu.pdx.cs410J.benlutz;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.util.Objects;

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

        binding.button.setOnClickListener(v -> searchAppointments());
    }

    private void searchAppointments() {
        String ownerName = binding.editTextText2.getText().toString();

        try {
            AppointmentBook book = loadAppointmentBook(ownerName);
            if (book != null) {
                PrettyPrint(book);
            } else {
                new AlertDialog.Builder(requireContext())
                        .setTitle("Not Found")
                        .setMessage("No appointment book found for " + ownerName)
                        .setPositiveButton(android.R.string.ok, null)
                        .show();
            }
        } catch (Exception e) {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Error")
                    .setMessage("Error searching for appointment book: " + e.getMessage())
                    .setPositiveButton(android.R.string.ok, null)
                    .show();
        }

    }

    private AppointmentBook loadAppointmentBook(String ownerName) throws Exception {
        FileInputStream fis = requireContext().openFileInput(ownerName + ".json");
        InputStreamReader isr = new InputStreamReader(fis);
        Gson gson = new Gson();
        Type appointmentBookType = new TypeToken<AppointmentBook>() {}.getType();
        AppointmentBook book = gson.fromJson(isr, appointmentBookType);
        isr.close();
        fis.close();
        return book;
    }

    private void PrettyPrint(AppointmentBook book) throws Exception {
        StringWriter writer = new StringWriter();
        PrettyPrinter printer = new PrettyPrinter(writer);
        printer.dump(book);

        new AlertDialog.Builder(requireContext())
                .setTitle("Appointment Book for " + book.getOwnerName())
                .setMessage(writer.toString())
                .setPositiveButton(android.R.string.ok, (dialog, which) -> dialog.dismiss())
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
