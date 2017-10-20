package com.westepper.step.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.westepper.step.R;
import com.westepper.step.base.Constants;
import com.westepper.step.base.SuperActivity;
import com.westepper.step.customViews.AchieveBadge;
import com.westepper.step.customViews.TitleBar;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.BindViews;

/**
 * Created by Mikiller on 2017/10/20.
 */

public class MyAchieveActivity extends SuperActivity {
    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.ll_achMenus)
    LinearLayout ll_achMenus;
    @BindView(R.id.rl_scoreBg)
    RelativeLayout rl_scoreBg;
    @BindView(R.id.tv_score)
    TextView tv_score;
    @BindView(R.id.tv_badgeTitle)
    TextView tv_badgeTitle;
    @BindView(R.id.tv_achTitle)
    TextView tv_achTitle;
    @BindView(R.id.tv_achNum)
    TextView tv_achNum;
    @BindViews({R.id.myAchCity, R.id.myAchL1, R.id.myAchL2, R.id.myAchL3})
    AchieveBadge[] badges;

    int achKind;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if(savedInstanceState != null){
            achKind = savedInstanceState.getInt(Constants.ACH_KIND);
        }else if(getIntent() != null){
            achKind = getIntent().getIntExtra(Constants.ACH_KIND, Constants.ACH_CITY);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myachieve);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(Constants.ACH_KIND, achKind);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void initView() {
        int bgColor = achKind == Constants.ACH_CITY ? getResources().getColor(R.color.splash_label) : getResources().getColor(R.color.colorPrimary);
        titleBar.setBackgroundColor(bgColor);
        titleBar.setSubImg(R.mipmap.ic_share);
        titleBar.setTitle(achKind == Constants.ACH_CITY ? "探索" : "成就");
        rl_scoreBg.setBackgroundColor(bgColor);
        titleBar.setTitleListener(new TitleBar.TitleListener() {
            @Override
            protected void onBackClicked() {
                back();
            }

            @Override
            protected void onMoreClicked() {
                super.onMoreClicked();
            }
        });
    }

    @Override
    protected void initData() {

    }
}
