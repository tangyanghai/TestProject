package com.lzy.imagepicker.mediapicker;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.lzy.imagepicker.bean.ImageFolder;

import java.util.ArrayList;

/**
 * <p>@author : tyh</p>
 * <p>@time : 2018/06/29</p>
 * <p>@for : $s$</p>
 * <p></p>
 */
public abstract class AbsDataSource implements LoaderManager.LoaderCallbacks<Cursor> {


    private static final int LOADER_ALL = 1;//扫描所有文件夹
    private static final int LOADER_CATEGORY = 2;//扫描单个文件夹

    private FragmentActivity context;
    private OnMediaLoadedListener loadedListener;
    private ArrayList<MediaFloder> mediaFloders = new ArrayList<>();   //所有的图片文件夹

    public AbsDataSource(FragmentActivity context, String path, OnMediaLoadedListener loadedListener) {
        this.loadedListener = loadedListener;
        this.context = context;

        LoaderManager loaderManager = context.getSupportLoaderManager();
        if (path == null) {
            loaderManager.initLoader(LOADER_ALL, null, this);//加载所有的文件
        } else {
            //加载指定目录的文件
            Bundle bundle = new Bundle();
            bundle.putString("path", path);
            loaderManager.initLoader(LOADER_CATEGORY, bundle, this);
        }
    }

    /**
     * MediaStore.Images.Media.EXTERNAL_CONTENT_URI
     *
     * @return 要返回的文件类型的URI
     */
    protected abstract Uri uri();

    /**
     * @return 要得到的信息
     */
    protected abstract String[] projection();

    /**
     * @param args
     * @return 赛选条件等号前面
     */
    protected String selection(Bundle args) {
        return null;
    }

    /**
     * @param args
     * @return 赛选条件--单个文件夹
     */
    protected String selectionSingle(Bundle args) {
        return null;
    }

    /**
     * @param args
     * @return 赛选条件等号后面
     */
    protected String[] selectionArgs(Bundle args) {
        return null;
    }

    /**
     * @param args
     * @return 排序条件
     */
    protected String sortOrder(Bundle args) {
        return null;
    }

    /**
     * @return 返回媒体类型的路径key
     */
    protected abstract String path();

    /**
     * 创建Loader,加入各种赛选条件
     *
     * @param id
     * @param args
     * @return
     */
    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        CursorLoader cursorLoader = null;
        //扫描所有图片
        if (id == LOADER_ALL)
            cursorLoader = new CursorLoader(context, uri(), projection(), null, null, sortOrder(args));
        //扫描某个图片文件夹
        if (id == LOADER_CATEGORY)
            cursorLoader = new CursorLoader(context, uri(), projection(), path() + " like '%" + args.getString("path") + "%'", null, sortOrder(args));

        return cursorLoader;
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mediaFloders.clear();








    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

}
