package ru.fazziclay.openwidgets.update.checker;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.net.UnknownHostException;

import ru.fazziclay.openwidgets.AppConfig;
import ru.fazziclay.openwidgets.Logger;
import ru.fazziclay.openwidgets.util.InternetUtils;

public class UpdateChecker {
    public static final int APP_BUILD = 12;
    public static final int APP_UPDATE_CHECKER_FORMAT_VERSION = 4;

    public static final String APP_UPDATE_CHECKER_URL = AppConfig.APP_UPDATE_CHECKER_URL;
    public static final String APP_SITE_URL = AppConfig.APP_SITE_URL;

    public static final int TRAFFIC_ECONOMY_MODE_DELAY = 24*60*60;

    public static long latestUpdate = 0;
    public static UpdateChecker updateChecker = null;
    public static Status status = Status.VARIABLE_NOT_SET;

    @SerializedName("version")
    int formatVersion = -1;
    Version latestRelease = null;

    @NonNull
    @Override
    public String toString() {
        return "UpdateChecker{" +
                "formatVersion=" + formatVersion +
                ", latestRelease=" + latestRelease +
                '}';
    }

    public static void getVersion(UpdateCheckerInterface versionInterface) {
        final Logger LOGGER = new Logger();

        Thread updateCheckerThread = new Thread(() -> {
            final Logger THREAD_LOGGER = new Logger();
            THREAD_LOGGER.log("Logger in updateCheckerThread");
            try {
                Gson gson = new Gson();
                updateChecker = gson.fromJson(InternetUtils.parseTextPage(APP_UPDATE_CHECKER_URL), UpdateChecker.class);
            } catch (UnknownHostException e) {
                versionInterface.run(Status.NO_NETWORK_CONNECTION, null, e);
                return;

            } catch (IOException exception) {
                versionInterface.run(Status.PARSING_ERROR, null, exception);
                return;
            }

            status = Status.VARIABLE_NOT_SET;
            if (updateChecker.formatVersion != APP_UPDATE_CHECKER_FORMAT_VERSION) {
                status = Status.FORMAT_VERSION_NOT_SUPPORTED;
            } else if (APP_BUILD > updateChecker.latestRelease.getBuild()) {
                status = Status.VERSION_NOT_RELEASE;
            } else if (APP_BUILD == updateChecker.latestRelease.getBuild()) {
                status = Status.VERSION_LATEST;
            } else if (APP_BUILD < updateChecker.latestRelease.getBuild()) {
                status = Status.VERSION_OUTDATED;
            }

            latestUpdate = System.currentTimeMillis() / 1000;
            THREAD_LOGGER.log("run interface: status=" + status);
            versionInterface.run(status, updateChecker.latestRelease, null);
        });

        if (latestUpdate < (System.currentTimeMillis()/1000)-TRAFFIC_ECONOMY_MODE_DELAY) {
            LOGGER.log("Started updateCheckerThread, name: " + updateCheckerThread.getName());
            updateCheckerThread.start();
        } else {
            LOGGER.log("Traffic economy mode. latestUpdate=" + latestUpdate + ", current/1000=" + System.currentTimeMillis()/1000);
            LOGGER.log("Run interface: status=" + status);
            versionInterface.run(status, updateChecker.latestRelease, null);
        }

        LOGGER.done();
    }

    public interface UpdateCheckerInterface {
        void run(Status status, Version latestRelease, Exception exception);
    }

    public enum Status {
        VARIABLE_NOT_SET,
        FORMAT_VERSION_NOT_SUPPORTED,
        VERSION_OUTDATED,
        VERSION_LATEST,
        VERSION_NOT_RELEASE,
        PARSING_ERROR,
        NO_NETWORK_CONNECTION
    }
}
