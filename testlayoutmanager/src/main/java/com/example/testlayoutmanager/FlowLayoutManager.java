package com.example.testlayoutmanager;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

/**
 * <p>@author : tyh</p>
 * <p>@time : 2018/08/07</p>
 * <p>@for : $s$</p>
 * <p></p>
 */
public class FlowLayoutManager extends RecyclerView.LayoutManager {

    //总高度
    int totalHeigh;
    SparseArray<Rect> allItemFrams = new SparseArray<>();
    //纵向滑动了的距离
    int verticalScrollOffset;

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        //没有条目,直接返回
        if (getItemCount() <= 0) {
            return;
        }

        //支持动画,直接跳过
        if (state.isPreLayout()) {
            return;
        }

        //将视图分离
        detachAndScrapAttachedViews(recycler);

        int offsetX = 0;
        int offsetY = 0;
        int viewH = 0;

        for (int i = 0; i < getItemCount(); i++) {
            View view = recycler.getViewForPosition(i);
            addView(view);
            measureChildWithMargins(view, 0, 0);

            int w = getDecoratedMeasuredWidth(view);
            int h = getDecoratedMeasuredHeight(view);

            Rect fram = allItemFrams.get(i);
            if (fram == null) {
                fram = new Rect();
            }

            //如果需要换行
            if (offsetX + w > getWidth()) {
                offsetX = w;
                offsetY += viewH;
                fram.set(0, offsetY, w, offsetY + h);
            } else {
                //不需要换行
                fram.set(offsetX, offsetY, offsetX + w, offsetY + h);
                offsetX += w;
            }
            viewH = h;
            allItemFrams.put(i, fram);
        }
        //总高度
        totalHeigh = offsetY + viewH;
        log("总高度" + totalHeigh);
        showView(recycler, state);
    }

    private void showView(RecyclerView.Recycler recycler, RecyclerView.State state) {
        //清空recyclerview的子view
        detachAndScrapAttachedViews(recycler);

        //显示区域
        Rect rect = new Rect(0, verticalScrollOffset, getWidth(), verticalScrollOffset + getHeight());

        //将滑出去的子view回收
        for (int i = 0; i < getItemCount(); i++) {
            log("开始回收滑出去的");
            View view = recycler.getViewForPosition(i);
            Rect childRect = allItemFrams.get(i);
            if (!Rect.intersects(rect, childRect)) {
                removeAndRecycleView(view, recycler);
                log("回收滑出去的" + i);
            }else {
                measureChildWithMargins(view, 0, 0);
                addView(view);
                layoutDecorated(view, childRect.left, childRect.top - verticalScrollOffset
                        , childRect.right, childRect.bottom - verticalScrollOffset);
                log("显示" + i);
            }
        }
    }

    public void log(String msg) {
        Log.e("------------", msg);
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int tral = dy;

        //去除边界外的尺寸
        if (verticalScrollOffset+dy<0) {
            tral = -verticalScrollOffset;
        }else if (verticalScrollOffset+dy>totalHeigh-getHeight()){
            tral = totalHeigh-getHeight()-verticalScrollOffset;
        }

        verticalScrollOffset+=tral;
        offsetChildrenVertical(tral);
        showView(recycler,state);
        return tral;
    }

    @Override
    public boolean canScrollVertically() {
        return true;
    }

    @Override
    public boolean isAutoMeasureEnabled() {
        return true;
    }

    @Override
    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
        super.onMeasure(recycler, state, widthSpec, heightSpec);
    }
}
