package com.c0rdination.openwidgets.android.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.c0rdination.openwidgets.Logger;
import com.c0rdination.openwidgets.R;
import com.c0rdination.openwidgets.android.widget.WidgetsService;
import com.c0rdination.openwidgets.data.Paths;
import com.c0rdination.openwidgets.data.settings.SettingsData;
import com.c0rdination.openwidgets.data.widgets.WidgetRegistry;
import com.c0rdination.openwidgets.network.Client;
import com.c0rdination.openwidgets.update.checker.UpdateChecker;
import com.c0rdination.openwidgets.util.NotificationUtils;
import com.c0rdination.openwidgets.util.ServiceUtils;
import com.c0rdination.openwidgets.util.Utils;

public class MainActivity extends Activity {
    private Thread loadingThread = null;

    private long startTime = 0;
    private long endTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        startTime = System.currentTimeMillis();

        loadingThread = new Thread(this::loading);
        loadingThread.start();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void loading() {
        Logger LOGGER = new Logger();

        Context context = this;
        try {
            SettingsActivity.restartRequired = false;

            Paths.updatePaths(context);
            LOGGER.info("loading(): app paths updated.");

            SettingsData.load();
            LOGGER.info("loading(): app settings loaded.");

            WidgetRegistry.load();
            LOGGER.info("loading(): widget registry loaded.");

            Utils.setAppLanguage(context, SettingsData.getSettingsData().getLanguage());

            NotificationUtils.createChannel(context,
                    WidgetsService.FOREGROUND_NOTIFICATION_CHANNEL_ID,
                    getString(R.string.notification_channel_WidgetsUpdaterServiceForeground_title),
                    getString(R.string.notification_channel_WidgetsUpdaterServiceForeground_description),
                    NotificationUtils.IMPORTANCE_LOW
            );
            WidgetsService.startIfNotStarted(context);

            Client.connectToServer();
        } catch (Exception e) {
            LOGGER.errorDescription("Error for loading app! GLOBAL PROBLEM!!!");
            LOGGER.error(e);
        }

        LOGGER.deviceInfo();
        LOGGER.info("Logger.LOG_FILE=" + Logger.LOG_FILE);
        LOGGER.info("UpdateChecker.APP_BUILD=" + UpdateChecker.APP_BUILD);
        LOGGER.info("UpdateChecker.APP_UPDATE_CHECKER_FORMAT_VERSION=" + UpdateChecker.APP_UPDATE_CHECKER_FORMAT_VERSION);
        LOGGER.info("UpdateChecker.APP_SITE_URL=" + UpdateChecker.APP_SITE_URL);
        LOGGER.info("UpdateChecker.APP_UPDATE_CHECKER_URL=" + UpdateChecker.APP_UPDATE_CHECKER_URL);
        LOGGER.info("WidgetsUpdaterService.FOREGROUND_NOTIFICATION_CHANNEL_ID=" + WidgetsService.FOREGROUND_NOTIFICATION_CHANNEL_ID);
        LOGGER.info("WidgetsUpdaterService.FOREGROUND_NOTIFICATION_ID=" + WidgetsService.FOREGROUND_NOTIFICATION_ID);
        LOGGER.info("WidgetsUpdaterService(ServiceUtils)isServiceStarted=" + ServiceUtils.isServiceStarted(context, WidgetsService.class));
        LOGGER.info("==================================");

        endTime = System.currentTimeMillis();
        LOGGER.info("The app loaded in " + (endTime - startTime) + "ms! Starting HomeActivity...");

        startActivity(new Intent(context, HomeActivity.class));
        finish();

        LOGGER.done();
    }
}
