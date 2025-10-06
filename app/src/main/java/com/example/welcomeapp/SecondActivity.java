package com.example.welcomeapp;

import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;




public class SecondActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        TextView secondGreeting = findViewById(R.id.secondGreeting);

        String selectedFont = getSharedPreferences("prefs", MODE_PRIVATE)
                .getString("selected_font", "Default");

        Typeface typeface;

        switch (selectedFont) {
            case "Cursive":
                typeface = ResourcesCompat.getFont(this, R.font.cursive);
                break;
            case "Monospace":
                typeface = ResourcesCompat.getFont(this, R.font.monospace);
                break;
            case "Sans":
                typeface = ResourcesCompat.getFont(this, R.font.sans);
                break;
            default:
                typeface = Typeface.DEFAULT;
                break;
        }

        secondGreeting.setTypeface(typeface);


        // Receive the name from the intent
        String name = getIntent().getStringExtra("username");

        if (name != null && !name.isEmpty()) {
            String message = getString(R.string.second_screen_message, name);
            secondGreeting.setText(message);

        }
    }
}
