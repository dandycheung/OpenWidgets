package ru.fazziclay.openwidgets.deprecated.cogs;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.service.notification.StatusBarNotification;
import android.text.SpannableString;
import android.text.format.Time;
import android.text.style.StyleSpan;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class DeprecatedUtils {

    public static boolean isNotifyShowed(Context context, int id) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (StatusBarNotification a : manager.getActiveNotifications()) {
                if (a.getId() == id) {
                    return true;
                }
            }
        }
        return false;
    }



    static public String getPage(String urlString) throws IOException {
        StringBuilder result = new StringBuilder();
        URL url = new URL(urlString);
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String str;
        while ((str = in.readLine()) != null) {
            result.append(str);
        }
        in.close();
        return result.toString();
    }

    public static String ColorToHex(int color) {
        int alpha = android.graphics.Color.alpha(color);
        int red = android.graphics.Color.red(color);
        int green = android.graphics.Color.green(color);
        int blue = android.graphics.Color.blue(color);

        String alphaHex = To00Hex(alpha);
        String redHex = To00Hex(red);
        String greenHex = To00Hex(green);
        String blueHex = To00Hex(blue);

        return "#" + alphaHex +
                redHex +
                greenHex +
                blueHex;
    }

    private static String To00Hex(int value) {
        String hex = "00".concat(Integer.toHexString(value));
        return hex.substring(hex.length()-2);
    }

    public static void showMessage(Context context, String _message) {
        Toast.makeText(context, _message, Toast.LENGTH_SHORT).show();
    }

    public static SpannableString setTextStyle(SpannableString text, int style) {
        int i = 0;
        while (i < text.length()) {
            text.setSpan(new StyleSpan(style), (i), (i + 1), 0);
            i++;
        }
        return text;
    }

    public static int conventToMegaHourFormat(float source, int max) {
        return Math.round(source * 100 / max);
    }

    public static float makeAccurate(int source, int coefficient) {
        return source + ((float) coefficient / 100.0f);
    }

    public static String dateFormat(String source) {
        Time today = new Time(Time.getCurrentTimezone());
        today.setToNow();

        int nowSeconds = today.second;
        int nowMinutes = today.minute;
        int nowHours   = today.hour;

        // ------------------------ 100 HOUR FORMAT
        int _nowSeconds = conventToMegaHourFormat(nowSeconds, 60);
        int _nowMinutes = conventToMegaHourFormat(makeAccurate(nowMinutes, _nowSeconds), 60);
        int _nowHours   = conventToMegaHourFormat(makeAccurate(nowHours, _nowMinutes), 24);
        // ------------------------

        source = source.replace("%s", String.valueOf(System.currentTimeMillis()/1000));
        source = source.replace("%u", String.valueOf(today.weekDay));
        source = source.replace("%Y", String.valueOf(today.year));
        source = source.replace("%j", String.valueOf(today.yearDay));
        // OLD TIME
        source = source.replace("%S", String.valueOf(nowSeconds));
        source = source.replace("%M", String.valueOf(nowMinutes));
        source = source.replace("%H", String.valueOf(nowHours));
        // New Time
        source = source.replace("%_S", String.valueOf(_nowSeconds));
        source = source.replace("%_M", String.valueOf(_nowMinutes));
        source = source.replace("%_H", String.valueOf(_nowHours));


        return source;
    }
}
