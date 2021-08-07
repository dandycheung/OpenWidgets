package ru.fazziclay.openwidgets.data.widgets;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;

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
        final Logger LOGGER = new Logger(WidgetsData.class, "load");

        if (widgetsData == null) {
            if (Paths.getAppFilePath() == null) {
                LOGGER.error("Paths.appFilePath == null. returned");
                return;
            }
            String fileContent = FileUtils.read((Paths.getAppFilePath() + WIDGETS_FILE), JsonUtils.EMPTY_JSON_CONTENT);
            WidgetsData temp = JsonUtils.fromJson(fileContent, WidgetsData.class);

            if (temp.formatVersion < WIDGETS_FORMAT_VERSION) {
                try {
                    WidgetsConverter.convert();
                } catch (JSONException exception) {
                    LOGGER.log("No fatal error.");
                    LOGGER.exception(exception);
                }
                widgetsData = JsonUtils.fromJson(fileContent, WidgetsData.class);
            } else {
                widgetsData = temp;
            }

            save();
        }
    }

    public static void save() {
        final Logger LOGGER = new Logger(WidgetsData.class, "save");

        if (widgetsData != null) {
            if (Paths.getAppFilePath() == null) {
                LOGGER.error("Paths.appFilePath == null. returned");
                return;
            }
            FileUtils.write((Paths.getAppFilePath() + WIDGETS_FILE), JsonUtils.toJson(widgetsData));
        }
    }
}
