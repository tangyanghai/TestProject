package com.example.testconstraintlayout;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * <p>@author : tyh</p>
 * <p>@time : 2018/08/09</p>
 * <p>@for : $s$</p>
 * <p></p>
 */
public class SquareImageView extends AppCompatImageView {
    public SquareImageView(Context context) {
        super(context);
    }

    public SquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int min = Math.min(getMeasuredHeight(),getMeasuredWidth());
        setMeasuredDimension(min,min);
    }
}
