package com.uilib.inputlayout;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by Mikiller on 2016/7/11.
 */
public class InputRelativeLayout extends RelativeLayout {
    private KeyboardStateListener listener;

    public InputRelativeLayout(Context context) {
        super(context);
        setWillNotDraw(false);
    }

    public InputRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
    }

    public InputRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
    }

    public void setKeyboardStateListener(KeyboardStateListener listener){
        this.listener = listener;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isKeyboardShown(this.getRootView())) {
            if(listener != null){
                listener.onKeyboardShown();
            }
        } else {
            if(listener != null)
                listener.onKeyboardHiden();
        }
    }

    private boolean isKeyboardShown(View rootView) {
        final int keyboardHeight = 100;
        Rect rect = new Rect();
        rootView.getWindowVisibleDisplayFrame(rect);
        DisplayMetrics dm = rootView.getResources().getDisplayMetrics();
        int hx = rootView.getBottom() - rect.bottom;
        return hx > keyboardHeight * dm.density;
    }
}
