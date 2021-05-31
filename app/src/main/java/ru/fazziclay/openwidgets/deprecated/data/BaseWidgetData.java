package ru.fazziclay.openwidgets.deprecated.data;

public class BaseWidgetData {
    public int widgetType;

    public BaseWidgetData(int widgetType) {
        this.widgetType = widgetType;
    }

    @Override
    public String toString() {
        return "BaseWidgetData{" +
                "widgetType=" + widgetType +
                '}';
    }
}
