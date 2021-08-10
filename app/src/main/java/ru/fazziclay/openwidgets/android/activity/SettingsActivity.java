package ru.fazziclay.openwidgets.android.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import java.net.UnknownHostException;

import ru.fazziclay.fazziclaylibs.FileUtils;
import ru.fazziclay.fazziclaylibs.InternetUtils;
import ru.fazziclay.openwidgets.Logger;
import ru.fazziclay.openwidgets.R;
import ru.fazziclay.openwidgets.data.Paths;
import ru.fazziclay.openwidgets.data.settings.SettingsData;
import ru.fazziclay.openwidgets.net.Client;
import ru.fazziclay.openwidgets.net.Packets;
import ru.fazziclay.openwidgets.util.DialogUtils;
import ru.fazziclay.openwidgets.util.Utils;

import static ru.fazziclay.openwidgets.ErrorDetectorWrapper.errorDetectorWrapper;

public class SettingsActivity extends AppCompatActivity {
    public static final String[][] SUPPORTED_LANGUAGES = {{"Russian", "ru"}, {"English", "en"}};
    public static final boolean FORCED_DEBUG_ALLOW = false;
    public static final String HOST_URL = "https://raw.githubusercontent.com/FazziCLAY/OpenWidgets/dev/1.4.2/server.host";
    public static boolean restartRequired = false;


    Button toDebugButton;

    Button appLanguageButton;

    Button settings_button_widgetsUpdateDelayMillis;

    CheckBox settings_checkBox_isStopWidgetsUpdaterAfterScreenOff;
    CheckBox settings_checkBox_isStartWidgetsUpdaterAfterScreenOn;

    CheckBox settings_checkBox_logger;
    Button settings_button_logger_clear;
    Button settings_button_logger_upload;
    Button settings_button_logger_share;

    private void loadVariables() {
        toDebugButton = findViewById(R.id.button_toDebug);
        appLanguageButton = findViewById(R.id.button_appLanguage);
        settings_button_widgetsUpdateDelayMillis = findViewById(R.id.settings_button_widgetsUpdateDelayMillis);
        settings_checkBox_isStopWidgetsUpdaterAfterScreenOff = findViewById(R.id.settings_checkBox_isStopWidgetsUpdaterAfterScreenOff);
        settings_checkBox_isStartWidgetsUpdaterAfterScreenOn = findViewById(R.id.settings_checkBox_isStartWidgetsUpdaterAfterScreenOn);
        settings_checkBox_logger = findViewById(R.id.settings_checkBox_logger);
        settings_button_logger_clear = findViewById(R.id.settings_button_logger_clear);
        settings_button_logger_upload = findViewById(R.id.settings_button_logger_upload);
        settings_button_logger_share = findViewById(R.id.settings_button_logger_share);
    }

    private void loadMainButtons() {
        SettingsData settingsData = SettingsData.getSettingsData();

        // Debug
        toDebugButton.setOnClickListener(v -> startActivity(new Intent(this, DebugActivity.class)));

        // Language
        appLanguageButton.setOnClickListener(v -> {
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
        settings_button_widgetsUpdateDelayMillis.setOnClickListener(v -> DialogUtils.inputDialog(this,
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
        settings_checkBox_isStopWidgetsUpdaterAfterScreenOff.setOnClickListener(v -> {
            settingsData.setStopWidgetsUpdaterAfterScreenOff(settings_checkBox_isStopWidgetsUpdaterAfterScreenOff.isChecked());

            settings_checkBox_isStartWidgetsUpdaterAfterScreenOn.setChecked(settings_checkBox_isStopWidgetsUpdaterAfterScreenOff.isChecked() || settingsData.isStartWidgetsUpdaterAfterScreenOn());
            settings_checkBox_isStartWidgetsUpdaterAfterScreenOn.setEnabled(!settings_checkBox_isStopWidgetsUpdaterAfterScreenOff.isChecked());
        });

        settings_checkBox_isStartWidgetsUpdaterAfterScreenOn.setOnClickListener(v -> settingsData.setStartWidgetsUpdaterAfterScreenOn(settings_checkBox_isStartWidgetsUpdaterAfterScreenOn.isChecked()));

        // Debug Logging
        settings_button_logger_clear.setOnClickListener(v -> DialogUtils.warningDialog(this,
                getString(R.string.settings_logger_clear_title),
                getString(R.string.settings_logger_clear_message),
                0,
                () -> {
                    final Logger LOGGER1 = new Logger(SettingsActivity.class, this.getClass(), "run");
                    LOGGER1.clear();
                    a();
                }));


        Context finalContext = this;
        settings_button_logger_upload.setOnClickListener(v -> DialogUtils.warningDialog(this,
                getString(R.string.settings_logger_upload_title),
                getString(R.string.settings_logger_upload_message),
                0,
                () -> {
                    final Logger LOGGER1 = new Logger(SettingsActivity.class, this.getClass(), "run");

                    Thread thread = new Thread(() -> {
                        String[] host;
                        try {
                            host = InternetUtils.parseTextPage(HOST_URL).split(":");
                        } catch (UnknownHostException exception) {
                            runOnUiThread(() -> DialogUtils.notifyDialog(finalContext,
                                    getString(R.string.updateChecker_text_NO_NETWORK_CONNECTION),
                                    (String) null
                            ));
                            return;
                        } catch (Exception exception) {
                            runOnUiThread(() -> DialogUtils.notifyDialog(finalContext,
                                    getString(R.string.ERROR),
                                    getString(R.string.ERROR_SEND_TO_DEVELOPERS) + "\n\n" + exception.toString()
                            ));
                            return;
                        }

                        new Client(host[0], Integer.parseInt(host[1])) {
                            boolean success = false;

                            @Override
                            public void onConnected() {
                                final Logger LOGGER1 = new Logger(SettingsActivity.class, this.getClass(), "onConnected");
                                send(Packets.PACKET_HANDSHAKE, String.valueOf(settingsData.getInstanceId()));
                                send(Packets.PACKET_LOGS_UPLOADING, FileUtils.read(Paths.getAppFilePath() + Logger.LOG_FILE));
                            }

                            @Override
                            public void onDisconnected(String reason) {
                                final Logger LOGGER1 = new Logger(SettingsActivity.class, this.getClass(), "onDisconnected");
                                LOGGER1.log("Disconnected reason: " + reason);
                                if (!success) {
                                    runOnUiThread(() -> DialogUtils.notifyDialog(finalContext,
                                            getString(R.string.ERROR),
                                            getString(R.string.ERROR_SEND_TO_DEVELOPERS) + "\n\nDisconnected: " + reason
                                    ));
                                }
                            }

                            @Override
                            public void onPacketReceive(int packetId, String data) {
                                final Logger LOGGER1 = new Logger(SettingsActivity.class, this.getClass(), "onPacketReceive");
                                LOGGER1.log("packetId=" + packetId);
                                LOGGER1.log("data=" + data);

                                if (packetId == Packets.PACKET_LOGS_UPLOADING_SUCCESSES) {
                                    success = true;
                                    runOnUiThread(() -> DialogUtils.notifyDialog(finalContext,
                                            getString(R.string.settings_logger_upload_title),
                                            getString(R.string.SECCUses)
                                    ));
                                }

                                if (packetId == Packets.PACKET_LOGS_UPLOADING_ERROR) {
                                    runOnUiThread(() -> DialogUtils.notifyDialog(finalContext,
                                            getString(R.string.ERROR),
                                            getString(R.string.ERROR_SEND_TO_DEVELOPERS) + "\n\nPACKET_LOGS_UPLOADING_ERROR: " + data
                                    ));
                                }

                                if (packetId == Packets.PACKET_LOGS_CLEARING_REQUEST) {
                                    runOnUiThread(() -> DialogUtils.inputDialog(finalContext,
                                            getString(R.string.settings_logger_clearing_request_title),
                                            getString(R.string.settings_logger_clearing_request_message) + "\n\n" + data,
                                            () -> {
                                                LOGGER1.clear();
                                                a();
                                            },
                                            new Button[]{}
                                    ));
                                }
                            }

                            @Override
                            public void onError(Exception exception) {
                                final Logger LOGGER1 = new Logger(SettingsActivity.class, this.getClass(), "onError");
                                LOGGER1.log("Error: " + exception.toString());

                                runOnUiThread(() -> DialogUtils.notifyDialog(finalContext,
                                        getString(R.string.ERROR),
                                        getString(R.string.ERROR_SEND_TO_DEVELOPERS) + "\n\n" + exception.getMessage())
                                );
                            }
                        };
                    });
                    thread.start();
                }));

        settings_button_logger_share.setOnClickListener(v -> {
            /*final Logger LOGGER1 = */
            new Logger(SettingsActivity.class, this.getClass(), "run");
            Utils.shareText(this, getString(R.string.settings_logger_share_title), FileUtils.read(Paths.getAppFilePath() + Logger.LOG_FILE));
        });

        settings_checkBox_logger.setOnClickListener(v -> {
            settingsData.setLogger(settings_checkBox_logger.isChecked());
            a();
        });
    }

    private void a() { // TODO: 09.08.2021 rename method
        SettingsData settingsData = SettingsData.getSettingsData();

        settings_checkBox_isStopWidgetsUpdaterAfterScreenOff.setChecked(settingsData.isStopWidgetsUpdaterAfterScreenOff());
        settings_checkBox_isStartWidgetsUpdaterAfterScreenOn.setChecked(settings_checkBox_isStopWidgetsUpdaterAfterScreenOff.isChecked() || settingsData.isStartWidgetsUpdaterAfterScreenOn());
        settings_checkBox_isStartWidgetsUpdaterAfterScreenOn.setEnabled(!settings_checkBox_isStopWidgetsUpdaterAfterScreenOff.isChecked());

        boolean isLoggerChecked = Logger.FORCED_LOGGING | settingsData.isLogger();
        toDebugButton.setVisibility(Utils.booleanToVisible(settingsData.isDebugAllow() || FORCED_DEBUG_ALLOW, View.GONE));
        settings_checkBox_logger.setChecked(isLoggerChecked);
        settings_checkBox_logger.setEnabled(!Logger.FORCED_LOGGING);
        settings_button_logger_clear.setVisibility(Utils.booleanToVisible((isLoggerChecked && !Logger.isCleared()), View.INVISIBLE));
        settings_button_logger_upload.setVisibility(Utils.booleanToVisible(isLoggerChecked && !Logger.isCleared(), View.INVISIBLE));
        settings_button_logger_share.setVisibility(Utils.booleanToVisible(isLoggerChecked && !Logger.isCleared(), View.INVISIBLE));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Logger LOGGER = new Logger(SettingsActivity.class, "onCreate");
        try {
            setContentView(R.layout.activity_settings);
            setTitle(R.string.activityTitle_settings);

            LOGGER.log("settingsData before=" + SettingsData.getSettingsData());
            SettingsData.load();
            loadVariables();
            loadMainButtons();
            a();
        } catch (Exception exception) {
            LOGGER.exception(exception);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SettingsActivity.restartRequired) {
            finish();
        }
    }
}