package com.lzy.imagepicker.mediapicker.loader;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.TextUtils;

import java.io.File;
import java.util.ArrayList;

/**
 * <p>@author : tyh</p>
 * <p>@time : 2018/06/29</p>
 * <p>@for : $s$</p>
 * <p></p>
 */
public abstract class AbsDataSource<Q extends AbsMediaItem,T extends AbsMediaFolder<Q>> implements LoaderManager.LoaderCallbacks<Cursor> {
    private static int count;
    private static final int BASE_LOAD_ALL = 1_000;
    private static final int BASE_LOAD_CATEGORY = 100_000;

    protected FragmentActivity activity;
    protected OnMediaLoadedListener<T> loadedListener;
    protected ArrayList<T> mediaFolders = new ArrayList<>();   //所有的图片文件夹

    public AbsDataSource(FragmentActivity context, String path, OnMediaLoadedListener<T> loadedListener) {
        this.loadedListener = loadedListener;
        this.activity = context;

        LoaderManager loaderManager = context.getSupportLoaderManager();
        count++;

        if (!isFileExist(path)) {
            loaderManager.initLoader(BASE_LOAD_ALL+count, null, this);//加载所有的文件
        } else {
            //加载指定目录的文件
            Bundle bundle = new Bundle();
            bundle.putString("path", path);
            loaderManager.initLoader(BASE_LOAD_CATEGORY+count, bundle, this);
        }
    }

    private boolean isFileExist(String path){

        if (TextUtils.isEmpty(path)) {
            return false;
        }

        File file = new File(path);
        if (file.exists()) {
            return true;
        }

        return false;
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
        //扫描所有文件夹
        if (id < BASE_LOAD_CATEGORY)
            cursorLoader = new CursorLoader(activity, uri(), projection(), null, null, sortOrder(args));
        //扫描某个某个文件夹
        else
            cursorLoader = new CursorLoader(activity, uri(), projection(), path() + " like '%" + args.getString("path") + "%'", null, sortOrder(args));

        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mediaFolders.clear();
        if (data != null) {
            ArrayList<Q> allVideos = new ArrayList<>();   //所有图片的集合,不分文件夹
            while (data.moveToNext()) {
                Q item= translateDate(data);
                allVideos.add(item);
                //根据父路径分类存放图片
                File file = new File(item.path);
                File parentFile = file.getParentFile();
                if (parentFile == null) {//有一款机型在这里报过空指针,防一下
                    continue;
                }
                T folder = getNewFolder();
                folder.name = parentFile.getName();
                folder.path = parentFile.getAbsolutePath();

                if (!mediaFolders.contains(folder)) {
                    ArrayList<Q> medias = new ArrayList<>();
                    medias.add(item);
                    folder.cover = item;
                    folder.medias = medias;
                    mediaFolders.add(folder);
                } else {
                    mediaFolders.get(mediaFolders.indexOf(folder)).medias.add(item);
                }
            }
            //防止没有图片报异常
            if (data.getCount() > 0 && allVideos.size() > 0) {
                //构造所有图片的集合
                T allImagesFolder = getNewFolder();
                allImagesFolder.name = getAllFolderName();
                allImagesFolder.path = "/";
                allImagesFolder.cover = allVideos.get(0);
                allImagesFolder.medias = allVideos;
                mediaFolders.add(0, allImagesFolder);  //确保第一条是所有图片
            }
        }
        loadedListener.onMediaLoaded(mediaFolders);
    }

    /**
     * @return 所有媒体文件夹名称
     */
    protected abstract String getAllFolderName();

    /**
     * @return 创建一个空的文件夹
     */
    protected abstract T getNewFolder();

    /**
     * 单个data转换成媒体类
     */
    protected abstract Q translateDate(Cursor data);

    @Override
    public void onLoaderReset(Loader loader) {

    }

}
