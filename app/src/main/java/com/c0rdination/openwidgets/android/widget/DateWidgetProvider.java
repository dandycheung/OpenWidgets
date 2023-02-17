package com.c0rdination.openwidgets.android.widget;

import com.c0rdination.openwidgets.data.widgets.widget.BaseWidget;

public class DateWidgetProvider extends BaseWidgetProvider {
    public BaseWidget newWidget(int appWidgetId) {
        return new DateWidget(appWidgetId);
    }
}
