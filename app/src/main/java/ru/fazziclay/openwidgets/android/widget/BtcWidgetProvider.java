package ru.fazziclay.openwidgets.android.widget;

import ru.fazziclay.openwidgets.data.widgets.widget.BaseWidget;

public class BtcWidgetProvider extends BaseWidgetProvider {
    public BaseWidget newWidget(int appWidgetId) {
        return new BtcWidget(appWidgetId);
    }
}
