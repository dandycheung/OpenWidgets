package ru.fazziclay.openwidgets.data.settings;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.util.Locale;

import ru.fazziclay.fazziclaylibs.FileUtils;
import ru.fazziclay.openwidgets.data.Paths;

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
                '}';
    }

    public static SettingsData getSettingsData() {
        return settingsData;
    }

    public static void load() {
        if (settingsData == null) {
            Gson gson = new Gson();
            SettingsData temp = gson.fromJson(FileUtils.read(Paths.appFilePath + "/" + SETTINGS_FILE, "{}"), SettingsData.class);

            if (temp.formatVersion < SETTINGS_FORMAT_VERSION) {
                SettingsConverter.convert();
                settingsData = gson.fromJson(FileUtils.read(Paths.appFilePath + "/" + SETTINGS_FILE, "{}"), SettingsData.class);
            } else {
                settingsData = temp;
            }

            save();
        }
    }

    public static void save() {
        if (settingsData != null) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            FileUtils.write(Paths.appFilePath + "/" + SETTINGS_FILE, gson.toJson(settingsData));
        }
    }
}
