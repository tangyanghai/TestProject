package com.example.testtimer;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void toFirstTimerActivity(View view) {
        FirstTimerActivity.start(this);
    }

    public void toMap(View view) {
        String act="android.intent.action.VIEW";
        String cat="android.intent.category.DEFAULT";
        String dat="androidamap://keywordNavi?sourceApplication=softname&keyword=方恒国际中心&style=2";
        String pkg="com.autonavi.minimap";
        Intent intent = new Intent();
        intent.setAction(act);
        intent.addCategory(cat);
        intent.setData(Uri.parse(dat));
        intent.setPackage(pkg);

    }
}
