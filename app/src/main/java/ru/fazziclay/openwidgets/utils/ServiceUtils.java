package ru.fazziclay.openwidgets.utils;

import android.app.ActivityManager;
import android.content.Context;

public class ServiceUtils {
    public static boolean isServiceStarted(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (service.service.getClassName().contains("WidgetsUpdaterService")) {
                return true;
            }
        }
        return false;
    }
}
