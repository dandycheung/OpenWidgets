package com.c0rdination.openwidgets.android.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;

import java.util.Arrays;

import com.c0rdination.openwidgets.Logger;
import com.c0rdination.openwidgets.data.Paths;
import com.c0rdination.openwidgets.data.widgets.WidgetRegistry;
import com.c0rdination.openwidgets.data.widgets.widget.BaseWidget;

public abstract class BaseWidgetProvider extends AppWidgetProvider {
    public abstract BaseWidget newWidget(int appWidgetId);

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Paths.updatePaths(context);
        final Logger LOGGER = new Logger();
        LOGGER.log("appWidgetIds=" + Arrays.toString(appWidgetIds));

        WidgetRegistry.load();
        WidgetRegistry widgetRegistry = WidgetRegistry.getWidgetRegistry();

        BaseWidget widget;
        for (int appWidgetId : appWidgetIds) {
            widget = widgetRegistry.getWidgetById(appWidgetId);
            if (widget == null) {
                widget = newWidget(appWidgetId);
                widgetRegistry.addWidget(widget);
                LOGGER.log("widget " + appWidgetId + " added!");
            }
            widget.updateWidget(context);
            LOGGER.log("widget " + appWidgetId + " updated!");
        }

        WidgetRegistry.save();
        LOGGER.done();
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        Paths.updatePaths(context);
        final Logger LOGGER = new Logger();
        LOGGER.log("appWidgetIds=" + Arrays.toString(appWidgetIds));

        WidgetRegistry.load();
        WidgetRegistry widgetRegistry = WidgetRegistry.getWidgetRegistry();

        for (int appWidgetId : appWidgetIds) {
            BaseWidget widget = widgetRegistry.getWidgetById(appWidgetId);
            if (widget != null) {
                widget.delete();
                LOGGER.log("widget " + appWidgetId + " deleted!");
            }
        }

        WidgetRegistry.save();
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
