package ru.fazziclay.openwidgets.android.widget;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;

import ru.fazziclay.openwidgets.Logger;
import ru.fazziclay.openwidgets.R;
import ru.fazziclay.openwidgets.data.widgets.WidgetRegistry;
import ru.fazziclay.openwidgets.data.widgets.widget.BaseWidget;
import ru.fazziclay.openwidgets.util.ColorUtils;

public class NetworkWidget extends BaseWidget {
    public static final String WIFI_BUTTON_CLICKED         = "WifiButtonClick";
    public static final String CELLULAR_BUTTON_CLICKED     = "CellularButtonClick";
    public static final String ACCESS_POINT_BUTTON_CLICKED = "AccessPointButtonClick";

    public static final String PUBLIC_IP_CLICKED           = "PublicIpClick";
    public static final String LOCAL_IP_CLICKED            = "LocalIpClick";

    public NetworkWidget(int widgetId) {
        super(widgetId);
        // restoreToDefaults();
    }

    @Override
    public void delete() {
        final Logger LOGGER = new Logger();
        LOGGER.info("widgetId: " + widgetId);

        WidgetRegistry.getWidgetRegistry().removeWidget(widgetId);
        WidgetRegistry.save();

        LOGGER.done();
    }

    @Override
    public void restoreToDefaults() {
        final Logger LOGGER = new Logger();
        LOGGER.info("widgetId: " + widgetId);

        // TODO:
        WidgetRegistry.save();

        LOGGER.done();
    }

    public void loadFromAnotherWidget(NetworkWidget from) {
        final Logger LOGGER = new Logger();
        LOGGER.info("widgetId: " + widgetId);
        LOGGER.info("from id: " + from.widgetId);

        // TODO:
        WidgetRegistry.save();
    }

    @NonNull
    @Override
    public String toString() {
        return ""; // TODO:
        // return "NetworkWidget{" + "}";
    }

    @Override
    public RemoteViews updateWidget(Context context) {
        RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.widget_network);

        // view.setOnClickPendingIntent(R.id.wifi_button_id, getPendingSelfIntent(context, WIFI_BUTTON_CLICKED));
        // view.setOnClickPendingIntent(R.id.cellular_button_id, getPendingSelfIntent(context, CELLULAR_BUTTON_CLICKED));
        // view.setOnClickPendingIntent(R.id.ap_button_id, getPendingSelfIntent(context, ACCESS_POINT_BUTTON_CLICKED));
        // view.setOnClickPendingIntent(R.id.public_ip_address, getPendingSelfIntent(context, PUBLIC_IP_CLICKED));
        // view.setOnClickPendingIntent(R.id.local_ip_address, getPendingSelfIntent(context, LOCAL_IP_CLICKED));

        // LAN
        // view.setTextViewTextSize(R.id.ssid_or_carrier, 2, 72);

        String ssid = ColorUtils.colorizeText("哇哈哈", 0x00333333).toString();
        view.setTextViewText(R.id.ssid_or_carrier, ssid);

        view.setTextViewText(R.id.line_separator, "|");

        String localIpAddress = "?.?.?.?";
        view.setTextViewText(R.id.local_ip_address, localIpAddress);

        String ipAddress = "!.!.!.!";
        view.setTextViewText(R.id.public_ip_address, ipAddress);

        // WAN
        String carrierName = "?#$%?";
        view.setTextViewText(R.id.ssid_or_carrier, carrierName);
        view.setTextViewText(R.id.line_separator, "");
        view.setTextViewText(R.id.local_ip_address, "");

        // view.setTextViewText(R.id.public_ip_address, "NO CONNECTION");
        // view.setTextViewText(R.id.ssid_or_carrier, "");
        // view.setTextViewText(R.id.line_separator, "");
        // view.setTextViewText(R.id.local_ip_address, "");

        return view;
    }

    protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }
}
