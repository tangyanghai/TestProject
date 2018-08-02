package com.example.testqiniuplayer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * <b>项目名：</b><span color=#FF5205>Carsland </span><br>
 * <b>包名：</b><span color=#FF5205>com.carsland.cld.base </span><br>
 * <b>文件名：</b><span color=#FF5205>BaseFramgent </span><br>
 * <b>创建者：</b><span color=#FF5205>wxr </span><br>
 * <b>创建时间：</b><span color=#FF5205>2017/10/13 10:56 </span><br>
 * <b>描述：fragment基类</b>
 */
public abstract class BaseFramgent
        extends Fragment
{
    private static final String TAG = BaseFramgent.class.getName();

    /**上下文成员变量*/
    public Context mContext  = null;
    /**BaseActivity成员变量*/
    public Activity mActivity = null;
    private Unbinder     mUnbinder = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        mContext = getActivity();
        mActivity = getActivity();
        View view = inflater.inflate(getLayoutId(), container, false);
        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData(view);
        initEvent(view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }

    /**页面跳转*/
    public void startActivity(Intent intent) {
        mActivity.startActivity(intent);
    }

    /**主线程运行*/
    public void runOnUiThread(Runnable run) {
        mActivity.runOnUiThread(run);
    }

    /**
     * 要显示的布局文件
     * @return 布局文件的ID
     */
    @LayoutRes
    public abstract int getLayoutId();

    /**
     * 初始化数据
     * @param view 布局的view
     */
    public abstract void initData(View view);

    /**
     * 初始化事件监听
     * @param view 布局的view
     */
    public abstract void initEvent(View view);
}
