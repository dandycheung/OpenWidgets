package ru.fazziclay.openwidgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;

import ru.fazziclay.openwidgets.util.Utils;

@Deprecated
public class ErrorDetectorWrapper {
    @SuppressLint("StaticFieldLeak")
    public static Context context;
    @Deprecated
    public static void errorDetectorWrapper(ErrorDetectorWrapperInterface errorDetectorWrapperInterface) {
        try {
            errorDetectorWrapperInterface.run();
        } catch (Exception exception) {
            try {
                new Handler().post(() -> Utils.showToast(context, "Error detected: " + exception.toString()));
                final Logger LOGGER = new Logger();
                LOGGER.error(exception);
            } catch (Exception ignored) {}
        }
    }

    public interface ErrorDetectorWrapperInterface {
        void run() throws Exception;
    }
}
