package com.acews.sdk.jni;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

/**
 * 作者：Acews
 * 日期：2019/12/20.
 */
public class CustomHorizontalPicker extends LinearLayout implements PickerLayoutManager.onScrollStopListener {

    private ItemAdapter mItemAdapter;
    private OnValueChangeListener mOnValueChangeListener;
    private RecyclerView mRecyclerView;
    private int mCurrentIndex = 0;

    public CustomHorizontalPicker(@NonNull Context context) {
        this(context, null);
    }

    public CustomHorizontalPicker(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("CustomViewStyleable")
    public CustomHorizontalPicker(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.customHorizontalPicker);
        float itemWidth = typedArray.getDimension(R.styleable.customHorizontalPicker_item_width, 50);
        int sideItems = typedArray.getInt(R.styleable.customHorizontalPicker_side_items, 2);
        typedArray.recycle();

        mRecyclerView = new RecyclerView(context);
        mRecyclerView.setClipToPadding(false);
        mRecyclerView.setPadding(dp2px((int) (itemWidth * sideItems)), 0, dp2px((int) (itemWidth * sideItems)), 0);
        mRecyclerView.setMinimumHeight(dp2px(50));

        ViewGroup.LayoutParams layoutParams = new LayoutParams(dp2px((int) (itemWidth * (sideItems * 2 + 1))),
            ViewGroup.LayoutParams.WRAP_CONTENT);
        addViewInLayout(mRecyclerView, 0, layoutParams);

        PickerLayoutManager pickerLayoutManager = new PickerLayoutManager(context, PickerLayoutManager.HORIZONTAL, false);
        pickerLayoutManager.setChangeAlpha(true);
        pickerLayoutManager.setOnScrollStopListener(this);
        mRecyclerView.setLayoutManager(pickerLayoutManager);

        //让item停留在中间位置
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(mRecyclerView);

        mItemAdapter = new ItemAdapter();
        mItemAdapter.setItemWidth(dp2px(itemWidth));
        mItemAdapter.bindToRecyclerView(mRecyclerView);

    }

    public void setData(ArrayList<String> arrayList) {
        mItemAdapter.setNewData(arrayList);
        if (arrayList.size() < 1) {
            return;
        }
        mRecyclerView.scrollToPosition(0);
//        mRecyclerView.smoothScrollToPosition(0);//这个方法或触发OnValueChangeListener
    }

    public List<String> getData() {
        return mItemAdapter.getData();
    }

    public void setCurrentIndex(int position) {
        List<String> list = mItemAdapter.getData();
        if (list.size() < 1) {
            return;
        }
        if (position < 0) {
            position = 0;
        }
        if (position > list.size() - 1) {
            position = list.size() - 1;
        }
        mCurrentIndex = position;
        mRecyclerView.scrollToPosition(position);
//        mRecyclerView.smoothScrollToPosition(0);//这个方法或触发OnValueChangeListener
    }

    public int getCurrentIndex() {
        return mCurrentIndex;
    }

    public String getValue() {
        return mItemAdapter.getItem(mCurrentIndex);
    }

    public void setOnValueChangeListener(OnValueChangeListener onValueChangeListener) {
        mOnValueChangeListener = onValueChangeListener;
    }

    public void setTextColor(int color) {
        mItemAdapter.setTextColor(color);
    }

    public void setTextSize(int textSize) {
        mItemAdapter.setTextSize(textSize);
    }

    public void setEnable(boolean enable) {
        PickerLayoutManager layoutManager = (PickerLayoutManager) mRecyclerView.getLayoutManager();
        assert layoutManager != null;
        layoutManager.setScrollEnabled(enable);
    }

    @Override
    public void selectedView(View view, int position) {
        mCurrentIndex = position;
        if (mOnValueChangeListener != null) {
            mOnValueChangeListener.onValueChange(view, position);
        }
    }

    private class ItemAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

        private int mTextColor = Color.parseColor("#666666");
        private int mItemWidth = 50;
        private int mTextSie = 24;

        private ItemAdapter() {
            super(null);
        }

        private void setItemWidth(int width) {
            mItemWidth = width;
            notifyDataSetChanged();
        }

        private void setTextColor(int textColor) {
            mTextColor = textColor;
            notifyDataSetChanged();
        }

        private void setTextSize(int textSize) {
            mTextSie = textSize;
            notifyDataSetChanged();
        }

        @Override
        protected View getItemView(int layoutResId, ViewGroup parent) {
            TextView textView = new TextView(getContext());
            textView.setLayoutParams(new LayoutParams(mItemWidth, ViewGroup.LayoutParams.WRAP_CONTENT));
            textView.setTextSize(mTextSie);
            textView.setGravity(Gravity.CENTER);
            textView.setId(R.id.text);
            textView.setPadding(0, dp2px(10), 0, dp2px(10));
            textView.setTextColor(mTextColor);
            return textView;
        }

        @Override
        protected void convert(BaseViewHolder helper, String item) {
            helper.setText(R.id.text, item);
        }

    }

    private int dp2px(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics());
    }

    public interface OnValueChangeListener {
        void onValueChange(View view, int position);
    }

}
