package ru.fazziclay.openwidgets.android.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;

import java.util.Arrays;

import ru.fazziclay.openwidgets.Logger;
import ru.fazziclay.openwidgets.data.Paths;
import ru.fazziclay.openwidgets.data.widgets.WidgetsData;
import ru.fazziclay.openwidgets.data.widgets.widget.BaseWidget;

public abstract class BaseWidgetProvider extends AppWidgetProvider {
    public abstract BaseWidget newWidget(int appWidgetId);

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Paths.updatePaths(context);
        final Logger LOGGER = new Logger();
        LOGGER.log("appWidgetIds=" + Arrays.toString(appWidgetIds));

        WidgetsData.load();
        WidgetsData widgetsData = WidgetsData.getWidgetsData();

        BaseWidget widget;
        for (int appWidgetId : appWidgetIds) {
            widget = widgetsData.getWidgetById(appWidgetId);
            if (widget == null) {
                widget = newWidget(appWidgetId);
                widgetsData.getWidgets().add(widget);
                LOGGER.log("widget " + appWidgetId + " added!");
            }
            widget.updateWidget(context);
            LOGGER.log("widget " + appWidgetId + " updated!");
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
            DateWidget dateWidget = WidgetsData.getWidgetsData().getWidgetById(appWidgetId);
            if (dateWidget != null) {
                dateWidget.delete();
                LOGGER.log("widget " + appWidgetId + " deleted!");
            }
        }

        WidgetsData.save();
        LOGGER.done();
    }

    @Override
    public void onEnabled(Context context) {
        final Logger LOGGER = new Logger();
        // TODO:
        // WidgetsService.startIfNotStarted(context);
        LOGGER.done();
    }

    @Override
    public void onDisabled(Context context) {
        final Logger LOGGER = new Logger();
        // TODO:
        // WidgetsService.stop(context);
        LOGGER.done();
    }
}
