package ru.fazziclay.openwidgets.utils;

import ru.fazziclay.openwidgets.activity.MainActivity;
import ru.fazziclay.openwidgets.deprecated.cogs.DeprecatedUtils;

public class ErrorDetectorWrapper {
    public static void errorDetectorWrapper(ErrorDetectorWrapperInterface errorDetectorWrapperInterface) {
        try {
            errorDetectorWrapperInterface.run();
        } catch (Exception e) {
            try {
                DeprecatedUtils.showMessage(MainActivity.getInstance(), e.toString());
            } catch (Exception ignored) {}
        }
    }
}
