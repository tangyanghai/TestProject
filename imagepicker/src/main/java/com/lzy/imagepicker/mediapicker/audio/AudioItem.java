package com.lzy.imagepicker.mediapicker.audio;

import android.graphics.Bitmap;

import com.lzy.imagepicker.mediapicker.AbsMediaItem;

/**
 * <p>@author : tyh</p>
 * <p>@time : 2018/07/02</p>
 * <p>@for : $s$</p>
 * <p></p>
 */
public class AudioItem extends AbsMediaItem {
    @Override
    public boolean hasSmallIcon() {
        return false;
    }

    @Override
    public Bitmap getSmallIcon() {
        return null;
    }
}
