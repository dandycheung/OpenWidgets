package ru.fazziclay.openwidgets.android.activity.configurator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.rarepebble.colorpicker.ColorPickerView;

import java.util.ArrayList;
import java.util.List;

import ru.fazziclay.openwidgets.R;
import ru.fazziclay.openwidgets.android.activity.SettingsActivity;
import ru.fazziclay.openwidgets.data.settings.SettingsData;
import ru.fazziclay.openwidgets.data.widgets.WidgetsData;
import ru.fazziclay.openwidgets.android.widget.DateWidget;
import ru.fazziclay.openwidgets.util.ColorUtils;
import ru.fazziclay.openwidgets.util.DialogUtils;
import ru.fazziclay.openwidgets.util.Utils;

import static ru.fazziclay.openwidgets.ErrorDetectorWrapper.errorDetectorWrapper;

public class DateWidgetConfiguratorActivity extends AppCompatActivity {
    boolean isRunning = false;
    int widgetId;
    DateWidget widget;

    LinearLayout layout_widgetPreview;
    Button pattern_content_button;
    Button pattern_size_button;
    Button pattern_color_button;
    Button pattern_backgroundColor_button;
    Button pattern_padding_button;
    Button background_color_button;
    Button background_gravity_button;
    Button background_padding_button;

    private void loadVariables() {
        layout_widgetPreview = findViewById(R.id.layout_widgetPreview);
        pattern_content_button = findViewById(R.id.pattern_content_button);
        pattern_size_button = findViewById(R.id.pattern_size_button);
        pattern_color_button = findViewById(R.id.pattern_color_button);
        pattern_backgroundColor_button = findViewById(R.id.pattern_backgroundColor_button);
        pattern_padding_button = findViewById(R.id.pattern_backgroundPadding_button);
        background_color_button = findViewById(R.id.background_color_button);
        background_gravity_button = findViewById(R.id.background_gravity_button);
        background_padding_button = findViewById(R.id.background_padding_button);
    }

    private void loadLogic() {
        pattern_content_button.setOnClickListener(v -> {
            EditText text = new EditText(this);
            text.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
            text.setText(widget.pattern);

            DialogUtils.inputDialog(this,
                    getString(R.string.widgetConfigurator_date_patternContent),
                    null,
                    0,
                    true,
                    true,
                    null,
                    getString(R.string.widgetConfigurator_date_patternContent_help),
                    () -> DialogUtils.notifyDialog(this, getString(R.string.widgetConfigurator_date_patternContent_help), getString(R.string.widgetConfigurator_date_patternContent_helpText), R.drawable.ic_launcher_foreground),
                    getString(R.string.CANCEL),
                    null,
                    getString(R.string.APPLY),
                    () -> {
                        widget.pattern = text.getText().toString();
                        WidgetsData.save();
                    },
                    Gravity.CENTER,
                    new EditText[]{text});

                  /*  DialogUtils.inputDialog(this,
                            getString(R.string.widgetConfigurator_date_patternContent),
                            null,
                            widget.pattern,
                            null,
                            InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE,
                            responseText -> {
                                widget.pattern = responseText;
                                WidgetsData.save();
                            },
                            getString(R.string.widgetConfigurator_date_patternContent_help),
                            () -> DialogUtils.notifyDialog(this, getString(R.string.widgetConfigurator_date_patternContent_help), getString(R.string.widgetConfigurator_date_patternContent_helpText), R.drawable.ic_launcher_foreground));
                */}
        );

        pattern_size_button.setOnClickListener(v -> DialogUtils.inputDialog(this,
                getString(R.string.widgetConfigurator_date_patternSize),
                null,
                String.valueOf(widget.patternSize),
                null,
                InputType.TYPE_CLASS_NUMBER,
                responseText -> errorDetectorWrapper(this, () -> {
                    widget.patternSize = Integer.parseInt(responseText);
                    WidgetsData.save();
                }))
        );

        pattern_color_button.setOnClickListener(v -> {
            final ColorPickerView colorPickerView = new ColorPickerView(this);
            colorPickerView.setColor(ColorUtils.parseColor(widget.patternColor, "#ffffffff"));
            colorPickerView.showAlpha(true);
            colorPickerView.showHex(true);
            colorPickerView.showPreview(true);

            DialogUtils.inputDialog(this,
                    getString(R.string.widgetConfigurator_date_patternColor),
                    null,
                    () -> {
                        widget.patternColor = ColorUtils.colorToHex(colorPickerView.getColor());
                        WidgetsData.save();
                    },
                    new ColorPickerView[]{colorPickerView});
        });

        pattern_backgroundColor_button.setOnClickListener(v -> {
            final ColorPickerView colorPickerView = new ColorPickerView(this);
            colorPickerView.setColor(ColorUtils.parseColor(widget.patternBackgroundColor, "#ffffffff"));
            colorPickerView.showAlpha(true);
            colorPickerView.showHex(true);
            colorPickerView.showPreview(true);

            DialogUtils.inputDialog(this,
                    getString(R.string.widgetConfigurator_date_patternBackgroundColor),
                    null,
                    () -> {
                        widget.patternBackgroundColor = ColorUtils.colorToHex(colorPickerView.getColor());
                        WidgetsData.save();
                    },
                    new ColorPickerView[]{colorPickerView});
        });

        pattern_padding_button.setOnClickListener(v -> DialogUtils.inputDialog(this,
                getString(R.string.widgetConfigurator_date_patternPadding),
                null,
                String.valueOf(widget.patternPadding),
                null,
                InputType.TYPE_CLASS_NUMBER,
                responseText -> {
                    widget.patternPadding = Integer.parseInt(responseText);
                    WidgetsData.save();
                }));

        background_color_button.setOnClickListener(v -> {
            final ColorPickerView colorPickerView = new ColorPickerView(this);
            colorPickerView.setColor(ColorUtils.parseColor(widget.backgroundColor, "#ffffffff"));
            colorPickerView.showAlpha(true);
            colorPickerView.showHex(true);
            colorPickerView.showPreview(true);

            DialogUtils.inputDialog(this,
                    getString(R.string.widgetConfigurator_date_backgroundColor),
                    null,
                    () -> {
                        widget.backgroundColor = ColorUtils.colorToHex(colorPickerView.getColor());
                        WidgetsData.save();
                    },
                    new ColorPickerView[]{colorPickerView});
        });

        background_gravity_button.setOnClickListener(v -> {
            CharSequence[] itemsNames = {getText(R.string.gravity_center), getText(R.string.gravity_center_horizontal), getText(R.string.gravity_center_vertical), getText(R.string.gravity_bottom), getText(R.string.gravity_left), getText(R.string.gravity_right), getText(R.string.gravity_top)};
            int[] itemsValues = {Gravity.CENTER, Gravity.CENTER_HORIZONTAL, Gravity.CENTER_VERTICAL, Gravity.BOTTOM, Gravity.START, Gravity.END, Gravity.TOP};
            List<CheckBox> checkBoxes = new ArrayList<>();

            for (CharSequence gravityName : itemsNames) {
                CheckBox checkBox = new CheckBox(this);
                checkBox.setText(gravityName); // "("+widget.backgroundGravity+" & "+itemsValues[ii]+")=" + (widget.backgroundGravity & itemsValues[ii])+" "+name
                checkBoxes.add(checkBox);
            }

            DialogUtils.inputDialog(this,
                    getString(R.string.widgetConfigurator_date_backgroundGravity),
                    null,
                    () -> {
                        int finalGravity = Gravity.NO_GRAVITY;
                        int i = 0;
                        for (CheckBox checkBox : checkBoxes) {
                            if (checkBox.isChecked()) {
                                int gravity = itemsValues[i];
                                finalGravity = finalGravity | gravity;
                            }
                            i++;
                        }

                        widget.backgroundGravity = finalGravity;
                        WidgetsData.save();
                    },
                    checkBoxes.toArray(new View[0]));
        });

        background_padding_button.setOnClickListener(v -> DialogUtils.inputDialog(this,
                getString(R.string.widgetConfigurator_date_backgroundPadding),
                null,
                String.valueOf(widget.backgroundPadding),
                null,
                InputType.TYPE_CLASS_NUMBER,
                responseText -> {
                    widget.backgroundPadding = Integer.parseInt(responseText);
                    WidgetsData.save();
                }));
    }

    private void loadDynamic() {
        Context finalContext = this;
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    layout_widgetPreview.removeAllViews();
                    RemoteViews view = widget.updateWidget(finalContext);
                    view.setTextViewTextSize(R.id.widget_date_pattern, 2, widget.patternSize-6);
                    layout_widgetPreview.addView(view.apply(getApplicationContext(), layout_widgetPreview));

                    if (!isFinishing()) {
                        handler.postDelayed(this, SettingsData.getSettingsData().getWidgetsUpdateDelayMillis());
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();

                    RemoteViews view = new RemoteViews(getApplicationContext().getPackageName(), R.layout.widget_date);
                    view.setTextViewText(R.id.widget_date_pattern, "Error: " + exception.toString());
                    layout_widgetPreview.addView(view.apply(getApplicationContext(), layout_widgetPreview));
                }
            }
        };
        handler.post(runnable);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Loading
        widgetId = getIntent().getIntExtra("widgetId", -1);
        widget = WidgetsData.getWidgetsData().getWidgetById(widgetId);
        if (widget == null) {
            Utils.showToast(this, "Error. widget not found.");
            finish();
            return;
        }
        // -------

        setTitle(R.string.activityTitle_dateWidgetConfigurator);
        setContentView(R.layout.activity_date_widget_configurator);

        loadVariables();
        loadLogic();
        loadDynamic();

        isRunning = true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (SettingsActivity.restartRequired) {
            finish();
            return;
        }

        if (isRunning) {
            widget = WidgetsData.getWidgetsData().getWidgetById(widgetId);
            if (widget == null) {
                Utils.showToast(this, getString(R.string.widgetConfigurator_widgetDeleted));
                finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_date_widget_configurator, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.dateWidgetConfigurator_menu_restoreToDefault):
                DialogUtils.warningDialog(this,
                        getString(R.string.widgetConfigurator_date_menu_restoreToDefault_warning_title),
                        getString(R.string.widgetConfigurator_date_menu_restoreToDefault_warning_message),
                        0,
                        () -> {
                            widget.restoreToDefaults();
                            startActivity(new Intent(this, DateWidgetConfiguratorActivity.class).putExtra("widgetId", widget.getWidgetId()));
                            finish();
                        });
                break;

            case (R.id.dateWidgetConfigurator_menu_loadFromAnotherWidget):
                DialogUtils.selectDateWidgetDialog(this,
                        getString(R.string.widgetConfigurator_date_menu_loadFromAnotherWidget_title),
                        getString(R.string.widgetConfigurator_date_menu_loadFromAnotherWidget_message),
                        responseWidget -> {
                            widget.loadFromAnotherWidget(responseWidget);
                            startActivity(new Intent(this, DateWidgetConfiguratorActivity.class).putExtra("widgetId", widget.getWidgetId()));
                            finish();
                        });
                break;

            case (R.id.dateWidgetConfigurator_menu_delete):
                DialogUtils.warningDialog(this,
                        getString(R.string.widgetConfigurator_date_menu_delete_warning_title),
                        getString(R.string.widgetConfigurator_date_menu_delete_warning_message),
                        0,
                        () -> {
                            widget.delete();
                            finish();
                        });
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
