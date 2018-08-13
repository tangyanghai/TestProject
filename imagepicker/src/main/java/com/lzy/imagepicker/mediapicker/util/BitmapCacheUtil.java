package com.lzy.imagepicker.mediapicker.util;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.LruCache;

/**
 * <p>@author : tyh</p>
 * <p>@time : 2018/08/03</p>
 * <p>@for : 图片缓存工具类</p>
 * <p></p>
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
public class BitmapCacheUtil {
    private static BitmapCacheUtil instance = null;
    private LruCache<String, Bitmap> mCache;

    private BitmapCacheUtil() {
        //最大内存容量
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        //最大缓存容量
        int maxSize = maxMemory / 8;
        mCache = new LruCache<String, Bitmap>(maxSize) {
            /**
             * 重写容量获取方式
             * 单个bitmap返回的是bitmap的大小
             * 如果不重写
             * 返回的是1
             */
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
    }

    public static BitmapCacheUtil getInstance() {
        if (instance == null) {
            synchronized (BitmapCacheUtil.class) {
                if (instance == null) {
                    instance = new BitmapCacheUtil();
                }
            }
        }
        return instance;
    }

    public void putBitmap(String key, Bitmap bitmap) {
        mCache.put(key, bitmap);
    }

    public Bitmap getBitmap(String key) {
        if (mCache == null) {
            return null;
        }
        return mCache.get(key);
    }

    /**
     * 移除对应的path的地址
     * @param path
     */
    public void remove(String path) {
        if (mCache == null) {
            return;
        }

        Bitmap bitmap = mCache.remove(path);
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
    }

    public void removeAll() {
        if (mCache == null) {
            return;
        }
        mCache.evictAll();
    }

}
