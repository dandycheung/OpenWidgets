package ru.fazziclay.openwidgets;

import android.util.Log;

import ru.fazziclay.fazziclaylibs.FileUtils;
import ru.fazziclay.openwidgets.data.Paths;
import ru.fazziclay.openwidgets.data.settings.SettingsData;
import ru.fazziclay.openwidgets.util.ExceptionUtils;
import ru.fazziclay.openwidgets.util.TimeUtils;

public class Logger {
    public static final String LOG_FILE = "debug/debug.log";
    public static final boolean FORCED_LOGGING = true;
    private static final String TIME_FORMAT = "%d.%m %H:%M:%S:%N";

    String calledInClass;
    String calledInAbstractClass = null;
    String function = "{unknown}";
    int lineInFile = 0;

    public Logger(Class calledInClass) {this.calledInClass = calledInClass.getSimpleName();}
    public Logger(Class calledInClass, String function) {
        this.calledInClass = calledInClass.getSimpleName();
        this.function = function;

        Exception exception = new Exception();
        if (exception.getStackTrace().length >= 1) lineInFile = exception.getStackTrace()[1].getLineNumber();

        function();
    }

    public Logger(Class calledInClass, Class calledInAbstractClass, String function) {
        this.calledInClass = calledInClass.getSimpleName();
        this.calledInAbstractClass = calledInAbstractClass.getCanonicalName();
        this.function = function;

        Exception exception = new Exception();
        if (exception.getStackTrace().length >= 1) lineInFile = exception.getStackTrace()[1].getLineNumber();

        function();
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

    public void done() {
        raw("DONE", "Done!");
    }

    public void clear() {
        FileUtils.write(Paths.getAppFilePath() + Logger.LOG_FILE, "");
        info("Logs cleared");
    }

    public static boolean isCleared() {
        return FileUtils.read(Paths.getAppFilePath() + LOG_FILE).split("\n").length <= 2;
    }

    public void info(String message) {raw("INFO", message);}
    public void log(String message) {raw("LOG", message);}
    public void error(String message) {raw("ERROR", message);}
    public void exception(Exception exception) {
        raw("Exception", exception.toString() + "\n-------------- StackTrace --------------\n" + ExceptionUtils.getStackTrace(exception) + "-------------- StackTrace end --------------");
    }
    public void function() {raw("FUNCTION_CALLED", "Called!");}
    public void returned(Object obj) {
        String str = null;
        if (obj != null) str = obj.toString();
        raw("RETURNED", str);
    }

    private void raw(String tag, String message) {
        String logTime = TimeUtils.dateFormat(TIME_FORMAT);
        String logPath = Paths.getAppFilePath() + LOG_FILE;

        SettingsData settingsData = SettingsData.getSettingsData();
        boolean isLogger = true;
        boolean established = true; // established logger depended components status (sorry my english...)

        if (settingsData != null) {
            isLogger = settingsData.isLogger();
            established = false;
        }

        if (isLogger || FORCED_LOGGING) {
            String s = String.format("[%s %s:%s %s %s] %s", logTime, this.calledInClass, lineInFile, function, tag, message);
            if (this.calledInAbstractClass != null) s = String.format("[%s %s:%s %s.%s %s] %s", logTime, this.calledInClass, lineInFile, calledInAbstractClass, function, tag, message);
            if (established) s = "[EST] " + s;
            Log.d("LOGGER", s);
            try {
                if (Paths.getAppFilePath() != null) FileUtils.write(logPath, FileUtils.read(logPath) + "\n" + s);
            } catch (Exception ignored) {}
        }
    }
}
