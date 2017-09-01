package com.uilib.mxselectreslayout;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Mikiller on 2016/8/15.
 */
public class WrapLinearLayoutManager extends LinearLayoutManager {
    public WrapLinearLayoutManager(Context context) {
        super(context);
    }

    public WrapLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public WrapLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    @Override
    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
        if (getItemCount() == 0 || state.getItemCount() == 0)
            super.onMeasure(recycler, state, widthSpec, heightSpec);
        else {
            View item;
            int height;
            item = recycler.getViewForPosition(0);
            measureChild(item, widthSpec, heightSpec);
            height = item.getMeasuredHeight() + getDecoratedBottom(item);
            if (getOrientation() == LinearLayoutManager.VERTICAL) {
                for (int i = 1; i < getChildCount(); i++) {
                    item = recycler.getViewForPosition(i);
                    measureChild(item, widthSpec, heightSpec);
                    height += item.getMeasuredHeight() + getDecoratedBottom(item);
                }
            }
            setMeasuredDimension(View.MeasureSpec.getSize(widthSpec), height);

        }
    }
}
