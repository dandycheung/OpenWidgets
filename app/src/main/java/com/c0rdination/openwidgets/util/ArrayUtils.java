package com.c0rdination.openwidgets.util;

import java.util.Iterator;
import java.util.List;

public class ArrayUtils {
    public static boolean removeByValue(List list, Object value) {
        Iterator var2 = list.iterator();

        Object o;
        do {
            if (!var2.hasNext())
                return false;

            o = var2.next();
        } while(o != value);

        list.remove(o);
        return true;
    }
}
