package ru.fazziclay.openwidgets.activity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.Gravity;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TooManyListenersException;

import ru.fazziclay.fazziclaylibs.FileUtils;
import ru.fazziclay.fazziclaylibs.NumberUtils;
import ru.fazziclay.openwidgets.R;
import ru.fazziclay.openwidgets.deprecated.cogs.DeprecatedUtils;
import ru.fazziclay.openwidgets.service.WidgetsUpdaterService;
import ru.fazziclay.openwidgets.updateChecker.UpdateChecker;
import ru.fazziclay.openwidgets.utils.DialogUtils;
import ru.fazziclay.openwidgets.utils.ErrorDetectorWrapper;
import ru.fazziclay.openwidgets.utils.ErrorDetectorWrapperInterface;
import ru.fazziclay.openwidgets.widgets.WidgetsManager;
import ru.fazziclay.openwidgets.widgets.data.BaseWidget;
import ru.fazziclay.openwidgets.widgets.data.WidgetsData;

import static ru.fazziclay.openwidgets.utils.ErrorDetectorWrapper.errorDetectorWrapper;

public class DebugActivity extends AppCompatActivity {
    // Variables
    public static String onlyDebugFlagPath;

    // OnlyDebugMode
    Button debug_button_onlyDebugMode_enable;
    Button debug_button_onlyDebugMode_disable;

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
    Button debug_button_updateChecker_changeVersionsUrl;

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

    // WidgetsManager
    Button debug_button_widgetsManager_add;
    Button debug_button_widgetsManager_remove;
    Button debug_button_widgetsManager_isWidgetExist;
    Button debug_button_widgetsManager_getWidgetById;
    Button debug_button_widgetsManager_getIterator;

    // Unknown
    Button debug_button_unknown_openActivity;
    Button debug_button_unknown_testJavaError;

    public static void setOnlyDebugFlagPath(Context context) {
        onlyDebugFlagPath = context.getFilesDir().getPath() + "/debug/onlyDebug.flag";
    }

    private void loadVariables() {
        // OnlyDebugMode
        debug_button_onlyDebugMode_enable = findViewById(R.id.debug_button_onlyDebugMode_enable);
        debug_button_onlyDebugMode_disable = findViewById(R.id.debug_button_onlyDebugMode_disable);

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
        debug_button_updateChecker_changeVersionsUrl = findViewById(R.id.debug_button_updateChecker_changeVersionsUrl);

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

        // WidgetsManager
        debug_button_widgetsManager_add = findViewById(R.id.debug_button_widgetsManager_add);
        debug_button_widgetsManager_remove = findViewById(R.id.debug_button_widgetsManager_remove);
        debug_button_widgetsManager_isWidgetExist = findViewById(R.id.debug_button_widgetsManager_isWidgetExist);
        debug_button_widgetsManager_getWidgetById = findViewById(R.id.debug_button_widgetsManager_getWidgetById);
        debug_button_widgetsManager_getIterator = findViewById(R.id.debug_button_widgetsManager_getIterator);

        // Unknown
        debug_button_unknown_openActivity = findViewById(R.id.debug_button_unknown_openActivity);
        debug_button_unknown_testJavaError = findViewById(R.id.debug_button_unknown_testJavaError);
    }

    private void loadLogic() {
        Context finalContext = this;

        // OnlyDebugMode
        debug_button_onlyDebugMode_enable.setOnClickListener(v -> errorDetectorWrapper(() -> FileUtils.write(onlyDebugFlagPath, "1")));
        debug_button_onlyDebugMode_disable.setOnClickListener(v -> errorDetectorWrapper(() -> FileUtils.write(onlyDebugFlagPath, "0")));

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
                new CheckBox[]{new CheckBox(this)}
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
                0,
                new Button[]{}
        )));


        debug_button_dialogUtils_test3.setOnClickListener(v -> errorDetectorWrapper(() -> DialogUtils.notifyDialog(this,
                "TITLE",
                "MESSAGE",
                R.drawable.date_widget_preview)));


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
                responseText -> errorDetectorWrapper(() -> UpdateChecker.appBuild = Integer.parseInt(responseText))
        )));


        debug_button_updateChecker_changeVersionsFormat.setOnClickListener(v -> errorDetectorWrapper(() -> DialogUtils.inputDialog(this,
                getString(R.string.debug_button_updateChecker_changeVersionsFormat),
                null,
                String.valueOf(UpdateChecker.appUpdateCheckerFormatVersion),
                null,
                InputType.TYPE_CLASS_NUMBER,
                responseText -> errorDetectorWrapper(() -> UpdateChecker.appUpdateCheckerFormatVersion = Integer.parseInt(responseText))
        )));

        debug_button_updateChecker_changeVersionsUrl.setOnClickListener(v -> errorDetectorWrapper(() -> DialogUtils.inputDialog(this,
                getString(R.string.debug_button_updateChecker_changeVersionsUrl),
                null,
                UpdateChecker.appVersionsUrl,
                null,
                InputType.TYPE_TEXT_VARIATION_LONG_MESSAGE,
                responseText -> errorDetectorWrapper(() -> UpdateChecker.appVersionsUrl = responseText)
        )));


        // Services
        debug_button_services_startWidgetsUpdater.setOnClickListener(v -> errorDetectorWrapper(() -> startService(new Intent(getApplicationContext(), WidgetsUpdaterService.class))));
        debug_button_services_stopWidgetsUpdater.setOnClickListener(v -> errorDetectorWrapper(() -> stopService(new Intent(getApplicationContext(), WidgetsUpdaterService.class))));
        debug_button_services_startedList.setOnClickListener(v -> errorDetectorWrapper(() -> {
            StringBuilder a = new StringBuilder();
            ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                a.append(service.service.getClassName().replace("ru.fazziclay.openwidgets", "<>")).append("\n");
            }

            DialogUtils.notifyDialog(this, getString(R.string.debug_button_services_startedList), a.toString());
        }));


        // WidgetsData
        debug_button_widgetsData_file.setOnClickListener(v -> errorDetectorWrapper(() -> DialogUtils.inputDialog(this,
                getString(R.string.debug_button_widgetsData_file),
                getString(R.string.debug_dialog_widgetsData_file_description),
                FileUtils.read(WidgetsData.filePath),
                null,
                InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE,
                responseText -> errorDetectorWrapper(() -> FileUtils.write(WidgetsData.filePath, responseText))
        )));

        debug_button_widgetsData_save.setOnClickListener(v -> errorDetectorWrapper(WidgetsData::save));
        debug_button_widgetsData_load.setOnClickListener(v -> errorDetectorWrapper(WidgetsData::load));

        debug_button_widgetsData_variables.setOnClickListener(v -> errorDetectorWrapper(() -> DialogUtils.notifyDialog(this, getString(R.string.debug_button_widgetsData_variables),
                "index=" + WidgetsData.index + "\n\n" +
                        "widgets=" + WidgetsData.widgets + "\n\n" +
                        "filePath=" + WidgetsData.filePath + "\n\n" +
                        "version=" + WidgetsData.version + "\n\n" +
                        "fileVersion=" + WidgetsData.fileVersion + "\n\n" +
                        "widgetsDataFile=" + WidgetsData.widgetsDataFile, R.mipmap.ic_launcher)));

        // WidgetManager
        debug_button_widgetsManager_add.setOnClickListener(v -> errorDetectorWrapper(() -> DialogUtils.inputDialog(this,
                getString(R.string.debug_button_widgetsManager_add),
                "Add widget by type=999",
                null,
                "Widget ID",
                InputType.TYPE_CLASS_NUMBER,
                responseText -> errorDetectorWrapper(() -> WidgetsManager.addWidget(Integer.parseInt(responseText), new BaseWidget(-999)))
        )));

        debug_button_widgetsManager_remove.setOnClickListener(v -> errorDetectorWrapper(() -> DialogUtils.inputDialog(this,
                getString(R.string.debug_button_widgetsManager_remove),
                "Remove widget",
                null,
                "Widget ID",
                InputType.TYPE_CLASS_NUMBER,
                responseText -> errorDetectorWrapper(() -> WidgetsManager.removeWidget(Integer.parseInt(responseText)))
        )));

        debug_button_widgetsManager_isWidgetExist.setOnClickListener(v -> errorDetectorWrapper(() -> DialogUtils.inputDialog(this,
                getString(R.string.debug_button_widgetsManager_isWidgetExist),
                null,
                null,
                "Widget ID",
                InputType.TYPE_CLASS_NUMBER,
                responseText -> errorDetectorWrapper(() -> DialogUtils.notifyDialog(this, getString(R.string.debug_button_widgetsManager_isWidgetExist) + " - Response", String.valueOf(WidgetsManager.isWidgetExist(Integer.parseInt(responseText))), R.mipmap.ic_launcher_round))
        )));

        debug_button_widgetsManager_getWidgetById.setOnClickListener(v -> errorDetectorWrapper(() -> DialogUtils.inputDialog(this,
                getString(R.string.debug_button_widgetsManager_getWidgetById),
                null,
                null,
                "Widget ID",
                InputType.TYPE_CLASS_NUMBER,
                responseText -> errorDetectorWrapper(() -> {
                    BaseWidget widget = WidgetsManager.getWidgetById(Integer.parseInt(responseText));
                    String response = "null";
                    if (widget != null) {
                        response =
                                "widgetType="+widget.widgetType+"\n\n"+
                                "Object String="+widget.toString()+"\n\n"+
                                "Json="+widget.toJSON().toString(2);
                    }
                    DialogUtils.notifyDialog(this, getString(R.string.debug_button_widgetsManager_getWidgetById) + " - Response", response, R.mipmap.ic_launcher_round);
                })
        )));

        debug_button_widgetsManager_getIterator.setOnClickListener(v -> errorDetectorWrapper(() -> {
            Iterator<Integer> iterator = WidgetsManager.getIterator();
            StringBuilder response = new StringBuilder();
            if (!iterator.hasNext()) response.append("none");
            while (iterator.hasNext()) {
                response.append(iterator.next());
                if (iterator.hasNext()) {
                    response.append("\n");
                }
            }
            DialogUtils.notifyDialog(this, getString(R.string.debug_button_widgetsManager_getIterator) + " - Response", response.toString(), R.mipmap.ic_launcher_round);
        }));

        // Unknown
        debug_button_unknown_openActivity.setOnClickListener(v -> errorDetectorWrapper(() -> {
            Class[] activities = {AboutActivity.class, DateWidgetConfiguratorActivity.class, DebugActivity.class, MainActivity.class, SettingsActivity.class};

            ArrayList<Button> buttons = new ArrayList<>();
            for (Class a : activities) {
                Button button = new Button(this);
                button.setAllCaps(false);
                button.setText(a.getName().replace("ru.fazziclay.openwidgets.activity.", ""));
                button.setOnClickListener(gg -> {
                    Intent intent = new Intent();
                    intent.setClass(this, a);
                    startActivity(intent);
                });
                buttons.add(button);
            }

            DialogUtils.inputDialog(this,
                    getString(R.string.debug_button_unknown_openActivity),
                    null,
                    0,
                    true,
                    true,
                    null,
                    null,
                    null,
                    "Chancel",
                    null,
                    null,
                    null,
                    Gravity.CENTER,
                    buttons.toArray(new Button[0]));
        }));

        debug_button_unknown_testJavaError.setOnClickListener(v -> errorDetectorWrapper(() -> {
            Exception[] exceptions = new Exception[]{
                    new Exception("Test exceptions!!!"),
                    new RuntimeException("Hello World"),
                    new ClassNotFoundException("Random errors?"),
                    new ArrayIndexOutOfBoundsException("by fazziclay"),
                    new IOException("100th hour format idea by _Dane4ka_"),
                    new ArithmeticException("Spection - Feel T..."),
                    new IllegalArgumentException("random! = "+ "error"),
                    new NoSuchFieldException("IDE WARNINGS...."),
                    new TooManyListenersException("random! (bool) = "+ NumberUtils.getRandom(0, 1))
            };
            
            throw exceptions[NumberUtils.getRandom(0, exceptions.length)];
        }));
    }

    private void loadDynamicTexts() {
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                errorDetectorWrapper(() -> debug_text_services_updateCheckerCounter.setText(("updateCheckerCounter = " + WidgetsUpdaterService.updateCheckerCounter + "/4=" + WidgetsUpdaterService.updateCheckerCounter / 4)));

                if (!isFinishing()) {
                    handler.postDelayed(this, 250);
                }
            }
        };
        handler.post(runnable);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);
        setTitle(R.string.activityTitle_debug);

        loadVariables();
        loadLogic();
        loadDynamicTexts();
    }
}