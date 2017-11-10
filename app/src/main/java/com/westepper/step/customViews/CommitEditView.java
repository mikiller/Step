package com.westepper.step.customViews;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.westepper.step.R;
import com.westepper.step.widgets.CommitGlobalLayoutListener;

/**
 * Created by Mikiller on 2017/11/1.
 */

public class CommitEditView extends RelativeLayout {

    RelativeLayout rl_commitInput;
    EditText edt_commit;
    Button btn_send;

    String hint;
    View focuceView;
    int DY = 0;
    boolean needTransY = true, needShow = true;
    CommitGlobalLayoutListener glListener;

    public CommitEditView(Context context) {
        this(context, null, 0);
    }

    public CommitEditView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommitEditView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context){
        LayoutInflater.from(context).inflate(R.layout.layout_commit_input, this);
        rl_commitInput = (RelativeLayout) findViewById(R.id.rl_commitInput);
        edt_commit = (EditText) findViewById(R.id.edt_commit);
        btn_send = (Button) findViewById(R.id.btn_send);

        ((Activity)context).getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(glListener = new CommitGlobalLayoutListener((Activity) context));
        glListener.setGlobalListener(new CommitGlobalLayoutListener.LayoutListener() {
            @Override
            public void onInputMethodShow(int transY) {
                if(!needShow)
                    return;
                rl_commitInput.setVisibility(View.VISIBLE);
                edt_commit.requestFocus();
                rl_commitInput.setTranslationY(transY + DY);
                if (!TextUtils.isEmpty(hint))
                    edt_commit.setHint(hint);
            }

            @Override
            public void onInputMethodHide() {
                rl_commitInput.setVisibility(View.GONE);
                edt_commit.setText("");
                needTransY = true;
                needShow = true;
            }
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ((Activity)getContext()).getWindow().getDecorView().getViewTreeObserver().removeOnGlobalLayoutListener(glListener);
        } else {
            ((Activity)getContext()).getWindow().getDecorView().getViewTreeObserver().removeGlobalOnLayoutListener(glListener);
        }
        super.onDetachedFromWindow();
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

    public void setNeedShow(boolean isNeed){
        needShow = isNeed;
    }

    public void setFocuceView(View focuceView) {
        this.focuceView = focuceView;
    }

    public void setDY(int dy) {
        DY = dy;
    }

    public boolean isVisible(){
        return rl_commitInput.getVisibility() == VISIBLE;
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
