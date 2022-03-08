package ru.fazziclay.openwidgets.data.widgets;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import ru.fazziclay.openwidgets.Logger;
import ru.fazziclay.openwidgets.data.Paths;
import ru.fazziclay.openwidgets.data.widgets.widget.BaseWidget;
import ru.fazziclay.openwidgets.util.FileUtils;
import ru.fazziclay.openwidgets.util.JsonUtils;

public class WidgetsData {
    public static final String WIDGETS_FILE = "widgets.json";
    public static final int WIDGETS_FORMAT_VERSION = 3;
    private static WidgetsData widgetsData = null;

    @SerializedName("version")
    int formatVersion = WIDGETS_FORMAT_VERSION;
    List<BaseWidget> widgets = new ArrayList<>();

    public static WidgetsData getWidgetsData() {
        final Logger LOGGER = new Logger();
        LOGGER.returned(widgetsData);
        return widgetsData;
    }

    public List<BaseWidget> getWidgets() {
        return widgets;
    }

    public <T extends BaseWidget> List<T> getWidgets(Class<T> clazz) {
        List<T> list = new ArrayList<>();
        if (widgets == null)
            return list;

        for (BaseWidget widget : widgets) {
            if (clazz.isInstance(widget)) // noinspection unchecked
                list.add((T)widget);
        }

        return list;
    }

    public <T extends BaseWidget> T getWidgetById(int id) {
        for (BaseWidget widget : getWidgets()) {
            if (widget.getWidgetId() == id) // noinspection unchecked
                return (T)widget;
        }
        return null;
    }

    @NonNull
    @Override
    public String toString() {
        return "WidgetsData{" +
                "formatVersion=" + formatVersion +
                ", widgets=" + widgets +
                '}';
    }

    public static void load() {
        final Logger LOGGER = new Logger();

        if (widgetsData == null) {
            LOGGER.info("widgetsData==null. loading...");
            if (Paths.getAppFilePath() == null) {
                LOGGER.error("Paths.appFilePath == null. returned");
                return;
            }

            String filePath = Paths.getAppFilePath() + WIDGETS_FILE;
            LOGGER.info("filePath=" + filePath);

            String json = FileUtils.read(filePath, JsonUtils.EMPTY_JSON_OBJECT_CONTENT);
            LOGGER.info("Before parsed: " + json);

            WidgetsData temp = JsonUtils.fromJson(json, WidgetsData.class);
            LOGGER.info("Parsed: " + temp.toString());

            if (temp.formatVersion < WIDGETS_FORMAT_VERSION) {
                LOGGER.log("Converting...");
                try {
                    WidgetsConverter.convert();
                } catch (Exception exception) {
                    LOGGER.errorDescription("Converting error. (no fatal)");
                    LOGGER.error(exception);
                }

                LOGGER.log("Converting done");
                widgetsData = JsonUtils.fromJson(FileUtils.read(filePath, JsonUtils.EMPTY_JSON_OBJECT_CONTENT), WidgetsData.class);
                LOGGER.info("Parsed after converting: " + widgetsData.toString());
            } else {
                widgetsData = temp;
            }

            save();
        } else {
            LOGGER.info("widgetsData!=null. loaded before.");
        }

        LOGGER.done();
    }

    public static void save() {
        final Logger LOGGER = new Logger();

        if (widgetsData != null) {
            LOGGER.info("widgetsData!=null. saving...");
            if (Paths.getAppFilePath() == null) {
                LOGGER.error("Paths.appFilePath == null. returned");
                LOGGER.returned();
                return;
            }

            String json = JsonUtils.toJson(widgetsData);
            LOGGER.log("Before save, widgetsData->JSON: " + json);
            FileUtils.write((Paths.getAppFilePath() + WIDGETS_FILE), json);
            LOGGER.log("Saved! widgetsData=" + widgetsData.toString());
        } else {
            LOGGER.info("widgetsData==null. Cannot be saved null.");
        }

        LOGGER.done();
    }
}
