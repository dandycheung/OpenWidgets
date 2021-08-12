package ru.fazziclay.openwidgets.data;

import android.content.Context;

import java.io.File;

public class Paths {
    private static final boolean USE_EXTERNAL_PATH = true; // TODO: 12.08.2021 change USE_EXTERNAL_PATH to false

    private static String appFilePath = null;
    private static String appCachePath = null;

    public static void updatePaths(Context context) {
        if (USE_EXTERNAL_PATH) {
            appFilePath = context.getExternalFilesDir("").getPath();
            appCachePath = context.getExternalCacheDir().getPath();
        } else {
            appFilePath = context.getFilesDir().getPath();
            appCachePath = context.getCacheDir().getPath();
        }
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
