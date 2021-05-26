package ru.fazziclay.openwidgets.widgets;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;

import ru.fazziclay.openwidgets.cogs.Utils;
import ru.fazziclay.openwidgets.cogs.WidgetsManager;


public class DigitalClock extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            Utils.showMessage(context, "Updated! id=" + String.valueOf(appWidgetId));
            WidgetsManager.addWidget(appWidgetId, 0);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            Utils.showMessage(context, "Deleted! id=" + String.valueOf(appWidgetId));
            WidgetsManager.removeWidget(appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        //Main.isWidgetsAvailable = true;
    }

    @Override
    public void onDisabled(Context context) {
        //Main.isWidgetsAvailable = false;
    }
}