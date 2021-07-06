package ru.fazziclay.openwidgets.activity;

import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import ru.fazziclay.openwidgets.R;
import ru.fazziclay.openwidgets.UpdateChecker;
import ru.fazziclay.openwidgets.widgets.data.WidgetsData;

public class AboutActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.activityTitle_about);

        String appVersionName;
        int appVersionBuild = -1;
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            appVersionName = pInfo.versionName;
            appVersionBuild = pInfo.versionCode;
        } catch (Exception e) {
            appVersionName = "Error: " + e.toString();
        }

        TextView textView = new TextView(this);
        textView.setPadding(16, 16, 16, 16);
        textView.setText(("Author:\n" +
                " - fazziclay@gmail.com\n" +
                " - https://fazziclay.ru\n" +
                "\n" +
                " Donate:\n" +
                " - https://fazziclay.ru/donate\n" +
                "\n" +
                "App Info:\n" +
                " - updateCheckerAppBuild: " + UpdateChecker.appBuild + "\n - androidAppVersionBuild: " + appVersionBuild + "\n - androidAppVersionName: " + appVersionName + "\n - widgetDataVersion: " + WidgetsData.version));

        addContentView(textView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }
}