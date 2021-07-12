package ru.fazziclay.openwidgets.utils;

public class ByteUtils {
    public static String byteToHex(int value) {
        String hex = "00".concat(Integer.toHexString(value));
        return hex.substring(hex.length()-2);
    }
}
