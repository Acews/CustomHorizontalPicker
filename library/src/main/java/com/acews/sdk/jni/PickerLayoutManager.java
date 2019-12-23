package com.acews.sdk.jni;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 来源于 https://github.com/adityagohad/HorizontalPicker
 */

public class PickerLayoutManager extends LinearLayoutManager {

    private float mScaleDownBy = 0.5f;
    private float mScaleDownDistance = 0.9f;
    private boolean mChangeAlpha = true;
    private int mPosition = -1;
    private boolean mIsScrollEnabled = true;
    private onScrollStopListener mOnScrollStopListener;

    public PickerLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);
        scaleDownView();
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int orientation = getOrientation();
        if (orientation == HORIZONTAL) {
            int scrolled = super.scrollHorizontallyBy(dx, recycler, state);
            scaleDownView();
            return scrolled;
        } else return 0;
    }

    private void scaleDownView() {
        float mid = getWidth() / 2.0f;
        float unitScaleDownDist = mScaleDownDistance * mid;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child != null) {
                float childMid = (getDecoratedLeft(child) + getDecoratedRight(child)) / 2.0f;
                float scale = 1.0f + (-1 * mScaleDownBy) * (Math.min(unitScaleDownDist, Math.abs(mid - childMid))) / unitScaleDownDist;
                child.setScaleX(scale);
                child.setScaleY(scale);
                if (mChangeAlpha) {
                    child.setAlpha(scale);
                }
            }
        }
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        if (state == 0) {
            if (mOnScrollStopListener != null) {
                int selected = 0;
                float lastHeight = 0f;
                for (int i = 0; i < getChildCount(); i++) {
                    View child = getChildAt(i);
                    if (child != null) {
                        if (lastHeight < child.getScaleY()) {
                            lastHeight = child.getScaleY();
                            selected = i;
                        }
                    }
                }
                View child = getChildAt(selected);
                if (child != null) {
                    int position = this.getPosition(child);
                    if (mPosition != position) {
                        mPosition = position;
                        mOnScrollStopListener.selectedView(child, position);
                    }
                }

            }
        }
    }

    public void setScrollEnabled(boolean scrollEnable) {
        mIsScrollEnabled = scrollEnable;
    }

    @Override
    public boolean canScrollHorizontally() {
        return mIsScrollEnabled && super.canScrollHorizontally();
    }

    @Override
    public boolean canScrollVertically() {
        return mIsScrollEnabled && super.canScrollVertically();
    }

    public float getScaleDownBy() {
        return mScaleDownBy;
    }

    public void setScaleDownBy(float scaleDownBy) {
        this.mScaleDownBy = scaleDownBy;
    }

    public float getScaleDownDistance() {
        return mScaleDownDistance;
    }

    public void setScaleDownDistance(float scaleDownDistance) {
        this.mScaleDownDistance = scaleDownDistance;
    }

    public boolean isChangeAlpha() {
        return mChangeAlpha;
    }

    public void setChangeAlpha(boolean changeAlpha) {
        this.mChangeAlpha = changeAlpha;
    }

    public void setOnScrollStopListener(onScrollStopListener onScrollStopListener) {
        this.mOnScrollStopListener = onScrollStopListener;
    }

    public interface onScrollStopListener {
        void selectedView(View view, int position);
    }
}
