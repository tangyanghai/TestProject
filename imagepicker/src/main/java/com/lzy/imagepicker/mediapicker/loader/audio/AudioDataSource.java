package com.lzy.imagepicker.mediapicker.loader.audio;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;

import com.lzy.imagepicker.mediapicker.loader.AbsDataSource;
import com.lzy.imagepicker.mediapicker.loader.OnMediaLoadedListener;

/**
 * <p>@author : tyh</p>
 * <p>@time : 2018/07/02</p>
 * <p>@for : $s$</p>
 * <p></p>
 */
public class AudioDataSource extends AbsDataSource<AudioItem, AudioFolder> {

    private final String[] AUDIO_PROJECTION = {    //查询图片需要的数据列
            MediaStore.Audio.Media.DISPLAY_NAME,   //音频的显示名称  aaa.jpg
            MediaStore.Audio.Media.DATA,           //音频的真实路径  /storage/emulated/0/pp/downloader/wallpaper/aaa.jpg
            MediaStore.Audio.Media.SIZE,           //音频的大小，long型  132492
            MediaStore.Audio.Media.MIME_TYPE,      //音频的类型
            MediaStore.Audio.Media.DATE_ADDED,     //音频添加的时间
            MediaStore.Audio.Media.DURATION        //音频时长
    };

    public AudioDataSource(FragmentActivity context, String path, OnMediaLoadedListener<AudioFolder> loadedListener) {
        super(context, path, loadedListener);
    }

    @Override
    protected Uri uri() {
        return MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    }

    @Override
    protected String[] projection() {
        return AUDIO_PROJECTION;
    }

    @Override
    protected String path() {
        return AUDIO_PROJECTION[1];
    }

    @Override
    protected String getAllFolderName() {
        return "所有音乐";
    }

    @Override
    protected AudioFolder getNewFolder() {
        return new AudioFolder();
    }

    @Override
    protected AudioItem translateDate(Cursor data) {
        //查询数据
        String vedioName = data.getString(data.getColumnIndexOrThrow(AUDIO_PROJECTION[0]));
        String vedioPath = data.getString(data.getColumnIndexOrThrow(AUDIO_PROJECTION[1]));
        long vedioSize = data.getLong(data.getColumnIndexOrThrow(AUDIO_PROJECTION[2]));
        String vedioMimeType = data.getString(data.getColumnIndexOrThrow(AUDIO_PROJECTION[3]));
        long vedioAddTime = data.getLong(data.getColumnIndexOrThrow(AUDIO_PROJECTION[4]));
        int vedioDuration = data.getInt(data.getColumnIndexOrThrow(AUDIO_PROJECTION[5]));
        //封装实体
        AudioItem audioItem = new AudioItem();
        audioItem.name = vedioName;
        audioItem.path = vedioPath;
        audioItem.size = vedioSize;
        audioItem.mimeType = vedioMimeType;
        audioItem.addTime = vedioAddTime;
        audioItem.duration = vedioDuration;
        return audioItem;
    }
}
