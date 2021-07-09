
package ru.fazziclay.openwidgets.activity;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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
import ru.fazziclay.openwidgets.widgets.data.DateWidget;
import ru.fazziclay.openwidgets.widgets.data.WidgetsData;


public class MainActivity extends AppCompatActivity {
    private static AppCompatActivity instance;
    public static AppCompatActivity getInstance() {
        return instance;
    }
    public static void setInstance(MainActivity instance) {
        MainActivity.instance = instance;
    }

    private void createNotifyChannel(String name, String description, String channelId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelId, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createNotifyChannel("ForegroundWidgetsUpdaterService", "ForegroundWidgetsUpdaterService", "ForegroundWidgetsUpdaterService");
        createNotifyChannel("DebugTest", "Test notify in debug screen", "DebugTest");
        createNotifyChannel("Update Checker", "Check app updates from internet", "UpdateChecker");

    }

    @Override
    protected void onResume() {
        // Android
        super.onResume();
        setContentView(R.layout.activity_main);
        setTitle(R.string.activityTitle_main);

        // My app
        setInstance(this);
        if (WidgetsData.index == null) {
            WidgetsData.load();
        }

        loadMainButtons();      // главные кнопок
        loadWidgetsButtons();   // кнопки виджетов
        loadUpdateChecker();    // update checker

        if (!isStarted()) {
            startService(new Intent(getApplicationContext(), WidgetsUpdaterService.class));
        }
    }

    public boolean isStarted() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (service.service.getClassName().contains("WidgetsUpdaterService")) {
                return true;
            }
        }
        return false;
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

        Iterator<Integer> iterator = WidgetsManager.getIterator();
        int i = 0;
        while (iterator.hasNext()) {
            int widgetId = iterator.next();
            BaseWidget widget = WidgetsManager.getWidgetById(widgetId);

            Button button = new Button(this);
            button.setAllCaps(false);
            Intent intent = new Intent().putExtra("widget_id", widgetId);
            CharSequence widgetName = getText(R.string.widgetName_unsupported);
            boolean isSupported = false;

            assert widget != null;
            if (widget.widgetType == DateWidget.type) {
                intent.setClass(this, DateWidgetConfiguratorActivity.class);
                isSupported = true;
                widgetName = getText(R.string.widgetName_date);
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
        UpdateChecker.sendNewUpdateAvailableNotification(this);
        UpdateChecker.getVersion((status, build, name, download_url) -> runOnUiThread(() -> {
            if (status != 0 && status != 3) {
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

                if (status == -1) {
                    updateCheckerText.setText(R.string.updateChecker_versionHight);
                    Button siteButton = new Button(this);
                    siteButton.setText(R.string.updateChecker_button_toSite);
                    siteButton.setOnClickListener(v -> {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://github.com/fazziclay/openwidgets/releases"));
                        startActivity(browserIntent);
                    });

                    Button downloadButton = new Button(this);
                    downloadButton.setText(R.string.updateChecker_button_download);
                    downloadButton.setOnClickListener(v -> {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(download_url));
                        startActivity(browserIntent);
                    });
                    updateCheckerButtonsLayout.addView(siteButton);
                    updateCheckerButtonsLayout.addView(downloadButton);
                }

                if (status == 1) {
                    updateCheckerText.setText(R.string.updateChecker_updateAvailable);
                    Button siteButton = new Button(this);
                    siteButton.setText(R.string.updateChecker_button_toSite);
                    siteButton.setOnClickListener(v -> {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://github.com/fazziclay/openwidgets/releases"));
                        startActivity(browserIntent);
                    });

                    Button downloadButton = new Button(this);
                    downloadButton.setText(R.string.updateChecker_button_download);
                    downloadButton.setOnClickListener(v -> {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(download_url));
                        startActivity(browserIntent);
                    });
                    updateCheckerButtonsLayout.addView(siteButton);
                    updateCheckerButtonsLayout.addView(downloadButton);
                }

                updateCheckerLayout.setVisibility(View.VISIBLE);
            }
        }));
    }
}