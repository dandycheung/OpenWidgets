package ru.fazziclay.openwidgets.widgets.data.converter;

import org.json.JSONObject;

import ru.fazziclay.fazziclaylibs.JSONUtils;
import ru.fazziclay.openwidgets.widgets.data.WidgetsData;

public class Converter {
    public static boolean isLast() {
        return (WidgetsData.version == WidgetsData.fileVersion);
    }

    public static JSONObject convertToLast(JSONObject source) {
        JSONObject converted = source;

        if (WidgetsData.fileVersion < 2) {
            converted = new JSONObject();
            JSONUtils.get(converted, "version", 2);
        }

        return converted;
    }
}
