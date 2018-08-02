package com.lzy.imagepicker.mediapicker.ui;

import android.widget.ImageView;

import com.lzy.imagepicker.mediapicker.loader.OnMediaLoadedListener;
import com.lzy.imagepicker.mediapicker.loader.audio.AudioDataSource;
import com.lzy.imagepicker.mediapicker.loader.audio.AudioFolder;
import com.lzy.imagepicker.mediapicker.loader.audio.AudioItem;

/**
 * <p>@author : tyh</p>
 * <p>@time : 2018/08/02</p>
 * <p>@for : $s$</p>
 * <p></p>
 */
public class AudioActivity extends MediaBaseActivity<AudioItem,AudioFolder,AudioDataSource> {
    @Override
    protected AudioDataSource initDataSource(String path, OnMediaLoadedListener<AudioFolder> onMediaLoadedListener) {
        return new AudioDataSource(this,path,onMediaLoadedListener);
    }

    @Override
    protected void setItemIcon(AudioItem audioItem, ImageView mImg) {

    }
}
