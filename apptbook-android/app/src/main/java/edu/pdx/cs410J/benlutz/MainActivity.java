package edu.pdx.cs410J.benlutz;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import edu.pdx.cs410J.benlutz.databinding.ActivityMainBinding;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        edu.pdx.cs410J.benlutz.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);

        binding.fab.setOnClickListener(view -> {
            String readmeText = readReadmeText();
            if (!readmeText.isEmpty()) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("README")
                        .setMessage(readmeText)
                        .setPositiveButton(android.R.string.ok, (dialog, which) -> dialog.dismiss())
                        .show();
            } else {
                Toast.makeText(MainActivity.this, "Failed to load README", Toast.LENGTH_LONG).show();
            }
        });
    }

    private String readReadmeText() {
        StringBuilder readmeText = new StringBuilder();
        try (InputStream readmeStream = this.getAssets().open("README.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(readmeStream))) {

            String line;
            while ((line = reader.readLine()) != null) {
                readmeText.append(line).append('\n');
            }
        } catch (IOException e) {
            System.err.println("Error reading README file: " + e.getMessage());
        }
        return readmeText.toString();
    }
}
