package ru.fazziclay.openwidgets.update.checker;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

public class Version {
    int build;
    String name;
    VersionChangeLog versionChangeLog;
    String downloadUrl;

    public int getBuild() {
        return build;
    }

    public String getName() {
        return name;
    }

    public VersionChangeLog getChangeLog() {
        return versionChangeLog;
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
                ", versionChangeLog=" + versionChangeLog +
                ", downloadUrl='" + downloadUrl + '\'' +
                '}';
    }

    public Version(
            int build,
            String name,
            VersionChangeLog versionChangeLog,
            String downloadUrl
    ) {
        this.build = build;
        this.name = name;
        this.versionChangeLog = versionChangeLog;
        this.downloadUrl = downloadUrl;
    }

    public static Version fromJSON(JSONObject jsonObject) throws JSONException {
        VersionChangeLog versionChangeLog = null;
        if (!jsonObject.isNull("changelog")) {
            versionChangeLog = VersionChangeLog.fromJSON(jsonObject.getJSONObject("changelog"));
        }
        return new Version(
                jsonObject.getInt("build"),
                jsonObject.getString("name"),
                versionChangeLog,
                jsonObject.getString("download_url")
        );
    }
}
