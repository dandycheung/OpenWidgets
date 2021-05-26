package ru.fazziclay.openwidgets.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import ru.fazziclay.openwidgets.R;
import ru.fazziclay.openwidgets.WidgetsUpdaterService;
import ru.fazziclay.openwidgets.cogs.FileUtil;
import ru.fazziclay.openwidgets.cogs.Utils;
import ru.fazziclay.openwidgets.cogs.WidgetsManager;

import static android.content.DialogInterface.BUTTON_NEGATIVE;
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

        Button testDialog                = (Button) findViewById(R.id.debug_testDialog);
        Button viewLog                   = (Button) findViewById(R.id.debug_viewLog);

        // widgets
        Button viewWidgetsJsonContent    = (Button) findViewById(R.id.debug_viewWidgetsJsonContent);
        Button addWidget                 = (Button) findViewById(R.id.debug_addWidget);
        Button removeWidget              = (Button) findViewById(R.id.debug_removeWidget);
        Button isWidgetExist             = (Button) findViewById(R.id.debug_isWidgetExist);

        // Service
        Button startWidgetsUpdaterService       = (Button) findViewById(R.id.debug_startWidgetsUpdaterService);
        Button stopWidgetsUpdaterService        = (Button) findViewById(R.id.debug_stopWidgetsUpdaterService);
        Button isStartedWidgetsUpdaterService   = (Button) findViewById(R.id.debug_isStartedWidgetsUpdaterService);
        Button startedServicesList              = (Button) findViewById(R.id.debug_startedServicesList);

        // Utils
        Button conventToMegaHourFormat      = (Button) findViewById(R.id.debug_conventToMegaHourFormat);

        conventToMegaHourFormat.setOnClickListener(v -> {
            Utils.log("[DEBUG_ACTIVITY] [Button] conventToMegaHourFormat");
            EditText viewSource = new EditText(this);
            viewSource.setTextSize(30);
            viewSource.setText((CharSequence) "");
            viewSource.setInputType(InputType.TYPE_CLASS_NUMBER);
            viewSource.setHint((CharSequence) "source");

            EditText viewMax = new EditText(this);
            viewMax.setTextSize(30);
            viewMax.setText((CharSequence) "");
            viewMax.setInputType(InputType.TYPE_CLASS_NUMBER);
            viewMax.setHint((CharSequence) "max");



            final AlertDialog dialog = new AlertDialog.Builder(Debug.this).create();
            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.VERTICAL);

            layout.addView(viewSource);
            layout.addView(viewMax);

            dialog.setTitle("addWidget");
            dialog.setButton(BUTTON_POSITIVE, "Применить", (dialog0, which) -> {
                int s = Integer.parseInt(viewSource.getText().toString());
                int m = Integer.parseInt(viewMax.getText().toString());
                int a = Utils.conventToMegaHourFormat(s, m);
                StringPicker(String.valueOf(a), "RESPONSE: ", "null", 15);
            });
            dialog.setButton(BUTTON_NEGATIVE, "Отмена", (dialog2, which) -> {});

            dialog.setView(layout);
            dialog.show();
        });

        // Тестовый диолог
        testDialog.setOnClickListener(v -> {
            EditText view = new EditText(this);
            view.setTextSize(30);
            view.setText((CharSequence) "Text");

            final AlertDialog dialog = new AlertDialog.Builder(Debug.this).create();
            dialog.setTitle("Title");
            dialog.setButton(BUTTON_POSITIVE, "POSIT", (dialog0, which) -> {
                Utils.showMessage(this, "BUTTON_POSITIVE: clicked!");
            });
            dialog.setButton(BUTTON_NEUTRAL, "NEUTR", (dialog1, which) -> {
                Utils.showMessage(this, "BUTTON_NEUTRAL: clicked!");
            });
            dialog.setButton(BUTTON_NEGATIVE, "NEGATI", (dialog2, which) -> {
                Utils.showMessage(this, "BUTTON_NEGATIVE: clicked!");
            });

            dialog.setView(view);
            dialog.show();
        });

        // Просмотреть логи
        viewLog.setOnClickListener(v -> {
            EditText view = new EditText(this);
            view.setTextSize(30);
            view.setText((CharSequence) Utils.log_text);

            final AlertDialog dialog = new AlertDialog.Builder(Debug.this).create();
            dialog.setTitle("Logs");
            dialog.setView(view);
            dialog.show();
        });

        // widgets
        viewWidgetsJsonContent.setOnClickListener(v -> {
            EditText view = new EditText(this);
            view.setTextSize(18);
            view.setText((CharSequence) FileUtil.readFile(APP_PATH + "/widgets.json"));

            final AlertDialog dialog = new AlertDialog.Builder(Debug.this).create();
            dialog.setTitle("widgets.json");
            dialog.setButton(BUTTON_POSITIVE, "OK", (dialog0, which) -> {});
            dialog.setButton(BUTTON_NEGATIVE, "SAVE", (dialog2, which) -> {
                FileUtil.writeFile(APP_PATH + "/widgets.json", view.getText().toString());
                try {
                    WidgetsManager.widgets = new JSONObject(view.getText().toString());

                } catch (JSONException e) {
                    WidgetsManager.widgets = new JSONObject();
                    Utils.showMessage(this, "Save error: "+e.toString());
                }
            });

            dialog.setView(view);
            dialog.show();
        });

        addWidget.setOnClickListener(v -> {
            Utils.log("[DEBUG_ACTIVITY] [Button] addWidget");
            EditText viewId = new EditText(this);
            viewId.setTextSize(30);
            viewId.setText((CharSequence) "");
            viewId.setInputType(InputType.TYPE_CLASS_NUMBER);
            viewId.setHint((CharSequence) "widget id");

            EditText viewType = new EditText(this);
            viewType.setTextSize(30);
            viewType.setText((CharSequence) "");
            viewType.setInputType(InputType.TYPE_CLASS_NUMBER);
            viewType.setHint((CharSequence) "widget type");



            final AlertDialog dialog = new AlertDialog.Builder(Debug.this).create();
            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.VERTICAL);

            layout.addView(viewId);
            layout.addView(viewType);

            dialog.setTitle("addWidget");
            dialog.setButton(BUTTON_POSITIVE, "Применить", (dialog0, which) -> {
                int id = Integer.parseInt(viewId.getText().toString());
                int type = Integer.parseInt(viewType.getText().toString());
                WidgetsManager.addWidget(id, type);
            });
            dialog.setButton(BUTTON_NEGATIVE, "Отмена", (dialog2, which) -> {});

            dialog.setView(layout);
            dialog.show();
        });

        removeWidget.setOnClickListener(v -> {
            StringPicker("", "Введите id виджета", "removeWidget", 15);
        });

        isWidgetExist.setOnClickListener(v -> {
            StringPicker("", "Введите id виджета", "isWidgetExist", 15);
        });

        // Service
        startWidgetsUpdaterService.setOnClickListener(v -> {
            startService(new Intent(getApplicationContext(), WidgetsUpdaterService.class));
        });

        stopWidgetsUpdaterService.setOnClickListener(v -> {
            stopService(new Intent(getApplicationContext(), WidgetsUpdaterService.class));
        });

        isStartedWidgetsUpdaterService.setOnClickListener(v -> {
            String response = String.valueOf(isServiceRunning(WidgetsUpdaterService.class));
            StringPicker(response, "RESPONSE: ", "null", 35f);
        });

        startedServicesList.setOnClickListener(v -> {
            StringBuilder response = new StringBuilder();

            ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                response.append("\n").append(service.service.getClassName());
            }

            StringPicker(response.toString(), "RESPONSE: ", "null", 15);
        });
    }


    public void StringPicker(String source_value, String title, String type, float text_size) {
        EditText view = new EditText(this);
        view.setTextSize(text_size);
        view.setText( String.valueOf(source_value) );

        final AlertDialog dialog = new AlertDialog.Builder(Debug.this).create();
        dialog.setTitle((CharSequence) "Text_Size: " + String.valueOf(view.getTextSize()) + " | " + title);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setButton(BUTTON_POSITIVE, "Применить", (dialog2, which) -> {
            StringPickerCalled(view.getText().toString(), type);
        });

        dialog.setButton(BUTTON_NEUTRAL, "Отмена", (dialog1, which) -> {});

        dialog.setView(view);
        dialog.show();
    }

    /*public void StringPicker(String source_value, String title, String type, float text_size) {
        EditText view = new EditText(this);
        view.setTextSize(text_size);
        view.setText( String.valueOf(source_value) );

        final AlertDialog dialog = new AlertDialog.Builder(Debug.this).create();
        dialog.setTitle((CharSequence) "Text_Size: " + String.valueOf(view.getTextSize()) + " | " + title);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setButton(BUTTON_POSITIVE, "Применить", (dialog2, which) -> {
            StringPickerCalled(view.getText().toString(), type);
        });

        dialog.setButton(BUTTON_NEUTRAL, "Отмена", (dialog1, which) -> {});

        dialog.setView(view);
        dialog.show();
    }*/

    public void StringPickerCalled(String output, String type) {
        if (type.equals("addWidget")) {
            int widgetId      = Integer.parseInt(output.split(" ")[0]);
            int widgetType    = Integer.parseInt(output.split(" ")[1]);

            addWidget(widgetId, widgetType);
        }

        if (type.equals("removeWidget")) {
            removeWidget(Integer.parseInt(output));
        }

        if (type.equals("isWidgetExist")) {
            String response = String.valueOf(isWidgetExist(Integer.parseInt(output)));
            StringPicker(response, "RESPONSE: ", "null", 15);
        }
    }

    public boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Utils.log("isServiceRunning(): true");
                return true;
            }
        }
        Utils.log("isServiceRunning(): false");
        return false;
    }
}