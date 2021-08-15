package ru.fazziclay.openwidgets.android.activity;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import ru.fazziclay.fazziclaylibs.FileUtils;
import ru.fazziclay.openwidgets.Logger;
import ru.fazziclay.openwidgets.R;
import ru.fazziclay.openwidgets.data.Paths;
import ru.fazziclay.openwidgets.databinding.ActivityLoggerBinding;

public class LoggerActivity extends AppCompatActivity {
    ActivityLoggerBinding binding;

    LinearLayout background;
    Button clearButton;
    Button textSizePlus;
    Button textSizeMinus;
    CheckBox autoScrollCheckBox;
    TextView logText;

    float textSize = 10.0f;

    private void loadVariables() {
        background = findViewById(R.id.logger_background);
        clearButton = findViewById(R.id.logger_clear);
        textSizePlus = findViewById(R.id.logger_textSizePlus);
        textSizeMinus = findViewById(R.id.logger_textSizeMinus);
        autoScrollCheckBox = findViewById(R.id.logger_autoScroll);
        logText = new TextView(this);

        // Setting
        background.addView(logText);
    }

    private void loadLogic() {
        final Logger LOGGER = new Logger();
        clearButton.setOnClickListener(v -> LOGGER.clear());
        textSizePlus.setOnClickListener(v -> {
            textSize = textSize + 1.0f;
            logText.setTextSize(textSize);
        });
        textSizeMinus.setOnClickListener(v -> {
            textSize = textSize - 1.0f;
            logText.setTextSize(textSize);
        });
        autoScrollCheckBox.setOnClickListener(v -> binding.loggerScroll.smoothScrollTo(0, binding.loggerScroll.getChildAt(0).getHeight()));
    }

    private void loadTextUpdater() {
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                String latestLogs = FileUtils.read(Paths.getAppFilePath() + Logger.LOG_FILE);

                if (!logText.getText().toString().equals(latestLogs)) logText.setText(latestLogs);
                if (autoScrollCheckBox.isChecked()) binding.loggerScroll.smoothScrollTo(0, binding.loggerScroll.getChildAt(0).getHeight());

                if (!isFinishing()) {
                    handler.postDelayed(this, 100);
                }
            }
        };
        handler.post(runnable);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoggerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle(R.string.activityTitle_logger);

        loadVariables();
        loadLogic();
        loadTextUpdater();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SettingsActivity.restartRequired) {
            finish();
        }
    }
}