package com.c0rdination.openwidgets.android.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.c0rdination.openwidgets.android.widget.WidgetsService;

public class AndroidBootCompleted extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))
            WidgetsService.startIfNotStarted(context);
    }
}
