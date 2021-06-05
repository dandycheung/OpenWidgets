package ru.fazziclay.openwidgets.activity;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import ru.fazziclay.openwidgets.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setTitle("OpenWidgets - About");

        TextView textView = new TextView(this);
        textView.setText(("contacts:\n - mail: fazziclay@gmail.com\n - site: https://fazziclay.ru/"));

        addContentView(textView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }
}