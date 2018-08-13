package com.example.testmemory;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {

    ViewGroup mContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContent = findViewById(R.id.ll_content);
    }

    public void jump(View view) {

    }

    @Override
    protected void onDestroy() {
//        ViewGroup parent = (ViewGroup) mContent.getParent();
//        emptyAllView(parent);
        super.onDestroy();
        setContentView(R.layout.empty);
    }

    private void emptyAllView(View view) {
        if (view instanceof ViewGroup) {
            int childCount = ((ViewGroup) view).getChildCount();
            for (int i = childCount-1; i >= 0; i--) {
                View childView = ((ViewGroup) view).getChildAt(i);
                emptyAllView(childView);
            }
            view = null;
        }else {
            view = null;
        }
    }
}
