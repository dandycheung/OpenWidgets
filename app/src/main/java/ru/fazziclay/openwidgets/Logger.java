package ru.fazziclay.openwidgets;

import android.util.Log;

import ru.fazziclay.fazziclaylibs.FileUtils;
import ru.fazziclay.openwidgets.data.Paths;
import ru.fazziclay.openwidgets.util.TimeUtils;

public class Logger extends ru.fazziclay.fazziclaylibs.logging.Logger {
    public static final String LOG_FILE = "debug/debug.log";
    private static final String TIME_FORMAT = "%d.%m %H:%M:%S:%N";

    static boolean isLoadedFromFile = false;
    static boolean isLoggedWithoutLoaded = false;
    static String logsData = "";
    static int logLine = 0;

    public static String getLogsData() {
        return logsData;
    }

    @Override
    public int getNumberInheriting() {
        return 1;
    }

    @Override
    public String getTimeFormat() {
        return TIME_FORMAT;
    }

    @Override
    public String getDate(String timeFormat) {
        return TimeUtils.dateFormat(timeFormat);
    }

    @Override
    public void printLog(String message) {
        message = logLine + message;
        logLine++;

        String filePath = null;
        if (Paths.getAppFilePath() != null) {
            filePath = Paths.getAppFilePath() + LOG_FILE;
        }

        try {
            if (filePath != null && !isLoadedFromFile) {
                if (isLoggedWithoutLoaded) {
                    logsData = FileUtils.read(filePath) + logsData;
                } else {
                    logsData = FileUtils.read(filePath);
                }
                isLoadedFromFile = true;
                isLoggedWithoutLoaded = false;
            }

            logsData += "\n" + message;
            if (!isLoadedFromFile) isLoggedWithoutLoaded = true;

        } catch (Throwable e) {
            logsData = "";
            FileUtils.write(filePath, "");
            error("[EMERGENCY] LOGS CLEARED DUE TO ERROR!");
            errorDescription("Error for logger!!!");
            exception(e);
        }

        Log.d("LOGGER", message);
        if (filePath != null && isLoadedFromFile) FileUtils.write(filePath, logsData);
    }

    @Override
    public void clear() {
        clear("Logs cleared!");
    }

    public void clear(String message) {
        logsData = "";
        if (Paths.getAppFilePath() != null) FileUtils.write(Paths.getAppFilePath() + LOG_FILE, "");
        logsData = "";
        if (Paths.getAppFilePath() != null) FileUtils.write(Paths.getAppFilePath() + LOG_FILE, "");
        raw("LOGS_CLEARED", message);
    }

    @Override
    public boolean isCleared() {
        if (Paths.getAppFilePath() != null) return FileUtils.read(Paths.getAppFilePath() + LOG_FILE).length() <= 2;
        return false;
    }

    // Init
    public Logger() {
        super();
    }

    public void deviceInfo() {
        String s = "\n-------- CURRENT DEVICE INFO --------";
        s+="\n ** OS VERSION: "             + System.getProperty("os.version");
        s+="\n ** ver INCREMENTAL: "        + android.os.Build.VERSION.INCREMENTAL;
        s+="\n ** ver OS API Level: "       + android.os.Build.VERSION.SDK_INT;
        s+="\n ** ver RELEASE: "            + android.os.Build.VERSION.RELEASE;
        s+="\n ** Device: "                 + android.os.Build.DEVICE;
        s+="\n ** Model (and Product): "    + android.os.Build.MODEL + " ("+android.os.Build.PRODUCT+")";
        s+="\n ** BRAND: "           + android.os.Build.BRAND;
        s+="\n ** DISPLAY: "         + android.os.Build.DISPLAY;
        s+="\n ** CPU_ABI: "         + android.os.Build.CPU_ABI;
        s+="\n ** CPU_ABI2: "        + android.os.Build.CPU_ABI2;
        s+="\n ** UNKNOWN: "         + android.os.Build.UNKNOWN;
        s+="\n ** HARDWARE: "        + android.os.Build.HARDWARE;
        s+="\n ** Build ID: "        + android.os.Build.ID;
        s+="\n ** MANUFACTURER: "    + android.os.Build.MANUFACTURER;
        s+="\n ** USER: "            + android.os.Build.USER;
        s+="\n ** HOST: "            + android.os.Build.HOST;
        s+="\n------- CURRENT DEVICE INFO end -------";

        raw("DEVICE_INFO", s);
    }
}
