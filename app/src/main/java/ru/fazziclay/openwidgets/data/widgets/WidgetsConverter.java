package ru.fazziclay.openwidgets.data.widgets;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ru.fazziclay.fazziclaylibs.FileUtils;
import ru.fazziclay.openwidgets.Logger;
import ru.fazziclay.openwidgets.data.Paths;

public class WidgetsConverter {

    public static void convert() throws JSONException {
        final Logger LOGGER = new Logger();

        JSONObject convertibleFile = new JSONObject(FileUtils.read(Paths.getAppFilePath() + WidgetsData.WIDGETS_FILE));
        int formatVersion = convertibleFile.getInt("version");

        int i = 0;
        while (formatVersion != WidgetsData.WIDGETS_FORMAT_VERSION) {
            if (formatVersion == 2) {
                JSONObject temp = new JSONObject();
                JSONArray dateWidgets = new JSONArray();

                // old keys
                JSONArray index = convertibleFile.getJSONArray("index");
                JSONObject widgets = convertibleFile.getJSONObject("widgets");

                // convert
                int i2to3 = 0;
                while (i2to3 < index.length()) {
                    int id;
                    JSONObject oldWidget;
                    int widgetType;

                    try {
                        id = index.getInt(i2to3);
                        oldWidget = widgets.getJSONObject(String.valueOf(id));
                        widgetType = oldWidget.getInt("widget_type");

                        if (widgetType == 1) {
                            JSONObject newWidget = new JSONObject();
                            newWidget.put("widgetId", id);
                            newWidget.put("pattern", oldWidget.getString("pattern")
                                    .replace("%_S", "%.S")
                                    .replace("%_M", "%.M")
                                    .replace("%_H", "%.H")
                            );
                            newWidget.put("patternSize", oldWidget.getInt("pattern_size"));
                            newWidget.put("patternColor", oldWidget.getString("pattern_color"));
                            newWidget.put("patternBackgroundColor", oldWidget.getString("pattern_background_color"));
                            newWidget.put("backgroundColor", oldWidget.getString("background_color"));
                            newWidget.put("backgroundGravity", oldWidget.getInt("background_gravity"));
                            newWidget.put("flags", new JSONArray().put(WidgetFlag.CONVERTED_FROM_FORMAT_VERSION_2));

                            dateWidgets.put(newWidget);
                        }
                    } catch (Exception e) {
                        LOGGER.error("convert 2 -> 3: error. (ignored)");
                        LOGGER.exception(e);
                    }

                    i2to3++;
                }

                formatVersion = 3;
                temp.put("dateWidgets", dateWidgets);
                temp.put("version", formatVersion);
                FileUtils.write(Paths.getAppFilePath() + "/" + WidgetsData.WIDGETS_FILE, temp.toString());
            }

            if (i > 1000) {
                LOGGER.error("i > 1000. breaking...");
                break;
            }

            i++;
        }

        LOGGER.done();
    }
}
