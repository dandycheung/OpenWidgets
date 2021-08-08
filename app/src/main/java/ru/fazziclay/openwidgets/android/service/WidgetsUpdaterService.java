package ru.fazziclay.openwidgets.android.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Handler;
import android.os.IBinder;
import android.view.Gravity;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import ru.fazziclay.openwidgets.Logger;
import ru.fazziclay.openwidgets.R;
import ru.fazziclay.openwidgets.data.settings.SettingsData;
import ru.fazziclay.openwidgets.data.widgets.WidgetsData;
import ru.fazziclay.openwidgets.data.widgets.widget.DateWidget;
import ru.fazziclay.openwidgets.util.ServiceUtils;
import ru.fazziclay.openwidgets.util.Utils;


public class WidgetsUpdaterService extends Service {
    public static final String FOREGROUND_NOTIFICATION_CHANNEL_ID = "WidgetsUpdaterServiceForeground";
    public static final int FOREGROUND_NOTIFICATION_ID = 100;
    public static boolean isRun;
    static BroadcastReceiver mPowerKeyReceiver = null;

    private void registerPowerKeyReceiver() {
        final Logger LOGGER = new Logger(WidgetsUpdaterService.class, "registerPowerKeyReceiver");

        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);

        mPowerKeyReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                LOGGER.log("action="+action);

                if (action.equals(Intent.ACTION_SCREEN_ON) && (SettingsData.getSettingsData().isStartWidgetsUpdaterAfterScreenOn() || SettingsData.getSettingsData().isStopWidgetsUpdaterAfterScreenOff())) {
                    startIsNot(context);
                }

                if (action.equals(Intent.ACTION_SCREEN_OFF) && SettingsData.getSettingsData().isStopWidgetsUpdaterAfterScreenOff()) {
                    stop(context);
                }
            }
        };

        getApplicationContext().registerReceiver(mPowerKeyReceiver, intentFilter);
    }

    public static void stop(Context context) {
        /*final Logger LOGGER = */new Logger(WidgetsUpdaterService.class, "stop");
        context.stopService(new Intent(context, WidgetsUpdaterService.class));
    }

    public static void startIsNot(Context context) {
        final Logger LOGGER = new Logger(WidgetsUpdaterService.class, "startIsNot");

        if (!ServiceUtils.isServiceStarted(context, WidgetsUpdaterService.class)) {
            context.startService(new Intent(context, WidgetsUpdaterService.class));
            LOGGER.log("started!");
        } else {
            LOGGER.log("before started");
        }
    }

    private void loop() {
        for (DateWidget dateWidget : WidgetsData.getWidgetsData().getDateWidgets()) {
            if (SettingsData.getSettingsData().isViewIdInWidgets()) {
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
        final Logger LOGGER = new Logger(WidgetsUpdaterService.class, "onCreate");
        isRun = true; LOGGER.log("isRun = true");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, FOREGROUND_NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getText(R.string.notification_foregroundWidgetsUpdaterService_title))
                .setContentText(getText(R.string.notification_foregroundWidgetsUpdaterService_text))
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setSound(null)
                .setAutoCancel(true);

        startForeground(FOREGROUND_NOTIFICATION_ID, builder.build());
        registerPowerKeyReceiver();
        super.onCreate();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        final Logger LOGGER = new Logger(WidgetsUpdaterService.class, "onStartCommand");

        WidgetsData.load();

        final Context finalContext = this;
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    loop();
                } catch (Exception exception) {
                    LOGGER.exception(exception);
                    Utils.showToast(finalContext, "OpenWidgets Error: "+exception.toString(), Toast.LENGTH_LONG);
                    stop(finalContext);
                    return;
                }
                if (isRun) {
                    handler.postDelayed(this, SettingsData.getSettingsData().getWidgetsUpdateDelayMillis());
                }
            }
        };

        handler.post(runnable);
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    public void onDestroy() {
        super.onDestroy();
        final Logger LOGGER = new Logger(WidgetsUpdaterService.class, "onDestroy");
        isRun = false; LOGGER.log("isRun = false");
    }

    public IBinder onBind(Intent intent) {
        final Logger LOGGER = new Logger(WidgetsUpdaterService.class, "onBind");
        LOGGER.returned(null);
        return null;
    }
}