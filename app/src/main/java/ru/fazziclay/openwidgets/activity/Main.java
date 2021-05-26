
package ru.fazziclay.openwidgets.activity;

import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import ru.fazziclay.openwidgets.R;
import ru.fazziclay.openwidgets.WidgetsUpdaterService;
import ru.fazziclay.openwidgets.cogs.FileUtil;
import ru.fazziclay.openwidgets.cogs.Utils;
import ru.fazziclay.openwidgets.cogs.WidgetsManager;


public class Main extends AppCompatActivity {
    @Override
    protected void onResume() {
        super.onResume();

        try {
            if (!isServiceStarted()) {
                startService(new Intent(getApplicationContext(), WidgetsUpdaterService.class));
                Utils.showMessage(this, "Service started!");
            }
        } catch (Exception e) {
            Utils.showMessage(this, String.valueOf(e));
        }

        setContentView(R.layout.activity_main);

        Button to_debug_activity = findViewById(R.id.button_debug_activity);
        to_debug_activity.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(), Debug2.class);
            startActivity(intent);
        });

        Button to_about_activity = findViewById(R.id.button_about);
        to_about_activity.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(), About.class);
            startActivity(intent);
        });

        LinearLayout widgetsButtonsSlot = findViewById(R.id.widgetsButtonsSlot);
        JSONArray widgetsIndex;
        JSONObject widgetsData;

        try {
            WidgetsManager.syncVariable();

            widgetsIndex = WidgetsManager.widgets.getJSONArray("index");
            widgetsData = WidgetsManager.widgets.getJSONObject("data");


            int i = 0;
            while (i < widgetsIndex.length()) {
                // Widgets
                int widgetId = widgetsIndex.getInt(i);
                JSONObject widget = widgetsData.getJSONObject(String.valueOf(widgetId));
                int widgetType = widget.getInt("widgetType");
                String widgetName = "Unsupported";

                if (widgetType == 0) {
                    widgetName = "Digital Clock";
                }

                String buttonName = widgetName + " (" + widgetId + ")";

                // Buttons
                Button button = new Button(this);
                button.setText(buttonName);
                button.setOnClickListener(v -> {
                    Intent intent = new Intent();
                    intent.putExtra("widget_id", widgetId);
                    intent.setClass(getApplicationContext(), DigitalClockConfigurate.class);
                    startActivity(intent);
                });

                widgetsButtonsSlot.addView(button);
                i++;
            }

            if (i == 0) {
                TextView textView = new TextView(this);
                textView.setText(R.string.widgetsIsNone);
                textView.setTextSize(40);
                textView.setAllCaps(false);
                widgetsButtonsSlot.addView(textView);
            } else {
                CheckBox checkBox = new CheckBox(this);
                checkBox.setText(R.string.widgetIdMode);
                checkBox.setChecked(WidgetsUpdaterService.idMode);
                checkBox.setOnClickListener(v -> WidgetsUpdaterService.idMode = checkBox.isChecked());
                widgetsButtonsSlot.addView(checkBox);
            }
        } catch (JSONException e) {
            Utils.showMessage(this, "Error to create widgets buttons: "+e.toString());
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private boolean isServiceStarted() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (service.service.getClassName().contains("WidgetsUpdaterService")) {
                return true;
            }
        }
        return false;
    }
}