package ru.fazziclay.openwidgets.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.Spannable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import ru.fazziclay.fazziclaylibs.NumberUtils;
import ru.fazziclay.openwidgets.R;
import ru.fazziclay.openwidgets.data.widgets.WidgetsData;
import ru.fazziclay.openwidgets.data.widgets.widget.DateWidget;

public class DialogUtils {
    public static void selectDateWidgetDialog(Context context,
                                              String title,
                                              String message,
                                              SelectDateWidgetListenerInterface selectDateWidgetListenerInterface) {
        List<View> widgets = new ArrayList<>();
        List<DateWidget> dateWidgets = WidgetsData.getWidgetsData().getDateWidgets();

        for (DateWidget dateWidget : dateWidgets) {
            Button button = new Button(context);
            button.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 10));
            button.setAllCaps(false);
            button.setOnClickListener(v -> selectDateWidgetListenerInterface.run(dateWidget));
            button.setText(MessageFormat.format("{0} ({1})", context.getString(R.string.widgetName_date), dateWidget.getWidgetId()));
            button.setOnLongClickListener(v -> {
                Utils.showToast(context, "Hello :)))))))))");
                int[] colors = {
                        Color.BLACK,
                        Color.CYAN,
                        Color.WHITE,
                        Color.RED,
                        Color.GREEN,
                        Color.BLACK,
                        Color.MAGENTA,
                        Color.YELLOW,
                        Color.LTGRAY
                };

                button.setTextColor(colors[NumberUtils.getRandom(0, colors.length-1)]);
                return true;
            });
            widgets.add(button);
        }

        DialogUtils.inputDialog(context, title, message, null, widgets.toArray(new View[0]));
    }

    public static void warningDialog(Context context,
                                    String title,
                                    String message,
                                    int icon,
                                    ButtonListenerInterface buttonListenerInterface) {
        DialogUtils.inputDialog(context,
                title,
                message,
                icon,
                true,
                true,
                null,
                null,
                null,
                context.getText(R.string.CHANCEL),
                null,
                context.getString(R.string.APPLY),
                buttonListenerInterface,
                0,
                new Button[]{}
        );
    }

    public static void notifyDialog(Context context,
                                    String title,
                                    Spannable message,
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
                                    Spannable message) {
        DialogUtils.notifyDialog(context, title, message, 0);
    }

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
        String applyButtonText = null;
        if (buttonListenerInterface != null) applyButtonText = context.getString(R.string.APPLY);
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
                applyButtonText,
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
        inputDialog(context, title, message, editTextStart, editTextHint, editTextInputType, buttonListenerInterface, null, null);
    }

    public static void inputDialog(Context context,
                                   String title,
                                   String message,
                                   CharSequence editTextStart,
                                   CharSequence editTextHint,
                                   int editTextInputType,
                                   InputListenerInterface buttonListenerInterface,
                                   String neutralButtonText,
                                   ButtonListenerInterface buttonNeutralListenerInterface) {

        EditText editText = new EditText(context);
        editText.setHint(editTextHint);
        editText.setInputType(editTextInputType);

        DialogUtils.inputDialog(context,
                title,
                message,
                0,
                true,
                true,
                null,//dialog -> DialogUtils.warningDialog(context, title, message + "-- UNSAVED CHANGES!!!", 0, () -> buttonListenerInterface.run(editText.getText().toString())),
                neutralButtonText,
                buttonNeutralListenerInterface,
                context.getText(R.string.CHANCEL),
                null,
                context.getString(R.string.APPLY),
                () -> buttonListenerInterface.run(editText.getText().toString()),
                Gravity.CENTER,
                new EditText[]{editText}
        );

        editText.setText(editTextStart);
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
            LinearLayout b = new LinearLayout(context);
            b.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            ScrollView a = new ScrollView(context);
            a.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            LinearLayout dialogBackground = new LinearLayout(context);
            dialogBackground.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            dialogBackground.setGravity(gravity);

            dialogBackground.setOrientation(LinearLayout.VERTICAL);
            for (View view : views) {
                dialogBackground.addView(view);
            }

            b.addView(a);
            a.addView(dialogBackground);
            dialog.setView(b);
        }

        dialog.show();
    }

    public interface SelectDateWidgetListenerInterface {
        void run(DateWidget responseWidget);
    }

    public interface InputListenerInterface {
        void run(String responseText);
    }

    public interface ButtonListenerInterface {
        void run();
    }
}
