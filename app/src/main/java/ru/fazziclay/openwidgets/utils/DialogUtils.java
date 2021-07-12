package ru.fazziclay.openwidgets.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import ru.fazziclay.openwidgets.R;

public class DialogUtils {
    public static void notifyDialog(Context context,
                                    String title,
                                    String message,
                                    int icon) {
        DialogUtils.inputDialog(context,
                title,
                message,
                icon,
                true,
                true,
                null,
                null,
                null,
                null,
                null,
                context.getString(R.string.OK),
                null,
                0,
                new Button[]{}
        );
    }

    public static void notifyDialog(Context context,
                                    String title,
                                    String message) {
        DialogUtils.notifyDialog(context, title, message, 0);
    }

    public static void inputDialog(Context context,
                                   String title,
                                   String message,
                                   ButtonListenerInterface buttonListenerInterface,
                                   View[] views) {
        DialogUtils.inputDialog(context,
                title,
                message,
                0,
                true,
                true,
                null,
                null,
                null,
                context.getString(R.string.CHANCEL),
                null,
                context.getString(R.string.APPLY),
                buttonListenerInterface,
                Gravity.CENTER,
                views);
    }

    public static void inputDialog(Context context,
                                   String title,
                                   String message,
                                   CharSequence editTextStart,
                                   CharSequence editTextHint,
                                   int editTextInputType,
                                   InputListenerInterface buttonListenerInterface) {

        EditText editText = new EditText(context);
        editText.setText(editTextStart);
        editText.setHint(editTextHint);
        editText.setInputType(editTextInputType);

        DialogUtils.inputDialog(context,
                title,
                message,
                0,
                true,
                true,
                null,
                null,
                null,
                null,
                null,
                context.getString(R.string.APPLY),
                () -> buttonListenerInterface.run(editText.getText().toString()),
                Gravity.CENTER,
                new EditText[]{editText}
        );
    }

    public static void inputDialog(Context context,
                                   CharSequence title,
                                   CharSequence message,
                                   int icon,
                                   boolean cancelable,
                                   boolean canceledOnTouchOutside,
                                   DialogInterface.OnCancelListener onCancelListener,
                                   CharSequence buttonTextNeutral,
                                   ButtonListenerInterface buttonNeutralListener,
                                   CharSequence buttonTextNegative,
                                   ButtonListenerInterface buttonNegativeListener,
                                   CharSequence buttonTextPositive,
                                   ButtonListenerInterface buttonPositiveListener,
                                   int gravity,
                                   View[] views) {

        final AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.setTitle(title);
        dialog.setIcon(icon);
        dialog.setMessage(message);
        dialog.setCancelable(cancelable);
        dialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
        dialog.setOnCancelListener(onCancelListener);
        dialog.setButton(DialogInterface.BUTTON_NEUTRAL, buttonTextNeutral, (dialogInterface, s) -> {if (buttonNeutralListener != null) buttonNeutralListener.run();});
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, buttonTextNegative, (dialogInterface, s) -> {if (buttonNegativeListener != null) buttonNegativeListener.run();});
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, buttonTextPositive, (dialogInterface, s) -> {if (buttonPositiveListener != null) buttonPositiveListener.run();});

        if (views.length > 0) {
            LinearLayout dialogBackground = new LinearLayout(context);
            dialogBackground.setGravity(gravity);

            dialogBackground.setOrientation(LinearLayout.VERTICAL);
            for (View view : views) {
                dialogBackground.addView(view);
            }
            dialog.setView(dialogBackground);
        }

        dialog.show();
    }
}
