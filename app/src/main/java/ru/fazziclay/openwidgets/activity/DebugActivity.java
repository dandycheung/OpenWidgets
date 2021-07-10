package ru.fazziclay.openwidgets.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import ru.fazziclay.openwidgets.R;
import ru.fazziclay.openwidgets.deprecated.cogs.DeprecatedUtils;
import ru.fazziclay.openwidgets.updateChecker.UpdateChecker;
import ru.fazziclay.openwidgets.utils.DialogUtils;
import ru.fazziclay.openwidgets.utils.ErrorDetectorWrapperInterface;

public class DebugActivity extends AppCompatActivity {
    // Variables
    // DialogUtils
    Button debug_button_dialogUtils_test1;
    Button debug_button_dialogUtils_test2;
    Button debug_button_dialogUtils_test3;
    Button debug_button_dialogUtils_test4;

    // UpdateChecker
    Button debug_button_updateChecker_thisVersion;
    Button debug_button_updateChecker_getVersion;
    Button debug_button_updateChecker_changeAppBuild;
    Button debug_button_updateChecker_changeVersionsFormat;

    // Services
    Button debug_button_services_startWidgetsUpdater;
    Button debug_button_services_stopWidgetsUpdater;
    Button debug_button_services_startedList;
    TextView debug_text_services_updateCheckerCounter;

    // WidgetsData
    Button debug_button_widgetsData_file;
    Button debug_button_widgetsData_save;
    Button debug_button_widgetsData_load;
    Button debug_button_widgetsData_variables;
    TextView debug_text_widgetsData_info;

    // WidgetsManager
    Button debug_button_widgetsManager_add;
    Button debug_button_widgetsManager_remove;
    Button debug_button_widgetsManager_isWidgetExist;
    Button debug_button_widgetsManager_getWidgetById;

    // Unknown
    Button debug_button_unknown_openActivity;
    Button debug_button_unknown_testJavaError;

    private void errorDetectorWrapper(ErrorDetectorWrapperInterface errorDetectorWrapperInterface) {
        try {
            errorDetectorWrapperInterface.run();
        } catch (Exception e) {
            DeprecatedUtils.showMessage(this, e.toString());
        }
    }

    private void loadVariables() {
        // DialogUtils
        debug_button_dialogUtils_test1 = findViewById(R.id.debug_button_dialogUtils_test1);
        debug_button_dialogUtils_test2 = findViewById(R.id.debug_button_dialogUtils_test2);
        debug_button_dialogUtils_test3 = findViewById(R.id.debug_button_dialogUtils_test3);
        debug_button_dialogUtils_test4 = findViewById(R.id.debug_button_dialogUtils_test4);

        // UpdateChecker
        debug_button_updateChecker_thisVersion = findViewById(R.id.debug_button_updateChecker_thisVersion);
        debug_button_updateChecker_getVersion = findViewById(R.id.debug_button_updateChecker_getVersion);
        debug_button_updateChecker_changeAppBuild = findViewById(R.id.debug_button_updateChecker_changeAppBuild);
        debug_button_updateChecker_changeVersionsFormat = findViewById(R.id.debug_button_updateChecker_changeVersionsFormat);

        // Services
        debug_button_services_startWidgetsUpdater = findViewById(R.id.debug_button_services_startWidgetsUpdater);
        debug_button_services_stopWidgetsUpdater = findViewById(R.id.debug_button_services_stopWidgetsUpdater);
        debug_button_services_startedList = findViewById(R.id.debug_button_services_startedList);
        debug_text_services_updateCheckerCounter = findViewById(R.id.debug_text_services_updateCheckerCounter);

        // WidgetsData
        debug_button_widgetsData_file = findViewById(R.id.debug_button_widgetsData_file);
        debug_button_widgetsData_save = findViewById(R.id.debug_button_widgetsData_save);
        debug_button_widgetsData_load = findViewById(R.id.debug_button_widgetsData_load);
        debug_button_widgetsData_variables = findViewById(R.id.debug_button_widgetsData_variables);
        debug_text_widgetsData_info = findViewById(R.id.debug_text_widgetsData_info);

        // WidgetsManager
        debug_button_widgetsManager_add = findViewById(R.id.debug_button_widgetsManager_add);
        debug_button_widgetsManager_remove = findViewById(R.id.debug_button_widgetsManager_remove);
        debug_button_widgetsManager_isWidgetExist = findViewById(R.id.debug_button_widgetsManager_isWidgetExist);
        debug_button_widgetsManager_getWidgetById = findViewById(R.id.debug_button_widgetsManager_getWidgetById);

        // Unknown
        debug_button_unknown_openActivity = findViewById(R.id.debug_button_unknown_openActivity);
        debug_button_unknown_testJavaError = findViewById(R.id.debug_button_unknown_testJavaError);
    }

    private void loadLogic() {
        Context finalContext = this;

        // DialogUtils
        debug_button_dialogUtils_test1.setOnClickListener(v -> errorDetectorWrapper(() -> DialogUtils.inputDialog(this,
                "TITLE",
                "MESSAGE",
                0,
                true,
                true,
                dialog -> DeprecatedUtils.showMessage(finalContext, "canceled!"),
                "NEUTRAL",
                () -> DeprecatedUtils.showMessage(finalContext, "NEUTRAL"),
                "NEGATIVE",
                () -> DeprecatedUtils.showMessage(finalContext, "NEGATIVE"),
                "POSITIVE",
                () -> DeprecatedUtils.showMessage(finalContext, "POSITIVE"),
                Gravity.CENTER,
                new CheckBox(this)
        )));


        debug_button_dialogUtils_test2.setOnClickListener(v -> errorDetectorWrapper(() -> DialogUtils.inputDialog(this,
                "TITLE",
                "MESSAGE",
                0,
                true,
                true,
                null,
                "NEUTRAL",
                null,
                null,
                null,
                "POSITIVE",
                () -> DeprecatedUtils.showMessage(finalContext, "POSITIVE"),
                0
        )));


        debug_button_dialogUtils_test3.setOnClickListener(v -> errorDetectorWrapper(() -> DialogUtils.notifyDialog(this,
                "TITLE",
                "MESSAGE",
                R.drawable.example_appwidget_preview)));


        debug_button_dialogUtils_test4.setOnClickListener(v -> errorDetectorWrapper(() -> DialogUtils.inputDialog(this,
                "TITLE",
                "MESSAGE",
                "editTextStart",
                "editTextHint",
                InputType.TYPE_CLASS_TEXT,
                responseText -> DeprecatedUtils.showMessage(this, "responseText="+responseText))));


        // UpdateChecker
        debug_button_updateChecker_thisVersion.setOnClickListener(v -> errorDetectorWrapper(() -> DialogUtils.notifyDialog(this, getString(R.string.debug_button_updateChecker_thisVersion),
                "appUpdateCheckerFormatVersion=" + UpdateChecker.appUpdateCheckerFormatVersion + "\n" +
                        "appBuild=" + UpdateChecker.appBuild + "\n" +
                        "appVersionsUrl=" + UpdateChecker.appVersionsUrl)));




        debug_button_updateChecker_getVersion.setOnClickListener(v -> errorDetectorWrapper(() -> {
            long startTime = System.currentTimeMillis();
            UpdateChecker.getVersion((status, build, name, downloadUrl) -> runOnUiThread(() -> {
                long endTime = System.currentTimeMillis();
                DialogUtils.notifyDialog(finalContext, getString(R.string.debug_button_updateChecker_getVersion),
                        "delay=" + (endTime - startTime) + "ms" + "\n" +
                                "status=" + status + "\n" +
                                "build=" + build + "\n" +
                                "name=" + name + "\n" +
                                "downloadUrl="+downloadUrl
                );
            }));
        }));


        debug_button_updateChecker_changeAppBuild.setOnClickListener(v -> errorDetectorWrapper(() -> DialogUtils.inputDialog(this,
                getString(R.string.debug_button_updateChecker_changeAppBuild),
                null,
                String.valueOf(UpdateChecker.appBuild),
                null,
                InputType.TYPE_CLASS_NUMBER,
                responseText -> errorDetectorWrapper(() ->UpdateChecker.appBuild = Integer.parseInt(responseText))
        )));


        debug_button_updateChecker_changeVersionsFormat.setOnClickListener(v -> errorDetectorWrapper(() -> DialogUtils.inputDialog(this,
                getString(R.string.debug_button_updateChecker_changeAppBuild),
                null,
                String.valueOf(UpdateChecker.appBuild),
                null,
                InputType.TYPE_CLASS_NUMBER,
                responseText -> errorDetectorWrapper(() ->UpdateChecker.appUpdateCheckerFormatVersion = Integer.parseInt(responseText))
        )));



        // Unknown
        debug_button_unknown_testJavaError.setOnClickListener(v -> errorDetectorWrapper(() -> {
            throw new Exception("Test exception!!!");
        }));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);
        setTitle(R.string.activityTitle_debug);

        loadVariables();
        loadLogic();
    }
}