package ru.fazziclay.openwidgets.util;

import android.content.Context;
import android.content.res.Configuration;
import android.widget.Toast;

import java.util.Locale;

import ru.fazziclay.openwidgets.ErrorDetectorWrapper;
import ru.fazziclay.openwidgets.android.ContextSaver;

public class Utils {
    public static void setAppLanguage(Context context, String language) {
        Configuration config = new Configuration();
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        config.locale = locale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }

    public static void showToast(Context context, String message, int duration) {
        Toast.makeText(context, message, duration).show();
    }

    public static void showToast(Context context, String message) {
        showToast(context, message, Toast.LENGTH_SHORT);
    }

    public static void showToast(String message) {
        ErrorDetectorWrapper.errorDetectorWrapper(() -> showToast(ContextSaver.getContext(), message, Toast.LENGTH_SHORT));
    }
}
