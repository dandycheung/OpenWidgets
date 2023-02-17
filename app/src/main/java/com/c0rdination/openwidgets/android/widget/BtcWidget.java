package com.c0rdination.openwidgets.android.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import com.c0rdination.openwidgets.Logger;
import com.c0rdination.openwidgets.R;
import com.c0rdination.openwidgets.data.widgets.WidgetRegistry;
import com.c0rdination.openwidgets.data.widgets.widget.BaseWidget;
import com.c0rdination.openwidgets.util.JsonUtils;

public class BtcWidget extends BaseWidget {
    public BtcWidget(int widgetId) {
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

    public void loadFromAnotherWidget(BtcWidget from) {
        final Logger LOGGER = new Logger();
        LOGGER.info("widgetId: " + widgetId);
        LOGGER.info("from id: " + from.widgetId);

        // TODO:
        WidgetRegistry.save();

        LOGGER.done();
    }

    @NonNull
    @Override
    public String toString() {
        return ""; // TODO:
        // return "BtcWidget{" + "}";
    }

    @Override
    public RemoteViews updateWidget(Context context) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_btc);

        String currency = "usd";

        // set widget's text to loading string
        views.setTextViewText(R.id.widget_text_price, "Loading...");
        views.setTextViewText(R.id.widget_day_change, "Loading...");
        views.setTextViewText(R.id.widget_symbol, "");

        rawUpdateWidget(context, views); // continues after this

        // refresh button
        Intent intentUpdate = new Intent(context, BtcWidget.class);
        intentUpdate.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intentUpdate.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[]{widgetId});

        // set up pending intent
        PendingIntent pendingUpdate = PendingIntent.getBroadcast(context, widgetId,
            intentUpdate, PendingIntent.FLAG_IMMUTABLE);

        views.setOnClickPendingIntent(R.id.widget_refresh_button, pendingUpdate);

        // function call to fetch data from HTTP GET request
        fetchBtcData(context, views, currency);

        return views;
    }

    private void fetchBtcData(Context context, RemoteViews views, String currency) {
        // current CoinGecko url to send GET request
        String url = "https://api.coingecko.com/api/v3/coins/markets?vs_currency="
            + currency + "&ids=bitcoin";

        final String symbol;
        final String isoCode;

        switch (currency) {
            default:
            case "usd":
                symbol = "$";
                isoCode = "USD";
                break;

            case "gbp":
                symbol = "£";
                isoCode = "GBP";
                break;

            case "eur":
                symbol = "€";
                isoCode = "EUR";
                break;

            case "cad":
                symbol = "$";
                isoCode = "CAD";
                break;

            case "mxn":
                symbol = "$";
                isoCode = "MXN";
                break;

            case "aud":
                symbol = "$";
                isoCode = "AUD";
                break;
        }

        // OkHttp
        Request request = new Request.Builder().url(url).build();

        OkHttpClient client = new OkHttpClient();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                final Logger LOGGER = new Logger();

                // successful GET request
                LOGGER.info("GET request successful.");

                // converts response into string
                String bodyJson = null;
                ResponseBody body = response.body();
                if (body != null)
                    bodyJson = body.string();

                // extracts object from JSON
                List<BtcData> list = JsonUtils.GSON.fromJson(bodyJson, new TypeToken<List<BtcData>>(){}.getType());

                BtcData data = null;
                if (list != null)
                    data = list.get(0);

                // post execute here
                // update widget with new data

                String priceString = data == null ? "?" : data.priceString();
                String dayChangeString = data == null ? "?" : data.dayChangeString();

                views.setTextViewText(R.id.widget_text_price, priceString);
                views.setTextViewText(R.id.widget_day_change, dayChangeString);
                views.setTextViewText(R.id.widget_iso_code, isoCode);
                views.setTextViewText(R.id.widget_symbol, symbol);

                if (dayChangeString.contains("+")) {
                    // green color
                    views.setTextColor(R.id.widget_day_change,
                        ContextCompat.getColor(context, R.color.positive_green)
                    );
                } else {
                    // red color
                    views.setTextColor(R.id.widget_day_change,
                        ContextCompat.getColor(context, R.color.negative_red)
                    );
                }

                // makes final call to update the widget
                rawUpdateWidget(context, views);
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                final Logger LOGGER = new Logger();

                // failed GET request
                LOGGER.info("Failed to execute GET request.");
            }
        });
    }
}
