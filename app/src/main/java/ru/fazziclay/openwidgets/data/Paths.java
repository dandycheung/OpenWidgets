package ru.fazziclay.openwidgets.data;

import android.content.Context;

public class Paths {
    public static String appFilePath = null;
    public static String appCachePath = null;

    public static void updatePaths(Context context) {
        appFilePath = context.getFilesDir().getPath();
        appCachePath = context.getCacheDir().getPath();
    }
}
