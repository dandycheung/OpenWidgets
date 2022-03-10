package ru.fazziclay.openwidgets.android.activity;

import android.content.pm.PackageInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import ru.fazziclay.openwidgets.Logger;
import ru.fazziclay.openwidgets.R;
import ru.fazziclay.openwidgets.data.settings.SettingsData;
import ru.fazziclay.openwidgets.data.widgets.WidgetRegistry;

public class AboutActivity extends AppCompatActivity {
    private static final int TEXT_SIZE = 18;
    private static final int TEXT_PADDING = 15;
    private static final int TEXT_LINKS_COLOR = Color.CYAN;

    private static final String ABOUT_AUTHOR_TEXT = (
            "Author (Developer):" + "\n" +
                    " - fazziclay@gmail.com" + "\n" +
                    " - https://fazziclay.github.io"
    );
    private static final String ABOUT_DONATE_TEXT = (
            "Donate:" + "\n" +
                    " - https://fazziclay.github.io/donate"
    );
    private static final String ABOUT_APP_SOURCE_CODE_TEXT = (
            "Source Code:" + "\n" +
                    " - https://github.com/fazziclay/openwidgets"
    );
    private static final String ABOUT_APP_INFO_TEXT = (
            "App Info:" + "\n" +
                    " - AppVersionBuild: %AppVersionBuild%" + "\n" +
                    " - AppVersionName: %AppVersionName%" + "\n" +
                    " - WidgetRegistryFormatVersion: %WidgetRegistryFormatVersion%" + "\n" +
                    " - SettingsDataFormatVersion: %SettingsDataFormatVersion%" + "\n" +
                    " - IID: %InstanceUUID%"
    );

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
