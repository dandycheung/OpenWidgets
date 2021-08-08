package ru.fazziclay.openwidgets.util;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.widget.Toast;

import java.io.File;
import java.util.Locale;

import ru.fazziclay.openwidgets.ErrorDetectorWrapper;
import ru.fazziclay.openwidgets.Logger;
import ru.fazziclay.openwidgets.android.ContextSaver;
import ru.fazziclay.openwidgets.data.Paths;

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

    public static void shareText(Context context, String title, String text) {
        /*
        Uri uri = Uri.parse("file://"+Paths.getAppFilePath() + Logger.LOG_FILE);

        Intent intentShareFile = new Intent(Intent.ACTION_SEND);
        intentShareFile.setType("*");
        //intentShareFile.putExtra(Intent.EXTRA_TEXT, text);
        intentShareFile.putExtra(Intent.EXTRA_SUBJECT, "Debug for OpenWidgets app:");
        intentShareFile.putExtra(Intent.EXTRA_STREAM, uri);
        //intentShareFile.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(Intent.createChooser(intentShareFile, title));*/
        shareFile(context, new File(Paths.getAppFilePath() + Logger.LOG_FILE));
    }

    private static void shareFile(Context context, File file) {
        String filePath = file.getAbsolutePath();
        File f = new File(filePath);

        Intent intentShareFile = new Intent(Intent.ACTION_SEND);
        File fileWithinMyDir = new File(filePath);

        if (fileWithinMyDir.exists()) {
            intentShareFile.setType("text/*");
            intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + filePath));
            intentShareFile.putExtra(Intent.EXTRA_SUBJECT, "MyApp File Share: " + f.getName());
            intentShareFile.putExtra(Intent.EXTRA_TEXT, "MyApp File Share: " + f.getName());

            context.startActivity(Intent.createChooser(intentShareFile, f.getName()));
        }
    }
}
