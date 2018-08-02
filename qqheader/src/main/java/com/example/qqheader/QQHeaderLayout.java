package com.example.qqheader;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.ListView;

import static com.android.internal.widget.helper.ItemTouchHelper.UP;

/**
 * <p>@author : tyh</p>
 * <p>@time : 2018/07/31</p>
 * <p>@for : $s$</p>
 * <p></p>
 */
public class QQHeaderLayout extends ListView {

    private static final String TAG = "==QQHeaderLayout==";

    /**
     * 默认初始高度
     */
    private static final int DEFAULT_SIZE_HEADER_HEIGHT = 100;
    /**
     * 默认图片能够拉伸的最大高度
     */
    private static final int DEFAULT_SIZE_HEADER_HEIGHT_MAX = 400;
    ImageView mImgView;

    public void setImgView(ImageView imgView) {
        this.mImgView = imgView;
    }

    public QQHeaderLayout(Context context) {
        this(context, null);
    }

    public QQHeaderLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QQHeaderLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            log("onTouchEvent", "开始动画");
            if (mImgView.getHeight() > DEFAULT_SIZE_HEADER_HEIGHT) {
                ResetImageAnimation animation = new ResetImageAnimation(DEFAULT_SIZE_HEADER_HEIGHT);
                animation.setDuration(300);
                animation.setInterpolator(new BounceInterpolator());
                mImgView.startAnimation(animation);
            }
        }
        return super.onTouchEvent(ev);
    }

    class ResetImageAnimation extends Animation {

        private int extraHeight;//多余的高度
        private int currentHeight;

        public ResetImageAnimation(int targetHeigh) {
            this.currentHeight = mImgView.getHeight();
            this.extraHeight = mImgView.getHeight() - targetHeigh;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            mImgView.getLayoutParams().height = (int) (currentHeight - interpolatedTime * extraHeight);
            mImgView.requestLayout();
            super.applyTransformation(interpolatedTime, t);
        }
    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        log("onScrollChanged", "int l=" + l + ", int t=" + t + ", int oldl=" + oldl + ", int oldt=" + oldl);
        super.onScrollChanged(l, t, oldl, oldt);
    }

    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        if (deltaY < 0) {//向下滑
            if (mImgView.getHeight() == DEFAULT_SIZE_HEADER_HEIGHT_MAX) {
                return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
            }

            int targitHeight = mImgView.getHeight() - deltaY;
            if (targitHeight > DEFAULT_SIZE_HEADER_HEIGHT_MAX) {
                targitHeight = DEFAULT_SIZE_HEADER_HEIGHT_MAX;
            }
            mImgView.getLayoutParams().height = targitHeight;
            mImgView.requestLayout();
        }
        log("overScrollBy", "deltaY=" + deltaY + ",scrollY=" + scrollY + ",scrollRangeY=" + scrollRangeY + ",maxOverScrollY=" + maxOverScrollY + ",isTouchEvent = " + isTouchEvent);
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        log("onSizeChanged", "w = " + w + ",h = " + h + ",oldw = " + oldw + ",oldh = " + oldh);
        Log.i(TAG, "onSizeChanged: " + getTop());
        //ListView会滑出去的高度（负数）
        int deltaY = getTop();

        if (deltaY>DEFAULT_SIZE_HEADER_HEIGHT_MAX) {
            mImgView.getLayoutParams().height = DEFAULT_SIZE_HEADER_HEIGHT_MAX;
            mImgView.requestLayout();
        }

        super.onSizeChanged(w, h, oldw, oldh);
    }

    void log(String method, String msg) {
        Log.e(TAG, method + "==>" + msg);
    }

}
