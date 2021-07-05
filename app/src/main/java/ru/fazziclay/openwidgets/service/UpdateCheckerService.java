package ru.fazziclay.openwidgets.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import ru.fazziclay.openwidgets.widgets.data.WidgetsData;

public class UpdateCheckerService extends Service {
    public void onCreate() {
        super.onCreate();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);


        return START_STICKY;
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public IBinder onBind(Intent intent) {
        return null;
    }
}