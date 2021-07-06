package ru.fazziclay.openwidgets;

import android.content.pm.PackageInfo;

import org.json.JSONException;
import org.json.JSONObject;

import ru.fazziclay.openwidgets.cogs.AppVersionInterface;
import ru.fazziclay.openwidgets.cogs.Utils;


public class UpdateChecker {
    public static final int appBuild = 3;
    public static final int appUpdateCheckerFormatVersion = 1;

    public static void getVersion(AppVersionInterface versionInterface) {
        new Thread(() -> {
            try {
                JSONObject a = new JSONObject(Utils.getPage("https://raw.githubusercontent.com/FazziCLAY/OpenWidgets/master/app_versions.json"));
                int formatVersion = a.getInt("format_version");
                int lastBuild = 0;
                String lastName = null;
                String lastDownloadUrl = null;

                if (formatVersion == appUpdateCheckerFormatVersion) {
                    JSONObject last = a.getJSONObject("last");
                    lastBuild = last.getInt("build");
                    lastName = last.getString("name");
                    lastDownloadUrl = last.getString("download_url");
                }

                // -1 - Текущая версия выше самой последней версии
                // 0 - Текущая версия последняя.
                // 1 - Имеется обновление
                // 2 - Format Version не такой какой понимает это приложение
                byte status = 0;
                if (formatVersion != appUpdateCheckerFormatVersion) {
                    status = 2;
                } else if (lastBuild > appBuild) {
                    status = 1;
                } else if (lastBuild < appBuild) {
                    status = -1;
                }

                versionInterface.onRun(status, lastBuild, lastName, lastDownloadUrl);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }).start();
    }
}