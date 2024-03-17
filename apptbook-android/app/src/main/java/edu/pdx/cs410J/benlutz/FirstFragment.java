package edu.pdx.cs410J.benlutz;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import edu.pdx.cs410J.benlutz.databinding.FragmentFirstBinding;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.button.setOnClickListener(v -> {
            String readmeText = readReadmeText();
            if (!readmeText.isEmpty()) {
                showReadmeDialog(readmeText);
            }
        });
    }

    private void showReadmeDialog(String readmeText) {
        final ScrollView scrollView = new ScrollView(getContext());
        final TextView textView = new TextView(getContext());
        textView.setText(readmeText);
        scrollView.addView(textView);

        new AlertDialog.Builder(getContext())
                .setTitle("README")
                .setView(scrollView)
                .setPositiveButton("Close", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private String readReadmeText() {
        StringBuilder readmeText = new StringBuilder();
        try (InputStream readmeStream = getActivity().getAssets().open("README.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(readmeStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                readmeText.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return readmeText.toString();
        //return "Hello world";
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
