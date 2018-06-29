package com.example.comprocessvedio.adapter;

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
public abstract class BaseAdapter<T> extends RecyclerView.Adapter<Holder> {

    List<T> mDatas;

    public BaseAdapter() {
    }

    public BaseAdapter(List<T> mDatas) {
        this.mDatas = mDatas;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(getItemLayoutByType(viewType), parent, false);
        return new Holder(view);
    }

    /**
     * 获取item layout
     */
    protected abstract @LayoutRes
    int getItemLayoutByType(int viewType);

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
     * <p>重新绑定部分数据</p>
     * <p>正确的刷新单条数据的姿势</p>
     * <p>需要的时候才去重写</p>
     */
    protected void reBindPart(Holder holder, int position, List<Object> payloads) {
    }

    ;

    /**
     * 正常的绑定数据
     */
    protected abstract void bindNormal(Holder holder, int position);

    /**
     * 设置数据
     */
    public void setData(List<T> datas) {
        this.mDatas = datas;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }
}
