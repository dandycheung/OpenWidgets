package ru.fazziclay.openwidgets.cogs;

public interface AppVersionInterface {
    void onRun(byte status, int build, String name, String downloadUrl);
}
