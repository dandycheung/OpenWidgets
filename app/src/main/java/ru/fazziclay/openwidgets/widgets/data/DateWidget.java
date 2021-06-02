package ru.fazziclay.openwidgets.widgets.data;

import org.json.JSONException;
import org.json.JSONObject;

import ru.fazziclay.openwidgets.activity.Main;
import ru.fazziclay.openwidgets.cogs.Utils;

public class DateWidget extends BaseWidget {
    public String pattern;

    public DateWidget(String pattern) {
        super(WidgetType.DateWidget);
        this.pattern = pattern;
    }

    @Override
    public String toString() {
        return "DateWidget{" +
                "pattern='" + pattern + '\'' +
                '}';
    }

    public JSONObject toJSON() {
        try {
            return new JSONObject().put("widget_type", widgetType).put("pattern", pattern);
        } catch (JSONException e) {
            Utils.showMessage(Main.getInstance(), "data.DateWidget.toJSON(): "+e);
            e.printStackTrace();
        }
        return new JSONObject();
    }
}
