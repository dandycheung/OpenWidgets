package com.c0rdination.openwidgets;

public class AppConfig {
    // for updater
    public static final String APP_UPDATE_CHECKER_URL = "https://raw.githubusercontent.com/FazziCLAY/OpenWidgets/master/app_versions.json";
    public static final String APP_SITE_URL = "https://github.com/fazziclay/openwidgets/releases";

    // for about
    public static final String ABOUT_AUTHOR_TEXT = (
            "Author (Developer):" + "\n" +
                    " - fazziclay@gmail.com" + "\n" +
                    " - https://fazziclay.github.io"
    );
    public static final String ABOUT_DONATE_TEXT = (
            "Donate:" + "\n" +
                    " - https://fazziclay.github.io/donate"
    );
    public static final String ABOUT_APP_SOURCE_CODE_TEXT = (
            "Source Code:" + "\n" +
                    " - https://github.com/fazziclay/openwidgets"
    );
    public static final String ABOUT_APP_INFO_TEXT = (
            "App Info:" + "\n" +
                    " - AppVersionBuild: %AppVersionBuild%" + "\n" +
                    " - AppVersionName: %AppVersionName%" + "\n" +
                    " - WidgetRegistryFormatVersion: %WidgetRegistryFormatVersion%" + "\n" +
                    " - SettingsDataFormatVersion: %SettingsDataFormatVersion%" + "\n" +
                    " - IID: %InstanceUUID%"
    );

    // for SettingsData
    public static final String SETTINGS_FILE = "settings.json";

    // for remote config
    public static final String SERVER_HOST_URL = "https://raw.githubusercontent.com/FazziCLAY/OpenWidgets/dev/1.4.2/server.host";
    public static final String SERVER_HOST = "109.68.213.227:4003";

    // for logger
    public static final String LOG_FILE = "debug/debug.log";
}
