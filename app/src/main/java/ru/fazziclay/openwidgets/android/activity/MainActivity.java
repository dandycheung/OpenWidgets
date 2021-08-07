package ru.fazziclay.openwidgets.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

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
    public static final int INTENT_EXTRA_SAVER_VALUE = new Random().nextInt();

    final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);

        ContextSaver.setContext(this);
        Paths.updatePaths(this);

        Runnable runnable = () -> {
            try {
                SettingsData.load();
                WidgetsData.load();

                Utils.setAppLanguage(this, SettingsData.getSettingsData().getLanguage());
                NotificationUtils.createChannel(
                        this,
                        "WidgetsUpdaterServiceForeground",
                        getString(R.string.notification_channel_WidgetsUpdaterServiceForeground_title),
                        getString(R.string.notification_channel_WidgetsUpdaterServiceForeground_description)
                );

                WidgetsUpdaterService.startIsNot(this);

            } catch (Exception exception) {
                final Logger LOGGER = new Logger(MainActivity.class, "onCreate");
                LOGGER.exception(exception);
            }


            startActivity(new Intent(this, HomeActivity.class).putExtra(INTENT_EXTRA_SAVER_ID, INTENT_EXTRA_SAVER_VALUE));
            finish();
        };
        handler.postDelayed(runnable, 10);
    }
}