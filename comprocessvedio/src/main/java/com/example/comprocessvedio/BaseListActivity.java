package com.example.comprocessvedio;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.comprocessvedio.adapter.BaseAdapter;

/**
 * <p>@author : tyh</p>
 * <p>@time : 2018/06/21</p>
 * <p>@for : $s$</p>
 * <p></p>
 */
public abstract class BaseListActivity<T extends BaseAdapter> extends AppCompatActivity {

    RecyclerView mRv;
    T mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);
        mRv = findViewById(R.id.rv);
        mRv.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = getAdapter();
        mRv.setAdapter(mAdapter);
    }

    public abstract T getAdapter();
}
