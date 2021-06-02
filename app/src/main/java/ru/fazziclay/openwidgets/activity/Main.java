
package ru.fazziclay.openwidgets.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.text.MessageFormat;

import ru.fazziclay.openwidgets.R;
import ru.fazziclay.openwidgets.service.WidgetsUpdaterService;
import ru.fazziclay.openwidgets.widgets.data.WidgetType;


public class Main extends AppCompatActivity {
    private static Main instance;
    public static Main getInstance() {
        return instance;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_main);

        instance = this;

        loadMainButtons();
        loadWidgetsButtons();
    }

    private void loadWidgetsButtons() {
        LinearLayout widgetsButtonsSlot = findViewById(R.id.widgetsButtonsSlot);

        int i = 0;
        while (i < 1000) {
            int widgetId = i;
            int widgetType = i;

            //
            if (i > 20 && i < 30) widgetType = 0;
            //

            Button button = new Button(this);
            Intent intent = new Intent().putExtra("widget_id", widgetId);
            CharSequence widgetName = getText(R.string.widgets_unsupported);
            boolean isSupported = false;

            if (widgetType == WidgetType.DateWidget) {
                isSupported = true;
                intent.setClass(this, Debug2.class);
                widgetName = "Date";
            }

            button.setText(MessageFormat.format("{0} ({1})", widgetName, widgetId));
            if (isSupported) {
                button.setOnClickListener(v -> startActivity(intent));
            }

            widgetsButtonsSlot.addView(button);
            i++;
        }

        if (i > 0) {
            findViewById(R.id.widgetsIsNoneText).setVisibility(View.GONE);
            CheckBox checkBox = findViewById(R.id.checkBox_widgetsIdMode);
            checkBox.setVisibility(View.VISIBLE);
            checkBox.setChecked(WidgetsUpdaterService.idMode);
            checkBox.setOnClickListener(v -> WidgetsUpdaterService.idMode = checkBox.isChecked());
        }
    }

    private void loadMainButtons() {
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
    }
}