package com.westepper.step.customViews;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Mikiller on 2017/9/4.
 */

public class UntouchableViewPager extends ViewPager {
    private boolean touchable = true;

    public UntouchableViewPager(Context context) {
        super(context);
    }

    public UntouchableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setTouchable(boolean canTouch){
        touchable = canTouch;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return touchable ? super.onTouchEvent(ev) : touchable;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return touchable ? super.onInterceptTouchEvent(ev) : touchable;
    }
}
