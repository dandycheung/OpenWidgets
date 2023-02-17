
package com.c0rdination.openwidgets.android.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.text.MessageFormat;

import com.c0rdination.openwidgets.Logger;
import com.c0rdination.openwidgets.R;
import com.c0rdination.openwidgets.android.activity.configurator.DateWidgetConfiguratorActivity;
import com.c0rdination.openwidgets.data.Paths;
import com.c0rdination.openwidgets.data.settings.SettingsData;
import com.c0rdination.openwidgets.data.widgets.WidgetFlag;
import com.c0rdination.openwidgets.data.widgets.WidgetRegistry;
import com.c0rdination.openwidgets.android.widget.DateWidget;
import com.c0rdination.openwidgets.data.widgets.widget.BaseWidget;
import com.c0rdination.openwidgets.databinding.ActivityHomeBinding;
import com.c0rdination.openwidgets.update.checker.UpdateChecker;
import com.c0rdination.openwidgets.util.DialogUtils;
import com.c0rdination.openwidgets.util.Utils;

public class HomeActivity extends AppCompatActivity {
    boolean firstOnResumeSkipFlag = false;
    ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Logger LOGGER = new Logger();

        try {
            if (Paths.getAppFilePath() == null || SettingsData.getSettingsData() == null || WidgetRegistry.getWidgetRegistry() == null) {
                LOGGER.log("App no loaded detected!");
                LOGGER.info("Paths.getAppFilePath()=" + Paths.getAppFilePath());
                LOGGER.info("SettingsData.getSettingsData()=" + SettingsData.getSettingsData());
                LOGGER.info("WidgetRegistry.getWidgetRegistry()=" + WidgetRegistry.getWidgetRegistry());
                LOGGER.log("Throw Exception...");
                throw new Exception("App no loaded!");
            }

            binding = ActivityHomeBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());
            setTitle(R.string.activityTitle_main);

            loadMainButtons();
            loadWidgetsButtons();
            loadUpdateChecker();
            disablePowerSaver();
        } catch (Exception exception) {
            LOGGER.errorDescription("Error for loading homeActivity");
            LOGGER.error(exception);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.ERROR);
            builder.setMessage(getString(R.string.ERROR_APP_STARTING).replace("%ERROR_MESSAGE%", exception.toString()));
            builder.show();
        }

        LOGGER.done();
    }

    @Override
    protected void onResume() {
        super.onResume();
        final Logger LOGGER = new Logger();

        if (SettingsActivity.restartRequired) {
            LOGGER.log("Restarting...");
            startActivity(new Intent(this, MainActivity.class));
            finish();
            LOGGER.returned();
            return;
        }

        LOGGER.info("firstOnResumeSkipFlag: " + firstOnResumeSkipFlag);
        if (firstOnResumeSkipFlag) loadWidgetsButtons();
        firstOnResumeSkipFlag = true;
        LOGGER.done();
    }

    private void loadMainButtons() {
        final Logger LOGGER = new Logger();
        binding.mainButtonAbout.setOnClickListener(v -> startActivity(new Intent(this, AboutActivity.class)));
        binding.mainButtonSettings.setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class)));
        LOGGER.done();
    }

    private void loadWidgetsButtons() {
        final Logger LOGGER = new Logger();

        binding.widgetsButtonsSlot.setVisibility(View.GONE);
        binding.mainDateWidgetsButtonsSlotTitle.setVisibility(View.GONE);
        binding.mainDateWidgetsButtonsSlot.setVisibility(View.GONE);
        binding.mainDateWidgetsButtonsSlot.removeAllViews();

        WidgetRegistry widgetRegistry = WidgetRegistry.getWidgetRegistry();

        int widgetCount = widgetRegistry == null ? 0 : widgetRegistry.getWidgets().size();
        boolean isWidgetsAvailable = widgetCount > 0;

        LOGGER.log("isWidgetsAvailable: " + isWidgetsAvailable + ", count: " + widgetCount);

        // Date Widgets
        if (isWidgetsAvailable) {
            for (BaseWidget widget : widgetRegistry.getWidgets()) {
                LinearLayout widgetButton = new LinearLayout(this);
                widgetButton.setOrientation(LinearLayout.HORIZONTAL);
                widgetButton.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                widgetButton.setPadding(0, 0, 0, 0);

                Button button = new Button(this);
                button.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 10));
                button.setAllCaps(false);

                // noinspection uncheck
                final DateWidget dateWidget = (widget instanceof DateWidget) ? (DateWidget) widget : null;

                button.setText(MessageFormat.format("{0} ({1})",
                    getString(dateWidget != null ? R.string.widgetName_date : R.string.widget_name_network),
                    widget.getWidgetId()));

                widgetButton.addView(button);

                if (dateWidget != null) {
                    button.setOnClickListener(v -> startActivity(new Intent(this, DateWidgetConfiguratorActivity.class).putExtra("widgetId", dateWidget.getWidgetId())));
                    button.setOnLongClickListener(v -> {
                        PopupMenu popupMenu = new PopupMenu(this, button);
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT)
                            popupMenu = new PopupMenu(this, button, Gravity.END);

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

                    if (widget.getFlags().contains(WidgetFlag.CONVERTED_FROM_FORMAT_VERSION_2)) {
                        Button warningButton = new Button(this);
                        warningButton.setText("!");
                        warningButton.setTextColor(Color.parseColor("#F41C00"));
                        warningButton.setTextSize(20);
                        warningButton.setOnClickListener(v -> DialogUtils.notifyDialog(this, getString(R.string.widgetConverter_fromV2_title), getString(R.string.widgetConverter_fromV2_message)));
                        widgetButton.addView(warningButton);
                    }
                }
                binding.mainDateWidgetsButtonsSlot.addView(widgetButton);
            }

            binding.mainDateWidgetsButtonsSlotTitle.setVisibility(View.VISIBLE);
            binding.mainDateWidgetsButtonsSlot.setVisibility(View.VISIBLE);
        }

        if (isWidgetsAvailable) {
            binding.mainTextWidgetsIsNone.setVisibility(View.GONE);
            binding.mainCheckBoxWidgetsIdMode.setVisibility(View.VISIBLE);
            binding.mainCheckBoxWidgetsIdMode.setChecked(SettingsData.getSettingsData().isViewIdInWidgets());
            binding.mainCheckBoxWidgetsIdMode.setOnClickListener(v -> SettingsData.getSettingsData().setViewIdInWidgets(binding.mainCheckBoxWidgetsIdMode.isChecked()));
            binding.widgetsButtonsSlot.setVisibility(View.VISIBLE);
        } else {
            binding.mainTextWidgetsIsNone.setVisibility(View.VISIBLE);
            binding.mainCheckBoxWidgetsIdMode.setVisibility(View.GONE);
            binding.widgetsButtonsSlot.setVisibility(View.GONE);
        }

        LOGGER.done();
    }

    private void loadUpdateChecker() {
        final Logger LOGGER = new Logger();

        binding.mainUpdateCheckerBackground.setVisibility(View.GONE);
        binding.mainUpdateCheckerButtonToSite.setVisibility(View.GONE);
        binding.mainUpdateCheckerButtonChangeLog.setVisibility(View.GONE);
        binding.mainUpdateCheckerButtonDownload.setVisibility(View.GONE);

        UpdateChecker.getVersion((status, latestRelease, exception) ->  {
            final Logger VERSION_LOGGER = new Logger();
            boolean updateCheckerVisible = true;
            boolean isButtonChangeLog = false;
            boolean isButtonDownload = false;
            CharSequence text = null;

            VERSION_LOGGER.log("Parsing status... status: " + status);
            if (status == UpdateChecker.Status.FORMAT_VERSION_NOT_SUPPORTED) {
                text = getText(R.string.updateChecker_text_FORMAT_VERSION_NOT_SUPPORTED);
            } else if (status == UpdateChecker.Status.VERSION_LATEST) {
                updateCheckerVisible = false;
            } else if (status == UpdateChecker.Status.VERSION_OUTDATED) {
                text = getText(R.string.updateChecker_text_VERSION_OUTDATED);
                isButtonChangeLog = true;
                isButtonDownload = true;
            } else if (status == UpdateChecker.Status.VERSION_NOT_RELEASE) {
                text = getText(R.string.updateChecker_text_VERSION_NOT_RELEASE);
                isButtonChangeLog = true;
                isButtonDownload = true;
            } else if (status == UpdateChecker.Status.NO_NETWORK_CONNECTION) {
                updateCheckerVisible = false;
                runOnUiThread(() -> {
                    Snackbar snackbar = Snackbar.make(binding.getRoot(), R.string.updateChecker_text_NO_NETWORK_CONNECTION, 4000);
                    snackbar.setAction(R.string.OK, v -> {});
                    snackbar.show();
                });
            } else {
                text = getString(R.string.updateChecker_text_ERROR)
                        .replace("%ERROR_CODE%", status.toString())
                        .replace("%ERROR_MESSAGE%", exception.toString());
            }

            VERSION_LOGGER.log("Status parsed.");
            VERSION_LOGGER.info("updateCheckerVisible: " + updateCheckerVisible);
            VERSION_LOGGER.info("isButtonChangeLog: " + isButtonChangeLog);
            VERSION_LOGGER.info("isButtonDownload: " + isButtonDownload);
            VERSION_LOGGER.info("text: " + text);

            final boolean finalIsButtonChangeLog = isButtonChangeLog;
            final boolean finalIsButtonDownload = isButtonDownload;
            final CharSequence finalText = text;
            final boolean finalUpdateCheckerVisible = updateCheckerVisible;
            runOnUiThread(() -> {
                binding.mainUpdateCheckerBackground.setVisibility(Utils.booleanToVisible(finalUpdateCheckerVisible, View.GONE));
                binding.mainUpdateCheckerText.setText(finalText);

                binding.mainUpdateCheckerButtonToSite.setVisibility(View.VISIBLE);
                binding.mainUpdateCheckerButtonToSite.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(UpdateChecker.APP_SITE_URL))));

                if (finalIsButtonChangeLog && latestRelease.getChangeLog() != null) {
                    binding.mainUpdateCheckerButtonChangeLog.setVisibility(View.VISIBLE);
                    binding.mainUpdateCheckerButtonChangeLog.setOnClickListener(v -> DialogUtils.notifyDialog(this, getString(R.string.updateChecker_button_changeLog), latestRelease.getChangeLog(SettingsData.getSettingsData().getLanguage())));
                }

                if (finalIsButtonDownload) {
                    binding.mainUpdateCheckerButtonDownload.setVisibility(View.VISIBLE);
                    binding.mainUpdateCheckerButtonDownload.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(latestRelease.getDownloadUrl()))));
                }
            });
        });

        LOGGER.done();
    }

    @SuppressLint("BatteryLife")
    private void disablePowerSaver() {
        final Logger LOGGER = new Logger();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);

            if (!powerManager.isIgnoringBatteryOptimizations(getPackageName())) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivity(intent);
                LOGGER.log("Dialog sent");
            } else {
                LOGGER.log("Before disabled");
            }
        } else {
            LOGGER.log("Android version not supported. Minimal supported: " + Build.VERSION_CODES.M);
        }

        LOGGER.done();
    }
}
