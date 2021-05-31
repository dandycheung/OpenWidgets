package ru.fazziclay.openwidgets.cogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import ru.fazziclay.openwidgets.R;


public class DialogUtils {
    public static void notifyDialog(Context context, String title, String message) {
        EditText view = new EditText(context);
        view.setTextSize(18);
        view.setText(message);

        final AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.setTitle(title);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, context.getResources().getString(R.string.OK), (dialog0, which) -> {});

        dialog.setView(view);
        dialog.show();
    }

    public static void inputDialog(Context context,
                                   String title,
                                   String buttonTextNeutral,
                                   InputDialogInterface buttonInterfaceNeutral,
                                   String buttonTextNegative,
                                   InputDialogInterface buttonInterfaceNegative,
                                   String buttonTextPositive,
                                   InputDialogInterface buttonInterfacePositive, View... views) {

        final AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.setTitle(title);
        dialog.setButton(DialogInterface.BUTTON_NEUTRAL, buttonTextNeutral, (dialogInterface, s) -> buttonInterfaceNeutral.onRun(null));
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, buttonTextNegative, (dialogInterface, s) -> buttonInterfaceNegative.onRun(null));
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, buttonTextPositive, (dialogInterface, s) -> buttonInterfacePositive.onRun(null));

        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        for (View view : views) {
            linearLayout.addView(view);
        }

        dialog.setView(linearLayout);
        dialog.show();
    }

    public static void inputDialog(Context context,
                                   String title,
                                   String message,
                                   String hint,
                                   int inputType,
                                   String buttonTextNeutral,
                                   InputDialogInterface buttonInterfaceNeutral,
                                   String buttonTextNegative,
                                   InputDialogInterface buttonInterfaceNegative,
                                   String buttonTextPositive,
                                   InputDialogInterface buttonInterfacePositive) {
        EditText view = new EditText(context);
        view.setTextSize(18);
        view.setHint(hint);
        if (inputType != -1) view.setInputType(inputType);
        view.setText(message);

        final AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.setTitle(title);
        dialog.setButton(DialogInterface.BUTTON_NEUTRAL, buttonTextNeutral, (dialogInterface, s) -> buttonInterfaceNeutral.onRun(view.getText().toString()));
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, buttonTextNegative, (dialogInterface, s) -> buttonInterfaceNegative.onRun(view.getText().toString()));
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, buttonTextPositive, (dialogInterface, s) -> buttonInterfacePositive.onRun(view.getText().toString()));

        dialog.setView(view);
        dialog.show();
    }

    public static void inputDialog(Context context,
                                   String title,
                                   String message,
                                   String hint,
                                   int inputType,
                                   String buttonTextPositive,
                                   InputDialogInterface buttonInterfacePositive) {
        inputDialog(context, title, message, hint, inputType, "", (d) -> {}, "Chancel", (d) -> {}, buttonTextPositive, buttonInterfacePositive);
    }

}
