package ru.fazziclay.openwidgets.data.widgets.widget;

import android.content.Context;
import android.view.Gravity;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;

import ru.fazziclay.openwidgets.R;
import ru.fazziclay.openwidgets.data.widgets.WidgetsData;
import ru.fazziclay.openwidgets.util.ColorUtils;
import ru.fazziclay.openwidgets.util.TimeUtils;

public class DateWidget extends BaseWidget {
    private static final String DEFAULT_PATTERN_COLOR = "#ffffff";
    private static final String DEFAULT_PATTERN_BACKGROUND_COLOR = "#00000000";
    private static final String DEFAULT_BACKGROUND_COLOR = "#22222222";
    
    public String pattern;
    public int patternSize;
    public String patternColor = DEFAULT_PATTERN_COLOR;
    public String patternBackgroundColor = DEFAULT_PATTERN_BACKGROUND_COLOR;
    public int patternPadding = 2;
    public String backgroundColor = DEFAULT_BACKGROUND_COLOR;
    public int backgroundGravity;
    public int backgroundPadding = 2;


    public DateWidget(int widgetId) {
        super(widgetId);
        restoreToDefaults();
    }

    @Override
    public void delete() {
        WidgetsData.getWidgetsData().getDateWidgets().remove(this);
        WidgetsData.save();
    }

    @Override
    public void restoreToDefaults() {
        this.pattern = "%H:%M:%S";
        this.patternSize = 60;
        this.patternColor = DEFAULT_PATTERN_COLOR;
        this.patternBackgroundColor = DEFAULT_PATTERN_BACKGROUND_COLOR;
        this.patternPadding = 2;
        this.backgroundColor = DEFAULT_BACKGROUND_COLOR;
        this.backgroundGravity = Gravity.CENTER;
        this.backgroundPadding = 2;
        WidgetsData.save();
    }

    public void loadFromAnotherWidget(DateWidget from) {
        this.pattern = from.pattern;
        this.patternSize = from.patternSize;
        this.patternColor = from.patternColor;
        this.patternBackgroundColor = from.patternBackgroundColor;
        this.patternPadding = from.patternPadding;
        this.backgroundColor = from.backgroundColor;
        this.backgroundGravity = from.backgroundGravity;
        this.backgroundPadding = from.backgroundPadding;
        WidgetsData.save();
    }

    @NonNull
    @Override
    public String toString() {
        return "DateWidget{" +
                "pattern='" + pattern + '\'' +
                ", patternSize=" + patternSize +
                ", patternColor='" + patternColor + '\'' +
                ", patternBackgroundColor='" + patternBackgroundColor + '\'' +
                ", patternPadding=" + patternPadding +
                ", backgroundColor='" + backgroundColor + '\'' +
                ", backgroundGravity=" + backgroundGravity +
                ", backgroundPadding=" + backgroundPadding +
                ", widgetId=" + widgetId +
                ", flags=" + flags +
                '}';
    }

    @Override
    public RemoteViews updateWidget(Context context) {
        if (patternSize > 1000) {
            patternSize = 1000;
        }
        int textColor = ColorUtils.parseColor(patternColor, DEFAULT_PATTERN_COLOR);

        RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.widget_date);
        view.setTextViewText(R.id.widget_date_text, ColorUtils.colorizeText(TimeUtils.dateFormat(pattern), textColor));
        view.setTextViewTextSize(R.id.widget_date_text, 2, patternSize);
        view.setTextColor(R.id.widget_date_text, textColor);
        view.setInt(R.id.widget_date_text, "setBackgroundColor", ColorUtils.parseColor(patternBackgroundColor, DEFAULT_PATTERN_BACKGROUND_COLOR));
        view.setInt(R.id.widget_date_background, "setBackgroundColor", ColorUtils.parseColor(backgroundColor, DEFAULT_BACKGROUND_COLOR));
        view.setInt(R.id.widget_date_background, "setGravity", backgroundGravity);
        view.setViewPadding(R.id.widget_date_text, patternPadding, patternPadding, patternPadding, patternPadding);
        view.setViewPadding(R.id.widget_date_background, backgroundPadding, backgroundPadding, backgroundPadding, backgroundPadding);
        return view;
    }
}
