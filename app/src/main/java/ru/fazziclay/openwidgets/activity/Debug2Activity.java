package ru.fazziclay.openwidgets.activity;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.rarepebble.colorpicker.ColorPickerView;

import org.json.JSONException;

import ru.fazziclay.fazziclaylibs.FileUtils;
import ru.fazziclay.openwidgets.R;
import ru.fazziclay.openwidgets.UpdateChecker;
import ru.fazziclay.openwidgets.service.WidgetsUpdaterService;
import ru.fazziclay.openwidgets.cogs.DialogUtils;
import ru.fazziclay.openwidgets.cogs.Utils;
import ru.fazziclay.openwidgets.widgets.WidgetsManager;
import ru.fazziclay.openwidgets.widgets.data.BaseWidget;
import ru.fazziclay.openwidgets.widgets.data.WidgetsData;

public class Debug2Activity extends AppCompatActivity {
    // Notify
    Button debug2_notification_createChannel;
    Button debug2_notification_sendTest;

    // Buttons
    Button testButton;

    // Services
    Button servicesStartWidgetUpdater;
    Button servicesStopWidgetUpdater;
    Button servicesStartedList;
    TextView debug2_services_updateCheckerCounter;

    // Widgets.data
    Button widgetsDataJSONFILE;
    Button widgetsDataSave;
    Button widgetsDataLoad;
    Button widgetsDataViewJavaVars;
    TextView widgetsDataVariablesText;

    // Widgets.Manager
    Button widgetsManagerAdd;
    Button widgetsManagerRemove;
    Button widgetsManagerGetById;
    Button widgetsManagerIsExist;

    // Update Checker
    Button debug2_tupdateChecker_thisVersion;
    Button debug2_tupdateChecker_getVersion;
    Button debug2_updateChecker_changeVersionsFormat;
    Button debug2_updateChecker_changeAppBuild;

    // Unknown
    Button debug2OpenActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug2);
        setTitle("OpenWidgets - Debug");

        loadViewsVariables();
        loadViews();


        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    debug2_services_updateCheckerCounter.setText(("updateCheckerCounter = "+WidgetsUpdaterService.updateCheckerCounter+" / 4 = "+WidgetsUpdaterService.updateCheckerCounter/4));

                } catch (Exception e) {
                    e.printStackTrace();
                }
                handler.postDelayed(this, 250);
            }
        };
        handler.post(runnable);
    }

    private void loadViewsVariables() {
        servicesStartWidgetUpdater = findViewById(R.id.debug2_servicesStartWidgetUpdater);
        servicesStopWidgetUpdater = findViewById(R.id.debug2_servicesStopWidgetUpdater);
        servicesStartedList = findViewById(R.id.debug2_servicesStartedList);
        widgetsDataViewJavaVars = findViewById(R.id.debug2_widgetsDataViewJavaVars);
        widgetsDataSave = findViewById(R.id.debug2_widgetsDataSave);
        widgetsDataLoad = findViewById(R.id.debug2_widgetsDataLoad);
        widgetsDataJSONFILE = findViewById(R.id.debug2_widgetsDataJSONFILE);
        widgetsDataVariablesText = findViewById(R.id.debug2_widgetsDataVariablesText);
        widgetsManagerAdd = findViewById(R.id.debug2_widgetsManagerAdd);
        widgetsManagerRemove = findViewById(R.id.debug2_widgetsManagerRemove);
        widgetsManagerIsExist = findViewById(R.id.debug2_widgetsManagerIsExist);
        widgetsManagerGetById = findViewById(R.id.debug2_widgetsManagerGetById);
        debug2OpenActivity = findViewById(R.id.debug2OpenActivity);
        debug2_tupdateChecker_getVersion = findViewById(R.id.debug2_tupdateChecker_getVersion);

        debug2_updateChecker_changeVersionsFormat = findViewById(R.id.debug2_updateChecker_changeVersionsFormat);
        debug2_updateChecker_changeAppBuild = findViewById(R.id.debug2_updateChecker_changeAppBuild);

        debug2_tupdateChecker_thisVersion = findViewById(R.id.debug2_tupdateChecker_thisVersion);

        debug2_services_updateCheckerCounter = findViewById(R.id.debug2_services_updateCheckerCounter);
    }


    private void loadViews() {
        debug2_tupdateChecker_thisVersion.setOnClickListener(v -> {
            DialogUtils.notifyDialog(this, "this ver", "appBuild="+UpdateChecker.appBuild+"\nappVersionsUrl="+UpdateChecker.appVersionsUrl+"\nappUpdateCheckerFormatVersion="+UpdateChecker.appUpdateCheckerFormatVersion);
        });

        if (debug2_notification_createChannel != null) {
            debug2_notification_createChannel.setOnClickListener(v -> {
            });
        }
        if (debug2_notification_sendTest != null) {
            debug2_notification_sendTest.setOnClickListener(v -> {
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "DebugTest")
                        .setSmallIcon(R.drawable.thumbnail_border)
                        .setContentTitle("testTitle")
                        .setContentText("123")
                        .setPriority(NotificationCompat.PRIORITY_HIGH);


                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

                // notificationId is a unique int for each notification that you must define
                notificationManager.notify(101, builder.build());

            });
        }
        debug2_updateChecker_changeVersionsFormat.setOnClickListener(v -> {
            DialogUtils.inputDialog(this, "ver format", String.valueOf(UpdateChecker.appUpdateCheckerFormatVersion), "._>", -1, "SET", responseText -> {
                UpdateChecker.appUpdateCheckerFormatVersion = Integer.parseInt(responseText);
            });
        });

        debug2_updateChecker_changeAppBuild.setOnClickListener(v -> {
            DialogUtils.inputDialog(this, "build", String.valueOf(UpdateChecker.appBuild), "._>", -1, "SET", responseText -> {
                UpdateChecker.appBuild = Integer.parseInt(responseText);
            });
        });

        if (testButton != null) {
            testButton.setOnClickListener(v -> {
                Utils.showMessage(this, "Clicked!");


                final ColorPickerView colorPickerView = new ColorPickerView(this);
                colorPickerView.setColor(Color.parseColor("#fff00099"));
                colorPickerView.showAlpha(true);
                colorPickerView.showHex(true);
                colorPickerView.showPreview(true);

                DialogUtils.inputDialog(this, "123", "okk", (re) -> {
                }, colorPickerView);
            });
        }

        servicesStartWidgetUpdater.setOnClickListener(v -> {
            try {
                startService(new Intent(getApplicationContext(), WidgetsUpdaterService.class));
            } catch (Exception e) {
                Utils.showMessage(this, "activity.Debug2: button=servicesStartWidgetsUpdater: error= "+e);
            }
        });

        servicesStopWidgetUpdater.setOnClickListener(v -> {
            try {
                stopService(new Intent(getApplicationContext(), WidgetsUpdaterService.class));
            } catch (Exception e) {
                Utils.showMessage(this, "activity.Debug2: button=servicesStopWidgetsUpdater: error= "+e);
            }
        });

        servicesStartedList.setOnClickListener(v -> {
            try {
                StringBuilder a = new StringBuilder();
                ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                    a.append(service.service.getClassName()).append("\n");
                }

                DialogUtils.notifyDialog(this, "services", a.toString());

            } catch (Exception e) {
                Utils.showMessage(this, "activity.Debug2: button=servicesStartedList: error= "+e);
            }
        });
        widgetsDataJSONFILE.setOnClickListener(v -> DialogUtils.inputDialog(this, "widgets.json", FileUtils.read(WidgetsData.filePath), "._.", -1, "Save", responseText -> FileUtils.write(WidgetsData.filePath, responseText)));
        widgetsDataLoad.setOnClickListener(v -> WidgetsData.load());
        widgetsDataSave.setOnClickListener(v -> WidgetsData.save());
        widgetsDataViewJavaVars.setOnClickListener(v -> DialogUtils.notifyDialog(this, "Java data", "index="+WidgetsData.index+"\n\nwidgets="+WidgetsData.widgets+"\n\nwidgetsDataFile="+WidgetsData.widgetsDataFile));
        widgetsDataVariablesText.setText(("version: "+WidgetsData.version+"\nfilePath: "+WidgetsData.filePath));

        widgetsManagerAdd.setOnClickListener(v -> DialogUtils.inputDialog(this, "add widget", "", "widget id", InputType.TYPE_CLASS_NUMBER, "Add", responseText -> WidgetsManager.addWidget(Integer.parseInt(responseText), new BaseWidget(-2))));
        widgetsManagerRemove.setOnClickListener(v -> DialogUtils.inputDialog(this, "remove widget", "", "widget id", InputType.TYPE_CLASS_NUMBER, "Remove", responseText -> WidgetsManager.removeWidget(Integer.parseInt(responseText))));
        widgetsManagerIsExist.setOnClickListener(v -> DialogUtils.inputDialog(this, "is widget exist", "", "widget id", InputType.TYPE_CLASS_NUMBER, "Check", responseText -> DialogUtils.notifyDialog(this, "response", String.valueOf(WidgetsManager.isWidgetExist(Integer.parseInt(responseText))))));
        widgetsManagerGetById.setOnClickListener(v -> DialogUtils.inputDialog(this, "get widget by id", "", "widget id", InputType.TYPE_CLASS_NUMBER, "Get", responseText -> {
            BaseWidget widget = WidgetsManager.getWidgetById(Integer.parseInt(responseText));
            String json = null;
            if (widget != null) {
                try {
                    json = widget.toJSON().toString(4);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            DialogUtils.notifyDialog(this, "response", "String = "+widget+"\n\nJSON = " + json);
        }));

        debug2OpenActivity.setOnClickListener(v -> {
            Button dateWidgetConfigActivityButton = new Button(this);
            dateWidgetConfigActivityButton.setAllCaps(false);
            dateWidgetConfigActivityButton.setText("DateWidgetConfiguratorActivity");
            dateWidgetConfigActivityButton.setOnClickListener(v1 -> {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), DateWidgetConfiguratorActivity.class);
                startActivity(intent);
            });
            DialogUtils.inputDialog(this, "open activity", "ok", re -> {}, dateWidgetConfigActivityButton);
        });

        debug2_tupdateChecker_getVersion.setOnClickListener(v -> {
            UpdateChecker.getVersion((status, build, name, download) -> {
                runOnUiThread(() -> {
                    DialogUtils.notifyDialog(this, "getVersion()", "status="+status+"\nbuild="+build+"\nname="+name+"\ndownload="+download);
                });
            });
        });
    }
}