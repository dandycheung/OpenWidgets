package ru.fazziclay.openwidgets.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import ru.fazziclay.fazziclaylibs.NumberUtils;
import ru.fazziclay.openwidgets.R;
import ru.fazziclay.openwidgets.data.settings.SettingsData;
import ru.fazziclay.openwidgets.util.DialogUtils;
import ru.fazziclay.openwidgets.util.Utils;

import static ru.fazziclay.openwidgets.ErrorDetectorWrapper.errorDetectorWrapper;

public class SettingsActivity extends AppCompatActivity {
    public static final String[][] SUPPORTED_LANGUAGES = {{"Russian", "ru"}, {"English", "en"}};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Android
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle(R.string.activityTitle_settings);

        Button toDebugButton = findViewById(R.id.button_toDebug);
        if (!SettingsData.getSettingsData().isDebugAllow()) {
            toDebugButton.setVisibility(View.GONE);
        }
        toDebugButton.setOnClickListener(v -> startActivity(new Intent(this, DebugActivity.class)));

        Button appLanguageButton = findViewById(R.id.button_appLanguage);
        appLanguageButton.setOnClickListener(v -> {
            ArrayList<View> views = new ArrayList<>();
            for (String[] language : SUPPORTED_LANGUAGES) {
                Button button = new Button(this);
                button.setText(language[0]);
                button.setOnClickListener(vv -> {
                    SettingsData.getSettingsData().setLanguage(language[1]);
                    errorDetectorWrapper(SettingsData::save);
                    finish();
                    System.exit(0);
                });
                views.add(button);
            }

            DialogUtils.inputDialog(this,
                    getString(R.string.settings_language),
                    null,
                    null,
                    views.toArray(new View[0]));
        });


        CheckBox settings_checkBox_logger = findViewById(R.id.settings_checkBox_logger);
        settings_checkBox_logger.setOnClickListener(v -> {
            SettingsData.getSettingsData().setLogger(settings_checkBox_logger.isChecked());

            if (hiddenDebugActivity()) {
                SettingsData.getSettingsData().setLogger(true);
                settings_checkBox_logger.setChecked(true);

                SettingsData.getSettingsData().setDebugAllow(!SettingsData.getSettingsData().isDebugAllow());
                SettingsData.save();
                if (SettingsData.getSettingsData().isDebugAllow()) {
                    Utils.showToast(this, "1..__..1 \uD83E\uDD16");
                } else {
                    Utils.showToast(this, "0..__..0 ❤");
                }

                startActivity(new Intent(this, this.getClass()));
                finish();
            }
        });
    }

    int hiddenDebugActivityCounter = 0;
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