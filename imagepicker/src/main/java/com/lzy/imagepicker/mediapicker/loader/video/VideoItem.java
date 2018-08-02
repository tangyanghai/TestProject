package com.lzy.imagepicker.mediapicker.loader.video;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;

import com.lzy.imagepicker.mediapicker.loader.AbsMediaItem;

/**
 * <p>@author : tyh</p>
 * <p>@time : 2018/07/02</p>
 * <p>@for : 视频文件</p>
 * <p></p>
 */
public class VideoItem extends AbsMediaItem {
    int width;//视频宽度
    int height;//视频高度
    int duration;//视频时长

    @Override
    public boolean hasSmallIcon() {
        return true;
    }

    @Override
    public Bitmap getSmallIcon() {
        return ThumbnailUtils.createVideoThumbnail(path,MediaStore.Images.Thumbnails.MINI_KIND);
    }

    @Override
    public String toString() {
        return "VideoItem{" +
                "width=" + width +
                ", height=" + height +
                ", duration=" + duration +
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
