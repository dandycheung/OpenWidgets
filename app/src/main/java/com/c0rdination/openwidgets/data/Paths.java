package com.c0rdination.openwidgets.data;

import android.content.Context;

import java.io.File;

public class Paths {
    private static final boolean USE_EXTERNAL_PATH = false;

    private static String appFilePath = null;
    private static String appCachePath = null;

    public static void updatePaths(Context context) {
        if (USE_EXTERNAL_PATH) {
            appFilePath = context.getExternalFilesDir("").getPath() + File.separator;
            appCachePath = context.getExternalCacheDir().getPath() + File.separator;
        } else {
            appFilePath = context.getFilesDir().getPath() + File.separator;
            appCachePath = context.getCacheDir().getPath() + File.separator;
        }
    }

    public static String getAppFilePath() {
        return appFilePath;
    }

    public static String getAppCachePath() {
        return appCachePath;
    }
}
