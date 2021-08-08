package ru.fazziclay.openwidgets.util;

public class ExceptionUtils {
    public static String getStackTrace(Exception exception) {
        StringBuilder stackTrace = new StringBuilder();
        for (StackTraceElement stackTraceElement : exception.getStackTrace()) {
            stackTrace.append(" at ").append(stackTraceElement.toString()).append("\n");
        }

        return stackTrace.toString();
    }
}
