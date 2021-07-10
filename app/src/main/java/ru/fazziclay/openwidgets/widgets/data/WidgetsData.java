package ru.fazziclay.openwidgets.widgets.data;

import android.view.Gravity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ru.fazziclay.fazziclaylibs.FileUtils;
import ru.fazziclay.fazziclaylibs.JSONUtils;
import ru.fazziclay.openwidgets.Config;
import ru.fazziclay.openwidgets.activity.MainActivity;
import ru.fazziclay.openwidgets.deprecated.cogs.DeprecatedUtils;
import ru.fazziclay.openwidgets.widgets.data.converter.Converter;

/**
* Суть: Взаимодействие с файлом виджетов (widgets.json)
* Версия: 2
* Переменные:
*   ('системные')
*   version - константа, для какой версии файла предназначен этот код
*   filePath - путь до файла widgets.json
*   widgetsDataFile - JSON Объект файла
*   fileVersion - Версия текущего загруженного файла
*
*   ('Из самого файла')
*   index - список из айдишников виджетов
*   widgets - Ключ и значение ключа, значение ключа это обьект виджета, по умолчанию BaseWidgets
*
* Функции:
*   save() - Сохранить всё из java формата в JSON
*   load() - Загрузить всё из JSON формата в java
* */

public class WidgetsData {
    public static final int version = 2;
    public static final String filePath = MainActivity.getInstance().getFilesDir().getPath() + "/widgets.json";
    public static JSONObject widgetsDataFile;
    public static int fileVersion;

    public static List<Integer> index = null;
    public static HashMap<String, BaseWidget> widgets = null;


    public static void save() {
        widgetsDataFile = new JSONObject();
        try {
            widgetsDataFile.put("version", fileVersion);
            JSONArray indexJson = (JSONArray) JSONUtils.get(widgetsDataFile, "index", new JSONArray());
            JSONObject widgetsJson = (JSONObject) JSONUtils.get(widgetsDataFile, "widgets", new JSONObject());

            for (int id : index) {
                indexJson.put(id);
                widgetsJson.put(String.valueOf(id), widgets.get(String.valueOf(id)).toJSON());
            }

            FileUtils.write(filePath, widgetsDataFile.toString(Config.savableFilesJsonIndent));

        } catch (Exception e) {
            DeprecatedUtils.showMessage(MainActivity.getInstance(), "data.WidgetsData.save(): "+e);
            e.printStackTrace();
        }
    }
    public static void load() {
        widgetsDataFile = JSONUtils.readJSONObjectFile(filePath);
        fileVersion = (int) JSONUtils.get(widgetsDataFile, "version", version);

        if (!Converter.isLast()) {
            widgetsDataFile = Converter.convertToLast(widgetsDataFile);
            fileVersion = (int) JSONUtils.get(widgetsDataFile, "version", version);
        }

        if (fileVersion == version) {
            try {
                index = new ArrayList<>();
                widgets = new HashMap<>();
                JSONArray indexJson = (JSONArray) JSONUtils.get(widgetsDataFile, "index", new JSONArray());
                JSONObject widgetsJson = (JSONObject) JSONUtils.get(widgetsDataFile, "widgets", new JSONObject());

                int i = 0;
                while (i < indexJson.length()) {
                    int id = (Integer) JSONUtils.get(indexJson, i, -1);
                    index.add(id);

                    JSONObject widgetJson = (JSONObject) JSONUtils.get(widgetsJson, String.valueOf(id), new JSONObject());
                    int widgetType = (int) JSONUtils.get(widgetJson, "widget_type", -1);
                    BaseWidget widget = new BaseWidget(widgetType);

                    if (widgetType == DateWidget.type) {
                        String pattern = (String) JSONUtils.get(widgetJson, "pattern", "Hello World");
                        int patternSize = (int) JSONUtils.get(widgetJson, "pattern_size", 40);
                        int patternSizeUnits = (int) JSONUtils.get(widgetJson, "pattern_size_units", 2);
                        String patternColor = (String) JSONUtils.get(widgetJson, "pattern_color", "#ffffff");
                        String patternBackgroundColor = (String) JSONUtils.get(widgetJson, "pattern_background_color", "#88888866");
                        String backgroundColor = (String) JSONUtils.get(widgetJson, "background_color", "#00ff00");
                        int gravity = (int) JSONUtils.get(widgetJson, "background_gravity", Gravity.CENTER);

                        widget = new DateWidget(pattern, patternSize, patternSizeUnits, patternColor, patternBackgroundColor, backgroundColor, gravity);
                    }

                    widgets.put(String.valueOf(id), widget);
                    i++;
                }
            } catch (Exception e) {
                DeprecatedUtils.showMessage(MainActivity.getInstance(), "data.WidgetsData.load(): "+e);
                e.printStackTrace();
            }
        }
    }
}
