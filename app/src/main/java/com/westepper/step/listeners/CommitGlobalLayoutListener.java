package com.westepper.step.listeners;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
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
    String nickName;
    RelativeLayout rl_commitInput;
    EditText edt_commit;
    boolean needTransY = true;

    public CommitGlobalLayoutListener(Activity context, RelativeLayout layout, EditText edt) {
        mContext = context;
        screenHeight = DisplayUtil.getScreenHeight(context);
        rl_commitInput = layout;
        edt_commit = edt;
    }

    public void setCommitHint(String name) {
        nickName = name;
    }

    public boolean isNeedTransY() {
        return needTransY;
    }

    public void setNeedTransY(boolean needTransY) {
        this.needTransY = needTransY;
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
        } else {
            sbHeight = keyboardHeight;
            rl_commitInput.setVisibility(View.GONE);
            edt_commit.setText("");
            needTransY = true;
        }
    }
}
