package ru.fazziclay.openwidgets.data.widgets;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.fazziclay.openwidgets.Logger;
import ru.fazziclay.openwidgets.data.Paths;
import ru.fazziclay.openwidgets.data.widgets.widget.BaseWidget;
import ru.fazziclay.openwidgets.util.FileUtils;
import ru.fazziclay.openwidgets.util.JsonUtils;

public class WidgetRegistry {
    public static final String WIDGET_REGISTRY_FILE = "widget_registry.json";
    public static final int WIDGET_REGISTRY_FORMAT_VERSION = 1;
    private static WidgetRegistry widgetsRegistry = null;

    @SerializedName("version")
    int formatVersion = WIDGET_REGISTRY_FORMAT_VERSION;

    @Expose(serialize = false, deserialize = false)
    Map<String, List<BaseWidget>> widgets = new HashMap<>();

    // These two fields were used while serializing/deserializing
    private List<String> widgetClassNames;
    private List<String> widgetJsons;

    public static WidgetRegistry getWidgetRegistry() {
        final Logger LOGGER = new Logger();
        LOGGER.returned(widgetsRegistry);
        return widgetsRegistry;
    }

    public boolean addWidget(BaseWidget widget) {
        if (widget == null)
            return false;

        String className = widget.getClass().getCanonicalName();
        if (className == null)
            return false;

        List<BaseWidget> siblings = widgets.get(className);
        if (siblings == null)
            siblings = new ArrayList<>();

        siblings.add(widget); // TODO: 去重？
        widgets.put(className, siblings);

        return true;
    }

    public boolean removeWidget(int id) {
        for (Map.Entry<String, List<BaseWidget>> entry : widgets.entrySet()) {
            List<BaseWidget> siblings = entry.getValue();
            if (siblings == null)
                continue;

            for (BaseWidget widget : siblings) {
                if (widget.getWidgetId() == id) {
                    siblings.remove(widget);
                    return true;
                }
            }
        }

        return false;
    }

    public List<BaseWidget> getWidgets() {
        List<BaseWidget> list = new ArrayList<>();
        for (Map.Entry<String, List<BaseWidget>> entry : widgets.entrySet()) {
            List<BaseWidget> siblings = entry.getValue();
            if (siblings != null)
                list.addAll(siblings);
        }
        return list;
    }

    public <T extends BaseWidget> List<T> getWidgets(Class<T> clazz) {
        List<T> list = new ArrayList<>();
        if (widgets == null)
            return list;

        // noinspection unchecked
        return (List<T>) widgets.get(clazz.getCanonicalName());
    }

    public <T extends BaseWidget> T getWidgetById(int id) {
        for (Map.Entry<String, List<BaseWidget>> entry : widgets.entrySet()) {
            List<BaseWidget> siblings = entry.getValue();
            if (siblings == null)
                continue;

            for (BaseWidget widget : siblings) {
                if (widget.getWidgetId() == id) // noinspection unchecked
                    return (T) widget;
            }
        }

        return null;
    }

    @NonNull
    @Override
    public String toString() {
        // 重要。遍历 widgets，将其 key 生成至 widgetClassNames
        widgetClassNames = new ArrayList<>();
        widgetJsons = new ArrayList<>();
        for (Map.Entry<String, List<BaseWidget>> entry : widgets.entrySet()) {
            widgetClassNames.add(entry.getKey());

            String json = JsonUtils.toJson(entry.getValue());
            widgetJsons.add(json);
        }

        return "WidgetRegistry{" +
            "formatVersion=" + formatVersion +
            ", widgetClassNames=" + widgetClassNames +
            ", widgetJsons=" + widgetJsons +
            '}';
    }

    public static void load() {
        final Logger LOGGER = new Logger();

        if (widgetsRegistry == null) {
            LOGGER.info("widgetRegistry == null, loading it...");
            if (Paths.getAppFilePath() == null) {
                LOGGER.error("Paths.appFilePath == null. returned");
                return;
            }

            String filePath = Paths.getAppFilePath() + WIDGET_REGISTRY_FILE;
            LOGGER.info("filePath=" + filePath);

            String json = FileUtils.read(filePath, JsonUtils.EMPTY_JSON_OBJECT_CONTENT);
            LOGGER.info("Before parsed: " + json);

            WidgetRegistry loaded = JsonUtils.fromJson(json, WidgetRegistry.class);
            LOGGER.info("Parsed: " + loaded.toString());

            if (loaded.formatVersion < WIDGET_REGISTRY_FORMAT_VERSION) {
                LOGGER.log("Widget registry migrating...");
                try {
                    // TODO: later
                    // WidgetRegistryMigrator.migrate();
                } catch (Exception exception) {
                    LOGGER.errorDescription("migrating error (not fatal).");
                    LOGGER.error(exception);
                }

                LOGGER.log("migrating done");
                widgetsRegistry = JsonUtils.fromJson(FileUtils.read(filePath, JsonUtils.EMPTY_JSON_OBJECT_CONTENT), WidgetRegistry.class);
                LOGGER.info("Parsed after migrating: " + widgetsRegistry.toString());
            } else {
                widgetsRegistry = loaded;
            }

            // Re-create all widget instances here
            widgetsRegistry.widgets = new HashMap<>();
            int widgetClassCount = widgetsRegistry.widgetClassNames.size();
            assert widgetClassCount == widgetsRegistry.widgetJsons.size();

            for (int i = 0; i < widgetClassCount; i++) {
                String className = widgetsRegistry.widgetClassNames.get(i);

                Class<?> clazz;
                try {
                    clazz = Class.forName(className);
                } catch (ClassNotFoundException e) {
                    clazz = null;
                }

                if (clazz == null)
                    continue;

                List<BaseWidget> siblings = new ArrayList<>();

                json = widgetsRegistry.widgetJsons.get(i);
                try {
                    JSONArray ja = new JSONArray(json);
                    for (int j = 0; j < ja.length(); j++) {
                        JSONObject jo = ja.getJSONObject(j);
                        String oj = jo.toString();

                        BaseWidget widget = (BaseWidget) JsonUtils.fromJson(oj, clazz);
                        siblings.add(widget);
                    }
                } catch (Exception e) {
                    LOGGER.error(e);
                }

                widgetsRegistry.widgets.put(className, siblings);
            }

            save();
        } else {
            LOGGER.info("widgetRegistry != null, loaded already.");
        }

        LOGGER.done();
    }

    public static void save() {
        final Logger LOGGER = new Logger();

        if (widgetsRegistry != null) {
            LOGGER.info("widgetsRegistry != null, saving it...");
            if (Paths.getAppFilePath() == null) {
                LOGGER.error("Paths.appFilePath == null. returned");
                LOGGER.returned();
                return;
            }

            String json = JsonUtils.toJson(widgetsRegistry);
            LOGGER.log("Before save, widgetsRegistry->JSON: " + json);
            FileUtils.write((Paths.getAppFilePath() + WIDGET_REGISTRY_FILE), json);
            LOGGER.log("Saved! widgetsRegistry=" + widgetsRegistry.toString());
        } else {
            LOGGER.info("widgetsRegistry == null, cannot be saved.");
        }

        LOGGER.done();
    }
}
