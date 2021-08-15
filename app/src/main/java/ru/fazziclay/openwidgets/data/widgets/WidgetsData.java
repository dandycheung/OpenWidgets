package ru.fazziclay.openwidgets.data.widgets;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import ru.fazziclay.fazziclaylibs.FileUtils;
import ru.fazziclay.openwidgets.Logger;
import ru.fazziclay.openwidgets.data.Paths;
import ru.fazziclay.openwidgets.data.widgets.widget.DateWidget;
import ru.fazziclay.openwidgets.util.JsonUtils;

public class WidgetsData {
    public static final String WIDGETS_FILE = "widgets.json";
    public static final int WIDGETS_FORMAT_VERSION = 3;
    private static WidgetsData widgetsData = null;


    @SerializedName("version")
    int formatVersion = WIDGETS_FORMAT_VERSION;
    List<DateWidget> dateWidgets = new ArrayList<>();

    public static WidgetsData getWidgetsData() {
        final Logger LOGGER = new Logger();
        LOGGER.returned(widgetsData);
        return widgetsData;
    }

    public List<DateWidget> getDateWidgets() {
        return dateWidgets;
    }

    public DateWidget getDateWidgetById(int id) {
        for (DateWidget dateWidget : getDateWidgets()) {
            if (dateWidget.getWidgetId() == id) {
                return dateWidget;
            }
        }
        return null;
    }

    @NonNull
    @Override
    public String toString() {
        return "WidgetsData{" +
                "formatVersion=" + formatVersion +
                ", dateWidgets=" + dateWidgets +
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
            LOGGER.info("filePath="+filePath);
            WidgetsData temp = JsonUtils.fromJson(FileUtils.read(filePath, JsonUtils.EMPTY_JSON_OBJECT_CONTENT), WidgetsData.class);
            LOGGER.info("Parsed: "+temp.toString());

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
                LOGGER.info("Parsed after converting: "+widgetsData.toString());
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
            FileUtils.write((Paths.getAppFilePath() + WIDGETS_FILE), JsonUtils.toJson(widgetsData));
            LOGGER.log("Saved! widgetsData="+widgetsData.toString());
        } else {
            LOGGER.info("widgetsData==null. Cannot be saved null.");
        }

        LOGGER.done();
    }
}
