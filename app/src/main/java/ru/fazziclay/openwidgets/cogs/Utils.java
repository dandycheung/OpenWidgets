package ru.fazziclay.openwidgets.cogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;

import static android.content.DialogInterface.BUTTON_NEUTRAL;
import static android.content.DialogInterface.BUTTON_POSITIVE;

public class Utils {
    public static String log_text = "";

    public static void log(String message) {
        log_text = log_text + "\n" + message;
    }


}
