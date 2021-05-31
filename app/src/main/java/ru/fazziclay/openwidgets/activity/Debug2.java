package ru.fazziclay.openwidgets.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import ru.fazziclay.openwidgets.R;
import ru.fazziclay.openwidgets.WidgetsUpdaterService;
import ru.fazziclay.openwidgets.cogs.Utils;

public class Debug2 extends AppCompatActivity {
    // Buttons
    Button testButton;
    Button servicesStartWidgetUpdater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug2);

        loadButtons();
        loadButtonsOnClickListener();
    }

    private void loadButtons() {
        testButton = findViewById(R.id.debug2_testButton);
        servicesStartWidgetUpdater = findViewById(R.id.debug2_servicesStartWidgetUpdater);
    }

    private void loadButtonsOnClickListener() {
        testButton.setOnClickListener(v -> Utils.showMessage(this, "Clicked!"));
        servicesStartWidgetUpdater.setOnClickListener(v -> {
            try {
                startService(new Intent(getApplicationContext(), WidgetsUpdaterService.class));
            } catch (Exception e) {
                Utils.showMessage(this, "activity.Debug2: button=servicesStartWidgetUpdater: error= "+e);
            }
        });
    }
}