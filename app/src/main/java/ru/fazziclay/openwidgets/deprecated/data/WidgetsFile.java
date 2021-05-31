package ru.fazziclay.openwidgets.deprecated.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ru.fazziclay.fazziclaylibs.FileUtils;
import ru.fazziclay.fazziclaylibs.JSONUtils;
import ru.fazziclay.openwidgets.deprecated.converter.WidgetsFileV1;

public class WidgetsFile {
    final public static int version = 2;
    public static JSONObject widgetsFile = null;

    public static int fileVersion;
    public static List<Integer> index;
    public static HashMap<String, BaseWidgetData> data;
    public static HashMap<String, BaseWidgetData> defaults;

    public static void loadWidgetsFile() throws JSONException {
        widgetsFile = JSONUtils.readJSONObjectFile("PATCH-WidgetsManager.APP_PATH" + "/widgets.json");
        fileVersion = (int) JSONUtils.get(widgetsFile, "version", version);

        if (fileVersion != version) {
            WidgetsFileV1.loadWidgetsFile();
            widgetsFile = WidgetsFileV1.convert();
            fileVersion = (int) JSONUtils.get(widgetsFile, "version", version);
        }

        if (fileVersion == version) {
            index = new ArrayList<>();
            try {
                JSONArray jsonArray = (JSONArray) JSONUtils.get(widgetsFile, "index", new JSONArray());
                int i = 0;
                while (i < jsonArray.length()) {
                    int id = jsonArray.getInt(i);
                    index.add(id);
                    i++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            data = new HashMap<>();
            try {
                JSONObject jsonObject = (JSONObject) JSONUtils.get(widgetsFile, "data", new JSONObject());
                int i = 0;
                while (i < index.size()) {
                    String key = String.valueOf(index.get(i));
                    BaseWidgetData widgetData = new BaseWidgetData(-1);
                    JSONObject widgetDataJson = jsonObject.getJSONObject(key);
                    int widgetType = widgetDataJson.getInt("widgetType");

                    if (widgetType == 0) {
                        String text = widgetDataJson.getString("text");
                        String textColor = widgetDataJson.getString("text_color");
                        int textStyle = widgetDataJson.getInt("text_style");
                        int textSize = widgetDataJson.getInt("text_size");
                        String backgroundColor = widgetDataJson.getString("background_color");
                        widgetData = new ru.fazziclay.openwidgets.deprecated.data.DigitalClockData(text, textColor, textStyle, textSize, backgroundColor);
                    }
                    data.put(key, widgetData);
                    i++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        save();
    }

    public static void save() {
        try {
            FileUtils.write("PATCH-WidgetsManager.APP_PATH" + "/widgets.json", widgetsFile.toString(4));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    // --- V2 Format ---
    // index - array of int`s. Id`s all system android widgets [1, 2, 3]
    // data - `Map|JSONObject` of BaseWidgetData as key in `index` {"1": {...}, "2": {...}} // 1 & 2 - widgets ids
    // defaults - default settings for widgets type {"0": {...}} // 0 - widgetType(digital clock)

}
