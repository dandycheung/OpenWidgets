package com.c0rdination.openwidgets.util;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.service.notification.StatusBarNotification;

import com.c0rdination.openwidgets.Logger;

public class NotificationUtils {
    public static final byte IMPORTANCE_LOW = 10;

    public static void createChannel(Context context, String channelId, String name, String description, byte importance) {
        final Logger LOGGER = new Logger();
        LOGGER.info("channelId="+channelId+", name="+name+", description="+description+", importance="+importance);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int i = NotificationManager.IMPORTANCE_DEFAULT;
            if (importance == IMPORTANCE_LOW)
                i = NotificationManager.IMPORTANCE_LOW;

            NotificationChannel channel = new NotificationChannel(channelId, name, i);
            channel.setDescription(description);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        } else {
            LOGGER.info("Android version not supported. Minimal supported: "+Build.VERSION_CODES.O);
        }

        LOGGER.done();
    }

    public static boolean isShowed(Context context, int id) {
        final Logger LOGGER = new Logger();
        LOGGER.info("id="+id);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (StatusBarNotification a : manager.getActiveNotifications()) {
                if (a.getId() == id) {
                    LOGGER.returned(true);
                    return true;
                }
            }
        } else {
            LOGGER.info("Android version not supported. Minimal supported="+Build.VERSION_CODES.M);
        }

        LOGGER.returned(false);
        return false;
    }
}
