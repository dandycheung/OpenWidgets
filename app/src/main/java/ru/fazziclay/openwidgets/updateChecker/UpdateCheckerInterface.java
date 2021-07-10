package ru.fazziclay.openwidgets.updateChecker;

public interface UpdateCheckerInterface {
    void run(byte status, int build, String name, String downloadUrl);
}