package com.example.testtimer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * <p>@author : tyh</p>
 * <p>@time : 2018/06/20</p>
 * <p>@for : $s$</p>
 * <p></p>
 */
public class SecondActivity extends AppCompatActivity {

    RecyclerView mRv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moveable_list);
        mRv = findViewById(R.id.rv_moveable);
        mRv.setLayoutManager(new LinearLayoutManager(this));
        mRv.setAdapter(new MoveAbleAdapter());
    }
}
