package com.c0rdination.openwidgets.util;

import java.util.Random;

public class NumberUtils {
    public static int getRandom(int minimum, int maximum) {
        if (minimum == -2147483648)
            minimum = 1;

        Random random = new Random(System.currentTimeMillis() + System.nanoTime());
        return random.nextInt(maximum - minimum + 1) + minimum;
    }

    public static String intToFixedLengthString(int number, int fixedLength) {
        return String.format("%0" + fixedLength + "d", number);
    }
}
