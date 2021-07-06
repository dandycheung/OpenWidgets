package ru.fazziclay.openwidgets.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import ru.fazziclay.openwidgets.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Android
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle(R.string.activityTitle_settings);
    }
}