package com.example.testtimer.adapter;

import android.annotation.LayoutRes;
import android.view.View;

import com.example.testtimer.FirstTimerActivity;
import com.example.testtimer.R;

import java.util.List;

/**
 * <p>@author : tyh</p>
 * <p>@time : 2018/06/20</p>
 * <p>@for : $construction$</p>
 * <p></p>
 */
public class Adapter extends BaseAdapter {

    List<Integer> mListTime;

    int count;

    public Adapter(List<Integer> mListTime) {
        this.mListTime = mListTime;
    }

    public @LayoutRes int getItemLayoutByType(int viewType){
        return R.layout.item_timer;
    }

    @Override
    protected void reBindPart(final Holder holder, int position, List<Object> payloads) {
        int id = R.id.tv_time;
        Integer integer = mListTime.get(position);
        integer -= count;
        if (integer < 0) {
            integer = 0;
        }
        holder.setText(id, integer.toString());
        holder.setOnclickListener(id,new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                FirstTimerActivity.start(holder.getContext());
            }
        });
    }

    @Override
    protected void bindNormal(Holder holder, int position) {
        int id = R.id.tv_time;
        Integer integer = mListTime.get(position);
        integer -= count;
        if (integer < 0) {
            integer = 0;
        }
        holder.setText(id, integer.toString());
    }

    public void update() {
        count += 1;
        for (int i = 0; i < getItemCount(); i++) {
            notifyItemChanged(i, 2);
        }
    }

    @Override
    public int getItemCount() {
        return mListTime == null ? 0 : mListTime.size();
    }
}
