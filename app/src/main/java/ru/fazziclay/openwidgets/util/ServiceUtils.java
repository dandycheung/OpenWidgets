package ru.fazziclay.openwidgets.util;

import android.app.ActivityManager;
import android.content.Context;

import java.util.ArrayList;

public class ServiceUtils {
    public static boolean isServiceStarted(Context context, Class search) {
        ArrayList<String> list = getStartedServices(context);
        for (String service : list) {
            if (service.equals(search.getName())) {
                return true;
            }
        }
        return false;
    }

    public static ArrayList<String> getStartedServices(Context context) {
        ArrayList<String> list = new ArrayList<>();
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            list.add(service.service.getClassName());
        }
        return list;
    }
}
