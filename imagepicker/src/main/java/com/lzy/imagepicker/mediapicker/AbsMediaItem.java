package com.lzy.imagepicker.mediapicker;

import android.content.Context;
import android.graphics.Bitmap;

import com.lzy.imagepicker.bean.ImageItem;

/**
 * <p>@author : tyh</p>
 * <p>@time : 2018/07/02</p>
 * <p>@for : $s$</p>
 * <p></p>
 */
public abstract class AbsMediaItem {
    public String name;       //媒体的名字
    public String path;       //媒体的路径
    public long size;         //媒体的大小
    public String mimeType;   //媒体的类型
    public long addTime;      //媒体的创建时间
    public String iid;        //媒体iid
    public String pathUrl;    //服务器路径
    public String compressPath; //压缩路径

    /**
     * 是否有缩略图
     */
    public abstract boolean hasSmallIcon();

    /**
     * @return 缩略图
     */
    public abstract Bitmap getSmallIcon();


    /** 媒体文件的路径和创建时间相同就认为是同一文件 */
    @Override
    public boolean equals(Object o) {
        try {
            ImageItem other = (ImageItem) o;
            return this.path.equalsIgnoreCase(other.path) && this.addTime == other.addTime;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return super.equals(o);
    }


    @Override
    public String toString() {
        return "ImageItem{" +
                "name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", size=" + size +
                ", mimeType='" + mimeType + '\'' +
                ", addTime=" + addTime +
                ", pathUrl='" + pathUrl + '\'' +
                ", compressPath='" + compressPath + '\'' +
                '}';
    }
}
