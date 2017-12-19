package com.westepper.step.customViews;

import android.content.Context;
import android.content.pm.LabeledIntent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.westepper.step.R;

/**
 * Created by Mikiller on 2017/10/20.
 */

public class AchieveBadge extends LinearLayout {
    private ImageButton btn_badge;
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
        btn_badge = (ImageButton) findViewById(R.id.btn_badge);
        tv_badge = (TextView) findViewById(R.id.tv_badge);
    }

    public void setBadgeImg(int resId){
        btn_badge.setImageResource(resId);
    }

    public View getBadge(){
        return btn_badge;
    }

    public void setBadgeNum(int num){
        tv_badge.setText(String.valueOf(num));
        btn_badge.setEnabled(num > 0);
    }

    public void setOnClickListener(OnClickListener listener){
        btn_badge.setOnClickListener(listener);
    }

    public int getBadgeKind() {
        return (int) btn_badge.getTag();
    }

    public void setBadgeKind(int badgeKind) {
        btn_badge.setTag(badgeKind);
    }
}
