package ru.fazziclay.openwidgets.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import ru.fazziclay.openwidgets.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Android
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle(R.string.activityTitle_settings);

        Button toDebugButton = findViewById(R.id.button_toDebug);
        toDebugButton.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(this, Debug2Activity.class);
            startActivity(intent);
        });
    }
}