package ru.fazziclay.openwidgets.update.checker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import ru.fazziclay.fazziclaylibs.InternetUtils;
import ru.fazziclay.fazziclaylibs.JSONUtils;
import ru.fazziclay.openwidgets.Logger;


public class UpdateChecker {
    public static int APP_BUILD = 6;
    public static int APP_UPDATE_CHECKER_FORMAT_VERSION = 4;
    public static String APP_UPDATE_CHECKER_URL = "https://raw.githubusercontent.com/FazziCLAY/OpenWidgets/master/app_versions_test.json";
    public static final boolean LOCAL_APP_VERSIONS = false;
    public static final String APP_SITE_URL = "https://github.com/fazziclay/openwidgets/releases";

    static Version lastRelease = null;

    static String parsed = "{}";
    public static JSONObject parsedJSON = null;
    static Status status = Status.VARIABLE_NOT_SET;
    static int lastUpdateCheckerFormatVersion = -1000;
    static boolean isLastUpdateCheckerFormatVersionSupported = true;


    private static void parse() throws IOException {
        if (!LOCAL_APP_VERSIONS) {
            parsed = InternetUtils.parseTextPage(APP_UPDATE_CHECKER_URL);
        }
    }

    private static void parseJSON() throws JSONException {
        isLastUpdateCheckerFormatVersionSupported = true;
        parsedJSON = new JSONObject(parsed);
        lastUpdateCheckerFormatVersion = (int) JSONUtils.get(parsedJSON, "format_version", -1000);

        if (lastUpdateCheckerFormatVersion == 1) { // migrate to 4 format version
            lastRelease = new Version(APP_BUILD, null, null, null);

        } else if (lastUpdateCheckerFormatVersion == APP_UPDATE_CHECKER_FORMAT_VERSION) {
            lastRelease = Version.fromJSON(parsedJSON.getJSONObject("latest_release"));

        } else {
            isLastUpdateCheckerFormatVersionSupported = false;
        }
    }

    public static void getVersion(UpdateCheckerInterface versionInterface) {
        final Logger LOGGER = new Logger(UpdateChecker.class, "getVersion");
        Thread updateCheckerThread = new Thread(() -> {
            try {
                parse();
                parseJSON();
                LOGGER.log("Parsed successful");

            } catch (IOException | JSONException exception) {
                status = Status.PARSING_ERROR;
                LOGGER.error("Parsing error.");
                LOGGER.exception(exception);
                LOGGER.log("Sent this error to versionInterface; status=" + status);
                versionInterface.run(status, lastRelease, exception);
                return;
            }

            if (!isLastUpdateCheckerFormatVersionSupported) {
                status = Status.FORMAT_VERSION_NOT_SUPPORTED;

            } else if (APP_BUILD > lastRelease.getBuild()) {
                status = Status.VERSION_NOT_RELEASE;

            } else if (APP_BUILD == lastRelease.getBuild()) {
                status = Status.VERSION_LATEST;

            } else if (APP_BUILD < lastRelease.getBuild()) {
                status = Status.VERSION_OUTDATED;

            }

            LOGGER.log("run interface: status=" + status);
            versionInterface.run(status, lastRelease, null);
        });

        LOGGER.log("started updateCheckerThread, updateCheckerThread.getName()="+updateCheckerThread.getName());
        updateCheckerThread.start();
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
        PARSING_ERROR
    }
}