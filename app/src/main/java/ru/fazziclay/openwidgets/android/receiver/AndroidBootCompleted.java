package ru.fazziclay.openwidgets.android.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import ru.fazziclay.openwidgets.android.service.WidgetsUpdaterService;

public class AndroidBootCompleted extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) WidgetsUpdaterService.startIsNot(context);
    }
}
