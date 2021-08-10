package ru.fazziclay.openwidgets.data.settings;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.Locale;

import ru.fazziclay.fazziclaylibs.FileUtils;
import ru.fazziclay.fazziclaylibs.NumberUtils;
import ru.fazziclay.openwidgets.Logger;
import ru.fazziclay.openwidgets.data.Paths;
import ru.fazziclay.openwidgets.util.JsonUtils;

public class SettingsData {
    public static final String SETTINGS_FILE = "settings.json";
    public static final int SETTINGS_FORMAT_VERSION = 1;
    private static SettingsData settingsData = null;


    @SerializedName("version")
    int formatVersion = SETTINGS_FORMAT_VERSION;
    String language = Locale.getDefault().getLanguage();
    boolean logger = false;
    boolean isDebugAllow = false;
    boolean viewIdInWidgets = false;
    int widgetsUpdateDelayMillis = 1000;
    boolean isStopWidgetsUpdaterAfterScreenOff = false;
    boolean isStartWidgetsUpdaterAfterScreenOn = true;
    int instanceId = -1;


    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public boolean isLogger() {
        return logger;
    }

    public void setLogger(boolean logger) {
        this.logger = logger;
    }

    public boolean isDebugAllow() {
        return isDebugAllow;
    }

    public void setDebugAllow(boolean debugAllow) {
        isDebugAllow = debugAllow;
    }

    public boolean isViewIdInWidgets() {
        return viewIdInWidgets;
    }

    public void setViewIdInWidgets(boolean viewIdInWidgets) {
        this.viewIdInWidgets = viewIdInWidgets;
    }

    public int getWidgetsUpdateDelayMillis() {
        return widgetsUpdateDelayMillis;
    }

    public void setWidgetsUpdateDelayMillis(int widgetsUpdateDelayMillis) {
        this.widgetsUpdateDelayMillis = widgetsUpdateDelayMillis;
    }

    public boolean isStopWidgetsUpdaterAfterScreenOff() {
        return isStopWidgetsUpdaterAfterScreenOff;
    }

    public void setStopWidgetsUpdaterAfterScreenOff(boolean stopWidgetsUpdaterAfterScreenOff) {
        isStopWidgetsUpdaterAfterScreenOff = stopWidgetsUpdaterAfterScreenOff;
    }

    public boolean isStartWidgetsUpdaterAfterScreenOn() {
        return isStartWidgetsUpdaterAfterScreenOn;
    }

    public void setStartWidgetsUpdaterAfterScreenOn(boolean startWidgetsUpdaterAfterScreenOn) {
        isStartWidgetsUpdaterAfterScreenOn = startWidgetsUpdaterAfterScreenOn;
    }

    public int getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(int instanceId) {
        this.instanceId = instanceId;
    }

    @NonNull
    @Override
    public String toString() {
        return "SettingsData{" +
                "formatVersion=" + formatVersion +
                ", language='" + language + '\'' +
                ", logger=" + logger +
                ", isDebugAllow=" + isDebugAllow +
                ", viewIdInWidgets=" + viewIdInWidgets +
                ", widgetsUpdateDelayMillis=" + widgetsUpdateDelayMillis +
                ", isStopWidgetsUpdaterAfterScreenOff=" + isStopWidgetsUpdaterAfterScreenOff +
                ", isStartWidgetsUpdaterAfterScreenOn=" + isStartWidgetsUpdaterAfterScreenOn +
                '}';
    }

    public static SettingsData getSettingsData() {
        return settingsData;
    }

    public static void load() {
        final Logger LOGGER = new Logger(SettingsData.class, "load");

        if (settingsData == null) {
            if (Paths.getAppFilePath() == null) {
                LOGGER.error("Paths.appFilePath == null. returned");
                return;
            }
            String fileContent = FileUtils.read((Paths.getAppFilePath() + SETTINGS_FILE), JsonUtils.EMPTY_JSON_CONTENT);
            SettingsData temp = JsonUtils.fromJson(fileContent, SettingsData.class);

            if (temp.formatVersion < SETTINGS_FORMAT_VERSION) {
                SettingsConverter.convert();
                settingsData = JsonUtils.fromJson(fileContent, SettingsData.class);
            } else {
                settingsData = temp;
            }

            if (settingsData.getInstanceId() == -1) settingsData.setInstanceId(NumberUtils.getRandom(0, 999999));

            save();
        }
    }

    public static void save() {
        final Logger LOGGER = new Logger(SettingsData.class, "save");

        if (settingsData != null) {
            if (Paths.getAppFilePath() == null) {
                LOGGER.error("Paths.appFilePath == null. returned");
                return;
            }
            FileUtils.write((Paths.getAppFilePath() + SETTINGS_FILE), JsonUtils.toJson(settingsData));
        }
    }
}
