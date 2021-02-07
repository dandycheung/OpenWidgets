package ru.fazziclay.openwidgets;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;



public class WidgetsUpdaterService extends Service {
    public WidgetsUpdaterService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}