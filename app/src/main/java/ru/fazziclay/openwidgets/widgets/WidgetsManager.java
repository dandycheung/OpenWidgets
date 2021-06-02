package ru.fazziclay.openwidgets.widgets;

import java.util.Iterator;

import ru.fazziclay.openwidgets.widgets.data.BaseWidget;
import ru.fazziclay.openwidgets.widgets.data.WidgetsData;

public class WidgetsManager {
    public static void addWidget(int widgetId, BaseWidget widget) {
        WidgetsData.load();
        if (isWidgetExist(widgetId)) return;
        WidgetsData.index.add(widgetId);
        WidgetsData.widgets.put(String.valueOf(widgetId), widget);
        WidgetsData.save();
    }
    public static void removeWidget(int widgetId) {
        WidgetsData.load();
        if (!isWidgetExist(widgetId)) return;
        WidgetsData.index.remove(Integer.valueOf(widgetId));
        WidgetsData.widgets.remove(String.valueOf(widgetId));
        WidgetsData.save();
    }
    public static boolean isWidgetExist(int widgetId) {
        return (WidgetsData.index.contains(widgetId));
    }
    public static BaseWidget getWidgetById(int widgetId) {
        if (!isWidgetExist(widgetId)) return null;
        for (int id : WidgetsData.index){
            if (id == widgetId) {
                return WidgetsData.widgets.get(String.valueOf(id));
            }
        }
        return null;
    }

    public static Iterator<Integer> getIterator() {
        return (WidgetsData.index.iterator());
    }
}
