package com.c0rdination.openwidgets.android.activity;

import android.content.pm.PackageInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.c0rdination.openwidgets.AppConfig;
import com.c0rdination.openwidgets.Logger;
import com.c0rdination.openwidgets.R;
import com.c0rdination.openwidgets.data.settings.SettingsData;
import com.c0rdination.openwidgets.data.widgets.WidgetRegistry;

public class AboutActivity extends AppCompatActivity {
    private static final int TEXT_SIZE = 18;
    private static final int TEXT_PADDING = 15;
    private static final int TEXT_LINKS_COLOR = Color.CYAN;

    private static final String ABOUT_AUTHOR_TEXT = AppConfig.ABOUT_AUTHOR_TEXT;
    private static final String ABOUT_DONATE_TEXT = AppConfig.ABOUT_DONATE_TEXT;
    private static final String ABOUT_APP_SOURCE_CODE_TEXT = AppConfig.ABOUT_APP_SOURCE_CODE_TEXT;
    private static final String ABOUT_APP_INFO_TEXT = AppConfig.ABOUT_APP_INFO_TEXT;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Logger LOGGER = new Logger();
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
        final TextView aboutSourceCodeTextView = new TextView(this);
        final TextView aboutAppInfoTextView = new TextView(this);

        aboutAuthorTextView.setPadding(TEXT_PADDING, TEXT_PADDING, TEXT_PADDING, TEXT_PADDING);
        aboutDonateTextView.setPadding(TEXT_PADDING, TEXT_PADDING, TEXT_PADDING, TEXT_PADDING);
        aboutSourceCodeTextView.setPadding(TEXT_PADDING, TEXT_PADDING, TEXT_PADDING, TEXT_PADDING);
        aboutAppInfoTextView.setPadding(TEXT_PADDING, TEXT_PADDING, TEXT_PADDING, TEXT_PADDING);

        aboutAuthorTextView.setTextSize(TEXT_SIZE);
        aboutDonateTextView.setTextSize(TEXT_SIZE);
        aboutSourceCodeTextView.setTextSize(TEXT_SIZE);
        aboutAppInfoTextView.setTextSize(TEXT_SIZE);

        aboutAuthorTextView.setText(ABOUT_AUTHOR_TEXT);
        aboutDonateTextView.setText(ABOUT_DONATE_TEXT);
        aboutSourceCodeTextView.setText(ABOUT_APP_SOURCE_CODE_TEXT);

        // Enable hypertext
        aboutAuthorTextView.setLinkTextColor(TEXT_LINKS_COLOR);
        aboutAuthorTextView.setLinksClickable(true);
        Linkify.addLinks(aboutAuthorTextView, Linkify.ALL);
        aboutDonateTextView.setLinkTextColor(TEXT_LINKS_COLOR);
        aboutDonateTextView.setLinksClickable(true);
        Linkify.addLinks(aboutDonateTextView, Linkify.ALL);
        aboutSourceCodeTextView.setLinkTextColor(TEXT_LINKS_COLOR);
        aboutSourceCodeTextView.setLinksClickable(true);
        Linkify.addLinks(aboutSourceCodeTextView, Linkify.ALL);

        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            aboutAppInfoTextView.setText(ABOUT_APP_INFO_TEXT
                    .replace("%AppVersionBuild%", String.valueOf(pInfo.versionCode))
                    .replace("%AppVersionName%", pInfo.versionName)
                    .replace("%WidgetRegistryFormatVersion%", String.valueOf(WidgetRegistry.WIDGET_REGISTRY_FORMAT_VERSION))
                    .replace("%SettingsDataFormatVersion%", String.valueOf(SettingsData.SETTINGS_FORMAT_VERSION))
                    .replace("%InstanceUUID%", SettingsData.getSettingsData().getInstanceUUID()));

        } catch (Exception e) {
            LOGGER.errorDescription("Error for get app info.");
            LOGGER.error(e);
        }

        background.addView(aboutAuthorTextView);
        background.addView(aboutDonateTextView);
        background.addView(aboutSourceCodeTextView);
        background.addView(aboutAppInfoTextView);

        LOGGER.done();
    }
}
