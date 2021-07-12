package ru.fazziclay.openwidgets.utils;

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
}
