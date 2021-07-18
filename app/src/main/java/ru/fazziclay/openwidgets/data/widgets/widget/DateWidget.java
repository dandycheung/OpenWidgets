package ru.fazziclay.openwidgets.data.widgets.widget;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;

import ru.fazziclay.openwidgets.R;
import ru.fazziclay.openwidgets.data.widgets.WidgetsData;
import ru.fazziclay.openwidgets.util.TimeUtils;

public class DateWidget extends BaseWidget {
    String pattern;
    int patternSize;
    String patternColor;
    String patternBackgroundColor;
    String backgroundColor;
    int backgroundGravity;


    public DateWidget(int widgetId) {
        super(widgetId);
        this.pattern = "%H:%M:%S";
        this.patternSize = 60;
        this.patternColor = "#ffffff";
        this.patternBackgroundColor = "#00000000";
        this.backgroundColor = "#22222222";
        this.backgroundGravity = Gravity.CENTER;
    }

    @Override
    public void delete() {
        WidgetsData.getWidgetsData().getDateWidgets().remove(this);
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
                ", backgroundColor='" + backgroundColor + '\'' +
                ", backgroundGravity=" + backgroundGravity +
                ", widgetId=" + widgetId +
                '}';
    }

    @Override
    public void updateWidget(Context context) {
        RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.widget_date);
        view.setTextViewText(R.id.widget_date_text, TimeUtils.dateFormat(pattern));
        view.setTextViewTextSize(R.id.widget_date_text, 2, patternSize);
        view.setTextColor(R.id.widget_date_text, Color.parseColor(patternColor));
        view.setInt(R.id.widget_date_text, "setBackgroundColor", Color.parseColor(patternBackgroundColor));
        view.setInt(R.id.widget_date_background, "setBackgroundColor", Color.parseColor(backgroundColor));
        view.setInt(R.id.widget_date_background, "setGravity", backgroundGravity);

        rawUpdateWidget(context, view);
    }
}
