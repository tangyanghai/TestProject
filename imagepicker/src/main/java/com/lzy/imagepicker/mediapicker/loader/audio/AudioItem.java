package com.lzy.imagepicker.mediapicker.loader.audio;

import android.graphics.Bitmap;

import com.lzy.imagepicker.mediapicker.loader.AbsMediaItem;

/**
 * <p>@author : tyh</p>
 * <p>@time : 2018/07/02</p>
 * <p>@for : $s$</p>
 * <p></p>
 */
public class AudioItem extends AbsMediaItem {
    int duration;

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public boolean hasSmallIcon() {
        return false;
    }

    @Override
    public Bitmap getSmallIcon() {
        return null;
    }

    @Override
    public String toString() {
        return "AudioItem{" +
                "duration=" + duration +
                ", name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", size=" + size +
                ", mimeType='" + mimeType + '\'' +
                ", addTime=" + addTime +
                ", iid='" + iid + '\'' +
                ", pathUrl='" + pathUrl + '\'' +
                '}';
    }
}
