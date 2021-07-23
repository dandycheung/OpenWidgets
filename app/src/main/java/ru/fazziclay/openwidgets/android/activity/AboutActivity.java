package ru.fazziclay.openwidgets.android.activity;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import ru.fazziclay.openwidgets.R;
import ru.fazziclay.openwidgets.data.settings.SettingsData;
import ru.fazziclay.openwidgets.data.widgets.WidgetsData;

public class AboutActivity extends Activity {
    private static final String ABOUT_AUTHOR_TEXT = (
            "Author:" + "\n" +
            " - fazziclay@gmail.com" + "\n" +
            " - https://fazziclay.ru"
    );
    private static final String ABOUT_DONATE_TEXT = (
                    " Donate:" + "\n" +
                    " - https://fazziclay.ru/donate"
    );
    private static final String ABOUT_APP_INFO_TEXT = (
                    "App Info:" + "\n" +
                    " - AppVersionBuild: %AppVersionBuild%" + "\n" +
                    " - AppVersionName: %AppVersionName%" + "\n" +
                    " - WidgetsDataFormatVersion: %WidgetsDataFormatVersion%" + "\n" +
                    " - SettingsDataFormatVersion: %SettingsDataFormatVersion%"
    );


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.activityTitle_about);



        final LinearLayout CONTENT = new LinearLayout(this);
        final ScrollView backgroundScroll = new ScrollView(this);
        final LinearLayout background = new LinearLayout(this);

        CONTENT.addView(backgroundScroll);
        backgroundScroll.addView(background);
        background.setOrientation(LinearLayout.VERTICAL);

        addContentView(CONTENT, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        final TextView aboutAuthorTextView = new TextView(this);
        final TextView aboutDonateTextView = new TextView(this);
        final TextView aboutAppInfoTextView = new TextView(this);

        aboutAuthorTextView.setPadding(16, 16, 16, 16);
        aboutDonateTextView.setPadding(16, 16, 16, 16);
        aboutAppInfoTextView.setPadding(16, 16, 16, 16);

        aboutAuthorTextView.setText(ABOUT_AUTHOR_TEXT);
        aboutDonateTextView.setText(ABOUT_DONATE_TEXT);
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            aboutAppInfoTextView.setText(ABOUT_APP_INFO_TEXT
                    .replace("%AppVersionBuild%", String.valueOf(pInfo.versionCode))
                    .replace("%AppVersionName%", pInfo.versionName)
                    .replace("%WidgetsDataFormatVersion%", String.valueOf(WidgetsData.WIDGETS_FORMAT_VERSION))
                    .replace("%SettingsDataFormatVersion%", String.valueOf(SettingsData.SETTINGS_FORMAT_VERSION)));

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        background.addView(aboutAuthorTextView);
        background.addView(aboutDonateTextView);
        background.addView(aboutAppInfoTextView);
    }
}