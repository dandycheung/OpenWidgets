package ru.fazziclay.openwidgets;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import org.json.JSONObject;

import ru.fazziclay.openwidgets.activity.MainActivity;
import ru.fazziclay.openwidgets.cogs.AppVersionInterface;
import ru.fazziclay.openwidgets.cogs.Utils;


public class UpdateChecker {
    public static int appBuild = 4;
    public static int appUpdateCheckerFormatVersion = 1;
    public static String appVersionsUrl = "https://raw.githubusercontent.com/FazziCLAY/OpenWidgets/master/app_versions.json";

    public static void getVersion(AppVersionInterface versionInterface) {
        new Thread(() -> {
            try {
                //JSONObject a = new JSONObject(Utils.getPage(appVersionsUrl));

                JSONObject a = new JSONObject();
                a.put("format_version", 1);
                JSONObject last1 = new JSONObject();
                last1.put("build", 5);
                last1.put("name", "TEST NAME");
                last1.put("download_url", "https://fazziclay.ru/");
                a.put("last", last1);


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
                // 3 - Ошибка
                byte status = 0;
                if (formatVersion != appUpdateCheckerFormatVersion) {
                    status = 2;
                } else if (lastBuild > appBuild) {
                    status = 1;
                } else if (lastBuild < appBuild) {
                    status = -1;
                }

                versionInterface.onRun(status, lastBuild, lastName, lastDownloadUrl);
            } catch (Exception e) {
                versionInterface.onRun((byte) 3, 0, null, null);
            }
        }).start();
    }

    public static void sendNewUpdateAvailableNotification(Context context) {
        UpdateChecker.getVersion((status, build, name, downloadUrl) -> {
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

            if (status == 0) {
                if (Utils.isNotifyShowed(context, 200)) notificationManager.cancel(200);
                if (Utils.isNotifyShowed(context, 201)) notificationManager.cancel(201);
                if (Utils.isNotifyShowed(context, 202)) notificationManager.cancel(202);

            } else {
                PendingIntent openAppIntent = PendingIntent.getActivity(context, 1, new Intent(context, MainActivity.class), 0);
                PendingIntent toSiteIntent = PendingIntent.getActivity(context, 1, new Intent(Intent.ACTION_VIEW, Uri.parse("http://github.com/fazziclay/openwidgets/releases")), 0);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "UpdateChecker")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setContentIntent(openAppIntent);

                if (status == 1 && !Utils.isNotifyShowed(context, 200)) {
                    PendingIntent downloadIntent = PendingIntent.getActivity(context, 1, new Intent(Intent.ACTION_VIEW, Uri.parse(downloadUrl)), 0);
                    builder = builder
                            .setContentText(context.getText(R.string.updateChecker_updateAvailable))
                            .addAction(R.mipmap.ic_launcher, context.getString(R.string.updateChecker_button_toSite), toSiteIntent)
                            .addAction(R.mipmap.ic_launcher, context.getString(R.string.updateChecker_button_download), downloadIntent);

                    notificationManager.notify(200, builder.build());
                }

                if (status == 2 && !Utils.isNotifyShowed(context, 201)) {
                    builder = builder
                            .setContentText(context.getText(R.string.updateChecker_newFormatVersion))
                            .addAction(R.mipmap.ic_launcher, context.getString(R.string.updateChecker_button_toSite), toSiteIntent);

                    notificationManager.notify(201, builder.build());
                }

                if (status == 2 && !Utils.isNotifyShowed(context, 201)) {
                    builder = builder
                            .setContentText(context.getText(R.string.updateChecker_newFormatVersion))
                            .addAction(R.mipmap.ic_launcher, context.getString(R.string.updateChecker_button_toSite), toSiteIntent);

                    notificationManager.notify(201, builder.build());
                }

                if (status == -1 && !Utils.isNotifyShowed(context, 202)) {
                    PendingIntent downloadIntent = PendingIntent.getActivity(context, 1, new Intent(Intent.ACTION_VIEW, Uri.parse(downloadUrl)), 0);
                    builder = builder
                            .setContentText(context.getText(R.string.updateChecker_versionHight))
                            .addAction(R.mipmap.ic_launcher, context.getString(R.string.updateChecker_button_toSite), toSiteIntent)
                            .addAction(R.mipmap.ic_launcher, context.getString(R.string.updateChecker_button_download), downloadIntent);

                    notificationManager.notify(202, builder.build());
                }
            }
        });
    }
}