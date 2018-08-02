package com.lzy.imagepicker.mediapicker.loader;

import android.graphics.Bitmap;

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
            AbsMediaFolder other = (AbsMediaFolder) o;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public T getCover() {
        return cover;
    }

    public void setCover(T cover) {
        this.cover = cover;
    }

    public ArrayList<T> getMedias() {
        return medias;
    }

    public void setMedias(ArrayList<T> medias) {
        this.medias = medias;
    }
}
