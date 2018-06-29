package com.example.testtimer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.testtimer.adapter.Adapter;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>@author : tyh</p>
 * <p>@time : 2018/06/20</p>
 * <p>@for : $construction$</p>
 * <p></p>
 */
public class FirstTimerActivity extends BaseTimerActivity {

    public static void start(Context context) {
        Intent intent = new Intent(context,FirstTimerActivity.class);
        context.startActivity(intent);
    }

    RecyclerView mRv;
    private Adapter mAdapter;
    private List<Integer> mListTime;

    @Override
    protected void initOnCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_first_timer);
        mRv = findViewById(R.id.rv_first_timer);
        mRv.setLayoutManager(new LinearLayoutManager(this));
        mListTime = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            mListTime.add((int) (Math.random()*1000+100000));
        }
        mAdapter = new Adapter(mListTime);
        mRv.setAdapter(mAdapter);
    }

    @Override
    public void update(long curTime) {
        mAdapter.update();
    }

}
