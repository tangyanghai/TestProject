package com.example.testtimer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.testtimer.timer.ITimerObserver;
import com.example.testtimer.timer.TimerObservable;

/**
 * <p>@author : tyh</p>
 * <p>@time : 2018/06/20</p>
 * <p>@for : $construction$</p>
 * <p></p>
 */
public abstract class BaseTimerActivity extends AppCompatActivity implements ITimerObserver {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initOnCreate(savedInstanceState);
    }


    @Override
    protected void onStart() {
        super.onStart();
        TimerObservable.getInstance().subscribe(this);
    }

    protected abstract void initOnCreate(Bundle savedInstanceState);


    @Override
    protected void onStop() {
        super.onStop();
        TimerObservable.getInstance().unSubscribe(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public abstract void update(long curTime);
}
