package ru.fazziclay.openwidgets.service;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.IBinder;
import android.view.Gravity;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import java.util.Iterator;

import ru.fazziclay.openwidgets.R;
import ru.fazziclay.openwidgets.activity.MainActivity;
import ru.fazziclay.openwidgets.updateChecker.UpdateChecker;
import ru.fazziclay.openwidgets.deprecated.cogs.DeprecatedUtils;
import ru.fazziclay.openwidgets.utils.ServiceUtils;
import ru.fazziclay.openwidgets.widgets.WidgetsManager;
import ru.fazziclay.openwidgets.widgets.data.BaseWidget;
import ru.fazziclay.openwidgets.widgets.data.DateWidget;
import ru.fazziclay.openwidgets.widgets.data.WidgetsData;


public class WidgetsUpdaterService extends Service {
    public static int updateCheckerCounter = 0;
    public static boolean idMode = false;
    public static boolean isRun;

    public static void stop(Context context) {
       context.stopService(new Intent(context, WidgetsUpdaterService.class));
    }

    public static void startIsNot(Context context) {
        if (!ServiceUtils.isServiceStarted(context)) {
            context.startService(new Intent(context, WidgetsUpdaterService.class));
        }
    }

    public void onCreate() {
        super.onCreate();
        isRun = true;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "ForegroundWidgetsUpdaterService")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getText(R.string.notification_foregroundWidgetsUpdaterService_title))
                .setContentText(getText(R.string.notification_foregroundWidgetsUpdaterService_text))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setNotificationSilent()
                .setAutoCancel(true);

        startForeground(100, builder.build());

        WidgetsData.loadIsNot();

        Context finalContext = this;
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    updateCheckerCounter--;
                    if (updateCheckerCounter < 1) {
                        updateCheckerCounter = 43200 * 4;
                        UpdateChecker.sendNewUpdateAvailableNotification(finalContext);
                    }
                    loop();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (isRun) {
                    handler.postDelayed(this, 250);
                }
            }
        };
        handler.post(runnable);
        return START_STICKY;
    }

    public void onDestroy() {
        super.onDestroy();
        isRun = false;
    }

    public IBinder onBind(Intent intent) {
        return null;
    }



    public void loop() {
        Iterator<Integer> iterator = WidgetsManager.getIterator();
        while (iterator.hasNext()) {
            int id = iterator.next();
            if (idMode) {
                RemoteViews views = new RemoteViews(getApplicationContext().getPackageName(), R.layout.widget_date);
                views.setOnClickPendingIntent(R.layout.widget_date, PendingIntent.getActivity(this, 1, new Intent(this, MainActivity.class), 0));
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

            assert widget != null;
            if (widget.widgetType == DateWidget.type) {
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
        views.setTextViewText(R.id.widget_date_text, DeprecatedUtils.dateFormat(widget.pattern));
        views.setTextViewTextSize(R.id.widget_date_text, widget.patternSizeUnits, widget.patternSize);
        views.setTextColor(R.id.widget_date_text, patternColor);
        views.setInt(R.id.widget_date_text, "setBackgroundColor", patternBackgroundColor);
        views.setInt(R.id.widget_date_background, "setBackgroundColor", backgroundColor);
        views.setInt(R.id.widget_date_background, "setGravity", widget.backgroundGravity);
        //views.setInt(R.id.app wi111d get_text, "setStyle", 0);


        updateWidget(widgetId, views);
    }

    public void updateWidget(int widgetId, RemoteViews view) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
        appWidgetManager.updateAppWidget(widgetId, view);
    }
}