package ru.fazziclay.openwidgets.widgets.data;

import org.json.JSONException;
import org.json.JSONObject;

import ru.fazziclay.openwidgets.activity.Main;
import ru.fazziclay.openwidgets.cogs.Utils;

/**
* Суть: Базовый объект настроек виджета
* Версия: 2
* Переменные:
*   widgetType - тип виджета, собственно этот параметр и определяет будет ли использоватся этот объект или его наследники
*
* Функции:
*   toString() - Преобразовать этот объект в строку, для debug анализа
*   toJSON() - Преобразовать этот объект в JSON Объект, для сохранения в JSON файл
* */
public class BaseWidget {
    public int widgetType;

    public BaseWidget(int widgetType) {
        this.widgetType = widgetType;
    }


    @Override
    public String toString() {
        return "BaseWidget{" +
                "widgetType=" + widgetType +
                '}';
    }

    /**
     * @return JSON Эксеппляр данного объекта
     */
    public JSONObject toJSON() {
        try {
            return new JSONObject().put("widget_type", widgetType);
        } catch (JSONException e) {
            Utils.showMessage(Main.getInstance(), "data.BaseWidget.toJSON(): "+e);
            e.printStackTrace();
        }
        return new JSONObject();
    }
}
