package ru.fazziclay.openwidgets.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import ru.fazziclay.openwidgets.R;

public class DebugActivity extends AppCompatActivity {
    // Variables
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


    private void loadVariables() {
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
    }

    private void loadLogic() {

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