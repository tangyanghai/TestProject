package com.example.testqiniuplayer;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * <p>@author : tyh</p>
 * <p>@time : 2018/07/23</p>
 * <p>@for : $s$</p>
 * <p></p>
 */
public class QiniuPlayActivity extends AppCompatActivity implements PlayFragment.VideoListener {

    String url;
    FrameLayout mVideoPlay;
    PlayFragment mPlayFragment;
    private double defaultScale;

    public static void start(Context context, String url) {
        Intent intent
                = new Intent(context, QiniuPlayActivity.class);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paly);
        mVideoPlay = findViewById(R.id.fl_qiniu_player);
        url = getIntent().getStringExtra("url");
        initPlayFragment();
    }


    /**
     * 初始化播放fragment
     */
    private void initPlayFragment() {
        mPlayFragment = new PlayFragment();
        Bundle bundel = PlayFragment.getBundel(url);
        mPlayFragment.setArguments(bundel);
        mPlayFragment.setVideoLister(this);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_qiniu_player, mPlayFragment)
                .commitAllowingStateLoss();
    }


    /**
     * 切换横竖屏
     *
     * @param b      是否横屏或竖屏
     * @param width  播放视频宽度
     * @param height 播放视频高度
     */
    public void setVideoPlaySize(boolean b, int width, int height) {
        LinearLayout.LayoutParams params;
        if (b) {//切回竖屏
            int w = getWindow().getWindowManager().getDefaultDisplay().getWidth();
            int h = (int) (MathUtil.div(height * w, width) + 0.5);
            params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, h);
        } else {//切回横屏
            params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        }
        mVideoPlay.setLayoutParams(params);
    }


    /**
     * 手机屏幕方向变化
     *
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            show_topbar_and_navigationbar(true);
        } else {
            show_topbar_and_navigationbar(false);
        }
    }


    /**
     * 显示/隐藏 状态栏和虚拟导航栏
     *
     * @param b
     */
    public void show_topbar_and_navigationbar(boolean b) {
        if (b) {
            enableLayoutFullScreen();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LOW_PROFILE
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION//隐藏虚拟导航
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN//全屏
                        | View.SYSTEM_UI_FLAG_FULLSCREEN//全屏
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY//状态栏和虚拟导航栏被调用出来后,自动隐藏
                );
            }
        }
    }


    /**
     * 激活布局全屏
     */
    protected void enableLayoutFullScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            }
        }
    }

    @Override
    public void onVideoSizeChanged(int width, int height) {
        resetPlayerSize(width, height);
    }

    /**
     * 刚进入界面时,根据屏幕宽度设置屏幕高度
     * 当拿到视频资源时,根据视频尺寸,调整页面尺寸
     *
     * @param videoWidth  视频宽度
     * @param videoHeight 视频高度
     */
    private void resetPlayerSize(int videoWidth, int videoHeight) {
        double scale = MathUtil.div(videoWidth, videoHeight, 2);
        if (scale != defaultScale) {
            defaultScale = scale;
            int width = getWindow().getWindowManager().getDefaultDisplay().getWidth();
            int height = (int) (MathUtil.div(videoHeight * width, videoWidth) + 0.5);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
            mVideoPlay.setLayoutParams(params);
        }
    }
}
