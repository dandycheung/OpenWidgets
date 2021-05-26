package ru.fazziclay.openwidgets;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.widget.RemoteViews;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.Date;

import ru.fazziclay.openwidgets.cogs.Utils;
import ru.fazziclay.openwidgets.cogs.WidgetsManager;

import static ru.fazziclay.openwidgets.cogs.Utils.*;



public class WidgetsUpdaterService extends Service {
    public static boolean idMode = false;

    public void onCreate() {
        super.onCreate();
        log("[WidgetsUpdaterService] onCreate()");
        sendNotification();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        log("[WidgetsUpdaterService] onStartCommand()");
        WidgetsManager.syncVariable();

        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    loop();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                handler.postDelayed(this, (1/5)*1000);
            }
        };
        handler.post(runnable);
        return START_STICKY;
    }

    public void onDestroy() {
        super.onDestroy();
        log("[WidgetsUpdaterService] onDestroy()");
    }

    public IBinder onBind(Intent intent) {
        log("[WidgetsUpdaterService] onBind()");
        return null;
    }

    private void sendNotification() {
        String id = "BackgroundWidgetUpdateService";
        String description = "BackgroundWidgetUpdateService";


        log("[WidgetsUpdaterService] sendNotification()");

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel = new NotificationChannel(id, description, importance);
            manager.createNotificationChannel(channel);

            notification = new Notification.Builder(this, id)
                    .setCategory(Notification.CATEGORY_STATUS)
                    .setSmallIcon(null)
                    .setAutoCancel(true)
                    .setContentTitle("Background service running!")
                    .setSubText("Это уведомление дожно оставаться активным, что бы служна обновления виджетов работала в фоновогм режиме. Это связано с политикой Android.")
                    .build();
        }

        startForeground(888, notification);
    }

    public void loop() throws JSONException {
        JSONArray  widgetsIndex = WidgetsManager.widgets.getJSONArray("index");
        JSONObject widgetsData  = WidgetsManager.widgets.getJSONObject("data");

        int i = 0;
        while (i < widgetsIndex.length()) {
            int        widgetId   = widgetsIndex.getInt(i);
            JSONObject widget     = widgetsData.getJSONObject(String.valueOf(widgetId));
            int        widgetType = widget.getInt("widgetType");

            if (idMode) {
                RemoteViews views = new RemoteViews(getApplicationContext().getPackageName(), R.layout.digital_clock);
                views.setTextViewText(R.id.digital_clock_widget_text, "ID: "+widgetId);                            // Текст
                views.setTextViewTextSize(R.id.digital_clock_widget_text, 1, 30);                          // Размер текста
                views.setTextColor(R.id.digital_clock_widget_text, Color.parseColor("#ffffffff"));                                         // Цвет текста
                views.setInt(R.id.digital_clock_widget_background, "setBackgroundColor", Color.parseColor("#55555555")); // Фоновый цвет виджета

                updateWidget(widgetId, views);
            } else {
                if (widgetType == 0) {
                    updateDigitalClock(widgetId, widget);
                }
            }
            i++;
        }
    }

    public void updateDigitalClock(int widgetId, JSONObject widgetData) throws JSONException {
        // Get widgetData
        SpannableString  text  = new SpannableString(Utils.dateFormat(widgetData.getString("text")));
        int text_color         = Color.parseColor(widgetData.getString("text_color"));
        int text_style         = widgetData.getInt("text_style");
        int text_size          = widgetData.getInt("text_size");
        int background_color   = Color.parseColor(widgetData.getString("background_color"));


        RemoteViews views = new RemoteViews(getApplicationContext().getPackageName(), R.layout.digital_clock);
        setTextStyle(text, text_style);                                                                         // Стиль текста
        views.setTextViewText(R.id.digital_clock_widget_text, text);                                            // Текст
        views.setTextViewTextSize(R.id.digital_clock_widget_text, 1, text_size);                          // Размер текста
        views.setTextColor(R.id.digital_clock_widget_text, text_color);                                         // Цвет текста
        views.setInt(R.id.digital_clock_widget_background, "setBackgroundColor", background_color); // Фоновый цвет виджета

        updateWidget(widgetId, views);
    }

    public void updateWidget(int widgetId, RemoteViews views) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
        appWidgetManager.updateAppWidget(widgetId, views);
    }

}