package ru.fazziclay.openwidgets.util;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.service.notification.StatusBarNotification;

import ru.fazziclay.openwidgets.Logger;

public class NotificationUtils {
    public static void createChannel(Context context, String channelId, String name, String description) {
        final Logger LOGGER = new Logger(NotificationUtils.class, "createChannel");
        LOGGER.log("channelId="+channelId+", name="+name+", description="+description);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, name, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(description);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            LOGGER.log("Done");
        } else {
            LOGGER.log("Android version not supported");
        }
    }

    public static boolean isShowed(Context context, int id) {
        final Logger LOGGER = new Logger(NotificationUtils.class, "isShowed");
        LOGGER.log("id="+id);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (StatusBarNotification a : manager.getActiveNotifications()) {
                if (a.getId() == id) {
                    LOGGER.returned(String.valueOf(true));
                    return true;
                }
            }
        } else {
            LOGGER.log("Android version not supported");
        }
        return false;
    }
}
