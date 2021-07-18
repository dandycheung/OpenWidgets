package ru.fazziclay.openwidgets.data.widgets.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;

public abstract class BaseWidget {
    int widgetId;

    public BaseWidget(int widgetId) {
        this.widgetId = widgetId;
    }

    public int getWidgetId() {
        return widgetId;
    }

    public abstract void delete();

    @NonNull
    @Override
    public String toString() {
        return "BaseWidget{" +
                "widgetId=" + widgetId +
                '}';
    }

    public void rawUpdateWidget(Context context, RemoteViews view) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        appWidgetManager.updateAppWidget(widgetId, view);
    }

    public abstract void updateWidget(Context context);
}
