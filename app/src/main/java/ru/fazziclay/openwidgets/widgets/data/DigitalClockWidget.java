package ru.fazziclay.openwidgets.widgets.data;

import org.json.JSONObject;

public class DigitalClockWidget extends BaseWidget {
    int a = 0;

    public DigitalClockWidget(int widgetType) {
        super(widgetType);
    }

    @Override
    public String toString() {
        return "DigitalClockWidget{" +
                "widgetType=" + widgetType +
                '}';
    }



    /**
     * @return JSON Эксеппляр данного объекта
     */
    @Override
    public JSONObject toJSON() {
        return super.toJSON();
    }
}
