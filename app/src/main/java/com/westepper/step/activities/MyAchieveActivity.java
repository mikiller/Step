package com.westepper.step.activities;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.uilib.customdialog.CustomDialog;
import com.westepper.step.R;
import com.westepper.step.base.Constants;
import com.westepper.step.base.SuperActivity;
import com.westepper.step.customViews.AchieveBadge;
import com.westepper.step.customViews.MyAchieveMenu;
import com.westepper.step.customViews.TitleBar;
import com.westepper.step.utils.ActivityManager;

import java.util.HashMap;
import java.util.Map;

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
        if (savedInstanceState != null) {
            achKind = savedInstanceState.getInt(Constants.ACH_KIND);
        } else if (getIntent() != null) {
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
        titleBar.setBgColor(bgColor);
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

        tv_badgeTitle.setText(achKind == Constants.ACH_CITY ? "城市徽章" : "探索等级");
        badges[0].setBadgeImg(achKind == Constants.ACH_CITY ? R.mipmap.ic_dis_city : R.mipmap.ic_ach_l1);
        badges[0].setBadgeNum(1);
        badges[1].setBadgeImg(achKind == Constants.ACH_CITY ? R.mipmap.ic_dis_l1 : R.mipmap.ic_ach_l2);
        badges[1].setBadgeNum(1);
        badges[2].setBadgeImg(achKind == Constants.ACH_CITY ? R.mipmap.ic_dis_l2 : R.mipmap.ic_ach_l3);
        badges[2].setBadgeNum(1);
        badges[3].setBadgeImg(achKind == Constants.ACH_CITY ? R.mipmap.ic_dis_l3 : R.mipmap.ic_ach_l4);
        badges[3].setBadgeNum(1);
        if (achKind == Constants.ACH_CITY) {
            for (int i = 0; i < badges.length; i++) {
                final int badgeKind = i;
                badges[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, Object> args = new HashMap<String, Object>();
                        args.put(Constants.BADGE_KIND, badgeKind);
                        Map<String, View> trans = new HashMap<String, View>();
                        trans.put(getString(R.string.city_badge), v);
                        ActivityManager.startActivityWithTransAnim(MyAchieveActivity.this, MyCityActivity.class, trans, args);
                    }
                });
            }
        }else{
            badges[0].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CustomDialog dlg = new CustomDialog(MyAchieveActivity.this);
                    dlg.setLayoutRes(R.layout.layout_congratulation);
                    dlg.setCancelable(true);
                    dlg.show();
                }
            });
        }

        tv_achTitle.setText(achKind == Constants.ACH_CITY ? "发现城市" : "获得成就");
        tv_achNum.setVisibility(achKind == Constants.ACH_CITY ? View.GONE : View.VISIBLE);

        if (achKind == Constants.ACH_CITY) {
            createCities();
        } else {
            createAchieves();
        }
    }

    private void createCities() {
        ll_achMenus.addView(createAchMenu("上海", 0, null, 0));
        ll_achMenus.addView(createAchMenu("杭州", 0, null, 0));
        ll_achMenus.addView(createAchMenu("北京", 0, null, 0));
        ll_achMenus.addView(createAchMenu("广州", 0, null, 0));
    }

    private void createAchieves() {
        ll_achMenus.addView(createAchMenu(null, R.mipmap.ic_ach_step, "初识STEP", 0));
        ll_achMenus.addView(createAchMenu(null, R.mipmap.ic_ach_dis, "探索世界", 0));
        ll_achMenus.addView(createAchMenu(null, R.mipmap.ic_ach_sh, "我爱上海", 0));
        ll_achMenus.addView(createAchMenu(null, R.mipmap.ic_ach_pos, "地标名胜", 0));
        ll_achMenus.addView(createAchMenu(null, R.mipmap.ic_ach_timer, "限时成就", 0));

    }

    private MyAchieveMenu createAchMenu(String title, int iconId, String subTitle, int pgs) {
        MyAchieveMenu menu = new MyAchieveMenu(this);
        if (!TextUtils.isEmpty(title)) {
            menu.setTitle(title);
            menu.setTag(title);
        } else {
            menu.setMenu_icon(iconId);
        }
        if (!TextUtils.isEmpty(subTitle)) {
            menu.setSubTitle(subTitle);
            menu.setNeedSubTitle(true);
            menu.setTag(subTitle);
        } else {
            menu.setNeedSubTitle(false);
        }
        menu.setPgs(pgs);

        return menu;
    }

    @Override
    protected void initData() {

    }
}
