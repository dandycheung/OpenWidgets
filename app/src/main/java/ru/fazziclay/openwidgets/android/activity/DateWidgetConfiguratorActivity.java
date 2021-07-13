package ru.fazziclay.openwidgets.android.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.rarepebble.colorpicker.ColorPickerView;

import ru.fazziclay.openwidgets.R;
import ru.fazziclay.openwidgets.deprecated.cogs.DeprecatedUtils;
import ru.fazziclay.openwidgets.util.ColorUtils;
import ru.fazziclay.openwidgets.util.DialogUtils;
import ru.fazziclay.openwidgets.deprecated.widgets.WidgetsManager;
import ru.fazziclay.openwidgets.deprecated.widgets.data.DateWidget;
import ru.fazziclay.openwidgets.deprecated.widgets.data.WidgetsData;

import static ru.fazziclay.openwidgets.ErrorDetectorWrapper.errorDetectorWrapper;

public class DateWidgetConfiguratorActivity extends AppCompatActivity {
    int widgetId;
    DateWidget widget;

    boolean isError = false;

    LinearLayout widgetPreview;
    Button patternContentButton;
    Button patternSizeButton;
    Button patternColorButton;
    Button patternBackgroundColorButton;
    Button backgroundColorButton;
    Button backgroundGravityButton;


    private void loadVariables() {
        widgetPreview = findViewById(R.id.layout_widgetPreview);
        patternContentButton = findViewById(R.id.pattern_content_button);
        patternSizeButton = findViewById(R.id.pattern_size_button);
        patternColorButton = findViewById(R.id.pattern_color_button);
        patternBackgroundColorButton = findViewById(R.id.pattern_backgroundColor_button);
        backgroundColorButton = findViewById(R.id.background_color_button);
        backgroundGravityButton = findViewById(R.id.background_gravity_button);
    }

    private void loadLogic() {
        patternContentButton.setOnClickListener(v -> errorDetectorWrapper(() -> DialogUtils.inputDialog(this,
                null,
                null,
                widget.pattern,
                null,
                InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE,
                responseText -> errorDetectorWrapper(() -> {
                    widget.pattern = responseText;
                    WidgetsData.save();
                }))));

        patternSizeButton.setOnClickListener(v -> errorDetectorWrapper(() -> DialogUtils.inputDialog(this,
                null,
                null,
                String.valueOf(widget.patternSize),
                null,
                InputType.TYPE_CLASS_NUMBER,
                responseText -> errorDetectorWrapper(() -> {
                    widget.patternSize = Integer.parseInt(responseText);
                    WidgetsData.save();
                }))));

        patternColorButton.setOnClickListener(v -> errorDetectorWrapper(() -> {
            final ColorPickerView colorPickerView = new ColorPickerView(this);
            colorPickerView.setColor(Color.parseColor(widget.patternColor));
            colorPickerView.showAlpha(true);
            colorPickerView.showHex(true);
            colorPickerView.showPreview(true);

            DialogUtils.inputDialog(this,
                    null,
                    null,
                    () -> {
                        widget.patternColor = ColorUtils.colorToHex(colorPickerView.getColor());
                        WidgetsData.save();
                    },
                    new ColorPickerView[]{colorPickerView});
        }));

        patternBackgroundColorButton.setOnClickListener(v -> errorDetectorWrapper(() -> {
            final ColorPickerView colorPickerView = new ColorPickerView(this);
            colorPickerView.setColor(Color.parseColor(widget.patternColor));
            colorPickerView.showAlpha(true);
            colorPickerView.showHex(true);
            colorPickerView.showPreview(true);

            DialogUtils.inputDialog(this,
                    null,
                    null,
                    () -> {
                        widget.patternBackgroundColor = ColorUtils.colorToHex(colorPickerView.getColor());
                        WidgetsData.save();
                    },
                    new ColorPickerView[]{colorPickerView});
        }));

        backgroundColorButton.setOnClickListener(v -> errorDetectorWrapper(() -> {
            final ColorPickerView colorPickerView = new ColorPickerView(this);
            colorPickerView.setColor(Color.parseColor(widget.patternColor));
            colorPickerView.showAlpha(true);
            colorPickerView.showHex(true);
            colorPickerView.showPreview(true);

            DialogUtils.inputDialog(this,
                    null,
                    null,
                    () -> {
                        widget.backgroundColor = ColorUtils.colorToHex(colorPickerView.getColor());
                        WidgetsData.save();
                    },
                    new ColorPickerView[]{colorPickerView});
        }));

        backgroundGravityButton.setOnClickListener(v -> {
            CharSequence[] itemsNames = {getText(R.string.gravity_center), getText(R.string.gravity_center_horizontal), getText(R.string.gravity_center_vertical), getText(R.string.gravity_bottom), getText(R.string.gravity_left), getText(R.string.gravity_right), getText(R.string.gravity_top)};
            int[] itemsValues = {Gravity.CENTER, Gravity.CENTER_HORIZONTAL, Gravity.CENTER_VERTICAL, Gravity.BOTTOM, Gravity.START, Gravity.END, Gravity.TOP};
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
    }

    private void loadWidgetPreview() {
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    widgetPreview.removeAllViews();

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
                    views.setTextViewText(R.id.widget_date_text, DeprecatedUtils.dateFormat(widget.pattern));
                    views.setTextViewTextSize(R.id.widget_date_text, widget.patternSizeUnits, widget.patternSize);
                    views.setTextColor(R.id.widget_date_text, patternColor);
                    views.setInt(R.id.widget_date_text, "setBackgroundColor", patternBackgroundColor);
                    views.setInt(R.id.widget_date_background, "setBackgroundColor", backgroundColor);
                    views.setInt(R.id.widget_date_background, "setGravity", widget.backgroundGravity);
                    widgetPreview.addView(views.apply(getApplicationContext(), widgetPreview));

                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (widget != null || !isFinishing()) {
                    handler.postDelayed(this, 250);
                }
            }
        };
        handler.post(runnable);
    }

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
        loadVariables();
        loadLogic();
        loadWidgetPreview();
    }
}