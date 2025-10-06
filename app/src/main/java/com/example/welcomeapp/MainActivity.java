package com.example.welcomeapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.content.res.Configuration;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.Calendar;

import android.graphics.Typeface;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.view.View;

import androidx.core.content.res.ResourcesCompat;

import android.media.MediaPlayer;

import android.content.Intent;


public class MainActivity extends AppCompatActivity {

    private EditText nameInput;
    private TextView greetingText;
    private Button continueButton;
    private Button resetButton;
    private SwitchCompat darkModeSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Link views
        nameInput = findViewById(R.id.nameInput);
        greetingText = findViewById(R.id.greetingText);
        continueButton = findViewById(R.id.continueButton);
        resetButton = findViewById(R.id.resetButton);
        darkModeSwitch = findViewById(R.id.darkModeSwitch);
        Spinner fontSpinner = findViewById(R.id.fontSpinner);

        // Set initial greeting
        String greeting = getTimeBasedGreeting();
        greetingText.setText(greeting);
        greetingText.setVisibility(TextView.VISIBLE);

        // 🌓 Dark mode switch
        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        darkModeSwitch.setChecked(nightModeFlags == Configuration.UI_MODE_NIGHT_YES);

        darkModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
            recreate();
        });

        // 🔤 Font spinner
        fontSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedFont = parent.getItemAtPosition(position).toString();
                Typeface typeface;

                switch (selectedFont) {
                    case "Cursive":
                        typeface = ResourcesCompat.getFont(MainActivity.this, R.font.cursive);
                        break;
                    case "Monospace":
                        typeface = ResourcesCompat.getFont(MainActivity.this, R.font.monospace);
                        break;
                    case "Sans":
                        typeface = ResourcesCompat.getFont(MainActivity.this, R.font.sans);
                        break;
                    default:
                        typeface = Typeface.DEFAULT;
                        break;
                }

                greetingText.setTypeface(typeface);

                // ✅ ADD THIS BLOCK RIGHT BELOW IT:
                getSharedPreferences("prefs", MODE_PRIVATE)
                        .edit()
                        .putString("selected_font", selectedFont)
                        .apply();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        // ✅ Continue button – greeting + sound
        continueButton.setOnClickListener(v -> {
            String name = nameInput.getText().toString().trim();
            if (!name.isEmpty()) {
                // Set the full greeting
                greetingText.setText(greeting + " " + name + "!");
                greetingText.setVisibility(TextView.VISIBLE);
                resetButton.setVisibility(Button.VISIBLE);

                // Play welcome sound
                MediaPlayer mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.welcome);
                mediaPlayer.start();
                mediaPlayer.setOnCompletionListener(mp -> mp.release());

                // Start SecondActivity
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                intent.putExtra("username", name);
                startActivity(intent);
            }
        });


        // 🔁 Reset button
        resetButton.setOnClickListener(v -> {
            nameInput.setText("");
            greetingText.setVisibility(TextView.GONE);
            resetButton.setVisibility(Button.GONE);
        });
    }

    private String getTimeBasedGreeting() {
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        if (hour < 12) {
            return "Good Morning";
        } else if (hour < 17) {
            return "Good Afternoon";
        } else {
            return "Good Evening";
        }
    }
}
