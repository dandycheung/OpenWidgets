package ru.fazziclay.openwidgets.android.activity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TooManyListenersException;

import ru.fazziclay.fazziclaylibs.FileUtils;
import ru.fazziclay.fazziclaylibs.NumberUtils;
import ru.fazziclay.openwidgets.Logger;
import ru.fazziclay.openwidgets.R;
import ru.fazziclay.openwidgets.android.service.WidgetsUpdaterService;
import ru.fazziclay.openwidgets.data.Paths;
import ru.fazziclay.openwidgets.data.settings.SettingsData;
import ru.fazziclay.openwidgets.data.widgets.WidgetsData;
import ru.fazziclay.openwidgets.update.checker.UpdateChecker;
import ru.fazziclay.openwidgets.util.DialogUtils;
import ru.fazziclay.openwidgets.util.Utils;

import static ru.fazziclay.openwidgets.ErrorDetectorWrapper.errorDetectorWrapper;

public class DebugActivity extends AppCompatActivity {
    public static final String onlyDebugFlagFile = "debug/onlyDebug.flag";

    // Data
    Button debug_button_data_paths_update;
    Button debug_button_data_paths_variables;

    // Data Settings
    Button debug_button_data_settings_file;
    Button debug_button_data_settings_variables;
    Button debug_button_data_settings_save;
    Button debug_button_data_settings_load;

    // Data Widgets
    Button debug_button_data_widgets_file;
    Button debug_button_data_widgets_variables;
    Button debug_button_data_widgets_save;
    Button debug_button_data_widgets_load;

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

    // Unknown
    Button debug_button_unknown_log;
    Button debug_button_unknown_openActivity;
    Button debug_button_unknown_testJavaError;


    private void loadVariables() {
        // Data
        debug_button_data_paths_update = findViewById(R.id.debug_button_data_paths_update);
        debug_button_data_paths_variables = findViewById(R.id.debug_button_data_paths_variables);

        // Data Settings
        debug_button_data_settings_file = findViewById(R.id.debug_button_data_settings_file);
        debug_button_data_settings_variables = findViewById(R.id.debug_button_data_settings_variables);
        debug_button_data_settings_save = findViewById(R.id.debug_button_data_settings_save);
        debug_button_data_settings_load = findViewById(R.id.debug_button_data_settings_load);

        // Data Widgets
        debug_button_data_widgets_file = findViewById(R.id.debug_button_data_widgets_file);
        debug_button_data_widgets_variables = findViewById(R.id.debug_button_data_widgets_variables);
        debug_button_data_widgets_save = findViewById(R.id.debug_button_data_widgets_save);
        debug_button_data_widgets_load = findViewById(R.id.debug_button_data_widgets_load);

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

        // Unknown
        debug_button_unknown_log = findViewById(R.id.debug_button_unknown_log);
        debug_button_unknown_openActivity = findViewById(R.id.debug_button_unknown_openActivity);
        debug_button_unknown_testJavaError = findViewById(R.id.debug_button_unknown_testJavaError);
    }

    private void loadLogic() {
        Context finalContext = this;

        // Data
        debug_button_data_paths_update.setOnClickListener(v -> errorDetectorWrapper(() -> Paths.updatePaths(finalContext)));
        debug_button_data_paths_variables.setOnClickListener(v -> errorDetectorWrapper(() -> DialogUtils.notifyDialog(finalContext, getString(R.string.debug_button_data_paths_variables),
                "appFilePath=" + Paths.appFilePath + "\n\n"+
                        "appCachePath=" + Paths.appCachePath
        )));

        // Data Settings
        debug_button_data_settings_file.setOnClickListener(v -> errorDetectorWrapper(() -> DialogUtils.inputDialog(this,
                getString(R.string.debug_button_data_settings_file),
                getString(R.string.debug_dialog_data_settings_file_description),
                FileUtils.read(Paths.appFilePath + "/" + SettingsData.SETTINGS_FILE),
                null,
                InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE,
                responseText -> errorDetectorWrapper(() -> FileUtils.write(Paths.appFilePath + "/" + SettingsData.SETTINGS_FILE, responseText))
        )));

        debug_button_data_settings_variables.setOnClickListener(v -> errorDetectorWrapper(() -> DialogUtils.notifyDialog(this, getString(R.string.debug_button_data_settings_variables),
                        "SETTINGS_FILE=" + SettingsData.SETTINGS_FILE + "\n\n" +
                "SettingsData="+SettingsData.getSettingsData().toString(), R.mipmap.ic_launcher)));

        debug_button_data_settings_save.setOnClickListener(v -> errorDetectorWrapper(SettingsData::save));
        debug_button_data_settings_load.setOnClickListener(v -> errorDetectorWrapper(SettingsData::load));

        // Data Widgets
        debug_button_data_widgets_file.setOnClickListener(v -> errorDetectorWrapper(() -> DialogUtils.inputDialog(this,
                getString(R.string.debug_button_data_widgets_file),
                getString(R.string.debug_dialog_data_widgets_file_description),
                FileUtils.read(Paths.appFilePath + "/" + WidgetsData.WIDGETS_FILE),
                null,
                InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE,
                responseText -> errorDetectorWrapper(() -> FileUtils.write(Paths.appFilePath + "/" + WidgetsData.WIDGETS_FILE, responseText))
        )));

        debug_button_data_widgets_variables.setOnClickListener(v -> errorDetectorWrapper(() -> DialogUtils.notifyDialog(this, getString(R.string.debug_button_data_widgets_variables),
                "APP_FORMAT_VERSION=" + WidgetsData.WIDGETS_FORMAT_VERSION + "\n\n" +
                        "WIDGETS_FILE=" + WidgetsData.WIDGETS_FILE + "\n\n" +
                        "WidgetsData" + WidgetsData.getWidgetsData().toString(), R.mipmap.ic_launcher)));

        debug_button_data_widgets_save.setOnClickListener(v -> errorDetectorWrapper(WidgetsData::save));
        debug_button_data_widgets_load.setOnClickListener(v -> errorDetectorWrapper(WidgetsData::load));


        // OnlyDebugMode
        debug_button_onlyDebugMode_enable.setOnClickListener(v -> errorDetectorWrapper(() -> FileUtils.write(Paths.appFilePath + "/" + onlyDebugFlagFile, "1")));
        debug_button_onlyDebugMode_disable.setOnClickListener(v -> errorDetectorWrapper(() -> FileUtils.write(Paths.appFilePath + "/" + onlyDebugFlagFile, "0")));

        // DialogUtils
        debug_button_dialogUtils_test1.setOnClickListener(v -> errorDetectorWrapper(() -> DialogUtils.inputDialog(this,
                "TITLE",
                "MESSAGE",
                0,
                true,
                true,
                dialog -> Utils.showToast(finalContext, "canceled!"),
                "NEUTRAL",
                () -> Utils.showToast(finalContext, "NEUTRAL"),
                "NEGATIVE",
                () -> Utils.showToast(finalContext, "NEGATIVE"),
                "POSITIVE",
                () -> Utils.showToast(finalContext, "POSITIVE"),
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
                () -> Utils.showToast(finalContext, "POSITIVE"),
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
                responseText -> Utils.showToast(this, "responseText="+responseText))));


        // UpdateChecker
        debug_button_updateChecker_thisVersion.setOnClickListener(v -> errorDetectorWrapper(() -> DialogUtils.notifyDialog(this, getString(R.string.debug_button_updateChecker_thisVersion),
                "APP_UPDATE_CHECKER_FORMAT_VERSION=" + UpdateChecker.APP_UPDATE_CHECKER_FORMAT_VERSION + "\n" +
                        "APP_BUILD=" + UpdateChecker.APP_BUILD + "\n" +
                        "APP_UPDATE_CHECKER_URL=" + UpdateChecker.APP_UPDATE_CHECKER_URL + "\n" +
                        "updateChecker="+UpdateChecker.updateChecker.toString()
        )));


        debug_button_updateChecker_getVersion.setOnClickListener(v -> errorDetectorWrapper(() -> {
            long startTime = System.currentTimeMillis();
            UpdateChecker.getVersion((status, latestRelease, exception) -> runOnUiThread(() -> {
                long endTime = System.currentTimeMillis();
                DialogUtils.notifyDialog(finalContext, getString(R.string.debug_button_updateChecker_getVersion),
                        "delay=" + (endTime - startTime) + "ms" + "\n\n" +
                                "status=" + status + "\n\n" +
                                "latestRelease=" + latestRelease + "\n\n" +
                                "exception=" + exception
                );
            }));
        }));


        /*debug_button_updateChecker_changeAppBuild.setOnClickListener(v -> errorDetectorWrapper(() -> DialogUtils.inputDialog(this,
                getString(R.string.debug_button_updateChecker_changeAppBuild),
                null,
                String.valueOf(UpdateChecker.APP_BUILD),
                null,
                InputType.TYPE_CLASS_NUMBER,
                responseText -> errorDetectorWrapper(() -> UpdateChecker.APP_BUILD = Integer.parseInt(responseText))
        )));


        debug_button_updateChecker_changeVersionsFormat.setOnClickListener(v -> errorDetectorWrapper(() -> DialogUtils.inputDialog(this,
                getString(R.string.debug_button_updateChecker_changeVersionsFormat),
                null,
                String.valueOf(UpdateChecker.APP_UPDATE_CHECKER_FORMAT_VERSION),
                null,
                InputType.TYPE_CLASS_NUMBER,
                responseText -> errorDetectorWrapper(() -> UpdateChecker.APP_UPDATE_CHECKER_FORMAT_VERSION = Integer.parseInt(responseText))
        )));

        debug_button_updateChecker_changeVersionsUrl.setOnClickListener(v -> errorDetectorWrapper(() -> DialogUtils.inputDialog(this,
                getString(R.string.debug_button_updateChecker_changeVersionsUrl),
                null,
                UpdateChecker.APP_UPDATE_CHECKER_URL,
                null,
                InputType.TYPE_TEXT_VARIATION_LONG_MESSAGE,
                responseText -> errorDetectorWrapper(() -> UpdateChecker.APP_UPDATE_CHECKER_URL = responseText)
        )));*/


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


        // Unknown
        debug_button_unknown_log.setOnClickListener(v -> {
            EditText text = new EditText(this);
            text.setText(FileUtils.read(Paths.appFilePath+"/"+Logger.LOG_FILE));
            text.setTextSize(2, 14f);

            DialogUtils.inputDialog(this,
                    getString(R.string.debug_button_unknown_log),
                    null,
                    0,
                    true,
                    true,
                    null,
                    null,
                    null,
                    null,
                    null,
                    getString(R.string.OK),
                    null,
                    0,
                    new View[]{text}
            );
        });

        debug_button_unknown_openActivity.setOnClickListener(v -> errorDetectorWrapper(() -> {
            Class[] activities = {AboutActivity.class, DebugActivity.class, MainActivity.class, SettingsActivity.class, LoggerActivity.class};

            ArrayList<Button> buttons = new ArrayList<>();
            for (Class a : activities) {
                Button button = new Button(this);
                button.setAllCaps(false);
                button.setText(a.getName().replace("ru.fazziclay.openwidgets.android.activity.", ""));
                button.setOnClickListener(gg -> startActivity(new Intent(this, a)));
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
                errorDetectorWrapper(() -> debug_text_services_updateCheckerCounter.setText(("updateCheckerCounter = " + "-" + "/4=" + "-")));

                if (!isFinishing()) {
                    handler.postDelayed(this, 250);
                }
            }
        };
        handler.post(runnable);
    }

    private void loadTemporaryDisabled() {
        final Button[] TEMPORARY_DISABLED_BUTTONS = {
                new Button(this),
                debug_button_updateChecker_changeVersionsFormat,
                debug_button_updateChecker_changeAppBuild,
                debug_button_updateChecker_changeVersionsUrl
        };

        for (Button button : TEMPORARY_DISABLED_BUTTONS) {
            button.setOnClickListener(v -> errorDetectorWrapper(() -> DialogUtils.notifyDialog(this, "Temporary disabled | Временно отключено", null)));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);
        setTitle(R.string.activityTitle_debug);

        loadVariables();
        loadLogic();
        loadDynamicTexts();
        loadTemporaryDisabled();
    }
}