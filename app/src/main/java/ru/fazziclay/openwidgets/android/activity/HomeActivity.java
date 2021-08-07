
package ru.fazziclay.openwidgets.android.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.text.MessageFormat;
import java.util.Iterator;

import ru.fazziclay.openwidgets.Logger;
import ru.fazziclay.openwidgets.R;
import ru.fazziclay.openwidgets.android.ContextSaver;
import ru.fazziclay.openwidgets.android.activity.configurator.DateWidgetConfiguratorActivity;
import ru.fazziclay.openwidgets.data.Paths;
import ru.fazziclay.openwidgets.data.settings.SettingsData;
import ru.fazziclay.openwidgets.data.widgets.WidgetFlag;
import ru.fazziclay.openwidgets.data.widgets.WidgetsData;
import ru.fazziclay.openwidgets.data.widgets.widget.DateWidget;
import ru.fazziclay.openwidgets.update.checker.UpdateChecker;
import ru.fazziclay.openwidgets.util.DialogUtils;


public class HomeActivity extends AppCompatActivity {

    private void loadMainButtons() {
        Button main_button_about = findViewById(R.id.main_button_about);
        Button main_button_settings = findViewById(R.id.main_button_settings);

        main_button_about.setOnClickListener(v -> startActivity(new Intent(this, AboutActivity.class)));
        main_button_settings.setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class)));
    }

    private void loadWidgetsButtons() {
        final Logger LOGGER = new Logger(HomeActivity.class, "loadWidgetsButtons");
        boolean isWidgetsNone = true;

        LinearLayout main_widgetsButtonsSlot = findViewById(R.id.widgetsButtonsSlot);
        LinearLayout main_dateWidgetsButtonsSlot = findViewById(R.id.main_dateWidgetsButtonsSlot);
        TextView main_dateWidgetsTitle = findViewById(R.id.main_dateWidgetsButtonsSlot_title);

        main_dateWidgetsButtonsSlot.setVisibility(View.GONE);
        main_widgetsButtonsSlot.setVisibility(View.GONE);
        main_dateWidgetsTitle.setVisibility(View.GONE);


        // Date Widgets
        main_dateWidgetsButtonsSlot.removeAllViews();
        WidgetsData widgetsData = WidgetsData.getWidgetsData();

        Iterator<DateWidget> dateWidgetIterator = widgetsData.getDateWidgets().iterator();
        boolean isDateWidgetsAvailable = widgetsData.getDateWidgets().size() > 0;
        if (isDateWidgetsAvailable) {
            isWidgetsNone = false;
            main_dateWidgetsButtonsSlot.setVisibility(View.VISIBLE);
            main_dateWidgetsTitle.setVisibility(View.VISIBLE);

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
                    PopupMenu popupMenu = new PopupMenu(this, button);
                    popupMenu.getMenuInflater().inflate(R.menu.menu_date_widget_configurator, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(item -> {
                        switch (item.getItemId()) {
                            case (R.id.dateWidgetConfigurator_menu_restoreToDefault):
                                DialogUtils.warningDialog(this,
                                        getString(R.string.widgetConfigurator_date_menu_restoreToDefault_warning_title),
                                        getString(R.string.widgetConfigurator_date_menu_restoreToDefault_warning_message),
                                        0,
                                        dateWidget::restoreToDefaults);
                                break;

                            case (R.id.dateWidgetConfigurator_menu_loadFromAnotherWidget):
                                DialogUtils.selectDateWidgetDialog(this,
                                        getString(R.string.widgetConfigurator_date_menu_loadFromAnotherWidget_title),
                                        getString(R.string.widgetConfigurator_date_menu_loadFromAnotherWidget_message),
                                        dateWidget::loadFromAnotherWidget);
                                break;

                            case (R.id.dateWidgetConfigurator_menu_delete):
                                DialogUtils.warningDialog(this,
                                        getString(R.string.widgetConfigurator_date_menu_delete_warning_title),
                                        getString(R.string.widgetConfigurator_date_menu_delete_warning_message),
                                        0,
                                        () -> {
                                            dateWidget.delete();
                                            loadWidgetsButtons();
                                        });
                                break;

                            default:
                                return super.onOptionsItemSelected(item);
                        }
                        return true;
                    });
                    popupMenu.show();

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

        TextView widgetsIsNoneText = findViewById(R.id.main_text_widgetsIsNone);
        CheckBox checkBox = findViewById(R.id.main_checkBox_widgetsIdMode);
        if (isWidgetsNone) {
            widgetsIsNoneText.setVisibility(View.VISIBLE);
            checkBox.setVisibility(View.GONE);
            main_widgetsButtonsSlot.setVisibility(View.GONE);
        } else {
            widgetsIsNoneText.setVisibility(View.GONE);
            checkBox.setVisibility(View.VISIBLE);
            checkBox.setChecked(SettingsData.getSettingsData().isViewIdInWidgets());
            checkBox.setOnClickListener(v -> SettingsData.getSettingsData().setViewIdInWidgets(checkBox.isChecked()));
            main_widgetsButtonsSlot.setVisibility(View.VISIBLE);
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
                Snackbar snackbar = Snackbar.make(findViewById(R.id.activity_home_root), R.string.updateChecker_text_NO_NETWORK_CONNECTION, 4000)
                        .setAction(R.string.OK, v -> {});
                snackbar.show();

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

    @SuppressLint("BatteryLife")
    private void disablePowerSaver() {
        final Logger LOGGER = new Logger(HomeActivity.class, "disablePowerSaver");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String packageName = getPackageName();
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);

            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + packageName));
                startActivity(intent);
                LOGGER.log("Dialog sent");
            } else {
                LOGGER.log("Before disabled");
            }
        } else {
            LOGGER.log("Android version not supported");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Paths.getAppFilePath() == null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        final Logger LOGGER = new Logger(HomeActivity.class, "onCreate");
        if (getIntent().getIntExtra(MainActivity.INTENT_EXTRA_SAVER_ID, Integer.MIN_VALUE) != MainActivity.INTENT_EXTRA_SAVER_VALUE) {
            LOGGER.error("INTENT_EXTRA_SAVER_ID value != intent extra value. activity finishing");
            finish();
            return;
        }

        ContextSaver.setContext(this);

        setContentView(R.layout.activity_home);
        setTitle(R.string.activityTitle_main);

        loadMainButtons();
        loadWidgetsButtons();
        loadUpdateChecker();
        disablePowerSaver();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SettingsActivity.restartRequired) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }
        loadWidgetsButtons();
    }
}