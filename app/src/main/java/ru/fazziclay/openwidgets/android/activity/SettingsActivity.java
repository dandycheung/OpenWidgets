package ru.fazziclay.openwidgets.android.activity;

import android.os.Bundle;
import android.text.InputType;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import ru.fazziclay.openwidgets.Logger;
import ru.fazziclay.openwidgets.R;
import ru.fazziclay.openwidgets.data.settings.SettingsData;
import ru.fazziclay.openwidgets.databinding.ActivitySettingsBinding;
import ru.fazziclay.openwidgets.util.DialogUtils;

import static ru.fazziclay.openwidgets.ErrorDetectorWrapper.errorDetectorWrapper;

public class SettingsActivity extends AppCompatActivity {
    public static final String[][] SUPPORTED_LANGUAGES = {{"Russian", "ru"}, {"English", "en"}};
    public static boolean restartRequired = false;

    SettingsData settingsData = null;
    ActivitySettingsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Logger LOGGER = new Logger();
        try {
            binding = ActivitySettingsBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());
            setTitle(R.string.activityTitle_settings);

            SettingsData.load();
            settingsData = SettingsData.getSettingsData();

            loadMainButtons();
            loadViews();
        } catch (Throwable t) {
            LOGGER.error(t);
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
                responseText -> errorDetectorWrapper(this, () -> {
                    SettingsData.getSettingsData().setWidgetsUpdateDelayMillis(Integer.parseInt(responseText));
                    SettingsData.save();
                })));

        binding.mainCheckBoxWidgetsIdMode.setOnClickListener(v -> settingsData.setViewIdInWidgets(binding.mainCheckBoxWidgetsIdMode.isChecked()));

        // Power Buttons Evens
        binding.settingsCheckBoxIsStopWidgetsUpdaterAfterScreenOff.setOnClickListener(v -> {
            settingsData.setStopWidgetsUpdaterAfterScreenOff(binding.settingsCheckBoxIsStopWidgetsUpdaterAfterScreenOff.isChecked());

            binding.settingsCheckBoxIsStartWidgetsUpdaterAfterScreenOn.setChecked(binding.settingsCheckBoxIsStopWidgetsUpdaterAfterScreenOff.isChecked() || settingsData.isStartWidgetsUpdaterAfterScreenOn());
            binding.settingsCheckBoxIsStartWidgetsUpdaterAfterScreenOn.setEnabled(!binding.settingsCheckBoxIsStopWidgetsUpdaterAfterScreenOff.isChecked());
        });

        binding.settingsCheckBoxIsStartWidgetsUpdaterAfterScreenOn.setOnClickListener(v -> settingsData.setStartWidgetsUpdaterAfterScreenOn(binding.settingsCheckBoxIsStartWidgetsUpdaterAfterScreenOn.isChecked()));
    }

    private void loadViews() {
        binding.mainCheckBoxWidgetsIdMode.setChecked(settingsData.isViewIdInWidgets());
        binding.settingsCheckBoxIsStopWidgetsUpdaterAfterScreenOff.setChecked(settingsData.isStopWidgetsUpdaterAfterScreenOff());
        binding.settingsCheckBoxIsStartWidgetsUpdaterAfterScreenOn.setChecked(binding.settingsCheckBoxIsStopWidgetsUpdaterAfterScreenOff.isChecked() || settingsData.isStartWidgetsUpdaterAfterScreenOn());
        binding.settingsCheckBoxIsStartWidgetsUpdaterAfterScreenOn.setEnabled(!binding.settingsCheckBoxIsStopWidgetsUpdaterAfterScreenOff.isChecked());
    }
}