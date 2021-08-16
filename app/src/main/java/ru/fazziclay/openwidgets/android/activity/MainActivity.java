package ru.fazziclay.openwidgets.android.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import ru.fazziclay.openwidgets.Logger;
import ru.fazziclay.openwidgets.R;
import ru.fazziclay.openwidgets.android.service.WidgetsUpdaterService;
import ru.fazziclay.openwidgets.data.Paths;
import ru.fazziclay.openwidgets.data.settings.SettingsData;
import ru.fazziclay.openwidgets.data.widgets.WidgetsData;
import ru.fazziclay.openwidgets.network.Client;
import ru.fazziclay.openwidgets.update.checker.UpdateChecker;
import ru.fazziclay.openwidgets.util.NotificationUtils;
import ru.fazziclay.openwidgets.util.ServiceUtils;
import ru.fazziclay.openwidgets.util.Utils;

public class MainActivity extends Activity {
    Thread loadingThread = null;
    Context context = null;
    long startTime = 0;
    long endTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        startTime = System.currentTimeMillis();
        context = this;
        loadingThread = new Thread(this::loading);
        loadingThread.start();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void loading() {
        Logger LOGGER = new Logger();

        try {
            SettingsActivity.restartRequired = false;
            Paths.updatePaths(context);
            SettingsData.load();
            WidgetsData.load();
            Utils.setAppLanguage(context, SettingsData.getSettingsData().getLanguage());
            NotificationUtils.createChannel(
                    context,
                    WidgetsUpdaterService.FOREGROUND_NOTIFICATION_CHANNEL_ID,
                    getString(R.string.notification_channel_WidgetsUpdaterServiceForeground_title),
                    getString(R.string.notification_channel_WidgetsUpdaterServiceForeground_description),
                    NotificationUtils.IMPORTANCE_LOW
            );
            WidgetsUpdaterService.startIsNot(context);
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
        LOGGER.info("WidgetsUpdaterService.FOREGROUND_NOTIFICATION_CHANNEL_ID=" + WidgetsUpdaterService.FOREGROUND_NOTIFICATION_CHANNEL_ID);
        LOGGER.info("WidgetsUpdaterService.FOREGROUND_NOTIFICATION_ID=" + WidgetsUpdaterService.FOREGROUND_NOTIFICATION_ID);
        LOGGER.info("WidgetsUpdaterService(ServiceUtils)isServiceStarted=" + ServiceUtils.isServiceStarted(context, WidgetsUpdaterService.class));
        LOGGER.info("==================================");
        endTime = System.currentTimeMillis();
        LOGGER.info("The app loaded in "+(endTime-startTime)+"ms! Starting HomeActivity...");
        startActivity(new Intent(context, HomeActivity.class));
        finish();
        LOGGER.done();
    }
}