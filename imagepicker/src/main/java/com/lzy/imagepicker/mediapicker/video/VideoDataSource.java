package com.lzy.imagepicker.mediapicker.video;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;

import com.lzy.imagepicker.mediapicker.AbsDataSource;
import com.lzy.imagepicker.mediapicker.OnMediaLoadedListener;

/**
 * <p>@author : tyh</p>
 * <p>@time : 2018/07/02</p>
 * <p>@for : $s$</p>
 * <p></p>
 */
public class VideoDataSource extends AbsDataSource<VideoItem, VideoFolder> {

    private final String[] VIDEO_PROJECTION = {     //查询图片需要的数据列
            MediaStore.Video.Media.DISPLAY_NAME,   //视频的显示名称  aaa.jpg
            MediaStore.Video.Media.DATA,           //视频的真实路径  /storage/emulated/0/pp/downloader/wallpaper/aaa.jpg
            MediaStore.Video.Media.SIZE,           //视频的大小，long型  132492
            MediaStore.Video.Media.WIDTH,          //视频的宽度，int型  1920
            MediaStore.Video.Media.HEIGHT,         //视频的高度，int型  1080
            MediaStore.Video.Media.MIME_TYPE,      //视频的类型     image/jpeg
            MediaStore.Video.Media.DATE_ADDED      //视频添加的时间
    };


    public VideoDataSource(FragmentActivity context, String path, OnMediaLoadedListener loadedListener) {
        super(context, path, loadedListener);
    }

    @Override
    protected Uri uri() {
        return MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
    }

    @Override
    protected String[] projection() {
        return VIDEO_PROJECTION;
    }

    @Override
    protected String path() {
        return VIDEO_PROJECTION[1];
    }

    @Override
    protected String getAllFolderName() {
        return "所有视频";
    }

    @Override
    protected VideoFolder getNewFolder() {
        return new VideoFolder();
    }

    @Override
    protected VideoItem translateDate(Cursor data) {
        //查询数据
        String vedioName = data.getString(data.getColumnIndexOrThrow(VIDEO_PROJECTION[0]));
        String vedioPath = data.getString(data.getColumnIndexOrThrow(VIDEO_PROJECTION[1]));
        long vedioSize = data.getLong(data.getColumnIndexOrThrow(VIDEO_PROJECTION[2]));
        int vedioWidth = data.getInt(data.getColumnIndexOrThrow(VIDEO_PROJECTION[3]));
        int vedioHeight = data.getInt(data.getColumnIndexOrThrow(VIDEO_PROJECTION[4]));
        String vedioMimeType = data.getString(data.getColumnIndexOrThrow(VIDEO_PROJECTION[5]));
        long vedioAddTime = data.getLong(data.getColumnIndexOrThrow(VIDEO_PROJECTION[6]));
        //封装实体
        VideoItem videoItem = new VideoItem();
        videoItem.name = vedioName;
        videoItem.path = vedioPath;
        videoItem.size = vedioSize;
        videoItem.width = vedioWidth;
        videoItem.height = vedioHeight;
        videoItem.mimeType = vedioMimeType;
        videoItem.addTime = vedioAddTime;
        return videoItem;
    }
}
