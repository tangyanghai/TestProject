package com.lzy.imagepicker.mediapicker.loader;


import java.util.List;

/**
 * <p>@author : tyh</p>
 * <p>@time : 2018/06/29</p>
 * <p>@for : 获取媒体文件回调</p>
 * <p></p>
 */
public interface OnMediaLoadedListener<T extends AbsMediaFolder> {
    void onMediaLoaded(List<T> folders);
}
