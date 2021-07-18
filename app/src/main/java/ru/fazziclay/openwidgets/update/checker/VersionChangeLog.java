package ru.fazziclay.openwidgets.update.checker;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

public class VersionChangeLog {
    String _default;
    HashMap<String, String> locales;

    public String getChangeLog(String language) {
        if (locales.containsKey(language)) {
            return locales.get(language);
        }
        return _default;
    }

    @NonNull
    @Override
    public String toString() {
        return "VersionChangeLog{" +
                "_default='" + _default + '\'' +
                ", locales=" + locales +
                '}';
    }

    public VersionChangeLog(
            String _default,
            HashMap<String, String> locales
    ) {
        this._default = _default;
        this.locales = locales;
    }

    public static VersionChangeLog fromJSON(JSONObject jsonObject) throws JSONException {
        if (jsonObject == null) return null;
        HashMap<String, String> locales = new HashMap<>();
        Iterator<String> keys = jsonObject.keys();

        while (keys.hasNext()) {
            String key = keys.next();
            String value = jsonObject.getString(key);
            if (!key.equals("default")) {
                locales.put(key, value);
            }
        }

        return new VersionChangeLog(jsonObject.getString("default"), locales);
    }
}