package ru.fazziclay.openwidgets.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import ru.fazziclay.openwidgets.Logger;
import ru.fazziclay.openwidgets.R;
import ru.fazziclay.openwidgets.android.ContextSaver;
import ru.fazziclay.openwidgets.android.service.WidgetsUpdaterService;
import ru.fazziclay.openwidgets.data.Paths;
import ru.fazziclay.openwidgets.data.settings.SettingsData;
import ru.fazziclay.openwidgets.data.widgets.WidgetsData;
import ru.fazziclay.openwidgets.util.NotificationUtils;
import ru.fazziclay.openwidgets.util.Utils;

public class MainActivity extends AppCompatActivity {
    public static final String INTENT_EXTRA_SAVER_ID = "saver";
    public static final int INTENT_EXTRA_SAVER_VALUE = 2318974;

    final Handler handler = new Handler(); // TODO: 12.08.2021 use Thread вместо handler runnable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);

        Paths.updatePaths(this);
        ContextSaver.setContext(this);

        Runnable runnable = () -> {
            SettingsActivity.restartRequired = false;
            Logger LOGGER = null;

            try {
                SettingsData.load();
                WidgetsData.load();

                Utils.setAppLanguage(this, SettingsData.getSettingsData().getLanguage());
                NotificationUtils.createChannel(
                        this,
                        WidgetsUpdaterService.FOREGROUND_NOTIFICATION_CHANNEL_ID,
                        getString(R.string.notification_channel_WidgetsUpdaterServiceForeground_title),
                        getString(R.string.notification_channel_WidgetsUpdaterServiceForeground_description),
                        NotificationUtils.IMPORTANCE_LOW
                );

                WidgetsUpdaterService.startIsNot(this);

            } catch (Exception exception) {
                LOGGER = new Logger(MainActivity.class, "onCreate");
                LOGGER.exception(exception);
            }
            if (LOGGER == null) LOGGER = new Logger(MainActivity.class, "onCreate");

            LOGGER.log("==================================");
            LOGGER.deviceInfo();
            LOGGER.log("App loaded! starting home activity!");
            startActivity(new Intent(this, HomeActivity.class).putExtra(INTENT_EXTRA_SAVER_ID, INTENT_EXTRA_SAVER_VALUE));
            finish();
        };
        handler.postDelayed(runnable, 100);
        super.onCreate(savedInstanceState);
    }
}