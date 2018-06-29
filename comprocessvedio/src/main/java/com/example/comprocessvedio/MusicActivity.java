package com.example.comprocessvedio;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import com.example.comprocessvedio.adapter.MusicAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>@author : tyh</p>
 * <p>@time : 2018/06/21</p>
 * <p>@for : $s$</p>
 * <p></p>
 */
public class MusicActivity extends BaseListActivity<MusicAdapter> {

    List<MainActivity.Music> mListMusic;

    public static void start(Context context, ArrayList<MainActivity.Music> musics){
        Intent intent = new Intent(context, MusicActivity.class);
        intent.putExtra("musics",musics);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListMusic = (List<MainActivity.Music>) getIntent().getSerializableExtra("musics");
        mAdapter.setData(mListMusic);
    }

    @Override
    public MusicAdapter getAdapter() {
        return new MusicAdapter();
    }
}
