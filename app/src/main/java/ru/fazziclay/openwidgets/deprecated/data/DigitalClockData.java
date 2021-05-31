package ru.fazziclay.openwidgets.deprecated.data;

public class DigitalClockData extends BaseWidgetData {
    String text;
    String textColor;
    int textStyle;
    int textSize;
    String backgroundColor;

    public DigitalClockData(String text, String textColor, int textStyle, int textSize, String backgroundColor) {
        super(0);
        this.text = text;
        this.textColor = textColor;
        this.textStyle = textStyle;
        this.textSize = textSize;
        this.backgroundColor = backgroundColor;
    }

    @Override
    public String toString() {
        return "DigitalClockData{" +
                "text='" + text + '\'' +
                ", textColor='" + textColor + '\'' +
                ", textStyle=" + textStyle +
                ", textSize=" + textSize +
                ", backgroundColor='" + backgroundColor + '\'' +
                '}';
    }
}
