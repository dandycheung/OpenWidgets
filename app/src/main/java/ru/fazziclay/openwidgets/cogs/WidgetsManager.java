package ru.fazziclay.openwidgets.cogs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WidgetsManager { // storage/emulated/0/Android
    public final static String APP_PATH = "/data/data/ru.fazziclay.openwidgets/files/";

    public static JSONObject widgets;

    public static int getWidgetTextStyle(int widgetId) {
        try {
            syncVariable();
            if (isWidgetExist(widgetId)) {
                return widgets.getJSONObject("data").getJSONObject(String.valueOf(widgetId)).getInt("text_style");
            }
            return 0;
        } catch (Exception e) {
            return 0;
        }
    }

    public static void changeWidgetTextStyle(int widgetId, int style) throws JSONException {
        syncVariable();
        if (isWidgetExist(widgetId)) {
            widgets.getJSONObject("data").getJSONObject(String.valueOf(widgetId)).put("text_style", style);
        }
        syncFile();
    }

    public static void changeWidgetColor(int widgetId, String color) throws JSONException {
        syncVariable();
        if (isWidgetExist(widgetId)) {
            widgets.getJSONObject("data").getJSONObject(String.valueOf(widgetId)).put("text_color", color);
        }
        syncFile();
    }

    public static String getWidgetColor(int widgetId) {
        try {
            syncVariable();
            if (isWidgetExist(widgetId)) {
                return widgets.getJSONObject("data").getJSONObject(String.valueOf(widgetId)).getString("text_color");
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public static void changeWidgetText(int widgetId, String newText) throws JSONException {
        syncVariable();
        if (isWidgetExist(widgetId)) {
            widgets.getJSONObject("data").getJSONObject(String.valueOf(widgetId)).put("text", newText);
        }
        syncFile();
    }

    public static String getWidgetText(int widgetId) {
        try {
            syncVariable();
            if (isWidgetExist(widgetId)) {
                return widgets.getJSONObject("data").getJSONObject(String.valueOf(widgetId)).getString("text");
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public static void syncVariable() {
        try {
            String fileContent = FileUtil.readFile(APP_PATH + "widgets.json");
            try {
                widgets = new JSONObject(fileContent);
            } catch (JSONException ignored) {}

            // If 'index' not exist => create
            try {
                widgets.getJSONArray("index");

            } catch (Exception e) {
                //widgets.put("index", new JSONArray("[]"));
                widgets = new JSONObject("{'index': [], 'data': {}}");
                syncFile();
            }

            // If 'data' not exist => create
            try {
                widgets.getJSONObject("data");

            } catch (Exception e) {
                //widgets.put("data", new JSONObject("{}"));
                widgets = new JSONObject("{'index': [], 'data': {}}");
                syncFile();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void syncFile() {
        try {
            FileUtil.writeFile(APP_PATH + "/widgets.json", widgets.toString(4));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static boolean isWidgetExist(int widgetId) {
        syncVariable();
        try {
            int i = 0;
            while (i < widgets.getJSONArray("index").length()) {
                if (widgets.getJSONArray("index").getString(i).equals(String.valueOf(widgetId))) {
                    return true;
                }
                i++;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void addWidget(int widgetId, int widgetType) {
        if (isWidgetExist(widgetId)) {
            return;
        }

        JSONObject default_settings = new JSONObject();

        if (widgetType == 0) {
            try {
                default_settings.put("widgetType", widgetType);
                default_settings.put("text", "NEW: %_H:%_M:%_S\nOLD: %H:%M:%S\nCustomize!");
                default_settings.put("text_color", "#ffffff");
                default_settings.put("text_style", 1);
                default_settings.put("text_size", 20);
                default_settings.put("background_color", "#000000");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            try {
                default_settings.put("widgetType", widgetType);
                default_settings.put("error", "Invalid widgetType");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        syncVariable();
        try {
            widgets.getJSONArray("index").put(widgetId);
            widgets.getJSONObject("data").put(String.valueOf(widgetId), default_settings);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        syncFile();
    }

    public static void removeWidget(int widgetId) {
        if (!isWidgetExist(widgetId)) {
            return;
        }

        syncVariable();

        try {
            JSONArray index = widgets.getJSONArray("index");

            int i = 0;
            while (i < index.length()) {
                if (index.getString(i).equals(String.valueOf(widgetId))) {
                    index = RemoveJSONArray(index, i);
                }
                i++;
            }
            widgets.getJSONObject("data").remove(String.valueOf(widgetId));
            widgets.put("index", index);

        } catch (Exception e) {
            e.printStackTrace();
        }

        syncFile();
    }

    private static JSONArray RemoveJSONArray(JSONArray jarray, int pos) {
        JSONArray Njarray = new JSONArray();
        try{
            for(int i=0;i<jarray.length();i++) {
                if(i!=pos) Njarray.put(jarray.get(i));
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
        return Njarray;
    }
}
