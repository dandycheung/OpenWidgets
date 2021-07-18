package ru.fazziclay.openwidgets.data.settings;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
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

    @NonNull
    @Override
    public String toString() {
        return "SettingsData{" +
                "formatVersion=" + formatVersion +
                ", language='" + language + '\'' +
                ", logger=" + logger +
                '}';
    }

    public static SettingsData getSettingsData() {
        return settingsData;
    }

    public static void load() {
        if (settingsData == null) {
            String path = Paths.appFilePath + "/" + SETTINGS_FILE;
            if (FileUtils.read(path).equals("")) {
                FileUtils.write(path, "{}");
            }

            Gson gson = new Gson();
            SettingsData temp = gson.fromJson(FileUtils.read(path), SettingsData.class);

            if (temp.formatVersion < SETTINGS_FORMAT_VERSION) {
                SettingsConverter.convert();
                settingsData = gson.fromJson(FileUtils.read(path), SettingsData.class);
            } else {
                settingsData = temp;
            }

            save();
        }
    }

    public static void save() {
        if (settingsData != null) {
            Gson gson = new Gson();
            FileUtils.write(Paths.appFilePath + "/" + SETTINGS_FILE, gson.toJson(settingsData));
        }
    }
}
