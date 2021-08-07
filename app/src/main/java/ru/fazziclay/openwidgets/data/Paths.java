package ru.fazziclay.openwidgets.data;

import android.content.Context;

import java.io.File;

public class Paths {
    private static String appFilePath = null;
    private static String appCachePath = null;

    public static void updatePaths(Context context) {
        appFilePath = context.getFilesDir().getPath();
        appCachePath = context.getCacheDir().getPath();
    }

    public static String getAppFilePath() {
        if (appFilePath == null) {
            return null;
        }
        return appFilePath + File.separator;
    }

    public static String getAppCachePath() {
        if (appCachePath == null) {
            return null;
        }
        return appCachePath + File.separator;
    }
}
