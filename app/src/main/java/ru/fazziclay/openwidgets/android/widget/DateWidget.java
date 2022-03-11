package ru.fazziclay.openwidgets.android.widget;

import android.content.Context;
import android.view.Gravity;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;

import ru.fazziclay.openwidgets.Logger;
import ru.fazziclay.openwidgets.R;
import ru.fazziclay.openwidgets.data.widgets.WidgetRegistry;
import ru.fazziclay.openwidgets.data.widgets.widget.BaseWidget;
import ru.fazziclay.openwidgets.util.ColorUtils;
import ru.fazziclay.openwidgets.util.TimeUtils;

public class DateWidget extends BaseWidget {
    private static final String DEFAULT_PATTERN = "%H:%M:%S";
    private static final int DEFAULT_PATTERN_SIZE = 60;
    private static final String DEFAULT_PATTERN_COLOR = "#ffffff";
    private static final String DEFAULT_PATTERN_BACKGROUND_COLOR = "#00000000";
    private static final int DEFAULT_PATTERN_PADDING = 2;
    private static final String DEFAULT_BACKGROUND_COLOR = "#22222222";
    private static final int DEFAULT_BACKGROUND_GRAVITY = Gravity.CENTER;
    private static final int DEFAULT_BACKGROUND_PADDING = 2;
    
    public String pattern                = DEFAULT_PATTERN;
    public int patternSize               = DEFAULT_PATTERN_SIZE;
    public String patternColor           = DEFAULT_PATTERN_COLOR;
    public String patternBackgroundColor = DEFAULT_PATTERN_BACKGROUND_COLOR;
    public int patternPadding            = DEFAULT_PATTERN_PADDING;
    public String backgroundColor        = DEFAULT_BACKGROUND_COLOR;
    public int backgroundGravity         = DEFAULT_BACKGROUND_GRAVITY;
    public int backgroundPadding         = DEFAULT_BACKGROUND_PADDING;

    public DateWidget(int widgetId) {
        super(widgetId);
        restoreToDefaults();
    }

    @Override
    public void delete() {
        final Logger LOGGER = new Logger();
        LOGGER.info("widgetId: " + widgetId);

        WidgetRegistry.getWidgetRegistry().removeWidget(widgetId);
        WidgetRegistry.save();

        LOGGER.done();
    }

    @Override
    public void restoreToDefaults() {
        final Logger LOGGER = new Logger();
        LOGGER.info("widgetId: " + widgetId);

        this.pattern                = DEFAULT_PATTERN;
        this.patternSize            = DEFAULT_PATTERN_SIZE;
        this.patternColor           = DEFAULT_PATTERN_COLOR;
        this.patternBackgroundColor = DEFAULT_PATTERN_BACKGROUND_COLOR;
        this.patternPadding         = DEFAULT_PATTERN_PADDING;
        this.backgroundColor        = DEFAULT_BACKGROUND_COLOR;
        this.backgroundGravity      = DEFAULT_BACKGROUND_GRAVITY;
        this.backgroundPadding      = DEFAULT_BACKGROUND_PADDING;
        WidgetRegistry.save();

        LOGGER.done();
    }

    public void loadFromAnotherWidget(DateWidget from) {
        final Logger LOGGER = new Logger();

        LOGGER.info("widgetId: " + widgetId);
        LOGGER.info("from id: " + from.widgetId);

        this.pattern                = from.pattern;
        this.patternSize            = from.patternSize;
        this.patternColor           = from.patternColor;
        this.patternBackgroundColor = from.patternBackgroundColor;
        this.patternPadding         = from.patternPadding;
        this.backgroundColor        = from.backgroundColor;
        this.backgroundGravity      = from.backgroundGravity;
        this.backgroundPadding      = from.backgroundPadding;

        WidgetRegistry.save();

        LOGGER.done();
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
        view.setTextViewText(R.id.widget_date_pattern, ColorUtils.colorizeText(TimeUtils.dateFormat(pattern), textColor));
        view.setTextViewTextSize(R.id.widget_date_pattern, 2, patternSize);
        view.setTextColor(R.id.widget_date_pattern, textColor);
        view.setInt(R.id.widget_date_pattern, "setBackgroundColor", ColorUtils.parseColor(patternBackgroundColor, DEFAULT_PATTERN_BACKGROUND_COLOR));
        view.setInt(R.id.widget_date_background, "setBackgroundColor", ColorUtils.parseColor(backgroundColor, DEFAULT_BACKGROUND_COLOR));
        view.setInt(R.id.widget_date_background, "setGravity", backgroundGravity);
        view.setViewPadding(R.id.widget_date_pattern, patternPadding, patternPadding, patternPadding, patternPadding);
        view.setViewPadding(R.id.widget_date_background, backgroundPadding, backgroundPadding, backgroundPadding, backgroundPadding);

        return view;
    }
}
