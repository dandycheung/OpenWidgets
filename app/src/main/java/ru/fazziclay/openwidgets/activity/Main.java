package ru.fazziclay.openwidgets.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import ru.fazziclay.openwidgets.R;



public class Main extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button to_debug_activity = (Button) findViewById(R.id.button_debug_activity);
        to_debug_activity.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(), Debug.class);
            startActivity(intent);
        });
    }
}