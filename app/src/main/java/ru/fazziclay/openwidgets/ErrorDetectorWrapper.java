package ru.fazziclay.openwidgets;

import ru.fazziclay.openwidgets.android.ContextSaver;
import ru.fazziclay.openwidgets.util.Utils;

public class ErrorDetectorWrapper {
    public static void errorDetectorWrapper(ErrorDetectorWrapperInterface errorDetectorWrapperInterface) {
        final Logger LOGGER = new Logger(ErrorDetectorWrapper.class, "errorDetectorWrapper");

        try {
            errorDetectorWrapperInterface.run();
        } catch (Exception exception) {
            try {
                if (ContextSaver.isContextAvailable()) Utils.showToast(ContextSaver.getContext(), "Error detected: " + exception.toString());
                LOGGER.exception(exception);
            } catch (Exception ignored) {}
        }
    }

    public interface ErrorDetectorWrapperInterface {
        void run() throws Exception;
    }
}
