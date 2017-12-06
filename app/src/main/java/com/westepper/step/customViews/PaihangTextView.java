package com.westepper.step.customViews;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.westepper.step.R;

/**
 * Created by Mikiller on 2017/9/6.
 */

public class PaihangTextView extends LinearLayout {
    private TextView tv_name, tv_dis_num, tv_ach_num;

    public PaihangTextView(Context context) {
        this(context, null);
    }

    public PaihangTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PaihangTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr){
        LayoutInflater.from(context).inflate(R.layout.layout_paihang, this, true);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_dis_num = (TextView) findViewById(R.id.tv_dis_num);
        tv_ach_num = (TextView) findViewById(R.id.tv_ach_num);
    }

    public void setName(String name){
        tv_name.setText(name);
    }

    public void setDisNum(long num){
        if(num / 1000000 >= 10){
            tv_dis_num.setText(String.format("%1$d千万", num / 10000000));
        }else if(num / 10000 >= 10){
            tv_dis_num.setText(String.format("%1$d万", num / 10000));
        } else{
            tv_dis_num.setText(String.valueOf(num));
        }
    }

    public void setAchNum(long num){
        if(num / 1000000 >= 10){
            tv_ach_num.setText(String.format("%1$d千万", num / 10000000));
        }else if(num / 10000 >= 10){
            tv_ach_num.setText(String.format("%1$d万", num / 10000));
        }else{
            tv_ach_num.setText(String.valueOf(num));
        }
    }

    public void setTxtColor(boolean isUser){
        tv_name.setTextColor(Color.parseColor(isUser? "#ff00a8ff" : "#ff323c46"));
        tv_dis_num.setTextColor(getResources().getColor(isUser? R.color.colorPrimary : R.color.text_color_black));
        tv_ach_num.setTextColor(getResources().getColor(isUser ? R.color.colorPrimary : R.color.mainTabTxt));
    }
}
