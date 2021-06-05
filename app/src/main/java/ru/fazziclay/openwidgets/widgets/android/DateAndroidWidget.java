package ru.fazziclay.openwidgets.widgets.android;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.view.Gravity;

import ru.fazziclay.openwidgets.widgets.WidgetsManager;
import ru.fazziclay.openwidgets.widgets.data.DateWidget;

public class DateAndroidWidget extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            WidgetsManager.addWidget(appWidgetId, new DateWidget("Hello World", 40, 2, "#ffffff", "#88888866", "#00ff00", Gravity.CENTER));
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            WidgetsManager.removeWidget(appWidgetId);
        }
    }
}