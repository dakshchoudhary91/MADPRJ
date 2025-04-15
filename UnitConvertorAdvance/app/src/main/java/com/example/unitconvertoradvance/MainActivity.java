package com.example.unitconvertoradvance;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import android.content.SharedPreferences;

public class MainActivity extends AppCompatActivity {
    private EditText inputValue;
    private Spinner fromUnit, toUnit;
    private Button convertButton;
    private Button settingsButton; // ✅ New Button
    private TextView resultText;

    private final String[] units = {"Feet", "Inches", "Centimeters", "Meters", "Yards"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // ✅ Load saved theme first
        SharedPreferences preferences = getSharedPreferences("settings", MODE_PRIVATE);
        boolean isDark = preferences.getBoolean("dark_mode", false);
        AppCompatDelegate.setDefaultNightMode(
                isDark ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
        );

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputValue = findViewById(R.id.inputValue);
        fromUnit = findViewById(R.id.fromUnit);
        toUnit = findViewById(R.id.toUnit);
        convertButton = findViewById(R.id.convertButton);
        resultText = findViewById(R.id.resultText);
        settingsButton = findViewById(R.id.settingsButton); // ✅ Link to layout button

        // Populate Spinners
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, units);
        fromUnit.setAdapter(adapter);
        toUnit.setAdapter(adapter);

        // Convert Button Click Listener
        convertButton.setOnClickListener(view -> performConversion());

        // ✅ Settings Button Click Listener
        settingsButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });
    }

    private void performConversion() {
        String from = fromUnit.getSelectedItem().toString();
        String to = toUnit.getSelectedItem().toString();
        String input = inputValue.getText().toString();

        if (input.isEmpty()) {
            resultText.setText("Please enter a value.");
            return;
        }

        double value = Double.parseDouble(input);
        double convertedValue = convertUnits(value, from, to);
        resultText.setText("Result: " + convertedValue + " " + to);
    }

    private double convertUnits(double value, String from, String to) {
        double valueInMeters = 0;

        // Convert input value to meters as a base unit
        switch (from) {
            case "Feet":
                valueInMeters = value * 0.3048;
                break;
            case "Inches":
                valueInMeters = value * 0.0254;
                break;
            case "Centimeters":
                valueInMeters = value * 0.01;
                break;
            case "Meters":
                valueInMeters = value;
                break;
            case "Yards":
                valueInMeters = value * 0.9144;
                break;
        }

        // Convert from meters to the target unit
        switch (to) {
            case "Feet":
                return valueInMeters / 0.3048;
            case "Inches":
                return valueInMeters / 0.0254;
            case "Centimeters":
                return valueInMeters / 0.01;
            case "Meters":
                return valueInMeters;
            case "Yards":
                return valueInMeters / 0.9144;
            default:
                return 0;
        }
    }
}
