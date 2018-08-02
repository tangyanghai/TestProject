package com.example.comprocessvedio.adapter;

import android.text.TextUtils;
import android.view.View;

import com.example.comprocessvedio.MainActivity;
import com.example.comprocessvedio.AudioManager;
import com.example.comprocessvedio.R;

import java.util.List;

/**
 * <p>@author : tyh</p>
 * <p>@time : 2018/06/21</p>
 * <p>@for : $s$</p>
 * <p></p>
 */
public class MusicAdapter extends BaseAdapter<MainActivity.Music> {


    public MusicAdapter() {
    }

    public MusicAdapter(List<MainActivity.Music> mDatas) {
        super(mDatas);
    }

    @Override
    protected int getItemLayoutByType(int viewType) {
        return R.layout.item_music;
    }

    @Override
    protected void bindNormal(Holder holder, int position) {
        final MainActivity.Music music = mDatas.get(position);
        if (music == null || TextUtils.isEmpty(music.getName())) {
            return;
        }
        holder.setText(R.id.music, String.format("名称: %1s   大小: %2s  时长: %3s", music.getName(), music.getSize(), music.getDuration()));
        holder.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = music.getUrl();
                if (!TextUtils.isEmpty(url)) {
                    AudioManager.play(url);
                }
            }
        });
    }

}
