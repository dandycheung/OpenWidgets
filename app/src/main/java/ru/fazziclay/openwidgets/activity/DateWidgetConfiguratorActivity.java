package ru.fazziclay.openwidgets.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.rarepebble.colorpicker.ColorPickerView;

import ru.fazziclay.openwidgets.R;
import ru.fazziclay.openwidgets.cogs.DialogUtils;
import ru.fazziclay.openwidgets.cogs.Utils;
import ru.fazziclay.openwidgets.widgets.WidgetsManager;
import ru.fazziclay.openwidgets.widgets.data.DateWidget;
import ru.fazziclay.openwidgets.widgets.data.WidgetsData;

public class DateWidgetConfiguratorActivity extends AppCompatActivity {
    int widgetId;
    DateWidget widget;

    boolean isError = false;

    Button patternContentButton;
    Button patternSizeButton;
    Button patternColorButton;
    Button patternBackgroundColorButton;
    Button backgroundColorButton;
    Button backgroundGravityButton;

    @Override
    protected void onResume() {
        super.onResume();
        if (!isError) {
            widget = (DateWidget) WidgetsManager.getWidgetById(widgetId);
            if (widget == null) {
                isError = true;
                setContentView(R.layout.error_message);
                TextView textView = findViewById(R.id.error_message);
                textView.setTextSize(40f);
                textView.setText(R.string.widgetConfigurator_widgetDeleted);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.activityTitle_dateWidgetConfigurator);

        if (!isError) {
            widgetId = getIntent().getIntExtra("widget_id", -99991);
            if (widgetId < -99990) {
                isError = true;
                setContentView(R.layout.error_message);
                TextView textView = findViewById(R.id.error_message);
                textView.setTextSize(40f);
                textView.setText(("Failed to create DateWidgetConfigurator. widgetId=" + widgetId));
                return;
            }
        }

        if (!isError) {
            widget = (DateWidget) WidgetsManager.getWidgetById(widgetId);
            if (widget == null) {
                isError = true;
                setContentView(R.layout.error_message);
                TextView textView = findViewById(R.id.error_message);
                textView.setTextSize(40f);
                textView.setText(R.string.widgetConfigurator_widgetDeleted);
                return;
            }
        }

        setContentView(R.layout.activity_date_widget_configurator);

        patternContentButton = findViewById(R.id.pattern_content_button);
        patternSizeButton = findViewById(R.id.pattern_size_button);
        patternColorButton = findViewById(R.id.pattern_color_button);
        patternBackgroundColorButton = findViewById(R.id.pattern_backgroundColor_button);
        backgroundColorButton = findViewById(R.id.background_color_button);
        backgroundGravityButton = findViewById(R.id.background_gravity_button);


        patternContentButton.setOnClickListener(v -> DialogUtils.inputDialog(this, "---", widget.pattern, "", -1, getText(R.string.APPLY).toString(), responseText -> {
            widget.pattern = responseText;
            WidgetsData.save();
        }));

        patternSizeButton.setOnClickListener(v -> {
            EditText sizeEditText = new EditText(this);
            EditText sizeUnitsEditText = new EditText(this);
            sizeEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
            sizeEditText.setHint("Size");
            sizeUnitsEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
            sizeUnitsEditText.setHint("Size Units");

            DialogUtils.inputDialog(this, "---", getText(R.string.APPLY).toString(), a -> {
                widget.patternSize = Integer.parseInt(sizeEditText.getText().toString());
                widget.patternSizeUnits = Integer.parseInt(sizeUnitsEditText.getText().toString());
                WidgetsData.save();
            }, sizeEditText, sizeUnitsEditText);
        });

        patternSizeButton.setOnClickListener(v -> {
            CharSequence size = "Size";
            CharSequence sizeUnits = "Size Units";

            TextView textView1 = new TextView(this);
            textView1.setText(size);
            TextView textView2 = new TextView(this);
            textView2.setText(sizeUnits);

            EditText sizeEditText = new EditText(this);
            EditText sizeUnitsEditText = new EditText(this);
            sizeEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
            sizeEditText.setHint(size);
            sizeEditText.setText(String.valueOf(widget.patternSize));
            sizeUnitsEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
            sizeUnitsEditText.setHint(sizeUnits);
            sizeUnitsEditText.setText(String.valueOf(widget.patternSizeUnits));

            DialogUtils.inputDialog(this, "---", getText(R.string.APPLY).toString(), a -> {
                widget.patternSize = Integer.parseInt(sizeEditText.getText().toString());
                widget.patternSizeUnits = Integer.parseInt(sizeUnitsEditText.getText().toString());
                WidgetsData.save();
            }, textView1, sizeEditText, textView2, sizeUnitsEditText);
        });

        patternColorButton.setOnClickListener(v -> {
            final ColorPickerView colorPickerView = new ColorPickerView(this);
            colorPickerView.setColor(Color.parseColor(widget.patternColor));
            colorPickerView.showAlpha(true);
            colorPickerView.showHex(true);
            colorPickerView.showPreview(true);

            DialogUtils.inputDialog(this, "---", getText(R.string.APPLY).toString(), a -> {
                widget.patternColor = Utils.ColorToHex(colorPickerView.getColor());
                WidgetsData.save();
            }, colorPickerView);
        });

        patternBackgroundColorButton.setOnClickListener(v -> {
            final ColorPickerView colorPickerView = new ColorPickerView(this);
            colorPickerView.setColor(Color.parseColor(widget.patternBackgroundColor));
            colorPickerView.showAlpha(true);
            colorPickerView.showHex(true);
            colorPickerView.showPreview(true);

            DialogUtils.inputDialog(this, "---", getText(R.string.APPLY).toString(), a -> {
                widget.patternBackgroundColor = Utils.ColorToHex(colorPickerView.getColor());
                WidgetsData.save();
            }, colorPickerView);
        });

        backgroundColorButton.setOnClickListener(v -> {
            final ColorPickerView colorPickerView = new ColorPickerView(this);
            colorPickerView.setColor(Color.parseColor(widget.backgroundColor));
            colorPickerView.showAlpha(true);
            colorPickerView.showHex(true);
            colorPickerView.showPreview(true);

            DialogUtils.inputDialog(this, "---", getText(R.string.APPLY).toString(), a -> {
                widget.backgroundColor = Utils.ColorToHex(colorPickerView.getColor());
                WidgetsData.save();
            }, colorPickerView);
        });

        backgroundGravityButton.setOnClickListener(v -> {
            CharSequence[] itemsNames = {getText(R.string.gravity_center), getText(R.string.gravity_center_horizontal), getText(R.string.gravity_center_vertical), getText(R.string.gravity_bottom), getText(R.string.gravity_left), getText(R.string.gravity_right), getText(R.string.gravity_top)};
            @SuppressLint("RtlHardcoded") int[] itemsValues = {Gravity.CENTER, Gravity.CENTER_HORIZONTAL, Gravity.CENTER_VERTICAL, Gravity.BOTTOM, Gravity.LEFT, Gravity.RIGHT, Gravity.TOP};
            int current = -1;
            for (int s : itemsValues) {
                current++;
                if (s == widget.backgroundGravity) {
                    break;
                }
            }


            new AlertDialog.Builder(this)
                    .setSingleChoiceItems(itemsNames, current, null)
                    .setPositiveButton(getText(R.string.APPLY), (dialog, whichButton) -> {
                        dialog.dismiss();
                        int selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
                        widget.backgroundGravity = itemsValues[selectedPosition];
                    })
                    .show();
        });

        LinearLayout linearLayout = findViewById(R.id.layout_widgetPreview);
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    linearLayout.removeAllViews();

                    int patternColor = Color.parseColor("#ffffff");
                    int patternBackgroundColor = Color.parseColor("#00000000");
                    int backgroundColor = Color.parseColor("#ff6993");

                    try {
                        patternColor = Color.parseColor(widget.patternColor);
                        patternBackgroundColor = Color.parseColor(widget.patternBackgroundColor);
                        backgroundColor = Color.parseColor(widget.backgroundColor);
                    } catch (Exception e) {
                        widget.patternColor = "#ffffff";
                        widget.patternBackgroundColor = "#00000000";
                        widget.backgroundColor = "#ff6993";
                        WidgetsData.save();
                    }


                    RemoteViews views = new RemoteViews(getApplicationContext().getPackageName(), R.layout.widget_date);
                    views.setTextViewText(R.id.widget_date_text, Utils.dateFormat(widget.pattern));
                    views.setTextViewTextSize(R.id.widget_date_text, widget.patternSizeUnits, widget.patternSize);
                    views.setTextColor(R.id.widget_date_text, patternColor);
                    views.setInt(R.id.widget_date_text, "setBackgroundColor", patternBackgroundColor);
                    views.setInt(R.id.widget_date_background, "setBackgroundColor", backgroundColor);
                    views.setInt(R.id.widget_date_background, "setGravity", widget.backgroundGravity);
                    linearLayout.addView(views.apply(getApplicationContext(), linearLayout));

                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (widget != null) {
                    handler.postDelayed(this, 250);
                }
            }
        };
        handler.post(runnable);


    }
}