package com.lzy.imagepicker.mediapicker;

import android.graphics.Bitmap;

import com.lzy.imagepicker.bean.ImageFolder;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.util.BitmapUtil;

import java.util.ArrayList;

/**
 * <p>@author : tyh</p>
 * <p>@time : 2018/07/02</p>
 * <p>@for : $s$</p>
 * <p></p>
 */
public abstract class AbsMediaFolder<T extends AbsMediaItem> {

    public String name;  //当前文件夹的名字
    public String path;  //当前文件夹的路径
    public T cover;   //当前文件夹需要要显示的缩略图，默认为最近的一次图片
    public ArrayList<T> medias;  //当前文件夹下所有图片的集合

    /**
     * 只要文件夹的路径和名字相同，就认为是相同的文件夹
     */
    @Override
    public boolean equals(Object o) {
        try {
            ImageFolder other = (ImageFolder) o;
            return this.path.equalsIgnoreCase(other.path) && this.name.equalsIgnoreCase(other.name);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return super.equals(o);
    }

    public boolean hasSmallIcon() {
        if (cover == null) {
            return false;
        }
        return cover.hasSmallIcon();
    }

    public Bitmap getSmallIcon() {
        if (cover == null) {
            return null;
        }
        return cover.getSmallIcon();
    }

}
