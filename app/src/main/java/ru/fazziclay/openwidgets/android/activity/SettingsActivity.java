package ru.fazziclay.openwidgets.android.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import ru.fazziclay.fazziclaylibs.FileUtils;
import ru.fazziclay.openwidgets.Logger;
import ru.fazziclay.openwidgets.R;
import ru.fazziclay.openwidgets.data.Paths;
import ru.fazziclay.openwidgets.data.settings.SettingsData;
import ru.fazziclay.openwidgets.databinding.ActivitySettingsBinding;
import ru.fazziclay.openwidgets.network.Client;
import ru.fazziclay.openwidgets.util.DialogUtils;
import ru.fazziclay.openwidgets.util.Utils;

import static ru.fazziclay.openwidgets.ErrorDetectorWrapper.errorDetectorWrapper;

public class SettingsActivity extends AppCompatActivity {
    public static final String[][] SUPPORTED_LANGUAGES = {{"Russian", "ru"}, {"English", "en"}};
    public static final boolean FORCED_DEBUG_ALLOW = true;
    public static boolean restartRequired = false;

    ActivitySettingsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Logger LOGGER = new Logger();
        try {
            binding = ActivitySettingsBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());
            setTitle(R.string.activityTitle_settings);

            LOGGER.log("settingsData before=" + SettingsData.getSettingsData());
            SettingsData.load();
            loadMainButtons();
            a();
        } catch (Exception exception) {
            LOGGER.exception(exception);
        }

        LOGGER.done();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SettingsActivity.restartRequired) {
            finish();
        }
    }

    private void loadMainButtons() {
        SettingsData settingsData = SettingsData.getSettingsData();

        // Debug
        binding.buttonToDebug.setOnClickListener(v -> startActivity(new Intent(this, DebugActivity.class)));

        // Language
        binding.buttonAppLanguage.setOnClickListener(v -> {
            RadioGroup languages = new RadioGroup(this);

            int i = 0;
            for (String[] language : SUPPORTED_LANGUAGES) {
                RadioButton languageButton = new RadioButton(this);
                languageButton.setText(language[0]);
                languageButton.setId(i);
                languageButton.setChecked((language[1].equals(settingsData.getLanguage())));
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
        binding.settingsButtonWidgetsUpdateDelayMillis.setOnClickListener(v -> DialogUtils.inputDialog(this,
                getString(R.string.settings_widgetsUpdateDelayMillis),
                getString(R.string.settings_widgetsUpdateDelayMillis_message),
                String.valueOf(SettingsData.getSettingsData().getWidgetsUpdateDelayMillis()),
                String.valueOf(1000),
                InputType.TYPE_CLASS_NUMBER,
                responseText -> errorDetectorWrapper(() -> {
                    SettingsData.getSettingsData().setWidgetsUpdateDelayMillis(Integer.parseInt(responseText));
                    SettingsData.save();
                })));

        // Power Buttons Evens
        binding.settingsCheckBoxIsStopWidgetsUpdaterAfterScreenOff.setOnClickListener(v -> {
            settingsData.setStopWidgetsUpdaterAfterScreenOff(binding.settingsCheckBoxIsStopWidgetsUpdaterAfterScreenOff.isChecked());

            binding.settingsCheckBoxIsStartWidgetsUpdaterAfterScreenOn.setChecked(binding.settingsCheckBoxIsStopWidgetsUpdaterAfterScreenOff.isChecked() || settingsData.isStartWidgetsUpdaterAfterScreenOn());
            binding.settingsCheckBoxIsStartWidgetsUpdaterAfterScreenOn.setEnabled(!binding.settingsCheckBoxIsStopWidgetsUpdaterAfterScreenOff.isChecked());
        });

        binding.settingsCheckBoxIsStartWidgetsUpdaterAfterScreenOn.setOnClickListener(v -> settingsData.setStartWidgetsUpdaterAfterScreenOn(binding.settingsCheckBoxIsStartWidgetsUpdaterAfterScreenOn.isChecked()));

        // Debug Logging
        binding.settingsButtonLoggerClear.setOnClickListener(v -> DialogUtils.warningDialog(this,
                getString(R.string.settings_logger_clear_title),
                getString(R.string.settings_logger_clear_message),
                0,
                () -> {
                    //final Logger LOGGER1 = new Logger();
                    //LOGGER1.clear();
                   // a();
                    Utils.showToast(this, "Function temporary disabled!");
                }));


        Context finalContext = this;
        binding.settingsButtonLoggerUpload.setOnClickListener(v -> DialogUtils.warningDialog(this,
                getString(R.string.settings_logger_upload_title),
                getString(R.string.settings_logger_upload_message).replace("%INSTANCE_ID%", String.valueOf(settingsData.getInstanceUUID())),
                0,
                Client::connectToServer));

        binding.settingsButtonLoggerShare.setOnClickListener(v -> {
            /*final Logger LOGGER1 = */ new Logger();
            Utils.shareText(this, getString(R.string.settings_logger_share_title), FileUtils.read(Paths.getAppFilePath() + Logger.LOG_FILE));
        });

        binding.settingsCheckBoxLogger.setOnClickListener(v -> {
            settingsData.setLogger(binding.settingsCheckBoxLogger.isChecked());
            a();
        });
    }

    private void a() { // TODO: 09.08.2021 rename method
        final Logger LOGGER = new Logger();

        SettingsData settingsData = SettingsData.getSettingsData();

        binding.settingsCheckBoxIsStopWidgetsUpdaterAfterScreenOff.setChecked(settingsData.isStopWidgetsUpdaterAfterScreenOff());
        binding.settingsCheckBoxIsStartWidgetsUpdaterAfterScreenOn.setChecked(binding.settingsCheckBoxIsStopWidgetsUpdaterAfterScreenOff.isChecked() || settingsData.isStartWidgetsUpdaterAfterScreenOn());
        binding.settingsCheckBoxIsStartWidgetsUpdaterAfterScreenOn.setEnabled(!binding.settingsCheckBoxIsStopWidgetsUpdaterAfterScreenOff.isChecked());

        binding.buttonToDebug.setVisibility(Utils.booleanToVisible(settingsData.isDebugAllow() || FORCED_DEBUG_ALLOW, View.GONE));
        binding.settingsButtonLoggerClear.setVisibility(Utils.booleanToVisible((!LOGGER.isCleared()), View.INVISIBLE));
        binding.settingsButtonLoggerUpload.setVisibility(Utils.booleanToVisible(!LOGGER.isCleared(), View.INVISIBLE));
        binding.settingsButtonLoggerShare.setVisibility(Utils.booleanToVisible(!LOGGER.isCleared(), View.INVISIBLE));
    }
}