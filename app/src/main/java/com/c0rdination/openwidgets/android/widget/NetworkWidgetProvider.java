package com.c0rdination.openwidgets.android.widget;

import android.content.Context;
import android.content.Intent;

import com.c0rdination.openwidgets.data.widgets.widget.BaseWidget;

public class NetworkWidgetProvider extends BaseWidgetProvider {
    public BaseWidget newWidget(int appWidgetId) {
        NetworkWidget widget = new NetworkWidget(appWidgetId);
        return widget;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (NetworkWidget.WIFI_BUTTON_CLICKED.equals(intent.getAction())) {
        } else if(NetworkWidget.CELLULAR_BUTTON_CLICKED.equals(intent.getAction())) {
        } else if(NetworkWidget.ACCESS_POINT_BUTTON_CLICKED.equals(intent.getAction())) {
        } else if(NetworkWidget.PUBLIC_IP_CLICKED.equals(intent.getAction())) {
        }
    }
}
