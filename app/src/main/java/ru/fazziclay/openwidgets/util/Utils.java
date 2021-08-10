package ru.fazziclay.openwidgets.util;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.util.Locale;

import ru.fazziclay.fazziclaylibs.FileUtils;
import ru.fazziclay.openwidgets.Logger;
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

    public static void shareText(Context context, String title, String text) {

        Uri uri = Uri.parse(Paths.getAppFilePath() + Logger.LOG_FILE);

        Intent intentShareFile = new Intent(Intent.ACTION_SEND);
        intentShareFile.setType("*/*");
        //intentShareFile.putExtra(Intent.EXTRA_TEXT, text);
        intentShareFile.putExtra(Intent.EXTRA_SUBJECT, "Debug for OpenWidgets app:");
        intentShareFile.putExtra(Intent.EXTRA_STREAM, uri);
        intentShareFile.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(Intent.createChooser(intentShareFile, title));
        //shareFile(context,Paths.getAppFilePath() + Logger.LOG_FILE);
    }

    private static void shareFile(Context context, String filePath) {
        String cachedFilePath = context.getExternalCacheDir().getAbsolutePath() + filePath;
        FileUtils.write(cachedFilePath, FileUtils.read(filePath));

        File file = new File(cachedFilePath);
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");

        share.putExtra(Intent.EXTRA_STREAM,
                Uri.parse(file.getAbsolutePath()));
        //share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        context.startActivity(Intent.createChooser(share, "Share"));
    }

    public static int booleanToVisible(boolean bool, int invisible) {
        if (bool) {
            return View.VISIBLE;
        }
        return invisible;
    }
}
