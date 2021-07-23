package ru.fazziclay.openwidgets.data.widgets;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import ru.fazziclay.fazziclaylibs.FileUtils;
import ru.fazziclay.openwidgets.data.Paths;
import ru.fazziclay.openwidgets.data.widgets.widget.DateWidget;

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
        if (widgetsData == null) {
            Gson gson = new Gson();
            WidgetsData temp = gson.fromJson(FileUtils.read(Paths.appFilePath + "/" + WIDGETS_FILE, "{}"), WidgetsData.class);

            if (temp.formatVersion < WIDGETS_FORMAT_VERSION) {
                try {
                    WidgetsConverter.convert();
                } catch (JSONException ignored) {}
                widgetsData = gson.fromJson(FileUtils.read(Paths.appFilePath + "/" + WIDGETS_FILE, "{}"), WidgetsData.class);
            } else {
                widgetsData = temp;
            }

            save();
        }
    }

    public static void save() {
        if (widgetsData != null) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            FileUtils.write(Paths.appFilePath + "/" + WIDGETS_FILE, gson.toJson(widgetsData));
        }
    }
}
