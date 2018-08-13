package com.lzy.imagepicker.mediapicker.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.ImageView;

import com.lzy.imagepicker.R;
import com.lzy.imagepicker.mediapicker.loader.OnMediaLoadedListener;
import com.lzy.imagepicker.mediapicker.loader.video.VideoDataSource;
import com.lzy.imagepicker.mediapicker.loader.video.VideoFolder;
import com.lzy.imagepicker.mediapicker.loader.video.VideoItem;
import com.lzy.imagepicker.mediapicker.util.BitmapCacheUtil;
import com.lzy.imagepicker.mediapicker.util.VideoUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * <p>@author : tyh</p>
 * <p>@time : 2018/08/02</p>
 * <p>@for : $s$</p>
 * <p></p>
 */
@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB_MR1)
public class VideoActivity extends MediaBaseActivity<VideoItem, VideoFolder, VideoDataSource> {

    private static final String TAG = "VideoActivity";
    private Set<String> mPathLoadPic;


    @Override
    protected VideoDataSource initDataSource(String path, OnMediaLoadedListener<VideoFolder> listener) {
        mPathLoadPic =new HashSet<>();
        return new VideoDataSource(this, path, listener);
    }

    @Override
    protected void setItemIcon(final String path, final int position, final ImageView img) {
        mPathLoadPic.add(path);
        Bitmap bitmap = VideoUtils.getInstance().getThumbForLocalVideo(TAG,path, new VideoUtils.OnGetBitmapListener() {
            @Override
            public void onGetBitmap(String path, Bitmap bitmap) {
                if (bitmap == null) {
                    bitmap = BitmapFactory.decodeResource(VideoActivity.this.getResources(), R.mipmap.default_image);
                    BitmapCacheUtil.getInstance().putBitmap(path, bitmap);
                }
                final Bitmap bitmap1 = bitmap;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        img.setImageBitmap(bitmap1);
                    }
                });
            }
        });

        if (bitmap != null) {
            img.setImageBitmap(bitmap);

        }

    }


    @Override
    protected void onDestroy() {
        VideoUtils.getInstance().closeExecutorServiceByTag(TAG);
        //清除缓存了的图片--这里会去清除LruCache中缓存的Bitmap,但是不会清除界面上的ImageView中的Bitmap
        for (String s : mPathLoadPic) {
            BitmapCacheUtil.getInstance().remove(s);
        }
        mPathLoadPic.clear();
        super.onDestroy();
    }

}
