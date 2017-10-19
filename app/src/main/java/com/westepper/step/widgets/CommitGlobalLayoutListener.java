package com.westepper.step.widgets;

import android.app.Activity;
import android.graphics.Rect;
import android.text.TextUtils;
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
    String nickName, hint;
    RelativeLayout rl_commitInput;
    EditText edt_commit;
    Button btn_send;
    View focuceView;
    boolean needTransY = true;

    public CommitGlobalLayoutListener(Activity context, RelativeLayout layout, EditText edt, Button btn) {
        mContext = context;
        screenHeight = DisplayUtil.getScreenHeight(context);
        rl_commitInput = layout;
        edt_commit = edt;
        btn_send = btn;
    }

    public void setCommitHint(String name) {
        nickName = name;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public boolean isNeedTransY() {
        return needTransY;
    }

    public void setNeedTransY(boolean needTransY) {
        this.needTransY = needTransY;
    }


    public void setFocuceView(View focuceView) {
        this.focuceView = focuceView;
    }

    @Override
    public void onGlobalLayout() {
        mContext.getWindow().getDecorView().getWindowVisibleDisplayFrame(window);
        keyboardHeight = screenHeight - window.height();
        if (keyboardHeight > screenHeight / 3) {
            rl_commitInput.setVisibility(View.VISIBLE);
            edt_commit.requestFocus();
            rl_commitInput.setTranslationY(sbHeight - keyboardHeight);
            if (!TextUtils.isEmpty(nickName))
                edt_commit.setHint(String.format("回复%1$s：", nickName));
            else if(!TextUtils.isEmpty(hint))
                edt_commit.setHint(hint);
        } else {
            sbHeight = keyboardHeight;
            rl_commitInput.setVisibility(View.GONE);
            edt_commit.setText("");
            needTransY = true;
        }
    }

    public void setOnSendListener(final OnSendListener listener){
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null)
                    listener.onSend(focuceView, edt_commit.getText().toString());
            }
        });
    }

    public interface OnSendListener{
        void onSend(View focuseView, String txt);
    }
}
