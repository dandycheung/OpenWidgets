package ru.fazziclay.openwidgets.util;

import android.graphics.Color;

import ru.fazziclay.fazziclaylibs.ByteUtils;

public class ColorUtils {
    public static String colorToHex(int color) {
        int alpha = android.graphics.Color.alpha(color);
        int red = android.graphics.Color.red(color);
        int green = android.graphics.Color.green(color);
        int blue = android.graphics.Color.blue(color);

        String alphaHex = ByteUtils.byteToHex(alpha);
        String redHex = ByteUtils.byteToHex(red);
        String greenHex = ByteUtils.byteToHex(green);
        String blueHex = ByteUtils.byteToHex(blue);

        return "#" + alphaHex +
                redHex +
                greenHex +
                blueHex;
    }

    public static int parseColor(String color, String defaultColor) {
        try {
            return Color.parseColor(color);
        } catch (Exception e) {
            return Color.parseColor(defaultColor);
        }
    }
}
