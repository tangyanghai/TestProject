package com.example.testmap.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testmap.MainActivity;
import com.example.testmap.R;
import com.example.testmap.map.MapBean;

import java.util.List;

/**
 * <p>@author : tyh</p>
 * <p>@time : 2018/06/28</p>
 * <p>@for : $s$</p>
 * <p></p>
 */
public class MapAdapter extends BaseAdapter<MapBean> {

    MainActivity.IAddressGetter addressGetter;

    public MapAdapter(List<MapBean> mLstMap, MainActivity.IAddressGetter addressGetter) {
        mDatas = mLstMap;
        this.addressGetter = addressGetter;
    }

    @Override
    protected int getItemLayoutByType(int viewType) {
        return R.layout.item_map;
    }

    @Override
    protected void bindNormal(final Holder holder, int position) {
        final MapBean mapBean = mDatas.get(position);
        holder.setText(R.id.name, mapBean.getName());
        holder.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mapBean.open(holder.getContext(), "深圳市宝安机场");
                String address = addressGetter.getAddress();
                if (TextUtils.isEmpty(address)) {
                    Toast.makeText(holder.getContext(), "请输入地址", Toast.LENGTH_SHORT).show();
                    return;
                }
                mapBean.toNavigation(holder.getContext(), address);
            }
        });
    }
}
