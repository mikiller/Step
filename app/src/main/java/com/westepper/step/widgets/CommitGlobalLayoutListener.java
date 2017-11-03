package com.westepper.step.widgets;

import android.app.Activity;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.uilib.utils.DisplayUtil;

/**
 * Created by Mikiller on 2017/10/10.
 */

public class CommitGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {
    Activity mContext;
    int sbHeight, keyboardHeight, screenHeight;
    Rect window = new Rect();
    LayoutListener globalListener;

    public CommitGlobalLayoutListener(Activity mContext) {
        this.mContext = mContext;
        screenHeight = DisplayUtil.getScreenHeight(mContext);
    }

    @Override
    public void onGlobalLayout() {
        mContext.getWindow().getDecorView().getWindowVisibleDisplayFrame(window);
        keyboardHeight = screenHeight - window.height();
        if(globalListener!= null) {
            if (keyboardHeight > screenHeight / 3) {
                globalListener.onInputMethodShow(sbHeight - keyboardHeight);
            } else {
                sbHeight = keyboardHeight;
                globalListener.onInputMethodHide();
            }
        }
    }

    public void setGlobalListener(LayoutListener listener){
        globalListener = listener;
    }

    public interface LayoutListener{
        void onInputMethodShow(int transY);
        void onInputMethodHide();
    }
}
