package ru.fazziclay.openwidgets.android.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;

import ru.fazziclay.openwidgets.android.service.WidgetsUpdaterService;
import ru.fazziclay.openwidgets.data.Paths;
import ru.fazziclay.openwidgets.data.widgets.WidgetsData;
import ru.fazziclay.openwidgets.data.widgets.widget.DateWidget;


public class DateAppWidget extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Paths.updatePaths(context);
        WidgetsData.load();

        for (int appWidgetId : appWidgetIds) {
            if (WidgetsData.getWidgetsData().getDateWidgetById(appWidgetId) == null) {
                WidgetsData.getWidgetsData().getDateWidgets().add(new DateWidget(appWidgetId));
            }
        }

        WidgetsData.save();
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        Paths.updatePaths(context);
        WidgetsData.load();

        for (int appWidgetId : appWidgetIds) {
            DateWidget dateWidget = WidgetsData.getWidgetsData().getDateWidgetById(appWidgetId);
            if (dateWidget != null) {
                dateWidget.delete();
            }
        }

        WidgetsData.save();
    }

    @Override
    public void onDisabled(Context context) {
        WidgetsUpdaterService.stop(context);
    }
}