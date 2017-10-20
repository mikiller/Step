package com.westepper.step.customViews;

import android.content.Context;
import android.content.pm.LabeledIntent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.westepper.step.R;

/**
 * Created by Mikiller on 2017/10/20.
 */

public class AchieveBadge extends LinearLayout {
    private ImageView iv_badge;
    private TextView tv_badge;
    public AchieveBadge(Context context) {
        this(context, null, 0);
    }

    public AchieveBadge(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AchieveBadge(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr){
        LayoutInflater.from(context).inflate(R.layout.layout_badge, this);
        iv_badge = (ImageView) findViewById(R.id.iv_badge);
        tv_badge = (TextView) findViewById(R.id.tv_badge);
    }

    public void setBadgeImg(int resId){
        iv_badge.setImageResource(resId);
    }

    public void setBadgeNum(int num){
        tv_badge.setText(String.valueOf(num));
    }
}
