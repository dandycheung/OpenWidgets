package ru.fazziclay.openwidgets.android.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
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
import ru.fazziclay.openwidgets.databinding.ActivitySettingsBinding;
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

    ActivitySettingsBinding binding;

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
                    final Logger LOGGER1 = new Logger(SettingsActivity.class, this.getClass(), "run");
                    LOGGER1.clear();
                    a();
                }));


        Context finalContext = this;
        binding.settingsButtonLoggerUpload.setOnClickListener(v -> DialogUtils.warningDialog(this,
                getString(R.string.settings_logger_upload_title),
                getString(R.string.settings_logger_upload_message).replace("%INSTANCE_ID%", String.valueOf(settingsData.getInstanceId())),
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

        binding.settingsButtonLoggerShare.setOnClickListener(v -> {
            /*final Logger LOGGER1 = */
            new Logger(SettingsActivity.class, this.getClass(), "run");
            Utils.shareText(this, getString(R.string.settings_logger_share_title), FileUtils.read(Paths.getAppFilePath() + Logger.LOG_FILE));
        });

        binding.settingsCheckBoxLogger.setOnClickListener(v -> {
            settingsData.setLogger(binding.settingsCheckBoxLogger.isChecked());
            a();
        });
    }

    private void a() { // TODO: 09.08.2021 rename method
        SettingsData settingsData = SettingsData.getSettingsData();

        binding.settingsCheckBoxIsStopWidgetsUpdaterAfterScreenOff.setChecked(settingsData.isStopWidgetsUpdaterAfterScreenOff());
        binding.settingsCheckBoxIsStartWidgetsUpdaterAfterScreenOn.setChecked(binding.settingsCheckBoxIsStopWidgetsUpdaterAfterScreenOff.isChecked() || settingsData.isStartWidgetsUpdaterAfterScreenOn());
        binding.settingsCheckBoxIsStartWidgetsUpdaterAfterScreenOn.setEnabled(!binding.settingsCheckBoxIsStopWidgetsUpdaterAfterScreenOff.isChecked());

        boolean isLoggerChecked = Logger.FORCED_LOGGING | settingsData.isLogger();
        binding.buttonToDebug.setVisibility(Utils.booleanToVisible(settingsData.isDebugAllow() || FORCED_DEBUG_ALLOW, View.GONE));
        binding.settingsCheckBoxLogger.setChecked(isLoggerChecked);
        binding.settingsCheckBoxLogger.setEnabled(!Logger.FORCED_LOGGING);
        binding.settingsButtonLoggerClear.setVisibility(Utils.booleanToVisible((isLoggerChecked && !Logger.isCleared()), View.INVISIBLE));
        binding.settingsButtonLoggerUpload.setVisibility(Utils.booleanToVisible(isLoggerChecked && !Logger.isCleared(), View.INVISIBLE));
        binding.settingsButtonLoggerShare.setVisibility(Utils.booleanToVisible(isLoggerChecked && !Logger.isCleared(), View.INVISIBLE));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Logger LOGGER = new Logger(SettingsActivity.class, "onCreate");
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
}