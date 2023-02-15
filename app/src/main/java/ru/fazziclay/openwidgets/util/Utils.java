package ru.fazziclay.openwidgets.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Locale;

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
        Intent intentShareFile = new Intent(Intent.ACTION_SEND);
        intentShareFile.setType("text/plain");
        intentShareFile.putExtra(Intent.EXTRA_TEXT, text);
        context.startActivity(Intent.createChooser(intentShareFile, title));
    }

    public static int booleanToVisible(boolean bool, int invisible) {
        if (bool)
            return View.VISIBLE;

        return invisible;
    }

    public static Bitmap loadBitmapFromView(Context context, View v) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        v.measure(View.MeasureSpec.makeMeasureSpec(dm.widthPixels, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(dm.heightPixels, View.MeasureSpec.EXACTLY));
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        Bitmap returnedBitmap = Bitmap.createBitmap(v.getMeasuredWidth(),
                v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(returnedBitmap);
        v.draw(c);

        return returnedBitmap;
    }

    public static void takeScreenshot(Activity activity, File imageFile) throws Exception {
        View rootView = activity.getWindow().getDecorView().getRootView();
        Bitmap bitmap = loadBitmapFromView(activity, rootView);
        FileOutputStream outputStream = new FileOutputStream(imageFile);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        outputStream.flush();
        outputStream.close();
    }
}
