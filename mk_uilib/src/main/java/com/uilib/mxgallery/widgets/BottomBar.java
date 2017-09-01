package com.uilib.mxgallery.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.uilib.R;
import com.uilib.mxgallery.listeners.OnBottomBtnClickListener;

/**
 * Created by Mikiller on 2017/5/15.
 */

public class BottomBar extends RelativeLayout {
    private TextView tv_left, tv_right, tv_right_num;

    public BottomBar(Context context) {
        super(context);
        initView(context, null);
    }

    public BottomBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs){
        LayoutInflater.from(context).inflate(R.layout.layout_bottom_bar, this, true);
        tv_left = (TextView) findViewById(R.id.tv_left);
        tv_right = (TextView) findViewById(R.id.tv_right);
        tv_right_num = (TextView) findViewById(R.id.tv_right_num);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BottomBar);
        if(ta != null){
            if(ta.getString(R.styleable.BottomBar_leftTxt) != null)
                tv_left.setText(ta.getString(R.styleable.BottomBar_leftTxt));
            if(ta.getString(R.styleable.BottomBar_rightTxt) != null)
                tv_right.setText(ta.getString(R.styleable.BottomBar_rightTxt));
            needNum(ta.getBoolean(R.styleable.BottomBar_needNum, true));
            ta.recycle();
        }
    }

    public void setLeftText(String txt){
        tv_left.setText(txt);
    }

    public void setLeftText(int id){
        tv_left.setText(getContext().getString(id));
    }

    public void setRightText(String txt){
        tv_right.setText(txt);
    }

    public void setRightText(int id){
        tv_right.setText(getContext().getString(id));
    }

    public void updateNum(int num){
        tv_right_num.setText(String.valueOf(num));
    }

    public void needNum(boolean isNeed){
        tv_right_num.setVisibility(isNeed ? VISIBLE : GONE);
    }

    public void updateBtnState(boolean isLeft, boolean enabled){
        if (isLeft)
            tv_left.setEnabled(enabled);
        else {
            tv_right.setEnabled(enabled);
            tv_right_num.setEnabled(enabled);
        }
    }

    public void setBtnListener(final OnBottomBtnClickListener listener){
        if(listener == null)
            return;
        tv_left.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onLeftClick();
            }
        });
        tv_right.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onRightClick();
            }
        });
    }
}
