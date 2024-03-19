package edu.pdx.cs410J.benlutz;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import java.io.File;
import java.io.FileReader;

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
    }

    private void searchAppointmentBook() {
        String ownerName = binding.editTextText2.getText().toString().trim();
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
