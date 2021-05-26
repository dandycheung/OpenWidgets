package ru.fazziclay.openwidgets.activity;

import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import ru.fazziclay.openwidgets.R;
import ru.fazziclay.openwidgets.WidgetsUpdaterService;
import ru.fazziclay.openwidgets.cogs.DialogUtils;
import ru.fazziclay.openwidgets.cogs.FileUtil;
import ru.fazziclay.openwidgets.cogs.Utils;
import ru.fazziclay.openwidgets.cogs.WidgetsManager;

import static ru.fazziclay.openwidgets.cogs.WidgetsManager.APP_PATH;

public class Debug2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug2);

        // TestButton
        Button testButton = findViewById(R.id.debug2_testButton);
        testButton.setOnClickListener(v -> Utils.showMessage(this, "Clicked!"));

        // widgets json
        Button widgetsJson = findViewById(R.id.debug2_widgetsJson);
        widgetsJson.setOnClickListener(v -> DialogUtils.inputDialog(this,
                "widgets.json",
                FileUtil.readFile(APP_PATH + "/widgets.json"),
                null,
                -1,
                "Save",
                (responseText) -> {
                    FileUtil.writeFile(APP_PATH + "/widgets.json", responseText);
                    WidgetsManager.syncVariable();
                }));
        
        // widgets isExist
        Button widgetsIsExist = findViewById(R.id.debug2_widgetsIsExist);
        widgetsIsExist.setOnClickListener(v -> DialogUtils.inputDialog(this,
                "Widgets isExist",
                null,
                "widget id",
                InputType.TYPE_CLASS_NUMBER,
                "Check",
                (responseText) -> DialogUtils.notifyDialog(this, "Response", String.valueOf(WidgetsManager.isWidgetExist(Integer.parseInt(responseText))))));

        // widgets add
        Button widgetsAdd = findViewById(R.id.debug2_widgetsAdd);
        widgetsAdd.setOnClickListener(v -> {
            // two edit texts
            EditText viewId = new EditText(this);
            viewId.setTextSize(18);
            viewId.setInputType(InputType.TYPE_CLASS_NUMBER);
            viewId.setHint("widget id");

            EditText viewType = new EditText(this);
            viewType.setTextSize(18);
            viewType.setInputType(InputType.TYPE_CLASS_NUMBER);
            viewType.setHint("widget type");

            DialogUtils.inputDialog(this,
                    "Widgets add",
                    "",
                    (c) -> {},
                    "Chancel",
                    (c) -> {},
                    "Add",
                    (c) -> WidgetsManager.addWidget(Integer.parseInt(viewId.getText().toString()), Integer.parseInt(viewType.getText().toString())),
                    viewId, viewType);
        });

        // widgets remove
        Button widgetsRemove = findViewById(R.id.debug2_widgetsRemove);
        widgetsRemove.setOnClickListener(v -> DialogUtils.inputDialog(this,
                "Widgets remove",
                "",
                "widget Id",
                InputType.TYPE_CLASS_NUMBER,
                "Remove",
                responseText -> WidgetsManager.removeWidget(Integer.parseInt(responseText))));

        //services start WidgetsUpdaterService
        Button servicesStartWidgetsUpdater = findViewById(R.id.debug2_servicesStartWidgetUpdater);
        servicesStartWidgetsUpdater.setOnClickListener(v -> startService(new Intent(getApplicationContext(), WidgetsUpdaterService.class)));

        //services stop WidgetsUpdaterService
        Button servicesStopWidgetsUpdater = findViewById(R.id.debug2_servicesStopWidgetUpdater);
        servicesStopWidgetsUpdater.setOnClickListener(v -> DialogUtils.notifyDialog(this, "Done", String.valueOf(stopService(new Intent(getApplicationContext(), WidgetsUpdaterService.class)))));

        //services started list
        Button servicesStartedList = findViewById(R.id.debug2_servicesStartedList);
        servicesStartedList.setOnClickListener(v -> {
            try {
                StringBuilder response = new StringBuilder();

                ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                    response.append("\n").append(service.service.getClassName());
                }

                DialogUtils.notifyDialog(this, "Response", response.toString().replace("ru.fazziclay.openwidgets.", ""));

            } catch (Exception e) {
                Utils.showMessage(this, "Run error: "+e.toString());
            }
        });

        // utils ConvertToMegaHourFormat
        Button utilsConvertToMegaHourFormat = findViewById(R.id.debug2_utilsConvertToMegaHourFormat);
        utilsConvertToMegaHourFormat.setOnClickListener(v -> {
            // two edit texts
            EditText viewA = new EditText(this);
            viewA.setTextSize(18);
            viewA.setInputType(InputType.TYPE_CLASS_NUMBER);
            viewA.setHint("a (source)");

            EditText viewB = new EditText(this);
            viewB.setTextSize(18);
            viewB.setInputType(InputType.TYPE_CLASS_NUMBER);
            viewB.setHint("b (max)");

            DialogUtils.inputDialog(this,
                    "Utils ConvertToMegaHourFormat",
                    "",
                    (c) -> {},
                    "Chancel",
                    (c) -> {},
                    "Calculate",
                    (c) -> DialogUtils.notifyDialog(this, "Calculated", String.valueOf(Utils.conventToMegaHourFormat(Integer.parseInt(viewA.getText().toString()), Integer.parseInt(viewB.getText().toString())))),
                    viewA, viewB);
        });

        // utils makeAccurate
        Button utilsMakeAccurate = findViewById(R.id.debug2_utilsMakeAccurate);
        utilsMakeAccurate.setOnClickListener(v -> {
            // two edit texts
            EditText viewA = new EditText(this);
            viewA.setTextSize(18);
            viewA.setInputType(InputType.TYPE_CLASS_NUMBER);
            viewA.setHint("a (source)");

            EditText viewB = new EditText(this);
            viewB.setTextSize(18);
            viewB.setInputType(InputType.TYPE_CLASS_NUMBER);
            viewB.setHint("b (coefficient)");

            DialogUtils.inputDialog(this,
                    "Utils ConvertToMegaHourFormat",
                    "",
                    (c) -> {},
                    "Chancel",
                    (c) -> {},
                    "Calculate",
                    (c) -> DialogUtils.notifyDialog(this, "Calculated", String.valueOf(Utils.makeAccurate(Integer.parseInt(viewA.getText().toString()), Integer.parseInt(viewB.getText().toString())))),
                    viewA, viewB);
        });

    }
}