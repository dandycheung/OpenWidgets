package ru.fazziclay.openwidgets.update.checker;

import androidx.annotation.NonNull;

import java.util.HashMap;

public class Version {
    int build;
    String name;
    HashMap<String, String> changelog;
    String downloadUrl;

    public int getBuild() {
        return build;
    }

    public String getName() {
        return name;
    }

    public String getChangeLog(String language) {
        if (changelog == null)
            return null;

        if (changelog.containsKey(language))
            return changelog.get(language);

        return changelog.get("default");
    }

    public HashMap<String, String> getChangeLog() {
        return changelog;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    @NonNull
    @Override
    public String toString() {
        return "Version{" +
                "build=" + build +
                ", name='" + name + '\'' +
                ", changelog=(***" + changelog + "***)" +
                ", downloadUrl='" + downloadUrl + '\'' +
                '}';
    }
}
