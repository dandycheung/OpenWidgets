package ru.fazziclay.openwidgets.android;

import android.annotation.SuppressLint;
import android.content.Context;

@SuppressLint("StaticFieldLeak")
public class ContextSaver {
    public static ContextSaver contextSaver = null;
    Context context = null;

    public static void setContext(Context context) {
        initContextSaverObject();
        contextSaver.context = context;
    }

    public static Context getContext() {
        initContextSaverObject();
        return contextSaver.context;
    }

    public static boolean isContextAvailable() {
        initContextSaverObject();
        return (contextSaver.context != null);
    }

    private static void initContextSaverObject() {
        if (contextSaver == null) {
            contextSaver = new ContextSaver();
        }
    }
}
