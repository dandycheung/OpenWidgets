package ru.fazziclay.openwidgets.cogs;

import android.content.Context;
import android.text.SpannableString;
import android.text.format.Time;
import android.text.style.StyleSpan;
import android.widget.Toast;

public class Utils {
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
