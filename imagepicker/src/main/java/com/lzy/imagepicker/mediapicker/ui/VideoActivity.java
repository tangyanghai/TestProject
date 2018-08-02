package com.lzy.imagepicker.mediapicker.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.LruCache;
import android.widget.ImageView;

import com.lzy.imagepicker.R;
import com.lzy.imagepicker.mediapicker.loader.OnMediaLoadedListener;
import com.lzy.imagepicker.mediapicker.loader.video.VideoDataSource;
import com.lzy.imagepicker.mediapicker.loader.video.VideoFolder;
import com.lzy.imagepicker.mediapicker.loader.video.VideoItem;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <p>@author : tyh</p>
 * <p>@time : 2018/08/02</p>
 * <p>@for : $s$</p>
 * <p></p>
 */
@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB_MR1)
public class VideoActivity extends MediaBaseActivity<VideoItem, VideoFolder, VideoDataSource> {
    @Override
    protected VideoDataSource initDataSource(String path, OnMediaLoadedListener<VideoFolder> listener) {
        return new VideoDataSource(this, path, listener);
    }

    @Override
    protected void setItemIcon(final VideoItem videoItem, final ImageView img) {
        if (mCache != null) {
            Bitmap bitmap = mCache.get(videoItem.path);
            if (bitmap != null) {
                img.setImageBitmap(bitmap);
                return;
            }

            mExecutors.execute(new ThumbRunnable(videoItem, new OnThumbGetListener() {
                @Override
                public void onThumbGet(final Bitmap b) {

                    if (b != null) {
                        mCache.put(videoItem.path, b);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                img.setImageBitmap(b);
                            }
                        });
                    } else {
                        final Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.mipmap.default_image);
                        mCache.put(videoItem.path, bitmap1);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                img.setImageBitmap(bitmap1);
                            }
                        });
                    }
                }
            }));
        }
    }

    private LruCache<String, Bitmap> mCache = new LruCache<>(50);

    private ExecutorService mExecutors = Executors.newFixedThreadPool(10);

    static class ThumbRunnable implements Runnable {
        VideoItem videoItem;
        OnThumbGetListener listener;

        public ThumbRunnable(VideoItem videoItem, OnThumbGetListener listener) {
            this.videoItem = videoItem;
            this.listener = listener;
        }

        @Override
        public void run() {
            Bitmap smallIcon = videoItem.getSmallIcon();
            listener.onThumbGet(smallIcon);
        }
    }

    private interface OnThumbGetListener {
        void onThumbGet(Bitmap bitmap);
    }

}
