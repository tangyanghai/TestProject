package com.example.testtimer;

import android.view.View;

import com.example.testtimer.adapter.BaseAdapter;
import com.example.testtimer.adapter.Holder;

import java.util.List;

/**
 * <p>@author : tyh</p>
 * <p>@time : 2018/06/20</p>
 * <p>@for : $s$</p>
 * <p></p>
 */
class MoveAbleAdapter extends BaseAdapter {

    @Override
    protected int getItemLayoutByType(int viewType) {
        return R.layout.item_moveable;
    }

    @Override
    protected void reBindPart(Holder holder, int position, List<Object> payloads) {

    }

    @Override
    protected void bindNormal(Holder holder, int position) {
        final View bottom = holder.getView(R.id.view_bottom);
        holder.setOnclickListener(R.id.view_top,new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bottom.getVisibility() == View.VISIBLE) {
                    bottom.setVisibility(View.GONE);
                }else {
                    bottom.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return 10;
    }
}
