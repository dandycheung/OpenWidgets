package ru.fazziclay.openwidgets.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import ru.fazziclay.openwidgets.R;

public class AboutActivity extends AppCompatActivity {
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
                    " - Dev..." // TODO: 13.07.2021 replace 'DEV' to app info
    );

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Context context = this;
        setTitle(R.string.activityTitle_about);

        final LinearLayout CONTENT = new LinearLayout(context);
        final ScrollView backgroundScroll = new ScrollView(context);
        final LinearLayout background = new LinearLayout(context);

        CONTENT.addView(backgroundScroll);
        backgroundScroll.addView(background);
        background.setOrientation(LinearLayout.VERTICAL);

        addContentView(CONTENT, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        final TextView aboutAuthorTextView = new TextView(context);
        final TextView aboutDonateTextView = new TextView(context);
        final TextView aboutAppInfoTextView = new TextView(context);

        aboutAuthorTextView.setPadding(16, 16, 16, 16);
        aboutDonateTextView.setPadding(16, 16, 16, 16);
        aboutAppInfoTextView.setPadding(16, 16, 16, 16);

        aboutAuthorTextView.setText(ABOUT_AUTHOR_TEXT);
        aboutDonateTextView.setText(ABOUT_DONATE_TEXT);
        aboutAppInfoTextView.setText(ABOUT_APP_INFO_TEXT);

        background.addView(aboutAuthorTextView);
        background.addView(aboutDonateTextView);
        background.addView(aboutAppInfoTextView);
    }
}