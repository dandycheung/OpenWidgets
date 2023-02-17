package com.c0rdination.openwidgets.util.base;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.c0rdination.openwidgets.util.ExceptionUtils;

public abstract class BaseLogger {
    protected String mainClass = null;
    protected String mainMethod = null;
    protected int mainLine = 0;
    protected String fromClass = null;
    protected String fromMethod = null;
    protected int fromLine = 0;

    public String getTimeFormat() {
        return "dd.MM HH:mm:ss:SSS";
    }

    public String getTime(String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(new Date());
    }

    /** @deprecated */
    @Deprecated
    public String getDate(String format) {
        return this.getTime(format);
    }

    public String getLogFormat(String time, String logTag, String logMessage) {
        return String.format("[%s (%s:%s %s) -> (%s:%s %s) %s] %s", time, this.fromClass, this.fromLine, this.fromMethod, this.mainClass, this.mainLine, this.mainMethod, logTag, logMessage);
    }

    public abstract int getNumberInheriting();

    public BaseLogger() {
        this.init();
        this.function();
    }

    public BaseLogger(String firstMessage) {
        this.init();
        this.function(firstMessage);
    }

    protected String getClassFromPath(String path) {
        String[] fromPath = path.split("\\.");
        return fromPath[fromPath.length - 1];
    }

    public void init() {
        StackTraceElement[] stackTrace = (new Exception()).getStackTrace();
        int posStackTraceCurrent = 2 + this.getNumberInheriting();
        int posStackTraceFrom = 3 + this.getNumberInheriting();
        if (stackTrace.length > posStackTraceCurrent) {
            this.mainClass = this.getClassFromPath(stackTrace[posStackTraceCurrent].getClassName());
            this.mainMethod = stackTrace[posStackTraceCurrent].getMethodName();
            this.mainLine = stackTrace[posStackTraceCurrent].getLineNumber();
        }

        if (stackTrace.length > posStackTraceFrom) {
            this.fromClass = this.getClassFromPath(stackTrace[posStackTraceFrom].getClassName());
            this.fromMethod = stackTrace[posStackTraceFrom].getMethodName();
            this.fromLine = stackTrace[posStackTraceFrom].getLineNumber();
        }

    }

    public void raw(String tag, String message) {
        this.printLog(this.getLogFormat(this.getDate(this.getTimeFormat()), tag, message));
    }

    public void printLog(String message) {
        System.out.println(message);
    }

    public void done() {
        this.raw("DONE", "Done!");
    }

    public abstract void clear();

    public abstract boolean isCleared();

    public void info(String message) {
        this.raw("INFO", message);
    }

    public void log(String message) {
        this.raw("LOG", message);
    }

    public void error(String message) {
        this.raw("ERROR", message);
    }

    public void error(Throwable throwable) {
        this.raw("ERROR", throwable.toString() + "'\n-------------- StackTrace --------------\n" + ExceptionUtils.getStackTrace(throwable) + "-------------- StackTrace end --------------");
    }

    public void errorDescription(String message) {
        this.raw("ERROR_DESCRIPTION", message);
    }

    /** @deprecated */
    @Deprecated
    public void exception(Throwable throwable) {
        this.error(throwable);
    }

    public void function() {
        this.raw("FUNCTION_CALLED", "Called!");
    }

    public void function(String firstMessage) {
        this.raw("FUNCTION_CALLED", firstMessage);
    }

    public void returned(Object obj) {
        String str = null;
        if (obj != null)
            str = obj.toString();

        this.raw("RETURNED", str);
    }

    public void returned() {
        this.raw("RETURNED", "");
    }
}
