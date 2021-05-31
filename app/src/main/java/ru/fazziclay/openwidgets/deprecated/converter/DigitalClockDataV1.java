package ru.fazziclay.openwidgets.deprecated.converter;

import org.json.JSONException;
import org.json.JSONObject;

public class DigitalClockDataV1 extends BaseWidgetDataV1 {
    String text;
    String textColor;
    int textStyle;
    int textSize;
    String backgroundColor;

    public DigitalClockDataV1(String text, String textColor, int textStyle, int textSize, String backgroundColor) {
        super(0);
        this.text = text;
        this.textColor = textColor;
        this.textStyle = textStyle;
        this.textSize = textSize;
        this.backgroundColor = backgroundColor;
    }

    @Override
    public String toString() {
        return "DigitalClockDataV1{" +
                "text='" + text + '\'' +
                ", textColor='" + textColor + '\'' +
                ", textStyle=" + textStyle +
                ", textSize=" + textSize +
                ", backgroundColor='" + backgroundColor + '\'' +
                '}';
    }

    @Override
    public JSONObject toConvertedJson() {
        try {
            return new JSONObject()
                    .put("widget_type", widgetType)
                    .put("text", text)
                    .put("text_color", textColor)
                    .put("text_style", textStyle)
                    .put("text_size", textSize)
                    .put("background_color", backgroundColor);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }
}

/*
{
    "index": [
        10,
        11
    ],
    "data": {
        "10": {
            "text": {
                ""
            }
         }
    }
}

*/
