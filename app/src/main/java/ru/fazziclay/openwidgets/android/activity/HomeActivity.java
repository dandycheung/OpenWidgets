
package ru.fazziclay.openwidgets.android.activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.MessageFormat;
import java.util.Iterator;

import ru.fazziclay.fazziclaylibs.NumberUtils;
import ru.fazziclay.openwidgets.Logger;
import ru.fazziclay.openwidgets.R;
import ru.fazziclay.openwidgets.android.activity.configurator.DateWidgetConfiguratorActivity;
import ru.fazziclay.openwidgets.data.settings.SettingsData;
import ru.fazziclay.openwidgets.data.widgets.WidgetFlag;
import ru.fazziclay.openwidgets.data.widgets.WidgetsData;
import ru.fazziclay.openwidgets.data.widgets.widget.DateWidget;
import ru.fazziclay.openwidgets.update.checker.UpdateChecker;
import ru.fazziclay.openwidgets.util.DialogUtils;
import ru.fazziclay.openwidgets.util.Utils;


public class HomeActivity extends AppCompatActivity {
    int otherMode = -1;

    private void loadMainButtons() {
        Button main_button_about = findViewById(R.id.main_button_about);
        Button main_button_settings = findViewById(R.id.main_button_settings);

        main_button_about.setOnClickListener(v -> startActivity(new Intent(this, AboutActivity.class)));
        main_button_settings.setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class)));
    }

    private void loadWidgetsButtons() {
        final Logger LOGGER = new Logger(HomeActivity.class, "loadWidgetsButtons");
        boolean isWidgetsNone = true;

        LinearLayout widgetsButtonsSlot = findViewById(R.id.widgetsButtonsSlot);
        LinearLayout main_dateWidgetsButtonsSlot = findViewById(R.id.main_dateWidgetsButtonsSlot);
        TextView dateWidgetsTitle = findViewById(R.id.main_dateWidgetsButtonsSlot_title);

        main_dateWidgetsButtonsSlot.setVisibility(View.GONE);
        widgetsButtonsSlot.setVisibility(View.GONE);
        dateWidgetsTitle.setVisibility(View.GONE);

        main_dateWidgetsButtonsSlot.removeAllViews();

        // Date Widgets
        Iterator<DateWidget> dateWidgetIterator = WidgetsData.getWidgetsData().getDateWidgets().iterator();
        boolean isDateWidgetsAvailable = WidgetsData.getWidgetsData().getDateWidgets().size() > 0;
        if (isDateWidgetsAvailable) {
            isWidgetsNone = false;
            main_dateWidgetsButtonsSlot.setVisibility(View.VISIBLE);
            dateWidgetsTitle.setVisibility(View.VISIBLE);

            while (dateWidgetIterator.hasNext()) {
                DateWidget dateWidget = dateWidgetIterator.next();

                LinearLayout widgetButton = new LinearLayout(this);
                widgetButton.setOrientation(LinearLayout.HORIZONTAL);
                widgetButton.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                widgetButton.setPadding(0,0,0,0);

                Button button = new Button(this);
                button.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 10));
                button.setAllCaps(false);
                button.setOnClickListener(v -> startActivity(new Intent(this, DateWidgetConfiguratorActivity.class).putExtra("widgetId", dateWidget.getWidgetId())));
                button.setText(MessageFormat.format("{0} ({1})", getString(R.string.widgetName_date), dateWidget.getWidgetId()));
                button.setOnLongClickListener(v -> {
                    button.setTextColor(NumberUtils.getRandom(0, 256*256*256));
                    return true;
                });

                widgetButton.addView(button);
                if (dateWidget.getFlags().contains(WidgetFlag.CONVERTED_FROM_FORMAT_VERSION_2)) {
                    Button warningButton = new Button(this);
                    warningButton.setText("!");
                    warningButton.setTextColor(Color.parseColor("#F41C00"));
                    warningButton.setTextSize(20);
                    warningButton.setOnClickListener(v -> DialogUtils.notifyDialog(this, getString(R.string.widgetConverter_fromV2_title), getString(R.string.widgetConverter_fromV2_message)));
                    widgetButton.addView(warningButton);
                }

                main_dateWidgetsButtonsSlot.addView(widgetButton);
            }
        }

        LOGGER.log("isWidgetsNone="+isWidgetsNone+", isDateWidgetsAvailable="+isDateWidgetsAvailable);
        // detect none
        if (!isWidgetsNone) {
            findViewById(R.id.main_text_widgetsIsNone).setVisibility(View.GONE);
            CheckBox checkBox = findViewById(R.id.main_checkBox_widgetsIdMode);
            checkBox.setVisibility(View.VISIBLE);
            checkBox.setChecked(SettingsData.getSettingsData().isViewIdInWidgets());
            checkBox.setOnClickListener(v -> SettingsData.getSettingsData().setViewIdInWidgets(checkBox.isChecked()));
            widgetsButtonsSlot.setVisibility(View.VISIBLE);
        }
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
                text = getText(R.string.updateChecker_text_FORMAT_VERSION_NOT_SUPPORTED);

            } else if (status == UpdateChecker.Status.VERSION_LATEST) {
                updateCheckerVisible = View.GONE;

            } else if (status == UpdateChecker.Status.VERSION_OUTDATED) {
                text = getText(R.string.updateChecker_text_VERSION_OUTDATED);
                isButtonChangeLog = true;
                isButtonDownload = true;

            } else if (status == UpdateChecker.Status.VERSION_NOT_RELEASE) {
                text = getText(R.string.updateChecker_text_VERSION_NOT_RELEASE);
                isButtonChangeLog = true;
                isButtonDownload = true;

            } else if (status == UpdateChecker.Status.NO_NETWORK_CONNECTION) {
                updateCheckerVisible = View.GONE;

            } else {
                text = getString(R.string.updateChecker_text_ERROR)
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

    private BroadcastReceiver mPowerKeyReceiver = null;

    private void registBroadcastReceiver(Logger logger) {
        logger.log("registBroadcastReceiver();");

        final IntentFilter theFilter = new IntentFilter();
        /** System Defined Broadcast */
        theFilter.addAction(Intent.ACTION_SCREEN_ON);
        theFilter.addAction(Intent.ACTION_SCREEN_OFF);

        mPowerKeyReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String strAction = intent.getAction();

                if (strAction.equals(Intent.ACTION_SCREEN_OFF)) {
                    logger.log("ON RECEIVE screen_off");
                }

                if (strAction.equals(Intent.ACTION_SCREEN_ON)) {
                    logger.log("ON RECEIVE screen_on");
                    Utils.showToast(context, "ON");
                }
            }
        };

        getApplicationContext().registerReceiver(mPowerKeyReceiver, theFilter);
    }

    @SuppressLint("BatteryLife")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getIntExtra(MainActivity.INTENT_EXTRA_SAVER_ID, Integer.MIN_VALUE) != MainActivity.INTENT_EXTRA_SAVER_VALUE) {
            finish();
            return;
        }

        // Activity
        setContentView(R.layout.activity_home);
        setTitle(R.string.activityTitle_main);

        loadMainButtons();
        loadWidgetsButtons();
        loadUpdateChecker();

        // Disable power saver
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String packageName = getPackageName();
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);

            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + packageName));
                startActivity(intent);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadWidgetsButtons();
    }
}