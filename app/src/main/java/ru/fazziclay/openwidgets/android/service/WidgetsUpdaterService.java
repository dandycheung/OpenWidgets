package ru.fazziclay.openwidgets.android.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import ru.fazziclay.openwidgets.Logger;
import ru.fazziclay.openwidgets.R;
import ru.fazziclay.openwidgets.data.widgets.WidgetsData;
import ru.fazziclay.openwidgets.data.widgets.widget.DateWidget;
import ru.fazziclay.openwidgets.util.ServiceUtils;
import ru.fazziclay.openwidgets.util.Utils;


public class WidgetsUpdaterService extends Service {
    private static final Logger LOGGER = new Logger(WidgetsUpdaterService.class);
    public static boolean isRun;

    public static void stop(Context context) {
        LOGGER.setFunction("stop");
        context.stopService(new Intent(context, WidgetsUpdaterService.class));
    }

    public static void startIsNot(Context context) {
        LOGGER.setFunction("startIsNot");

        if (!ServiceUtils.isServiceStarted(context, WidgetsUpdaterService.class)) {
            LOGGER.log("started!");
            context.startService(new Intent(context, WidgetsUpdaterService.class));
        }
        LOGGER.log("before started ;(");
    }

    private void loop() {
        for (DateWidget dateWidget : WidgetsData.getWidgetsData().getDateWidgets()) {
            dateWidget.updateWidget(this);
        }
    }

    public void onCreate() {
        super.onCreate();
        LOGGER.setFunction("onCreate");
        isRun = true;

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "WidgetsUpdaterServiceForeground")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getText(R.string.notification_foregroundWidgetsUpdaterService_title))
                .setContentText(getText(R.string.notification_foregroundWidgetsUpdaterService_text))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        startForeground(100, builder.build());
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        final Logger LOGGER = new Logger(WidgetsUpdaterService.class, "onStartCommand");

        final Context finalContext = this;
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    loop();
                } catch (Exception exception) {
                    LOGGER.exception(exception);
                    Utils.showToast(finalContext, "OpenWidgets: Error: "+exception.toString(), Toast.LENGTH_LONG);
                    stop(finalContext);
                    return;
                }
                if (isRun) {
                    handler.postDelayed(this, 250);
                }
            }
        };

        WidgetsData.load();
        handler.post(runnable);
        return START_STICKY;
    }

    public void onDestroy() {
        super.onDestroy();
        LOGGER.setFunction("onDestroy");
        isRun = false;
    }

    public IBinder onBind(Intent intent) {
        LOGGER.setFunction("onBind");
        LOGGER.returned(null);
        return null;
    }
}