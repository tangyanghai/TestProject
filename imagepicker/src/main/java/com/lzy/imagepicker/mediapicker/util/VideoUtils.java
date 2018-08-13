package com.lzy.imagepicker.mediapicker.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * <p>@author : tyh</p>
 * <p>@time : 2018/08/03</p>
 * <p>@for : $s$</p>
 * <p></p>
 */
public class VideoUtils {
    private static VideoUtils instance = null;
    private static final String KEY_DEFAULT = "key_default";
    /**
     * 线程池缓存
     * key-->指定线程池的key
     * value-->线程池
     */
    private Map<String, ExecutorService> mExecutorsByTag;

    private VideoUtils() {
    }

    public static VideoUtils getInstance() {
        if (instance == null) {
            synchronized (VideoUtils.class) {
                if (instance == null) {
                    instance = new VideoUtils();
                }
            }
        }
        return instance;
    }

    /**
     * 获取本地视频缩略图
     *
     * @param path     视频路径
     * @param listener 回调
     * @return 缓存回调
     */
    public Bitmap getThumbForLocalVideo(String path, OnGetBitmapListener listener) {
        Bitmap bitmap = getThumbForLocalVideo(null, path, listener);
        if (bitmap != null) {
            return bitmap;
        }
        return null;
    }

    /**
     * 获取本地视频缩略图
     *
     * @param key      线程标记
     * @param path     视频路径
     * @param listener 回调
     * @return 缓存缩略图
     */
    public Bitmap getThumbForLocalVideo(String key, String path, OnGetBitmapListener listener) {
        Bitmap bitmap = BitmapCacheUtil.getInstance().getBitmap(path);
        if (bitmap != null) {
            return bitmap;
        }
        execute(TextUtils.isEmpty(key) ? KEY_DEFAULT : key, path, listener);
        return null;
    }

    /**
     * 关闭相应tag的线程池
     * 在不需要该线程池中的线程继续运行的时候,就关闭它
     *
     * @param tag 线程池tag
     */
    public void closeExecutorServiceByTag(String tag) {
        if (mExecutorsByTag == null) {
            return;
        }

        if (!mExecutorsByTag.containsKey(tag)) {
            return;
        }

        ExecutorService executorService = mExecutorsByTag.get(tag);
        if (!executorService.isShutdown()) {
            executorService.shutdown();
            executorService.shutdownNow();
        }

        mExecutorsByTag.remove(tag);
    }

    /**
     * 获取缩略图
     *
     * @param key      线程标记
     * @param path     图片路径
     * @param listener 回调
     */
    private void execute(String key, String path, OnGetBitmapListener listener) {
        synchronized (instance) {
            if (mExecutorsByTag == null) {
                mExecutorsByTag = new HashMap<>();
            }

            ExecutorService executorService = mExecutorsByTag.get(key);
            if (executorService == null) {
                executorService = Executors.newFixedThreadPool(5);
                mExecutorsByTag.put(key, executorService);
            }

            executorService.execute(new ThumbRunnable(path, listener));
        }
    }

    /**
     * 获取视频缩略图线程
     */
    static class ThumbRunnable implements Runnable {
        String path;
        OnGetBitmapListener listener;

        public ThumbRunnable(String path, OnGetBitmapListener listener) {
            this.path = path;
            this.listener = listener;
        }

        @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB_MR1)
        @Override
        public void run() {
            Bitmap smallIcon = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Images.Thumbnails.MINI_KIND);
            Bitmap scaledBitmap = null;
            if (smallIcon != null) {
                int width = smallIcon.getWidth();
                int height = smallIcon.getHeight();
                Matrix matrix = new Matrix();
                int maxSize = 200;
                int x, y;
                float scale;
                if (width == height) {
                    x = 0;
                    y = 0;
                    scale = maxSize * 1f / width;
                } else if (width > height) {
                    if (height < maxSize) {
                        maxSize = height;
                    }
                    y = 0;
                    x = (width - height) / 2;
                    width = height;
                    scale = maxSize * 1f / height;
                } else {
                    if (width < maxSize) {
                        maxSize = width;
                    }
                    x = 0;
                    y = (height - width) / 2;
                    height = width;
                    scale = maxSize * 1f / width;
                }
                matrix.setScale(scale, scale);
                Log.e("====", "得到的图片宽度 = " + width + " 得到的图片高度 = " + height + " 得到的图片大小 = " + smallIcon.getByteCount());
                scaledBitmap = Bitmap.createBitmap(smallIcon, x, y, width, height, matrix, false);
                Log.e("====", "压缩的图片宽度 = " + scaledBitmap.getWidth() + " 压缩的图片高度 = " + scaledBitmap.getHeight() + " 压缩的图片大小 = " + scaledBitmap.getByteCount());
                BitmapCacheUtil.getInstance().putBitmap(path, scaledBitmap);
            }
            if (smallIcon != null && !smallIcon.isRecycled()) {
                smallIcon.recycle();
            }
            listener.onGetBitmap(path, scaledBitmap);
        }
    }

    /**
     * 获取到视频缩略图回调
     */
    public interface OnGetBitmapListener {
        /**
         * @param path   视频路径
         * @param bitmap 视频缩略图,有可能为空,自行在外部去判断,为空时的逻辑处理,例如设置一个默认的缩略图
         */
        void onGetBitmap(String path, Bitmap bitmap);
    }

}
