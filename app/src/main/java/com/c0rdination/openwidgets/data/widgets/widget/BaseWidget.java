package com.c0rdination.openwidgets.data.widgets.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseWidget {
    protected int widgetId;
    protected List<Integer> flags = new ArrayList<>();

    public BaseWidget(int widgetId) {
        this.widgetId = widgetId;
    }

    public int getWidgetId() {
        return widgetId;
    }

    public List<Integer> getFlags() {
        return flags;
    }

    public abstract void delete();
    public abstract void restoreToDefaults();

    @NonNull
    @Override
    public String toString() {
        return "BaseWidget{" +
                "widgetId=" + widgetId +
                ", flags=" + flags +
                '}';
    }

    public void rawUpdateWidget(Context context, RemoteViews view) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        appWidgetManager.updateAppWidget(widgetId, view);
    }

    public abstract RemoteViews updateWidget(Context context);
}
