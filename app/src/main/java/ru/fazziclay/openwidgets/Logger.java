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

    String calledInFile;
    String function = null;

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
        FileUtils.write(Paths.appFilePath + "/" + Logger.LOG_FILE, "");
        info("Logs cleared");
    }

    public void info(String message) {raw("INFO", message);}
    public void log(String message) {raw("LOG", message);}
    public void error(String message) {raw("ERROR", message);}
    public void exception(Exception exception) {
        raw("Exception", exception.toString() + "\n-------------- StackTrace --------------\n" + ExceptionUtils.getStackTrace(exception) + "-------------- StackTrace end --------------");
    }
    public void debug(String message) {raw("DEBUG", message);}
    public void function() {raw("FUNCTION_CALLED", "Called!");}
    public void functionReturned(String function, Object returned) {raw("FUNCTION_RETURNED", "function "+function+" returned "+returned.toString());}
    public void returned(String message) {
        raw("RETURNED", message);
    }

    public void returnedThrow(String message) {
        raw("RETURNED_THROW", message);
    }

    public void returnedThrow(Exception exception) {
        raw("RETURNED_THROW", exception.toString());
    }

    private void raw(String tag, String message) {
        if (!(SettingsData.getSettingsData().isLogger() || PRINUDIL_LOGGING)) return;
        String logPath = Paths.appFilePath+"/"+ LOG_FILE;
        String s = String.format("[%s %s %s] %s", TimeUtils.dateFormat("%H:%M:%S"), tag, this.calledInFile, message);
        if (function != null) s = String.format("[%s %s %s.%s] %s", TimeUtils.dateFormat("%H:%M:%S"), tag, this.calledInFile, function, message);

        Log.d(tag, s);
        FileUtils.write(logPath, FileUtils.read(logPath) + "\n" + s);
    }
}
