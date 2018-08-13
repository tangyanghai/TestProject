package com.example.testlayoutmanager;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>@author : tyh</p>
 * <p>@time : 2018/08/08</p>
 * <p>@for : $s$</p>
 * <p></p>
 */
public class CardLayoutManager extends RecyclerView.LayoutManager {

    private SparseArray<Integer> itemHeights = new SparseArray<>();//各条目的高度
    private int verticalOffsetSize;//纵向滑动的距离
    private int mHeightToTop = 50;//距离上边的默认距离
    private int totalHight;//总高度
    private int mHeightBottom;
    private int width;
    private int heightView;

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

        totalHight = 0;
        //获取各条目的高度
        for (int i = 0; i < getItemCount(); i++) {
            View view = recycler.getViewForPosition(i);
            addView(view);
            measureChildWithMargins(view, 0, 0);
            int w = getDecoratedMeasuredWidth(view);
            int h = getDecoratedMeasuredHeight(view);
            itemHeights.put(i, h);
            totalHight += h;
            width = Math.max(width, w);
            heightView = Math.max(heightView, h);
        }
        totalHight += mHeightToTop;
        mHeightBottom = getHeight() - mHeightToTop - heightView;
        //隐藏和显示view
        showAndHide(recycler, state);
    }

    /**
     * @param recycler
     * @param state
     */
    private void showAndHide(RecyclerView.Recycler recycler, RecyclerView.State state) {
        //清空recyclerview的子view
        detachAndScrapAttachedViews(recycler);

        //已经滑出去了多少张卡片
        showAndHide(recycler);
    }

    private List<ViewHolder> list = new ArrayList<>();

    private void showAndHide(RecyclerView.Recycler recycler) {

        //显示区域--宽度方向
        int spaceHorizontal = (getWidth() - width) / 2;
        int temp = 0;
        int left,top,right,bottom;
        list.clear();
        //记录下所有的view将要显示的状态和位置信息--显示或者隐藏
        for (int i = 0; i < getItemCount(); i++) {
            View view = recycler.getViewForPosition(i);
            int h = itemHeights.get(i);
            temp += h;
            //已经滑出去了的
            if (-verticalOffsetSize - mHeightBottom > temp || temp > -verticalOffsetSize + getHeight() - mHeightBottom - mHeightToTop) {
                left = spaceHorizontal;
                top = mHeightToTop;
                right = getWidth() - spaceHorizontal;
                bottom = top + h;
                list.add(new ViewHolder(view, left, top, right, bottom, i));
            } else {
                left = spaceHorizontal;
                top = -verticalOffsetSize + getHeight() - mHeightBottom - temp;
                right = getWidth() - spaceHorizontal;
                bottom = top + h;
                ViewHolder viewHolder = new ViewHolder(view, left, top, right, bottom, i);
                viewHolder.show = true;
                list.add(viewHolder);
            }
        }

        //倒序方式隐藏和显示view,
        // 因为位置越是靠后的view越是在底层,
        // 所以必须要倒序,
        // 不然本该是最下面的一张板,
        // 将会压在最上面
        showViewByOppositePosition(recycler);
    }

    /**
     * 倒序显示view
     */
    private void showViewByOppositePosition(RecyclerView.Recycler recycler) {
        for (int i = list.size() - 1; i >= 0; i--) {
            ViewHolder viewHolder = list.get(i);
            if (viewHolder.show) {
                viewHolder.show();
            } else {
                if (i > 0) {
                    ViewHolder next = list.get(i - 1);
                    if (next.show) {
                        viewHolder.show();
                    } else {
                        viewHolder.recycle(recycler);
                    }
                }
            }
        }
    }


    public void log(String msg) {
        Log.e("------------", msg);
    }


    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int temp = dy;
        log("dy = " + dy + ", verticalOffsetSize = " + verticalOffsetSize + ",heightView = " + heightView);
        if (verticalOffsetSize + dy > 0) {//往上滑
            temp = -verticalOffsetSize;
        } else if (dy + verticalOffsetSize - heightView < mHeightToTop - totalHight) {
            temp = mHeightToTop - totalHight + heightView - verticalOffsetSize;
            log("越界 temp=" + temp);
        }
        verticalOffsetSize += temp;
        offsetChildrenVertical(temp);
        showAndHide(recycler, state);
        return temp;
    }

    @Override
    public boolean canScrollVertically() {
        return true;
    }

    @Override
    public boolean isAutoMeasureEnabled() {
        return true;
    }


    class ViewHolder {
        View view;
        int left;
        int top;
        int right;
        int bottom;
        boolean show;
        int position;

        public ViewHolder(View view, int left, int top, int right, int bottom, int position) {
            this.view = view;
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
            this.position = position;
        }

        public void show() {
            measureChildWithMargins(view, 0, 0);
            addView(view);
            layoutDecorated(view, left, top, right, bottom);
            log("显示" + position);
        }

        public void recycle(RecyclerView.Recycler recycler) {
            removeAndRecycleView(view, recycler);
            log("隐藏" + position);
        }
    }

}
