package ru.fazziclay.openwidgets.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import ru.fazziclay.openwidgets.R;
import ru.fazziclay.openwidgets.cogs.Utils;
import ru.fazziclay.openwidgets.cogs.WidgetsManager;

import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_NEUTRAL;
import static android.content.DialogInterface.BUTTON_POSITIVE;
import static ru.fazziclay.openwidgets.R.id.digital_clock_configurate_background;
import static ru.fazziclay.openwidgets.cogs.Utils.setTextStyle;

public class DigitalClockConfigurate extends AppCompatActivity {
    public int widget_id;
    LinearLayout digital_clock_configurate_backgroundLinearLayout;
    RemoteViews widgetPreviewRemoteViews;
    View widgetPreviewView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_digital_clock_configurate);
        Intent intent = getIntent();
        widget_id = intent.getIntExtra("widget_id", -1);

        digital_clock_configurate_backgroundLinearLayout = findViewById(digital_clock_configurate_background);
        widgetPreviewRemoteViews = new RemoteViews(getApplicationContext().getPackageName(), R.layout.digital_clock);
        widgetPreviewView = widgetPreviewRemoteViews.apply(getApplicationContext(), new LinearLayout(getApplicationContext()));
        digital_clock_configurate_backgroundLinearLayout.addView(widgetPreviewView);

        Button digitalClockConfigurate_textButton = findViewById(R.id.digitalClockConfigurate_text);
        digitalClockConfigurate_textButton.setOnClickListener(v -> {
            // edit text
            EditText textEditText = new EditText(this);
            textEditText.setTextSize(18);
            textEditText.setText(WidgetsManager.getWidgetText(widget_id));
            textEditText.setHint("text");

            // Layout
            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.addView(textEditText);

            final AlertDialog dialog = new AlertDialog.Builder(this).create();
            dialog.setTitle("Widget text");
            dialog.setButton(BUTTON_POSITIVE, "Save", (dialog0, which) -> {
                try {
                    String text = textEditText.getText().toString();
                    WidgetsManager.changeWidgetText(widget_id, text);

                } catch (Exception e) {
                    Utils.showMessage(this, "Run error: "+e.toString());
                }
            });
            dialog.setButton(BUTTON_NEGATIVE, "Chancel", (dialog2, which) -> {});
            dialog.setButton(BUTTON_NEUTRAL, "Variables", (dialog2, which) -> {
                TextView textView = new TextView(this);
                textView.setTextSize(16);
                textView.setText("%H - This hour\n%M - This minute\n%S - This second\n%_H - This hour in 100h format\n%_M - This minute in 100h format\n%_S - This second in 100h format");

                // Layout
                LinearLayout variablesLayout = new LinearLayout(this);
                variablesLayout.setOrientation(LinearLayout.VERTICAL);
                variablesLayout.addView(textView);

                final AlertDialog variablesDialog = new AlertDialog.Builder(this).create();
                variablesDialog.setTitle("Variables");
                variablesDialog.setButton(BUTTON_POSITIVE, "OK", (dialog0, which1) -> {});

                variablesDialog.setView(variablesLayout);
                variablesDialog.show();
            });

            dialog.setView(layout);
            dialog.show();
        });


        Button digitalClockConfigurate_textColorButton = findViewById(R.id.digitalClockConfigurate_textColor);
        digitalClockConfigurate_textColorButton.setOnClickListener(v -> {
            // edit text
            EditText textEditText = new EditText(this);
            textEditText.setTextSize(18);
            textEditText.setText(WidgetsManager.getWidgetColor(widget_id));
            textEditText.setHint("text");

            // Layout
            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.addView(textEditText);

            final AlertDialog dialog = new AlertDialog.Builder(this).create();
            dialog.setTitle("Widget color");
            dialog.setButton(BUTTON_POSITIVE, "Save", (dialog0, which) -> {
                try {
                    String color = textEditText.getText().toString();
                    WidgetsManager.changeWidgetColor(widget_id, color);

                } catch (Exception e) {
                    Utils.showMessage(this, "Run error: "+e.toString());
                }
            });
            dialog.setButton(BUTTON_NEGATIVE, "Chancel", (dialog2, which) -> {});

            dialog.setView(layout);
            dialog.show();
        });
/*
        Button digitalClockConfigurate_textSizeButton = findViewById(R.id.digitalClockConfigurate_textSize);
        digitalClockConfigurate_textSizeButton.setOnClickListener(v -> {
            // edit text
            EditText textEditText = new EditText(this);
            textEditText.setTextSize(18);
            textEditText.setText(WidgetsManager.getWidgetTextSize(widget_id));
            textEditText.setHint("text");

            // Layout
            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.addView(textEditText);

            final AlertDialog dialog = new AlertDialog.Builder(this).create();
            dialog.setTitle("Widget text size");
            dialog.setButton(BUTTON_POSITIVE, "Save", (dialog0, which) -> {
                try {
                    String color = textEditText.getText().toString();
                    WidgetsManager.changeWidgetTextSize(widget_id, color);

                } catch (Exception e) {
                    Utils.showMessage(this, "Run error: "+e.toString());
                }
            });
            dialog.setButton(BUTTON_NEGATIVE, "Chancel", (dialog2, which) -> {});

            dialog.setView(layout);
            dialog.show();
        });
*/
        Button digitalClockConfigurate_textStyleButton = findViewById(R.id.digitalClockConfigurate_textStyle);
        digitalClockConfigurate_textStyleButton.setOnClickListener(v -> {
            CheckBox checkBoxBold = new CheckBox(this);
            checkBoxBold.setText(R.string.textStyle_bold);


            CheckBox checkBoxItalic = new CheckBox(this);
            checkBoxItalic.setText(R.string.textStyle_italic);

            int currentStyle = WidgetsManager.getWidgetTextStyle(widget_id);
            if (currentStyle == 1 || currentStyle == 3) {
                checkBoxBold.setChecked(true);
            }

            if (currentStyle == 2 || currentStyle == 3) {
                checkBoxItalic.setChecked(true);
            }

            // Layout
            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.addView(checkBoxBold);
            layout.addView(checkBoxItalic);

            final AlertDialog dialog = new AlertDialog.Builder(this).create();
            dialog.setTitle("Widget text style");
            dialog.setButton(BUTTON_POSITIVE, "Save", (dialog0, which) -> {
                int style = 0;
                if (checkBoxBold.isChecked() && checkBoxItalic.isChecked()) {
                    style = 3;
                }

                if (!checkBoxBold.isChecked() && checkBoxItalic.isChecked()) {
                    style = 2;
                }

                if (checkBoxBold.isChecked() && !checkBoxItalic.isChecked()) {
                    style = 1;
                }

                try {
                    WidgetsManager.changeWidgetTextStyle(widget_id, style);

                } catch (Exception e) {
                    Utils.showMessage(this, "Run error: "+e.toString());
                }
            });
            dialog.setButton(BUTTON_NEGATIVE, "Chancel", (dialog2, which) -> {});

            dialog.setView(layout);
            dialog.show();
        });



        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    loop();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                handler.postDelayed(this, 1000);
            }
        };
        handler.post(runnable);
    }

    private void loop() throws JSONException {
        JSONObject widgetData;
        digital_clock_configurate_backgroundLinearLayout.removeView(widgetPreviewView);
        widgetData = WidgetsManager.widgets.getJSONObject("data").getJSONObject(String.valueOf(widget_id));

        // Get widgetData
        SpannableString text = new SpannableString(Utils.dateFormat(widgetData.getString("text")));
        int text_color = Color.parseColor(widgetData.getString("text_color"));
        int text_style = widgetData.getInt("text_style");
        int text_size = widgetData.getInt("text_size");
        int background_color = Color.parseColor(widgetData.getString("background_color"));


        setTextStyle(text, text_style);                                                                         // Стиль текста
        widgetPreviewRemoteViews.setTextViewText(R.id.digital_clock_widget_text, text);                                            // Текст
        widgetPreviewRemoteViews.setTextViewTextSize(R.id.digital_clock_widget_text, 1, text_size);                          // Размер текста
        widgetPreviewRemoteViews.setTextColor(R.id.digital_clock_widget_text, text_color);                                         // Цвет текста
        widgetPreviewRemoteViews.setInt(R.id.digital_clock_widget_background, "setBackgroundColor", background_color); // Фоновый цвет виджета

        widgetPreviewView = widgetPreviewRemoteViews.apply(getApplicationContext(), new LinearLayout(getApplicationContext()));
        digital_clock_configurate_backgroundLinearLayout.addView(widgetPreviewView);
    }
}