package ru.fazziclay.openwidgets.android.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.view.Gravity;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import java.util.List;

import ru.fazziclay.openwidgets.Logger;
import ru.fazziclay.openwidgets.R;
import ru.fazziclay.openwidgets.data.settings.SettingsData;
import ru.fazziclay.openwidgets.data.widgets.WidgetsData;
import ru.fazziclay.openwidgets.data.widgets.widget.DateWidget;
import ru.fazziclay.openwidgets.network.Client;
import ru.fazziclay.openwidgets.util.ServiceUtils;
import ru.fazziclay.openwidgets.util.Utils;

public class WidgetsUpdaterService extends Service {
    public static final String FOREGROUND_NOTIFICATION_CHANNEL_ID = "WidgetsUpdaterServiceForeground";
    public static final int FOREGROUND_NOTIFICATION_ID = 100;

    private static final int VIEW_ID_IN_WIDGETS_PATTERN_SIZE = 39;
    private static final int VIEW_ID_IN_WIDGETS_PATTERN_COLOR = Color.parseColor("#ffffffff");
    private static final int VIEW_ID_IN_WIDGETS_PATTERN_BACKGROUND_COLOR = Color.parseColor("#88888888");
    private static final int VIEW_ID_IN_WIDGETS_BACKGROUND_COLOR = Color.parseColor("#55555555");

    int checkServer = 0;
    public static boolean isRunning;
    static BroadcastReceiver powerKeyReceiver = null;

    static SettingsData settingsData = null;
    static WidgetsData widgetsData = null;

    private void registerPowerKeyReceiver() {
        final Logger LOGGER = new Logger();

        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);

        powerKeyReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();

                if (action.equals(Intent.ACTION_SCREEN_ON) && (SettingsData.getSettingsData().isStartWidgetsUpdaterAfterScreenOn() || SettingsData.getSettingsData().isStopWidgetsUpdaterAfterScreenOff())) {
                    startIfNotStarted(context);
                }

                if (action.equals(Intent.ACTION_SCREEN_OFF) && SettingsData.getSettingsData().isStopWidgetsUpdaterAfterScreenOff()) {
                    stop(context);
                }

                if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                    Client.connectToServer();
                }
            }
        };

        getApplicationContext().registerReceiver(powerKeyReceiver, intentFilter);
        LOGGER.done();
    }

    public static void stop(Context context) {
        final Logger LOGGER = new Logger();
        context.stopService(new Intent(context, WidgetsUpdaterService.class));
        LOGGER.done();
    }

    public static void startIfNotStarted(Context context) {
        final Logger LOGGER = new Logger();

        if (!ServiceUtils.isServiceStarted(context, WidgetsUpdaterService.class)) {
            LOGGER.info("Service not started before. Starting...");
            context.startService(new Intent(context, WidgetsUpdaterService.class));
        } else {
            LOGGER.log("Already started");
        }
        LOGGER.done();
    }

    private void loop() {
        if (checkServer-- < 0) {
            checkServer = 86400;
            Client.connectToServer();
        }

        List<DateWidget> dateWidgets;
        dateWidgets = widgetsData.getDateWidgets();

        int i = 0;
        while (i < dateWidgets.size()) {
            DateWidget dateWidget = dateWidgets.get(i);

            if (settingsData.isViewIdInWidgets()) {
                RemoteViews view = new RemoteViews(getApplicationContext().getPackageName(), R.layout.widget_date);
                view.setTextViewText(R.id.widget_date_pattern, "ID: " + dateWidget.getWidgetId());
                view.setTextViewTextSize(R.id.widget_date_pattern, 2, VIEW_ID_IN_WIDGETS_PATTERN_SIZE);
                view.setTextColor(R.id.widget_date_pattern, VIEW_ID_IN_WIDGETS_PATTERN_COLOR);
                view.setInt(R.id.widget_date_pattern, "setBackgroundColor", VIEW_ID_IN_WIDGETS_PATTERN_BACKGROUND_COLOR);
                view.setInt(R.id.widget_date_background, "setBackgroundColor", VIEW_ID_IN_WIDGETS_BACKGROUND_COLOR);
                view.setInt(R.id.widget_date_background, "setGravity", Gravity.CENTER);
                dateWidget.rawUpdateWidget(this, view);
                i++;
                continue;
            }

            dateWidget.rawUpdateWidget(this, dateWidget.updateWidget(this));
            i++;
        }
    }

    public void onCreate() {
        super.onCreate();

        final Logger LOGGER = new Logger();
        isRunning = true;
        LOGGER.log("isRunning = true");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, FOREGROUND_NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getText(R.string.notification_foregroundWidgetsUpdaterService_title))
                .setContentText(getText(R.string.notification_foregroundWidgetsUpdaterService_text))
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setSound(null)
                .setAutoCancel(true);

        startForeground(FOREGROUND_NOTIFICATION_ID, builder.build());
        LOGGER.log("Foreground started!");
        registerPowerKeyReceiver();

        LOGGER.done();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        final Logger LOGGER = new Logger();

        final Context finalContext = this;
        final Handler loopHandler = new Handler();
        Thread loopThread = new Thread() {
            @Override
            public void run() {
                try {
                    if (settingsData == null) {
                        SettingsData.load();
                        settingsData = SettingsData.getSettingsData();
                    }

                    if (widgetsData == null) {
                        WidgetsData.load();
                        widgetsData = WidgetsData.getWidgetsData();
                    }

                    loop();

                    loopHandler.postDelayed(this, settingsData.getWidgetsUpdateDelayMillis());
                } catch (Throwable throwable) {
                    final Logger THREAD_LOGGER = new Logger();
                    THREAD_LOGGER.error(throwable);
                    try {
                        new Handler(Looper.getMainLooper()).post(() -> Utils.showToast(finalContext, "OpenWidgets Error: " + throwable, Toast.LENGTH_LONG));
                    } catch (Exception e) {
                        THREAD_LOGGER.errorDescription("Sending toast message error");
                        THREAD_LOGGER.error(e);
                    }
                    WidgetsUpdaterService.stop(finalContext);
                }
            }
        };

        loopHandler.post(loopThread);

        LOGGER.returned(START_STICKY);
        return START_STICKY;
    }

    public void onDestroy() {
        super.onDestroy();

        final Logger LOGGER = new Logger();
        isRunning = false;
        LOGGER.log("isRunning = false");
        LOGGER.done();
    }

    public IBinder onBind(Intent intent) {
        final Logger LOGGER = new Logger();
        LOGGER.returned(null);
        return null;
    }
}
