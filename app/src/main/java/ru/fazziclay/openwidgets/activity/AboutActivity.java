package ru.fazziclay.openwidgets.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import ru.fazziclay.openwidgets.R;
import ru.fazziclay.openwidgets.widgets.data.WidgetsData;

public class AboutActivity extends AppCompatActivity {
    String appVersionName = "error.";
    int appVersionCode = -557;
    int widgetDataVersion = -557;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setTitle("OpenWidgets - About");

        try {
            widgetDataVersion = WidgetsData.version;
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            appVersionName = pInfo.versionName;
            appVersionCode = pInfo.versionCode;
        } catch (Exception e) {
            appVersionName = "Error: " + e.toString();
        }

        TextView textView = new TextView(this);
        textView.setPadding(10, 10, 10, 10);
        textView.setText(("contacts:\n - mail: fazziclay@gmail.com\n - site: https://fazziclay.ru/\n\n\n" +
                "--- appVersionName: "+appVersionName + "\n" +
                "--- appVersionCode: "+appVersionCode + "\n" +
                "--- widgetDataVersion: "+widgetDataVersion + "\n"
                ));

        addContentView(textView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }
}