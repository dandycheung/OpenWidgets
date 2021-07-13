package ru.fazziclay.openwidgets.deprecated.widgets.android;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;

import ru.fazziclay.openwidgets.deprecated.widgets.WidgetsManager;
import ru.fazziclay.openwidgets.deprecated.widgets.data.DateWidget;

@Deprecated
public class DateAndroidWidget extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            WidgetsManager.addWidget(appWidgetId, DateWidget.defaultWidget);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            WidgetsManager.removeWidget(appWidgetId);
        }
    }
}