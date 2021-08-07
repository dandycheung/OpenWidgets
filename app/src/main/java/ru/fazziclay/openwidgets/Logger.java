package ru.fazziclay.openwidgets;

import android.util.Log;

import ru.fazziclay.fazziclaylibs.FileUtils;
import ru.fazziclay.openwidgets.data.Paths;
import ru.fazziclay.openwidgets.data.settings.SettingsData;
import ru.fazziclay.openwidgets.util.ExceptionUtils;
import ru.fazziclay.openwidgets.util.TimeUtils;

public class Logger {
    public static final String LOG_FILE = "debug/debug.log";
    public static final boolean PRINUDIL_LOGGING = true;
    private static final String TIME_FORMAT = "%H:%M:%S:%N";

    String calledInFile;
    String function = "{unknown}";

    public Logger(Class calledFrom) {this.calledInFile = calledFrom.getSimpleName();}
    public Logger(Class calledFrom, String function) {
        this.calledInFile = calledFrom.getSimpleName();
        this.function = function;

        function();
    }

    public void setFunction(String function) {
        this.function = function;
        function();
    }

    public void clear() {
        FileUtils.write(Paths.getAppFilePath() + "/" + Logger.LOG_FILE, "");
        info("Logs cleared");
    }

    public void info(String message) {raw("INFO", message);}
    public void log(String message) {raw("LOG", message);}
    public void error(String message) {raw("ERROR", message);}
    public void exception(Exception exception) {
        raw("Exception", exception.toString() + "\n-------------- StackTrace --------------\n" + ExceptionUtils.getStackTrace(exception) + "-------------- StackTrace end --------------");
    }
    //public void debug(String message) {raw("DEBUG", message);}
    public void function() {raw("FUNCTION_CALLED", "Called!");}
    //public void functionReturned(String function, Object returned) {raw("FUNCTION_RETURNED", "function "+function+" returned "+returned.toString());}
    public void returned(String message) {
        raw("RETURNED", message);
    }

    //public void returnedThrow(String message) {
    //    raw("RETURNED_THROW", message);
    //}

    //public void returnedThrow(Exception exception) {
    //    raw("RETURNED_THROW", exception.toString());
    //}

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

        if (isLogger || PRINUDIL_LOGGING) {
            String s = String.format("[%s %s.%s %s] %s", logTime, this.calledInFile, function, tag, message);
            if (established) s = "[EST] " + s;
            Log.d("LOGGER", s);
            try {
                FileUtils.write(logPath, FileUtils.read(logPath) + "\n" + s);
            } catch (Exception ignored) {}
        }
    }
}
