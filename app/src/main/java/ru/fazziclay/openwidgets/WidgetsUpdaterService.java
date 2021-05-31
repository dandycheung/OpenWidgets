package ru.fazziclay.openwidgets;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.widget.RemoteViews;

import ru.fazziclay.openwidgets.activity.Main;



public class WidgetsUpdaterService extends Service {
    public static boolean idMode = false;

    public void onCreate() {
        super.onCreate();
        sendNotification();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    loop();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                handler.postDelayed(this, 250);
            }
        };
        handler.post(runnable);
        return START_STICKY;
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    private void sendNotification() {
        String id = "ForegroundWidgetsUpdaterService";

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification = new Notification();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(id, "", NotificationManager.IMPORTANCE_LOW);
            manager.createNotificationChannel(channel);

            notification = new Notification.Builder(this, id)
                    .setCategory(Notification.CATEGORY_SERVICE)
                    .setSmallIcon(null)
                    .setAutoCancel(true)
                    .build();
        }

        startForeground(888, notification);
    }


    public void loop() {
    }

    public void updateDigitalClock(int widgetId) {
        RemoteViews views = new RemoteViews(getApplicationContext().getPackageName(), R.layout.widget_digital_clock);
        views.setTextViewText(R.id.digital_clock_widget_text, "ID: "+widgetId);
        views.setTextViewTextSize(R.id.digital_clock_widget_text, 1, 50);
        views.setTextColor(R.id.digital_clock_widget_text, Color.parseColor("#ffffff"));
        views.setInt(R.id.digital_clock_widget_background, "setBackgroundColor", Color.parseColor("#999999"));

        updateWidget(widgetId, views);
    }

    public void updateWidget(int widgetId, RemoteViews views) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
        appWidgetManager.updateAppWidget(widgetId, views);
    }
}