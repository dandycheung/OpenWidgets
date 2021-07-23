package ru.fazziclay.openwidgets.android.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;


import ru.fazziclay.fazziclaylibs.FileUtils;
import ru.fazziclay.openwidgets.Logger;
import ru.fazziclay.openwidgets.R;
import ru.fazziclay.openwidgets.data.Paths;

public class LoggerActivity extends Activity {
    ScrollView scrollView;
    LinearLayout background;
    Button clearButton;
    Button textSizePlus;
    Button textSizeMinus;
    CheckBox autoScrollCheckBox;
    TextView logText;

    float textSize = 10.0f;

    private void loadVariables() {
        scrollView = findViewById(R.id.logger_scroll);
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
        final Logger LOGGER = new Logger(LoggerActivity.class, "loadVariables");
        clearButton.setOnClickListener(v -> LOGGER.clear());
        textSizePlus.setOnClickListener(v -> textSize = textSize + 1.0f);
        textSizeMinus.setOnClickListener(v -> textSize = textSize - 1.0f);
    }

    private void loadTextUpdater() {
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                logText.setText(FileUtils.read(Paths.appFilePath + "/" + Logger.LOG_FILE));
                logText.setTextSize(textSize);
                if (autoScrollCheckBox.isChecked()) scrollView.scrollTo(0, 999999999);

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
        setContentView(R.layout.activity_logger);
        setTitle(R.string.activityTitle_logger);

        loadVariables();
        loadLogic();
        loadTextUpdater();
    }
}