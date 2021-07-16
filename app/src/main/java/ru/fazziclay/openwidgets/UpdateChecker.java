package ru.fazziclay.openwidgets;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import ru.fazziclay.fazziclaylibs.InternetUtils;
import ru.fazziclay.fazziclaylibs.JSONUtils;


public class UpdateChecker {
    public static int APP_BUILD = 6;
    public static int APP_UPDATE_CHECKER_FORMAT_VERSION = 3;
    public static String APP_VERSIONS_URL = "https://raw.githubusercontent.com/FazziCLAY/OpenWidgets/master/app_versions_test.json";
    public static final boolean LOCAL_APP_VERSIONS = true;

    static ArrayList<Version> versionsHistory = null;
    static Version lastRelease = null;

    static String parsed = "{1}";
    public static JSONObject parsedJSON = null;
    static Status status = Status.VARIABLE_NOT_SET;
    static int lastUpdateCheckerFormatVersion = -1000;
    static boolean isLastUpdateCheckerFormatVersionSupported = true;


    private static Version getLatestRelease() {
        int i = 0;
        while (i < versionsHistory.size()) {
            Version version = versionsHistory.get(i);
            if (version.type == VersionType.RELEASE) {
                return version;
            }
            i++;
        }

        return null;
    }

    private static Version getVersionByBuild(int build) {
        int i = 0;
        while (i < versionsHistory.size()) {
            Version version = versionsHistory.get(i);
            if (version.build == build) {
                return version;
            }
            i++;
        }

        return null;
    }

    private static void parse() throws IOException {
        if (!LOCAL_APP_VERSIONS) {
            parsed = InternetUtils.parseTextPage(APP_VERSIONS_URL);
        }
    }

    private static void parseJSON() throws JSONException {
        isLastUpdateCheckerFormatVersionSupported = true;
        parsedJSON = new JSONObject(parsed);
        lastUpdateCheckerFormatVersion = (int) JSONUtils.get(parsedJSON, "format_version", -1000);

        if (lastUpdateCheckerFormatVersion == 1) { // migrate to 3 format version
            lastRelease = new Version(APP_BUILD, null, VersionType.RELEASE, null, null);

        } else if (lastUpdateCheckerFormatVersion == APP_UPDATE_CHECKER_FORMAT_VERSION) {
            versionsHistory = new ArrayList<>();
            JSONArray versionsHistoryJson = parsedJSON.getJSONArray("versions_history");
            int i = 0;
            while (i < versionsHistoryJson.length()) {
                versionsHistory.add(Version.fromJSON(versionsHistoryJson.getJSONObject(i)));
                i++;
            }

            lastRelease = getLatestRelease();

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

            } else if (versionsHistory.size() == 0) {
                status = Status.VERSIONS_HISTORY_EMPTY;

            } else if (getVersionByBuild(APP_BUILD) == null) {
                status = Status.VERSION_UNKNOWN;

            } else if (lastRelease == null) {
                status = Status.LATEST_RELEASE_NOT_FOUND;

            } else if (APP_BUILD == lastRelease.getBuild()) {
                status = Status.VERSION_LATEST;

            } else if (APP_BUILD < lastRelease.getBuild()) {
                status = Status.VERSION_OUTDATED;

            } else if (APP_BUILD > lastRelease.getBuild()) {
                status = Status.VERSION_NOT_RELEASE;
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
        FORMAT_VERSION_NOT_SUPPORTED,
        VERSIONS_HISTORY_EMPTY,
        LATEST_RELEASE_NOT_FOUND,
        VARIABLE_NOT_SET,
        PARSING_ERROR,
        VERSION_OUTDATED,
        VERSION_LATEST,
        VERSION_UNKNOWN,
        VERSION_NOT_RELEASE
    }
}