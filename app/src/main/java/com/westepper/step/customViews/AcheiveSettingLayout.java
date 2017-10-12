package com.westepper.step.customViews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.westepper.step.R;

/**
 * Created by Mikiller on 2017/10/12.
 */

public class AcheiveSettingLayout extends RelativeLayout {
    private LinearLayout ll_ach_setting;
    private RelativeLayout rl_ach_map;
    private ImageButton btn_ach_map;
    private CheckBox ckb_ach_map1;

    public AcheiveSettingLayout(Context context) {
        this(context, null, 0);
    }

    public AcheiveSettingLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AcheiveSettingLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr){
        LayoutInflater.from(context).inflate(R.layout.layout_acheive_setting, this);


        getRootView().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setVisibility(GONE);
            }
        });
    }
}
