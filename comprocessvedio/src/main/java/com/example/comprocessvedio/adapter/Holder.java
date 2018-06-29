package com.example.comprocessvedio.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;


/**
 * 通用holder
 */
public class Holder extends RecyclerView.ViewHolder {

    SparseArray<View> mViews;

    public Holder(View itemView) {
        super(itemView);
        mViews = new SparseArray<>();
    }

    /**
     * 设置文字
     */
    public void setText(int viewId, String s) {
        TextView tv = getView(viewId);
        if (tv != null) {
            tv.setText(s);
        }
    }

    /**
     * @return 当前item的Context
     */
    public Context getContext() {
        return itemView.getContext();
    }

    /**
     * 对view设置点击事件
     */
    public void setOnclickListener(int id, View.OnClickListener onClickListener) {
        View view = getView(id);
        if (view != null) {
            view.setOnClickListener(onClickListener);
        }
    }

    /**
     * 获取对应id的View
     */
    public <T extends View> T getView(int viewId) {
        T view = null;
        try {
            view = (T) mViews.get(viewId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (view == null) {
            try {
                view = itemView.findViewById(viewId);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (view != null) {
                mViews.put(viewId, view);
            }
        }
        return view;
    }

    public void setOnItemClickListener(View.OnClickListener onClickListener) {
        itemView.setOnClickListener(onClickListener);
    }
}
