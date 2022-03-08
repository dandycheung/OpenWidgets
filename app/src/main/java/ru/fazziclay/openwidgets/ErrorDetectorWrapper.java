package ru.fazziclay.openwidgets;

import android.content.Context;
import android.os.Handler;

import ru.fazziclay.openwidgets.util.Utils;

public class ErrorDetectorWrapper {
    public static void errorDetectorWrapper(Context context, ErrorDetectorWrapperInterface errorDetectorWrapperInterface) {
        try {
            errorDetectorWrapperInterface.run();
        } catch (Exception exception) {
            try {
                new Handler().post(() -> Utils.showToast(context, "Error: " + exception));
                final Logger LOGGER = new Logger();
                LOGGER.error(exception);
            } catch (Exception ignored) {}
        }
    }

    public interface ErrorDetectorWrapperInterface {
        void run() throws Exception;
    }
}
