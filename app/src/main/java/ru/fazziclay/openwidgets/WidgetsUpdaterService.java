package ru.fazziclay.openwidgets;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.widget.RemoteViews;


import org.json.JSONObject;

import java.util.Date;

import static ru.fazziclay.openwidgets.cogs.Utils.*;



public class WidgetsUpdaterService extends Service {
    final String NOTIFICATION_TITLE         = "(title)";
    final String NOTIFICATION_DESCRIPTION   = "(description)";


    public void onCreate() {
        super.onCreate();
        log("WidgetsUpdaterService -> onCreate()");
        sendNotification(NOTIFICATION_TITLE, NOTIFICATION_DESCRIPTION);
    }


    public int onStartCommand(Intent intent, int flags, int startId) {
        log("WidgetsUpdaterService -> onStartCommand()");

        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                loop();
                handler.postDelayed(this, 500);
            }
        };
        handler.post(runnable);
        return START_STICKY;
    }


    public void onDestroy() {
        super.onDestroy();
        log("WidgetsUpdaterService -> onDestroy()");
    }


    public IBinder onBind(Intent intent) {
        log("WidgetsUpdaterService -> onBind()");
        return null;
    }


    private void sendNotification(String content_title, String sub_text) {
        String id = "ForegroundNotify";
        String description = "(59: String description)";


        log("WidgetsUpdaterService -> sendNotification(), content_title=" + content_title + "; sub_text=" + sub_text);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel = new NotificationChannel(id, description, importance);
            manager.createNotificationChannel(channel);

            notification = new Notification.Builder(this, id)
                    .setCategory(Notification.CATEGORY_MESSAGE)
                    .setSmallIcon(null)
                    .setAutoCancel(true)
                    .setContentTitle(content_title)
                    .setSubText(sub_text)
                    .build();
        }

        startForeground(888, notification);
    }


    public void loop() {
        /*Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date()); // Give your own date
        try {
            JSONObject json = AppUtils.get_widgets(getApplicationContext());

            JSONArray index = json.getJSONArray("index");
            JSONObject data = json.getJSONObject("data");

            int i = 0;
            while (i < index.length()) {
                int current_id = index.getInt(i);
                JSONObject current_data = data.getJSONObject(String.valueOf(current_id));


                // replace variables
                String hour = String.valueOf( calendar.get(Calendar.HOUR_OF_DAY) );
                if (calendar.get(Calendar.HOUR_OF_DAY) < 10) {
                    hour = "0" + hour;
                }

                String minute = String.valueOf( calendar.get(Calendar.MINUTE) );
                if (calendar.get(Calendar.MINUTE) < 10) {
                    minute = "0" + minute;
                }

                String seconds = String.valueOf( calendar.get(Calendar.SECOND) );
                if (calendar.get(Calendar.SECOND) < 10) {
                    seconds = "0" + seconds;
                }

                /////
                String text = current_data.getString("text");

                text = text.replace("%n", "\n");
                text = text.replace("%d", String.valueOf( calendar.get(Calendar.DAY_OF_MONTH) ));
                text = text.replace("%m", String.valueOf( calendar.get(Calendar.MONTH)+1 ));
                text = text.replace("%j", String.valueOf( calendar.get(Calendar.DAY_OF_YEAR) ));

                text = text.replace("%k", String.valueOf( hour ));
                text = text.replace("%M", String.valueOf( minute ));
                text = text.replace("%S", String.valueOf( seconds ));

                text = text.replace("%A", "-");
                text = text.replace("%u", String.valueOf( calendar.get(Calendar.DAY_OF_WEEK) - 1));
                text = text.replace("%Y", String.valueOf( calendar.get(Calendar.YEAR) ));
                text = text.replace("%s", String.valueOf( System.currentTimeMillis()/1000 ));


                /*
                 * Day: %d/%m (%j) %nTime: %k:%M:%S %nWeek: %A (%u) %nYear: %Y %nUnix: %s
                 *
                 * *

                // Construct the RemoteViews object
                RemoteViews views = new RemoteViews(getApplicationContext().getPackageName(), R.layout.clock_widget);
                views.setTextViewText(R.id.clock_widget_text, (CharSequence) text);
                views.setTextViewTextSize(R.id.clock_widget_text, 1, current_data.getInt("text_size"));

                views.setInt(R.id.clock_widget_background, "setBackgroundColor", Color.parseColor(current_data.getString("widget_background_color")));
                views.setInt(R.id.clock_widget_text, "setBackgroundColor", Color.parseColor(current_data.getString("text_background_color")));
                views.setTextColor(R.id.clock_widget_text, Color.parseColor(current_data.getString("text_color")));


                // Instruct the widget manager to update the widget
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
                appWidgetManager.updateAppWidget(current_id, views);


                i++;
            }


        } catch (Exception e) {
            AppUtils.ShowMessage(getApplicationContext(), e.toString() + "\nCode: UpdateWidgetsService");
        }*/

    }
}