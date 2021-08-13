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
        final Logger LOGGER = new Logger();
        LOGGER.info("old=" + this.language);
        LOGGER.info("new=" + language);
        this.language = language;
    }

    public boolean isLogger() {
        return logger;
    }

    public void setLogger(boolean logger) {
        final Logger LOGGER = new Logger();
        LOGGER.info("old=" + this.logger);
        LOGGER.info("new=" + logger);
        this.logger = logger;
    }

    public boolean isDebugAllow() {
        return isDebugAllow;
    }

    public void setDebugAllow(boolean isDebugAllow) {
        final Logger LOGGER = new Logger();
        LOGGER.info("old=" + this.isDebugAllow);
        LOGGER.info("new=" + isDebugAllow);
        this.isDebugAllow = isDebugAllow;
    }

    public boolean isViewIdInWidgets() {
        return viewIdInWidgets;
    }

    public void setViewIdInWidgets(boolean viewIdInWidgets) {
        final Logger LOGGER = new Logger();
        LOGGER.info("old=" + this.viewIdInWidgets);
        LOGGER.info("new=" + viewIdInWidgets);
        this.viewIdInWidgets = viewIdInWidgets;
    }

    public int getWidgetsUpdateDelayMillis() {
        return widgetsUpdateDelayMillis;
    }

    public void setWidgetsUpdateDelayMillis(int widgetsUpdateDelayMillis) {
        final Logger LOGGER = new Logger();
        LOGGER.info("old=" + this.widgetsUpdateDelayMillis);
        LOGGER.info("new=" + widgetsUpdateDelayMillis);
        this.widgetsUpdateDelayMillis = widgetsUpdateDelayMillis;
    }

    public boolean isStopWidgetsUpdaterAfterScreenOff() {
        return isStopWidgetsUpdaterAfterScreenOff;
    }

    public void setStopWidgetsUpdaterAfterScreenOff(boolean isStopWidgetsUpdaterAfterScreenOff) {
        final Logger LOGGER = new Logger();
        LOGGER.info("old=" + this.isStopWidgetsUpdaterAfterScreenOff);
        LOGGER.info("new=" + isStopWidgetsUpdaterAfterScreenOff);
        this.isStopWidgetsUpdaterAfterScreenOff = isStopWidgetsUpdaterAfterScreenOff;
    }

    public boolean isStartWidgetsUpdaterAfterScreenOn() {
        return isStartWidgetsUpdaterAfterScreenOn;
    }

    public void setStartWidgetsUpdaterAfterScreenOn(boolean isStartWidgetsUpdaterAfterScreenOn) {
        final Logger LOGGER = new Logger();
        LOGGER.info("old=" + this.isStartWidgetsUpdaterAfterScreenOn);
        LOGGER.info("new=" + isStartWidgetsUpdaterAfterScreenOn);
        this.isStartWidgetsUpdaterAfterScreenOn = isStartWidgetsUpdaterAfterScreenOn;
    }

    public int getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(int instanceId) {
        final Logger LOGGER = new Logger();
        LOGGER.info("old=" + this.instanceId);
        LOGGER.info("new=" + instanceId);
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
        final Logger LOGGER = new Logger();
        LOGGER.returned(settingsData);
        return settingsData;
    }

    public static void load() {
        final Logger LOGGER = new Logger();

        if (settingsData == null) {
            LOGGER.info("settingsData==null. loading...");
            if (Paths.getAppFilePath() == null) {
                LOGGER.error("Paths.appFilePath == null. returned");
                LOGGER.returned();
                return;
            }
            String filePath = Paths.getAppFilePath() + SETTINGS_FILE;
            LOGGER.info("filePath="+filePath);
            SettingsData temp = JsonUtils.fromJson(FileUtils.read(filePath, JsonUtils.EMPTY_JSON_OBJECT_CONTENT), SettingsData.class);
            LOGGER.info("Parsed: "+temp.toString());

            if (temp.formatVersion < SETTINGS_FORMAT_VERSION) {
                LOGGER.log("Converting...");
                SettingsConverter.convert();
                LOGGER.log("Converting done");
                LOGGER.info("Parsed after converting: "+temp.formatVersion);
                settingsData = JsonUtils.fromJson(FileUtils.read(filePath, JsonUtils.EMPTY_JSON_OBJECT_CONTENT), SettingsData.class);
            } else {
                settingsData = temp;
            }

            if (settingsData.getInstanceId() == -1) {
                LOGGER.log("instanceId==-1. Get new...");
                settingsData.setInstanceId(NumberUtils.getRandom(0, 999999));
                LOGGER.log("instanceId==" + settingsData.getInstanceId());
            }

            save();
        } else {
            LOGGER.info("settingsData!=null. loaded before.");
        }

        LOGGER.done();
    }

    public static void save() {
        final Logger LOGGER = new Logger();

        if (settingsData != null) {
            LOGGER.info("settingsData!=null. saving...");
            if (Paths.getAppFilePath() == null) {
                LOGGER.error("Paths.appFilePath == null. returned");
                LOGGER.returned();
                return;
            }
            FileUtils.write((Paths.getAppFilePath() + SETTINGS_FILE), JsonUtils.toJson(settingsData));
            LOGGER.log("Saved! settingsData="+settingsData.toString());
        } else {
            LOGGER.info("settingsData==null. Cannot be saved null.");
        }

        LOGGER.done();
    }
}
