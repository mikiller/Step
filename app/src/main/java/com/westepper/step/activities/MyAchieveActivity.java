package com.westepper.step.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;

import com.westepper.step.R;
import com.westepper.step.adapters.AchieveRcvAdapter;
import com.westepper.step.adapters.ReachedAchRcvAdapter;
import com.westepper.step.base.BaseLogic;
import com.westepper.step.base.BaseModel;
import com.westepper.step.base.Constants;
import com.westepper.step.base.SuperActivity;
import com.westepper.step.customViews.TitleBar;
import com.westepper.step.logics.GetMyAchievementsLogic;
import com.westepper.step.logics.GetMyDiscoverBaseInfoLogic;
import com.westepper.step.models.BaseInfoModel;
import com.westepper.step.responses.Achieve;
import com.westepper.step.responses.AchieveProgress;
import com.westepper.step.responses.DiscoveryBaseInfo;
import com.westepper.step.responses.MyAchievements;
import com.westepper.step.utils.MapUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Mikiller on 2017/10/20.
 */

public class MyAchieveActivity extends SuperActivity {
    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.rcv_ach)
    RecyclerView rcv_ach;

    int achKind;
    AchieveRcvAdapter adapter;
    ReachedAchRcvAdapter reachedAdapter;

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
        titleBar.setBgColor(achKind == Constants.ACH_CITY ? getResources().getColor(R.color.splash_label) : getResources().getColor(R.color.colorPrimary));
        titleBar.setSubImg(R.mipmap.ic_share);
        titleBar.setTitle(achKind == Constants.ACH_CITY ? "探索" : "成就");
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

        rcv_ach.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rcv_ach.setAdapter(adapter = new AchieveRcvAdapter(this, achKind));
        adapter.setListener(new AchieveRcvAdapter.OnMenuClickListener() {
            @Override
            public void onMenuClicked(String title, int kind) {
                if(kind == Constants.ACH_CITY){
                    titleBar.setTitle(title);
                    GetMyDiscoverBaseInfoLogic logic = new GetMyDiscoverBaseInfoLogic(MyAchieveActivity.this, new BaseInfoModel(5));
                    logic.setCallback(new BaseLogic.LogicCallback<DiscoveryBaseInfo>() {
                        @Override
                        public void onSuccess(DiscoveryBaseInfo response) {
                            adapter.getMyAchieve().setType(Constants.ACH_AREA);
                            adapter.getMyAchieve().setL2percent(response.getL2percent());
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onFailed(String code, String msg, DiscoveryBaseInfo localData) {

                        }
                    });
                    logic.sendRequest();

                }else{
                    //goto sub ach
                    titleBar.setTitle(title);
                    reachedAdapter = new ReachedAchRcvAdapter(MyAchieveActivity.this);
//                    for(Achieve achieve: MainActivity.mapData.getAchievementList()){
                    for(Achieve achieve : MapUtils.getInstance().mapData.getAchievementList()){
                        if(achieve.getAchieveKind().equals(title)){
                            reachedAdapter.setReachIds(MapUtils.getInstance().mapData.getReachedAchieveIdList());
                            reachedAdapter.setAchAreaList(achieve.getAchieveAreaList());
                            rcv_ach.setAdapter(reachedAdapter);
                            break;
                        }
                    }
                }
            }
        });
        getMyAchieves();
    }

    private AchieveProgress createAchMenu(int id, String title, int pgs) {
        AchieveProgress achPgs = new AchieveProgress();
        achPgs.setCategoryName(title);
        achPgs.setCategoryId(id);
        achPgs.setPercent(pgs);
        return achPgs;
    }

    @Override
    protected void initData() {

    }

    private void getMyAchieves(){
        final AchieveProgress[] achList = new AchieveProgress[5];
        achList[0] = createAchMenu(1, "初识STEP", 0);
        achList[1] = createAchMenu(2, "我爱上海", 0);
        achList[2] = createAchMenu(3, "探索世界", 0);
        achList[3] = createAchMenu(4, "地标名胜", 0);
        achList[4] = createAchMenu(5, "限时成就", 0);
        GetMyAchievementsLogic logic = new GetMyAchievementsLogic(this, new BaseModel());
        logic.setType(achKind);
        logic.setCallback(new BaseLogic.LogicCallback<MyAchievements>() {
            @Override
            public void onSuccess(MyAchievements response) {
                if(achKind == Constants.ACH_BADGE) {
                    for (AchieveProgress ap : response.getPercentList()) {
                        achList[ap.getCategoryId() - 1] = ap;
                    }
                    response.setPercentList(Arrays.asList(achList));
                }
                adapter.setMyAchieve(response);
            }

            @Override
            public void onFailed(String code, String msg, MyAchievements localData) {

            }
        });
        logic.sendRequest();
    }

    @Override
    public void back(){
        if(adapter.isNeedHead())
            super.back();
        else{
            adapter.setNeedHead(true);
//                    adapter.setPgsList(createData());
            getMyAchieves();
            if(rcv_ach.getAdapter() instanceof ReachedAchRcvAdapter)
                rcv_ach.setAdapter(adapter);
            titleBar.setTitle(achKind == Constants.ACH_CITY ? "探索" : "成就");
        }
    }
}
