package ru.fazziclay.openwidgets.widgets.data;

import android.view.Gravity;

import org.json.JSONException;
import org.json.JSONObject;

import ru.fazziclay.openwidgets.activity.MainActivity;
import ru.fazziclay.openwidgets.cogs.Utils;

public class DateWidget extends BaseWidget {
    public static DateWidget defaultWidget = new DateWidget("Customize!\n%H:%M:%S", 40, 2, "#ffffff", "#00000000", "#44444444", Gravity.CENTER);
    public static final int type = 1;

    public String pattern;
    public int patternSize;
    public int patternSizeUnits;
    public String patternColor;
    public String patternBackgroundColor;
    public String backgroundColor;
    public int backgroundGravity;

    public DateWidget(String pattern, int patternSize, int patternSizeUnits, String patternColor, String patternBackgroundColor, String backgroundColor, int backgroundGravity) {
        super(DateWidget.type);
        this.pattern = pattern;
        this.patternSize = patternSize;
        this.patternSizeUnits = patternSizeUnits;
        this.patternColor = patternColor;
        this.patternBackgroundColor = patternBackgroundColor;
        this.backgroundColor = backgroundColor;
        this.backgroundGravity = backgroundGravity;
    }

    @Override
    public String toString() {
        return "DateWidget{" +
                "pattern='" + pattern + '\'' +
                ", patternSize=" + patternSize +
                ", patternSizeUnits=" + patternSizeUnits +
                ", patternColor='" + patternColor + '\'' +
                ", patternBackgroundColor='" + patternBackgroundColor + '\'' +
                ", backgroundColor='" + backgroundColor + '\'' +
                ", backgroundGravity=" + backgroundGravity +
                '}';
    }

    public JSONObject toJSON() {
        try {
            return new JSONObject()
                    .put("widget_type", type)
                    .put("pattern", pattern)
                    .put("pattern_size", patternSize)
                    .put("pattern_size_units", patternSizeUnits)
                    .put("pattern_color", patternColor)
                    .put("pattern_background_color", patternBackgroundColor)
                    .put("background_color", backgroundColor)
                    .put("background_gravity", backgroundGravity);
        } catch (JSONException e) {
            Utils.showMessage(MainActivity.getInstance(), "data.DateWidget.toJSON(): "+e);
            e.printStackTrace();
        }
        return new JSONObject();
    }
}
