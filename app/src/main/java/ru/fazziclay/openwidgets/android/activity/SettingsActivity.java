package ru.fazziclay.openwidgets.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import ru.fazziclay.fazziclaylibs.FileUtils;
import ru.fazziclay.fazziclaylibs.NumberUtils;
import ru.fazziclay.openwidgets.Logger;
import ru.fazziclay.openwidgets.R;
import ru.fazziclay.openwidgets.data.Paths;
import ru.fazziclay.openwidgets.data.settings.SettingsData;
import ru.fazziclay.openwidgets.util.DialogUtils;
import ru.fazziclay.openwidgets.util.Utils;

import static ru.fazziclay.openwidgets.ErrorDetectorWrapper.errorDetectorWrapper;

public class SettingsActivity extends AppCompatActivity {
    public static final String[][] SUPPORTED_LANGUAGES = {{"Russian", "ru"}, {"English", "en"}};
    public static boolean restartRequired = false;

    int hiddenDebugActivityCounter = 0;

    Button toDebugButton;
    boolean isDebugAllow = false;

    Button appLanguageButton;
    String currentLanguage = "en";

    Button settings_button_widgetsUpdateDelayMillis;

    CheckBox settings_checkBox_logger;
    boolean isLogger = true;

    private void loadVariables() {
        toDebugButton = findViewById(R.id.button_toDebug);
        appLanguageButton = findViewById(R.id.button_appLanguage);
        settings_button_widgetsUpdateDelayMillis = findViewById(R.id.settings_button_widgetsUpdateDelayMillis);
        settings_checkBox_logger = findViewById(R.id.settings_checkBox_logger);
    }

    private void loadMainButtons() {
        final Logger LOGGER = new Logger(SettingsActivity.class, "loadMainButtons");

        SettingsData settingsData = SettingsData.getSettingsData();
        if (settingsData != null) {
            isDebugAllow = settingsData.isDebugAllow();
            currentLanguage = settingsData.getLanguage();
            isLogger = settingsData.isLogger();
        }

        // Debug
        if (!(isDebugAllow || Logger.PRINUDIL_LOGGING)) {
            toDebugButton.setVisibility(View.GONE);
        }
        toDebugButton.setOnClickListener(v -> startActivity(new Intent(this, DebugActivity.class)));

        // Language
        appLanguageButton.setOnClickListener(v -> {
            LOGGER.log("clicked to appLanguageButton");
            if (settingsData == null) {
                LOGGER.log("settingsData == null. returned.");
                DialogUtils.notifyDialog(this, getString(R.string.ERROR), getString(R.string.ERROR_SEND_TO_DEVELOPERS).replace("%ERROR%", "settingsData == null"));
                return;
            }
            RadioGroup languages = new RadioGroup(this);

            int i = 0;
            for (String[] language : SUPPORTED_LANGUAGES) {
                RadioButton languageButton = new RadioButton(this);
                languageButton.setText(language[0]);
                languageButton.setId(i);
                languageButton.setChecked((language[1].equals(currentLanguage)));
                languages.addView(languageButton);
                i++;
            }

            DialogUtils.inputDialog(this,
                    getString(R.string.settings_language),
                    null,
                    () -> {
                        settingsData.setLanguage(SUPPORTED_LANGUAGES[languages.getCheckedRadioButtonId()][1]);
                        SettingsData.save();
                        restartRequired = true;
                        finish();
                    },
                    new RadioGroup[]{languages});
        });

        // Widgets update delay
        settings_button_widgetsUpdateDelayMillis.setOnClickListener(v -> DialogUtils.inputDialog(this,
                getString(R.string.settings_widgetsUpdateDelayMillis),
                getString(R.string.settings_widgetsUpdateDelayMillis_message),
                String.valueOf(SettingsData.getSettingsData().getWidgetsUpdateDelayMillis()),
                null,
                InputType.TYPE_CLASS_NUMBER,
                responseText -> errorDetectorWrapper(() -> {
                    SettingsData.getSettingsData().setWidgetsUpdateDelayMillis(Integer.parseInt(responseText));
                    SettingsData.save();
                })));

        // Debug Logging
        settings_checkBox_logger.setChecked(isLogger || Logger.PRINUDIL_LOGGING);
        if (Logger.PRINUDIL_LOGGING) settings_checkBox_logger.setEnabled(false);

        Button settings_button_share_logs = findViewById(R.id.settings_button_share_logs);
        settings_button_share_logs.setOnClickListener(v -> Utils.shareText(this, "--- SHARE DEBUG LOG ---", FileUtils.read(Paths.getAppFilePath() + Logger.LOG_FILE)));
        if (settings_checkBox_logger.isChecked()) settings_button_share_logs.setVisibility(View.VISIBLE);

        settings_checkBox_logger.setOnClickListener(v -> {
            LOGGER.log("clicked to settings_checkBox_logger");
            if (settingsData == null) {
                LOGGER.log("settingsData == null. returned.");
                DialogUtils.notifyDialog(this, getString(R.string.ERROR), getString(R.string.ERROR_SEND_TO_DEVELOPERS).replace("%ERROR%", "settingsData == null"));
                return;
            }
            settingsData.setLogger(settings_checkBox_logger.isChecked());
            settings_button_share_logs.setVisibility(View.GONE);
            if (settings_checkBox_logger.isChecked()) settings_button_share_logs.setVisibility(View.VISIBLE);

            if (hiddenDebugActivity()) {
                settingsData.setLogger(true);
                settings_checkBox_logger.setChecked(true);

                settingsData.setDebugAllow(!settingsData.isDebugAllow());
                SettingsData.save();
                if (settingsData.isDebugAllow()) {
                    Utils.showToast(this, "1..__..1 \uD83E\uDD16");
                } else {
                    Utils.showToast(this, "0..__..0 ❤");
                }

                startActivity(new Intent(this, this.getClass()));
                finish();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle(R.string.activityTitle_settings);

        SettingsData.load();
        loadVariables();
        loadMainButtons();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SettingsActivity.restartRequired) {
            finish();
        }
    }

    private boolean hiddenDebugActivity() {
        hiddenDebugActivityCounter++;
        if (NumberUtils.getRandom(0, 1000) == 0) {
            hiddenDebugActivityCounter = -10;
            Utils.showToast(this, "Ha Ha 0.1% chances. hiddenDebugActivityCounter=-10");
        }
        if (SettingsData.getSettingsData().isDebugAllow()) {
            return hiddenDebugActivityCounter > 10;
        }
        return hiddenDebugActivityCounter > 50;
    }
}