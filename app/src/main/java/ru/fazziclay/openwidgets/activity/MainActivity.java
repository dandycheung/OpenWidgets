
package ru.fazziclay.openwidgets.activity;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.MessageFormat;
import java.util.Iterator;

import ru.fazziclay.openwidgets.R;
import ru.fazziclay.openwidgets.UpdateChecker;
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
        // Android
        super.onResume();
        setContentView(R.layout.activity_main);
        setTitle(R.string.activityTitle_main);

        // My app
        setInstance(this);
        loadMainButtons();      // главные кнопок
        loadWidgetsButtons();   // кнопки виджетов
        loadUpdateChecker();    // update checker
    }

    private void loadMainButtons() {
        Button aboutButton = findViewById(R.id.button_about);
        Button settingsButton = findViewById(R.id.button_settings);

        aboutButton.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(), AboutActivity.class);
            startActivity(intent);
        });

        settingsButton.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
        });
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
            CharSequence widgetName = "-- NO TRANSLATABLE AND HARDCODED -- (Не поддерживаемый виджет)";
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



    private void loadUpdateChecker() {
        UpdateChecker.getVersion((status, build, name, download_url) -> runOnUiThread(() -> {
            if (status != 0) {
                LinearLayout updateCheckerLayout = findViewById(R.id.updateChecker);
                LinearLayout updateCheckerButtonsLayout = findViewById(R.id.updateChecker_buttonsLayout);
                TextView updateCheckerText = findViewById(R.id.updateChecker_text);

                if (status == 2) {
                    updateCheckerText.setText(R.string.updateChecker_newFormatVersion);
                    Button siteButton = new Button(this);
                    siteButton.setText(R.string.updateChecker_button_toSite);
                    siteButton.setOnClickListener(v -> {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://github.com/fazziclay/openwidgets/releases"));
                        startActivity(browserIntent);
                    });
                    updateCheckerButtonsLayout.addView(siteButton);
                }

                updateCheckerLayout.setVisibility(View.VISIBLE);
            }
        }));
    }
}