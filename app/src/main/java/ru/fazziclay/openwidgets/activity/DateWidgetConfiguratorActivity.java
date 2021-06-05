package ru.fazziclay.openwidgets.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
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
    public int widgetId;
    public DateWidget widget;

    Button patternContentButton;
    Button patternSizeButton;
    Button patternColorButton;
    Button patternBackgroundColorButton;
    Button backgroundColorButton;
    Button backgroundGravityButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        widgetId = getIntent().getIntExtra("widget_id", -1001);
        if (widgetId < -1000) {
            Utils.showMessage(this, "Error to create DateWidgetConfigurator. reason=(widgetId < -1000)");
            finishActivity(widgetId);
            return;
        }
        widget = (DateWidget) WidgetsManager.getWidgetById(widgetId);
        if (widget == null) {
            Utils.showMessage(this, "Error to getWidgetById: null. wId="+widgetId);
            finishActivity(widgetId);
            return;
        }

        setContentView(R.layout.activity_date_widget_configurator);
        setTitle("OpenWidgets - Date Widget Configurator");
        TextView idView = findViewById(R.id.date_widget_configurator_id);
        idView.setText(("Widget ID = " + widgetId));
        patternContentButton = findViewById(R.id.pattern_content_button);
        patternSizeButton = findViewById(R.id.pattern_size_button);
        patternColorButton = findViewById(R.id.pattern_color_button);
        patternBackgroundColorButton = findViewById(R.id.pattern_backgroundColor_button);
        backgroundColorButton = findViewById(R.id.background_color_button);
        backgroundGravityButton = findViewById(R.id.background_gravity_button);


        patternContentButton.setOnClickListener(v -> DialogUtils.inputDialog(this, "pattern content", widget.pattern, "", -1, "Apply", responseText -> {
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

            DialogUtils.inputDialog(this, "pattern size", "Apply", a -> {
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

            DialogUtils.inputDialog(this, "pattern size", "Apply", a -> {
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

            DialogUtils.inputDialog(this, "pattern color", "Apply", a -> {
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

            DialogUtils.inputDialog(this, "pattern background color", "Apply", a -> {
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

            DialogUtils.inputDialog(this, "background color", "Apply", a -> {
                widget.backgroundColor = Utils.ColorToHex(colorPickerView.getColor());
                WidgetsData.save();
                Utils.showMessage(this, "int="+colorPickerView.getColor()+ "; hex="+widget.backgroundColor);
            }, colorPickerView);
        });

        backgroundGravityButton.setOnClickListener(v -> {
            String[] itemsNames = {"CENTER", "CENTER_HORIZONTAL", "CENTER_VERTICAL", "BOTTOM", "LEFT", "RIGHT", "TOP"};
            int[] itemsValues = {Gravity.CENTER, Gravity.CENTER_HORIZONTAL, Gravity.CENTER_VERTICAL, Gravity.BOTTOM, Gravity.LEFT, Gravity.RIGHT, Gravity.TOP};
            int current = -1;
            for (int s : itemsValues) {
                current++;
                if (s == widget.backgroundGravity) {
                    break;
                }
            }


            new AlertDialog.Builder(this)
                    .setSingleChoiceItems(itemsNames, current, null)
                    .setPositiveButton("Apply", (dialog, whichButton) -> {
                        dialog.dismiss();
                        int selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
                        widget.backgroundGravity = itemsValues[selectedPosition];
                    })
                    .show();
        });
    }
}