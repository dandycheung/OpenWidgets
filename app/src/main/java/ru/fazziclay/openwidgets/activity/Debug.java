package ru.fazziclay.openwidgets.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import ru.fazziclay.openwidgets.R;
import ru.fazziclay.openwidgets.cogs.FileUtil;
import ru.fazziclay.openwidgets.cogs.Utils;

import static android.content.DialogInterface.BUTTON_NEUTRAL;
import static android.content.DialogInterface.BUTTON_POSITIVE;
import static ru.fazziclay.openwidgets.cogs.WidgetsManager.APP_PATH;
import static ru.fazziclay.openwidgets.cogs.WidgetsManager.addWidget;
import static ru.fazziclay.openwidgets.cogs.WidgetsManager.isWidgetExist;
import static ru.fazziclay.openwidgets.cogs.WidgetsManager.removeWidget;

public class Debug extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);

        Button viewWidgetsJsonContent    = (Button) findViewById(R.id.debug_viewWidgetsJsonContent);
        Button addWidget                 = (Button) findViewById(R.id.debug_addWidget);
        Button removeWidget              = (Button) findViewById(R.id.debug_removeWidget);
        Button isWidgetExist             = (Button) findViewById(R.id.debug_isWidgetExist);



        viewWidgetsJsonContent.setOnClickListener(v -> {
            StringPicker(FileUtil.readFile(APP_PATH + "widgets.json"), "Dialog", "null");
        });

        addWidget.setOnClickListener(v -> {
            StringPicker("", "Введите id виджета", "addWidget");
        });

        removeWidget.setOnClickListener(v -> {
            StringPicker("", "Введите id виджета", "removeWidget");
        });

        isWidgetExist.setOnClickListener(v -> {
            StringPicker("", "Введите id виджета", "isWidgetExist");
        });
    }


    public void StringPicker(String source_value, String title, String type) {
        EditText view = new EditText(this);
        view.setText( String.valueOf(source_value) );

        final AlertDialog dialog = new AlertDialog.Builder(Debug.this).create();
        dialog.setTitle((CharSequence) title);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setButton(BUTTON_POSITIVE, "Применить", (dialog2, which) -> {
            StringPickerCalled(view.getText().toString(), type);
        });

        dialog.setButton(BUTTON_NEUTRAL, "Отмена", (dialog1, which) -> {});

        dialog.setView(view);
        dialog.show();
    }

    public void StringPickerCalled(String output, String type) {
        if (type.equals("addWidget")) {
            addWidget(Integer.parseInt(output));
        }

        if (type.equals("removeWidget")) {
            removeWidget(Integer.parseInt(output));
        }

        if (type.equals("isWidgetExist")) {
            String response = String.valueOf(isWidgetExist(Integer.parseInt(output)));
            StringPicker(response, "RESPONSE: ", "null");
        }
    }
}