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

    public static boolean isRun;
    public static boolean waitWidgetsDataUpdating = false;
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
                final Logger LOGGER = new Logger();
                String action = intent.getAction();
                LOGGER.log("action="+action);

                if (action.equals(Intent.ACTION_SCREEN_ON) && (SettingsData.getSettingsData().isStartWidgetsUpdaterAfterScreenOn() || SettingsData.getSettingsData().isStopWidgetsUpdaterAfterScreenOff())) {
                    startIsNot(context);
                }

                if (action.equals(Intent.ACTION_SCREEN_OFF) && SettingsData.getSettingsData().isStopWidgetsUpdaterAfterScreenOff()) {
                    stop(context);
                }

                if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                    Client.connectToServer();
                }

                LOGGER.done();
            }
        };

        getApplicationContext().registerReceiver(powerKeyReceiver, intentFilter);
        LOGGER.log("Receiver registered. intentFilter="+intentFilter);
        LOGGER.done();
    }

    public static void stop(Context context) {
        final Logger LOGGER = new Logger();
        LOGGER.log("Stopping...");
        context.stopService(new Intent(context, WidgetsUpdaterService.class));
        LOGGER.done();
    }

    public static void startIsNot(Context context) {
        final Logger LOGGER = new Logger();

        if (!ServiceUtils.isServiceStarted(context, WidgetsUpdaterService.class)) {
            LOGGER.info("Service not started before. Starting...");
            context.startService(new Intent(context, WidgetsUpdaterService.class));
        } else {
            LOGGER.log("Before started");
        }
        LOGGER.done();
    }

    private void loop() {
        if (waitWidgetsDataUpdating) return;
        for (DateWidget dateWidget : widgetsData.getDateWidgets()) {
            if (settingsData.isViewIdInWidgets()) {
                RemoteViews view = new RemoteViews(getApplicationContext().getPackageName(), R.layout.widget_date);
                view.setTextViewText(R.id.widget_date_text, ""+dateWidget.getWidgetId());
                view.setTextViewTextSize(R.id.widget_date_text, 2, 39);
                view.setTextColor(R.id.widget_date_text, Color.parseColor("#ffffffff"));
                view.setInt(R.id.widget_date_text, "setBackgroundColor", Color.parseColor("#88888888"));
                view.setInt(R.id.widget_date_background, "setBackgroundColor", Color.parseColor("#55555555"));
                view.setInt(R.id.widget_date_background, "setGravity", Gravity.CENTER);
                dateWidget.rawUpdateWidget(this, view);
                continue;
            }
            dateWidget.rawUpdateWidget(this, dateWidget.updateWidget(this));
        }
    }

    public void onCreate() {
        super.onCreate();
        final Logger LOGGER = new Logger();
        isRun = true;
        LOGGER.log("isRun = true");

        Client.connectToServer();

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

        LOGGER.log("Trying to load settings for safety...");
        SettingsData.load();
        settingsData = SettingsData.getSettingsData();

        LOGGER.log("Trying to load widgets for safety...");
        WidgetsData.load();
        widgetsData = WidgetsData.getWidgetsData();

        final Context finalContext = this;

        final Handler loopHandler = new Handler();
        Thread loopThread = new Thread() {
            @Override
            public void run() {
                try {
                    loop();

                    loopHandler.postDelayed(this, settingsData.getWidgetsUpdateDelayMillis());
                } catch (Throwable throwable) {
                    final Logger THREAD_LOGGER = new Logger();
                    THREAD_LOGGER.log("Exception in loop thread!");
                    THREAD_LOGGER.error(throwable);
                    THREAD_LOGGER.log("Sending Toast message");
                    try {
                        new Handler(Looper.getMainLooper()).post(() -> Utils.showToast(finalContext, "OpenWidgets Error: " + throwable.toString(), Toast.LENGTH_LONG));
                    } catch (Exception e) {
                        THREAD_LOGGER.errorDescription("Sending Toast message error");
                        THREAD_LOGGER.error(e);
                    }
                    THREAD_LOGGER.log("Stopping service...");
                    WidgetsUpdaterService.stop(finalContext);
                    THREAD_LOGGER.log("Stopped.");
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
        isRun = false;
        LOGGER.log("isRun = false");
        LOGGER.done();
    }

    public IBinder onBind(Intent intent) {
        final Logger LOGGER = new Logger();
        LOGGER.returned(null);
        return null;
    }
}