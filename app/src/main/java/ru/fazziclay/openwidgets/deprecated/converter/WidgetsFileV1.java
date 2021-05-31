package ru.fazziclay.openwidgets.deprecated.converter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ru.fazziclay.fazziclaylibs.FileUtils;
import ru.fazziclay.fazziclaylibs.JSONUtils;

class UnknownWidgetsFileVersion extends Exception {
    public UnknownWidgetsFileVersion(String message) {
        super(message);
    }
}

public class WidgetsFileV1 {
    final public static int version = 1;
    public static JSONObject widgetsFile = null;

    public static int fileVersion;
    public static List<Integer> index;
    public static HashMap<String, BaseWidgetDataV1> data;

    public static void loadWidgetsFile() {
        widgetsFile = JSONUtils.readJSONObjectFile("PATCH-WidgetsManager.APP_PATH" + "/widgets.json");
        fileVersion = (int) JSONUtils.get(widgetsFile, "version", version);

        if (fileVersion != version) {
            try {
                throw new UnknownWidgetsFileVersion("Unknown widgets file version.");
            } catch (UnknownWidgetsFileVersion unknownWidgetsFileVersion) {
                unknownWidgetsFileVersion.printStackTrace();
                System.exit(111);
            }
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
                    BaseWidgetDataV1 widgetData = new BaseWidgetDataV1(-1);
                    JSONObject widgetDataJson = jsonObject.getJSONObject(key);
                    int widgetType = widgetDataJson.getInt("widgetType");

                    if (widgetType == 0) {
                        String text = widgetDataJson.getString("text");
                        String textColor = widgetDataJson.getString("text_color");
                        int textStyle = widgetDataJson.getInt("text_style");
                        int textSize = widgetDataJson.getInt("text_size");
                        String backgroundColor = widgetDataJson.getString("background_color");
                        widgetData = new DigitalClockDataV1(text, textColor, textStyle, textSize, backgroundColor);
                    }
                    data.put(key, widgetData);
                    i++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

//        save();
    }

    public static void save() {
        try {
            FileUtils.write("PATCH-WidgetsManager.APP_PATH" + "/widgets.json", widgetsFile.toString(4));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static JSONObject convert() throws JSONException {
        JSONObject converted = new JSONObject();
        converted.put("version", 2);
        converted.put("index", new JSONArray());
        converted.put("data", new JSONObject());
        for (Integer id : index) {
            converted.getJSONArray("index").put(id);
            converted.getJSONObject("data").put(String.valueOf(id), data.get(String.valueOf(id)).toConvertedJson());
        }

        converted.put("defaults", new JSONObject());

        return converted;
    }
    // --- V1 Format ---
    // index - array of int`s. Id`s all system android widgets
    // data - `Map|JSONObject` of BaseWidgetData as key in `index`

}
