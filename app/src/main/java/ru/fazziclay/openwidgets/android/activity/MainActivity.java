
package ru.fazziclay.openwidgets.android.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import ru.fazziclay.openwidgets.Logger;
import ru.fazziclay.openwidgets.R;
import ru.fazziclay.openwidgets.update.checker.UpdateChecker;
import ru.fazziclay.openwidgets.android.ContextSaver;
import ru.fazziclay.openwidgets.data.Paths;
import ru.fazziclay.openwidgets.data.settings.SettingsData;
import ru.fazziclay.openwidgets.data.widgets.WidgetsData;
import ru.fazziclay.openwidgets.util.DialogUtils;
import ru.fazziclay.openwidgets.util.NotificationUtils;
import ru.fazziclay.openwidgets.util.Utils;


public class MainActivity extends AppCompatActivity {

    private void loadMainButtons() {
        Button main_button_about = findViewById(R.id.main_button_about);
        Button main_button_settings = findViewById(R.id.main_button_settings);

        main_button_about.setOnClickListener(v -> startActivity(new Intent(this, AboutActivity.class)));
        main_button_settings.setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class)));
    }

    private void loadUpdateChecker() {
        LinearLayout main_updateChecker_background = findViewById(R.id.main_updateChecker_background);
        TextView main_updateChecker_text = findViewById(R.id.main_updateChecker_text);
        Button main_updateChecker_button_toSite = findViewById(R.id.main_updateChecker_button_toSite);
        Button main_updateChecker_button_changeLog = findViewById(R.id.main_updateChecker_button_changeLog);
        Button main_updateChecker_button_download = findViewById(R.id.main_updateChecker_button_download);

        main_updateChecker_background.setVisibility(View.GONE);
        main_updateChecker_button_toSite.setVisibility(View.GONE);
        main_updateChecker_button_changeLog.setVisibility(View.GONE);
        main_updateChecker_button_download.setVisibility(View.GONE);

        UpdateChecker.getVersion((status, latestRelease, exception) -> runOnUiThread(() ->  {
            int updateCheckerVisible = View.VISIBLE;
            boolean isButtonChangeLog = false;
            boolean isButtonDownload = false;
            CharSequence text = null;

            if (status == UpdateChecker.Status.FORMAT_VERSION_NOT_SUPPORTED) {
                text = getText(R.string.updateChecker_FORMAT_VERSION_NOT_SUPPORTED);

            } else if (status == UpdateChecker.Status.VERSION_LATEST) {
                updateCheckerVisible = View.GONE;

            } else if (status == UpdateChecker.Status.VERSION_OUTDATED) {
                text = getText(R.string.updateChecker_VERSION_OUTDATED);
                isButtonChangeLog = true;
                isButtonDownload = true;

            } else if (status == UpdateChecker.Status.VERSION_NOT_RELEASE) {
                text = getText(R.string.updateChecker_VERSION_NOT_RELEASE);
                isButtonChangeLog = true;
                isButtonDownload = true;

            } else {
                text = getString(R.string.updateChecker_ERROR)
                        .replace("%ERROR_CODE%", status.toString())
                        .replace("%ERROR_MESSAGE%", exception.toString());
            }

            main_updateChecker_background.setVisibility(updateCheckerVisible);
            main_updateChecker_text.setText(text);
            main_updateChecker_button_toSite.setVisibility(View.VISIBLE);
            main_updateChecker_button_toSite.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(UpdateChecker.APP_SITE_URL))));

            if (isButtonChangeLog && latestRelease.getChangeLog() != null) {
                main_updateChecker_button_changeLog.setVisibility(View.VISIBLE);
                main_updateChecker_button_changeLog.setOnClickListener(v -> DialogUtils.notifyDialog(this, getString(R.string.updateChecker_button_changeLog), latestRelease.getChangeLog(SettingsData.getSettingsData().getLanguage())));
            }

            if (isButtonDownload) {
                main_updateChecker_button_download.setVisibility(View.VISIBLE);
                main_updateChecker_button_download.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(latestRelease.getDownloadUrl()))));
            }
        }));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ContextSaver.setContext(this);
        Paths.updatePaths(this);

        NotificationUtils.createChannel(this, "WidgetsUpdaterServiceForeground", "1", "2");

        final Logger LOGGER = new Logger(MainActivity.class, "onCreate");
        LOGGER.log("LOGGER CREATED");
        LOGGER.log("------ App starting... ------");

        // Load app
        SettingsData.load();
        WidgetsData.load();
        Utils.setAppLanguage(this, SettingsData.getSettingsData().getLanguage());
        LOGGER.log("------ App loaded! ------");

        // Activity
        setContentView(R.layout.activity_main);
        setTitle(R.string.activityTitle_main);

        loadMainButtons();
        loadUpdateChecker();
        LOGGER.log("------ MainActivity loaded! ------");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}