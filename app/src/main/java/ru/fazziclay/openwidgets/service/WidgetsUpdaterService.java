package ru.fazziclay.openwidgets.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.view.Gravity;
import android.widget.RemoteViews;

import java.util.Iterator;

import ru.fazziclay.openwidgets.R;
import ru.fazziclay.openwidgets.cogs.Utils;
import ru.fazziclay.openwidgets.widgets.WidgetsManager;
import ru.fazziclay.openwidgets.widgets.data.BaseWidget;
import ru.fazziclay.openwidgets.widgets.data.DateWidget;
import ru.fazziclay.openwidgets.widgets.data.WidgetType;
import ru.fazziclay.openwidgets.widgets.data.WidgetsData;


public class WidgetsUpdaterService extends Service {
    public static boolean idMode = false;

    public void onCreate() {
        super.onCreate();
        sendNotification();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        WidgetsData.load();

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
            NotificationChannel channel = new NotificationChannel(id, "ForegroundWidgetsUpdaterService", NotificationManager.IMPORTANCE_LOW);
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
        Iterator<Integer> iterator = WidgetsManager.getIterator();
        while (iterator.hasNext()) {
            int id = iterator.next();
            if (idMode) {
                RemoteViews views = new RemoteViews(getApplicationContext().getPackageName(), R.layout.widget_date);
                views.setTextViewText(R.id.widget_date_text, "ID: "+id);
                views.setTextViewTextSize(R.id.widget_date_text, 2, 39);
                views.setTextColor(R.id.widget_date_text, Color.parseColor("#ffffffff"));
                views.setInt(R.id.widget_date_text, "setBackgroundColor", Color.parseColor("#88888888"));
                views.setInt(R.id.widget_date_background, "setBackgroundColor", Color.parseColor("#55555555"));
                views.setInt(R.id.widget_date_background, "setGravity", Gravity.CENTER);
                updateWidget(id, views);
                continue;
            }

            BaseWidget widget = WidgetsManager.getWidgetById(id);

            if (widget.widgetType == WidgetType.DateWidget) {
                updateDateWidget(id, (DateWidget) widget);
            }
        }
    }

    public void updateDateWidget(int widgetId, DateWidget widget) {
        int patternColor = Color.parseColor("#ffffff");
        int patternBackgroundColor = Color.parseColor("#00000000");
        int backgroundColor = Color.parseColor("#ff6993");

        try {
            patternColor = Color.parseColor(widget.patternColor);
            patternBackgroundColor = Color.parseColor(widget.patternBackgroundColor);
            backgroundColor = Color.parseColor(widget.backgroundColor);
        } catch (Exception e) {
            widget.patternColor = "#ffffff";
            widget.patternBackgroundColor = "#00000000";
            widget.backgroundColor = "#ff6993";
            WidgetsData.save();
        }

        RemoteViews views = new RemoteViews(getApplicationContext().getPackageName(), R.layout.widget_date);
        views.setTextViewText(R.id.widget_date_text, Utils.dateFormat(widget.pattern));
        views.setTextViewTextSize(R.id.widget_date_text, widget.patternSizeUnits, widget.patternSize);
        views.setTextColor(R.id.widget_date_text, patternColor);
        views.setInt(R.id.widget_date_text, "setBackgroundColor", patternBackgroundColor);
        views.setInt(R.id.widget_date_background, "setBackgroundColor", backgroundColor);
        views.setInt(R.id.widget_date_background, "setGravity", widget.backgroundGravity);
        //views.setInt(R.id.appwi111dget_text, "setStyle", 0);


        updateWidget(widgetId, views);
    }

    public void updateWidget(int widgetId, RemoteViews view) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
        appWidgetManager.updateAppWidget(widgetId, view);
    }
}