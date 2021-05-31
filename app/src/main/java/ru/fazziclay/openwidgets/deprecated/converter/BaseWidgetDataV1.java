package ru.fazziclay.openwidgets.deprecated.converter;

import org.json.JSONException;
import org.json.JSONObject;

public class BaseWidgetDataV1 {
    public int widgetType;

    public BaseWidgetDataV1(int widgetType) {
        this.widgetType = widgetType;
    }

    @Override
    public String toString() {
        return "BaseWidgetDataV1{" +
                "widgetType=" + widgetType +
                '}';
    }

    public JSONObject toConvertedJson() {
        try {
            return new JSONObject().put("widget_type", widgetType);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }
}
