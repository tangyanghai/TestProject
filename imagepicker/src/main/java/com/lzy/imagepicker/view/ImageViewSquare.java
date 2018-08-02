package com.lzy.imagepicker.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * <p>@author : tyh</p>
 * <p>@time : 2018/08/02</p>
 * <p>@for : $s$</p>
 * <p></p>
 */
public class ImageViewSquare extends AppCompatImageView {
    public ImageViewSquare(Context context) {
        super(context);
    }

    public ImageViewSquare(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageViewSquare(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(),getMeasuredWidth());
    }
}
