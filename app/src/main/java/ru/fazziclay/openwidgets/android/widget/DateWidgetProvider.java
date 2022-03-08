package ru.fazziclay.openwidgets.android.widget;

import ru.fazziclay.openwidgets.data.widgets.widget.BaseWidget;

public class DateWidgetProvider extends BaseWidgetProvider {
    public BaseWidget newWidget(int appWidgetId) {
        return new DateWidget(appWidgetId);
    }
}
