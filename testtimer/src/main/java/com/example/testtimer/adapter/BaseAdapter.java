package com.example.testtimer.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * <p>@author : tyh</p>
 * <p>@time : 2018/06/20</p>
 * <p>@for : $s$</p>
 * <p></p>
 */
public abstract class BaseAdapter extends RecyclerView.Adapter<Holder> {
    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(getItemLayoutByType(viewType), parent, false);
        return new Holder(view);
    }

    /**
     * 获取item layout
     */
    protected abstract @LayoutRes int getItemLayoutByType(int viewType);

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position, @NonNull List<Object> payloads) {
        if (payloads.size() == 0) {
            bindNormal(holder, position);
        } else {
            reBindPart(holder, position, payloads);
        }
    }

    /**
     * 重新绑定部分数据
     */
    protected abstract void reBindPart(Holder holder, int position, List<Object> payloads);

    /**
     * 绑定所有数据
     */
    protected abstract void bindNormal(Holder holder, int position);

}
