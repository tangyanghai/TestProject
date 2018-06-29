package com.example.comprocessvedio;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.os.Handler;
import android.os.Message;
import android.os.storage.StorageManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.dn.tim.lib_permission.annotation.Permission;
import com.dn.tim.lib_permission.annotation.PermissionCanceled;
import com.dn.tim.lib_permission.annotation.PermissionDenied;
import com.example.comprocessvedio.utils.FileUtils;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = ">>MainActivity<<";
    private ArrayList<Music> musicList;
    private List<String> remainList;
    private MediaMetadataRetriever mmr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Permission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    private void requestOnePermission() {
        Toast.makeText(this, "请求一个权限成功（写权限）", Toast.LENGTH_SHORT).show();
    }

    @PermissionCanceled()
    private void cancel() {
        Log.i(TAG, "writeCancel: ");
        Toast.makeText(this, "cancel", Toast.LENGTH_SHORT).show();
    }

    @PermissionDenied()
    private void deny() {
        Log.i(TAG, "writeDeny:");
        Toast.makeText(this, "deny", Toast.LENGTH_SHORT).show();
    }


    /**
     * 获取音乐文件
     */
    @Permission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public void toGetMusic(View view) {
        loadFileData();
        if (musicList != null && musicList.size() != 0) {
            MusicActivity.start(this, musicList);
        }
    }

    private boolean isCheckFile = false;

    /**
     *
     */
    @Permission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public void toGetVideo(View view) {
        //获取内外存储卡路径
        loadFileData();
        mmr = new MediaMetadataRetriever();
        remainList = new CopyOnWriteArrayList<>();
        String inPath = getStoragePath(this, false);
        getMusicFromSdCard(inPath);
        String externalPath = getStoragePath(this, true);
        if (externalPath == null) {
            externalPath = "没有外置存储卡";
        } else {
            getMusicFromSdCard(externalPath);
        }
        Toast.makeText(this, String.format("内部存储卡位置: %1s, 外部存储卡位置: %2s", inPath, externalPath), Toast.LENGTH_SHORT).show();
    }


    //查询本地的音乐文件
    private void loadFileData() {
        ContentResolver resolver = this.getContentResolver();
        Cursor cursor = resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
        cursor.moveToFirst();
        musicList = new ArrayList();
        if (cursor.moveToFirst()) {
            do {
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)); // 名称
                long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE)); // 大小
                String url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)); // 时长
                String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                if (duration >= 1000 && duration <= 900000) {
                    Music music = new Music();
                    music.setName(title);
                    music.setSize(size);
                    music.setUrl(url);
                    music.setAlbum(album);
                    music.setDuration(duration);
                    musicList.add(music);
                }
            } while (cursor.moveToNext());
        }
        List musicname = new ArrayList();
        for (Music music : musicList) {
            //将歌曲的名字都提取出来装在一个数组中
            musicname.add(music.getUrl());
        }
        Toast.makeText(MainActivity.this, musicname + "", Toast.LENGTH_SHORT).show();
        cursor.close();
    }

    /**
     * @param mContext    上下文
     * @param is_removale true=外部,false=内部
     * @return 存储卡位置
     */
    private static String getStoragePath(Context mContext, boolean is_removale) {
        StorageManager mStorageManager = (StorageManager) mContext.getSystemService(Context.STORAGE_SERVICE);
        Class<?> storageVolumeClazz = null;
        try {
            storageVolumeClazz = Class.forName("android.os.storage.StorageVolume");
            Method getVolumeList = mStorageManager.getClass().getMethod("getVolumeList");
            Method getPath = storageVolumeClazz.getMethod("getPath");
            Method isRemovable = storageVolumeClazz.getMethod("isRemovable");
            Object result = getVolumeList.invoke(mStorageManager);
            final int length = Array.getLength(result);
            for (int i = 0; i < length; i++) {
                Object storageVolumeElement = Array.get(result, i);
                String path = (String) getPath.invoke(storageVolumeElement);
                boolean removable = (Boolean) isRemovable.invoke(storageVolumeElement);
                if (is_removale == removable) {
                    return path;
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                Music music = getMusicInfo((String) msg.obj);
                if (music != null) {
                    musicList.add(music);
                }
                if (remainList.size() == 0) {
                    MusicActivity.start(MainActivity.this, musicList);
                }
            }
        }
    };

    public void getMusicFromSdCard(String path) {
        File root = new File(path);

        String name = root.getName();
        if (root.isFile()) {
            if (name.endsWith(".mp3")) {
                Log.i(TAG, "===找到一个文件 " + root.getAbsolutePath());
                Message msg = handler.obtainMessage();
                msg.what = 0;
                msg.obj = path;
                handler.sendMessage(msg);
                remainList.add(path);
            }
//            Log.i(TAG, "找到一个文件 " + root.getAbsolutePath() + "  " + (isMusic ? "====是音频" : "不是音频"));
        } else {
            File[] files = root.listFiles();
            if (files != null && files.length > 0) {
                for (File file : files) {
                    getMusicFromSdCard(file.getPath());
                }
            }
        }
    }

    /**
     * 跳转到系统文件夹管理器
     */
    public void toSystemDM(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
        //intent.setType(“image/*”);//选择图片
        intent.setType("audio/*"); //选择音频
        //intent.setType("video/*"); //选择视频 （mp4 3gp 是android支持的视频格式）
        //intent.setType(“video/*;image/*”);//同时选择视频和图片
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//        intent.addCategory(Intent.EXTRA_ALLOW_MULTIPLE);

        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 音频文件实体类
     */
    public static class Music implements Serializable {
        String name;
        long size;
        String url;
        long duration;
        String album;

        public String getAlbum() {
            return album;
        }

        public void setAlbum(String album) {
            this.album = album;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getSize() {
            return size;
        }

        public void setSize(long size) {
            this.size = size;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public long getDuration() {
            return duration;
        }

        public void setDuration(long duration) {
            this.duration = duration;
        }

        /**
         * 全路径相同,就是同一个音乐
         */
        @Override
        public boolean equals(Object obj) {
            return TextUtils.equals(this.url,((Music)obj).getUrl());
        }
    }

    public synchronized Music getMusicInfo(String str) {
        try {
            Music music = new Music();
            mmr.setDataSource(str);
            music.setUrl(str);
            Log.d(TAG, "title:" + str);
            music.setName(getFileName(str));
            music.setSize(FileUtils.getFileSize(str));
            String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION); // 播放时长单位为毫秒
            Log.d(TAG, "duration:" + duration);
            music.setDuration(Long.parseLong(duration));
            return music;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            Log.i(TAG, "getMusicInfo: 出错路径:  " + str);
        } catch (RuntimeException e) {
            e.printStackTrace();
            Log.i(TAG, "getMusicInfo: 出错路径:  " + str);
        } finally {
            remainList.remove(str);
        }

        return null;
    }

    private String getFileName(String str) {

        try {
            File file = new File(str);
            String name = file.getName();
            return name;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
