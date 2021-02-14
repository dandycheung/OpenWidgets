package ru.fazziclay.openwidgets.cogs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WidgetsManager {
    public final static String APP_PATH        = "/storage/emulated/0/Android/data/ru.fazziclay.openwidgets/files/";
    final static String DEFAULT_WIDGET  = "{'': ''}";


    public static JSONObject widgets;

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

    public static void addWidget(int widgetId) {
        if (isWidgetExist(widgetId)) {
            return;
        }

        syncVariable();

        try {
            widgets.getJSONArray("index").put(String.valueOf(widgetId));
            widgets.getJSONObject("data").put(String.valueOf(widgetId), new JSONObject(DEFAULT_WIDGET));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        syncFile();
    }

    public static void removeWidget(int widgetId) {
        if (!isWidgetExist(widgetId)) {
            return;
        }

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
