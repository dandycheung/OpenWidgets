package ru.fazziclay.openwidgets.android.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;

import java.util.Arrays;

import ru.fazziclay.openwidgets.Logger;
import ru.fazziclay.openwidgets.android.service.WidgetsUpdaterService;
import ru.fazziclay.openwidgets.data.Paths;
import ru.fazziclay.openwidgets.data.widgets.WidgetsData;
import ru.fazziclay.openwidgets.data.widgets.widget.DateWidget;

public class DateAppWidget extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Paths.updatePaths(context);
        final Logger LOGGER = new Logger();
        LOGGER.log("appWidgetIds=" + Arrays.toString(appWidgetIds));

        WidgetsData.load();
        WidgetsData widgetsData = WidgetsData.getWidgetsData();

        for (int appWidgetId : appWidgetIds) {
            if (widgetsData.getDateWidgetById(appWidgetId) == null) {
                widgetsData.getDateWidgets().add(new DateWidget(appWidgetId));
                LOGGER.log("widget " + appWidgetId + " added!");
                widgetsData.getDateWidgetById(appWidgetId).updateWidget(context);
                LOGGER.log("widget " + appWidgetId + " updated!");
            }
        }

        WidgetsData.save();
        LOGGER.done();
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        Paths.updatePaths(context);
        final Logger LOGGER = new Logger();
        LOGGER.log("appWidgetIds=" + Arrays.toString(appWidgetIds));

        WidgetsData.load();

        for (int appWidgetId : appWidgetIds) {
            DateWidget dateWidget = WidgetsData.getWidgetsData().getDateWidgetById(appWidgetId);
            if (dateWidget != null) {
                dateWidget.delete();
                LOGGER.log("widget " + appWidgetId + " deleted!");
            }
        }

        WidgetsData.save();
        LOGGER.done();
    }

    @Override
    public void onDisabled(Context context) {
        final Logger LOGGER = new Logger();
        WidgetsUpdaterService.stop(context);
        LOGGER.done();
    }
}
