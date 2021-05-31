package ru.fazziclay.openwidgets.widgets.data;

import org.json.JSONException;
import org.json.JSONObject;

import ru.fazziclay.openwidgets.activity.Main;
import ru.fazziclay.openwidgets.cogs.Utils;

public class TextWidget extends BaseWidget {
    String text;

    public TextWidget(int widgetType, String text) {
        super(widgetType);
        this.text = text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "TextWidget{" +
                "text='" + text + '\'' +
                '}';
    }

    /**
     * @return JSON Эксеппляр данного объекта
     */
    @Override
    public JSONObject toJSON() {
        try {
            return new JSONObject().put("widget_type", widgetType).put("text", text);
        } catch (JSONException e) {
            Utils.showMessage(Main.getInstance(), "data.TextWidget.toJSON(): "+e);
            e.printStackTrace();
        }
        return new JSONObject();
    }
}
