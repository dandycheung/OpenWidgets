package ru.fazziclay.openwidgets.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonUtils {
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static final String EMPTY_JSON_OBJECT_CONTENT = "{}";

    public static <T> T fromJson(String content, Class<T> typeOfT) {
         return GSON.fromJson(content, typeOfT);
    }

    public static String toJson(Object src) {
        return GSON.toJson(src);
    }
}
