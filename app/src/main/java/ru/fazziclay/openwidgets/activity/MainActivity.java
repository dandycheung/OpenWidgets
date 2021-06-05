
package ru.fazziclay.openwidgets.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.text.MessageFormat;
import java.util.Iterator;

import ru.fazziclay.openwidgets.R;
import ru.fazziclay.openwidgets.service.WidgetsUpdaterService;
import ru.fazziclay.openwidgets.widgets.WidgetsManager;
import ru.fazziclay.openwidgets.widgets.data.BaseWidget;
import ru.fazziclay.openwidgets.widgets.data.WidgetType;
import ru.fazziclay.openwidgets.widgets.data.WidgetsData;


public class MainActivity extends AppCompatActivity {
    private static AppCompatActivity instance;
    public static AppCompatActivity getInstance() {
        return instance;
    }
    public static void setInstance(MainActivity instance) {
        MainActivity.instance = instance;
    }

    @Override
    protected void onResume() {
        super.onResume();

        setInstance(this);
        setContentView(R.layout.activity_main);

        loadMainButtons();
        loadWidgetsButtons();
    }

    private void loadWidgetsButtons() {
        LinearLayout widgetsButtonsSlot = findViewById(R.id.widgetsButtonsSlot);
        WidgetsData.load();

        Iterator<Integer> iterator = WidgetsManager.getIterator();
        int i = 0;
        while (iterator.hasNext()) {
            int widgetId = iterator.next();
            BaseWidget widget = WidgetsManager.getWidgetById(widgetId);

            Button button = new Button(this);
            Intent intent = new Intent().putExtra("widget_id", widgetId);
            CharSequence widgetName = getText(R.string.widgets_unsupported);
            boolean isSupported = false;

            if (widget.widgetType == WidgetType.DateWidget) {
                isSupported = true;
                intent.setClass(this, DateWidgetConfiguratorActivity.class);
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
            intent.setClass(getApplicationContext(), Debug2Activity.class);
            startActivity(intent);
        });

        Button to_about_activity = findViewById(R.id.button_about);
        to_about_activity.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(), AboutActivity.class);
            startActivity(intent);
        });
    }
}