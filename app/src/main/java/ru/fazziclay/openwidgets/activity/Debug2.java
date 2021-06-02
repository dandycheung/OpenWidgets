package ru.fazziclay.openwidgets.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;

import ru.fazziclay.fazziclaylibs.FileUtils;
import ru.fazziclay.openwidgets.R;
import ru.fazziclay.openwidgets.service.WidgetsUpdaterService;
import ru.fazziclay.openwidgets.cogs.DialogUtils;
import ru.fazziclay.openwidgets.cogs.Utils;
import ru.fazziclay.openwidgets.widgets.WidgetsManager;
import ru.fazziclay.openwidgets.widgets.data.BaseWidget;
import ru.fazziclay.openwidgets.widgets.data.DateWidget;
import ru.fazziclay.openwidgets.widgets.data.WidgetsData;

public class Debug2 extends AppCompatActivity {
    // Buttons
    Button testButton;

    // Services
    Button servicesStartWidgetUpdater;

    // Widgets.data
    Button widgetsDataJSONFILE;
    Button widgetsDataSave;
    Button widgetsDataLoad;
    Button widgetsDataViewJavaVars;
    TextView widgetsDataVariablesText;

    // Widgets.Manager
    Button widgetsManagerAdd;
    Button widgetsManagerRemove;
    Button widgetsManagerGetById;
    Button widgetsManagerIsExist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug2);

        loadViewsVariables();
        loadViews();
    }

    private void loadViewsVariables() {
        testButton = findViewById(R.id.debug2_testButton);
        servicesStartWidgetUpdater = findViewById(R.id.debug2_servicesStartWidgetUpdater);
        widgetsDataViewJavaVars = findViewById(R.id.debug2_widgetsDataViewJavaVars);
        widgetsDataSave = findViewById(R.id.debug2_widgetsDataSave);
        widgetsDataLoad = findViewById(R.id.debug2_widgetsDataLoad);
        widgetsDataJSONFILE = findViewById(R.id.debug2_widgetsDataJSONFILE);
        widgetsDataVariablesText = findViewById(R.id.debug2_widgetsDataVariablesText);
        widgetsManagerAdd = findViewById(R.id.debug2_widgetsManagerAdd);
        widgetsManagerRemove = findViewById(R.id.debug2_widgetsManagerRemove);
        widgetsManagerIsExist = findViewById(R.id.debug2_widgetsManagerIsExist);
        widgetsManagerGetById = findViewById(R.id.debug2_widgetsManagerGetById);
    }

    private void loadViews() {
        testButton.setOnClickListener(v -> {
            Utils.showMessage(this, "Clicked!");


            DialogUtils.notifyDialog(this, "---", "activity.extra="+getIntent().getExtras().toString()+"");
        });

        servicesStartWidgetUpdater.setOnClickListener(v -> {
            try {
                startService(new Intent(getApplicationContext(), WidgetsUpdaterService.class));
            } catch (Exception e) {
                Utils.showMessage(this, "activity.Debug2: button=servicesStartWidgetUpdater: error= "+e);
            }
        });

        widgetsDataJSONFILE.setOnClickListener(v -> DialogUtils.inputDialog(this, "widgets.json", FileUtils.read(WidgetsData.filePath), "._.", -1, "Save", responseText -> FileUtils.write(WidgetsData.filePath, responseText)));
        widgetsDataLoad.setOnClickListener(v -> WidgetsData.load());
        widgetsDataSave.setOnClickListener(v -> WidgetsData.save());
        widgetsDataViewJavaVars.setOnClickListener(v -> DialogUtils.notifyDialog(this, "Java data", "index="+WidgetsData.index+"\n\nwidgets="+WidgetsData.widgets+"\n\nwidgetsDataFile="+WidgetsData.widgetsDataFile));
        widgetsDataVariablesText.setText(("version: "+WidgetsData.version+"\nfilePath: "+WidgetsData.filePath));

        widgetsManagerAdd.setOnClickListener(v -> DialogUtils.inputDialog(this, "add widget", "", "widget id", InputType.TYPE_CLASS_NUMBER, "Add", responseText -> WidgetsManager.addWidget(Integer.parseInt(responseText), new DateWidget("Hello World"))));
        widgetsManagerRemove.setOnClickListener(v -> DialogUtils.inputDialog(this, "remove widget", "", "widget id", InputType.TYPE_CLASS_NUMBER, "Remove", responseText -> WidgetsManager.removeWidget(Integer.parseInt(responseText))));
        widgetsManagerIsExist.setOnClickListener(v -> DialogUtils.inputDialog(this, "is widget exist", "", "widget id", InputType.TYPE_CLASS_NUMBER, "Check", responseText -> DialogUtils.notifyDialog(this, "response", String.valueOf(WidgetsManager.isWidgetExist(Integer.parseInt(responseText))))));
        widgetsManagerGetById.setOnClickListener(v -> DialogUtils.inputDialog(this, "get widget by id", "", "widget id", InputType.TYPE_CLASS_NUMBER, "Get", responseText -> {
            BaseWidget widget = WidgetsManager.getWidgetById(Integer.parseInt(responseText));
            String json = null;
            if (widget != null) {
                try {
                    json = widget.toJSON().toString(4);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            DialogUtils.notifyDialog(this, "response", "String = "+widget+"\n\nJSON = " + json);
        }));
    }
}