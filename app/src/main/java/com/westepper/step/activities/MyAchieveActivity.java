package com.westepper.step.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.westepper.step.R;
import com.westepper.step.adapters.AchieveRcvAdapter;
import com.westepper.step.adapters.ReachedAchRcvAdapter;
import com.westepper.step.base.BaseLogic;
import com.westepper.step.base.BaseModel;
import com.westepper.step.base.Constants;
import com.westepper.step.base.SuperActivity;
import com.westepper.step.customViews.TitleBar;
import com.westepper.step.logics.GetMyAchievementsLogic;
import com.westepper.step.responses.Achieve;
import com.westepper.step.responses.AchieveProgress;
import com.westepper.step.responses.MyAchievements;

import java.util.ArrayList;
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
                if(adapter.isNeedHead())
                    back();
                else{
                    adapter.setNeedHead(true);
//                    adapter.setPgsList(createData());
                    getMyAchieves();
                    if(rcv_ach.getAdapter() instanceof ReachedAchRcvAdapter)
                        rcv_ach.setAdapter(adapter);
                    titleBar.setTitle(achKind == Constants.ACH_CITY ? "探索" : "成就");
                }
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
                    //createAreaData();
//                    adapter.setPgsList(createAreas());
                    //get sub area percent from server

                    titleBar.setTitle(title);
                }else{
                    //goto sub ach
                    titleBar.setTitle(title);
                    reachedAdapter = new ReachedAchRcvAdapter(MyAchieveActivity.this);
                    for(Achieve achieve: MainActivity.mapData.getAchievementList()){
                        if(achieve.getAchieveKind().equals(title)){
                            if(title.equals("城市探索")) {
                                List<String> ids = new ArrayList<String>();
                                ids.add("3");
                                ids.add("4");
                                reachedAdapter.setReachIds(ids);
                            }
                            reachedAdapter.setAchAreaList(achieve.getAchieveAreaList());
                            rcv_ach.setAdapter(reachedAdapter);
                            break;
                        }
                    }
//                    List<String> reachList = new ArrayList<String>();
//                    reachList.add("1");
//                    reachList.add("2");
//                    reachedAdapter.setAchAreaList(reachList);
//                    rcv_ach.setAdapter(reachedAdapter);
                }
            }
        });
//        adapter.setPgsList(createData());
        getMyAchieves();
    }

//    private List<AchieveProgress> createData() {
//        List<AchieveProgress> data = new ArrayList<>();
//        if (achKind == Constants.ACH_CITY) {
//            createCities(data);
//        } else {
//            createAchieves(data);
//        }
//        return data;
//    }

//    private List<AchieveProgress> createAreas(){
//        List<AchieveProgress> data = new ArrayList<>();
//        data.add(createAchMenu("静安区", 0, 0, Constants.ACH_AREA));
//        data.add(createAchMenu("黄浦区", 0, 0, Constants.ACH_AREA));
//        data.add(createAchMenu("虹口区", 0, 0, Constants.ACH_AREA));
//        return data;
//    }
//
//    private void createCities(List<AchieveProgress> data) {
//        data.add(createAchMenu("上海", 0, 0, achKind));
//        data.add(createAchMenu("杭州", 0, 0, achKind));
//        data.add(createAchMenu("北京", 0, 0, achKind));
//        data.add(createAchMenu("广州", 0, 0, achKind));
//    }
//
//    private void createAchieves(List<AchieveProgress> data) {
//
//        data.add(createAchMenu("初识STEP", R.mipmap.ic_ach_step, 0, achKind));
//        data.add(createAchMenu("城市探索", R.mipmap.ic_ach_dis, 0, achKind));
//        data.add(createAchMenu("我爱上海", R.mipmap.ic_ach_sh, 0, achKind));
//        data.add(createAchMenu("地标名胜", R.mipmap.ic_ach_pos, 0, achKind));
//        data.add(createAchMenu("限时成就", R.mipmap.ic_ach_timer, 0, achKind));
//
//    }

    private AchieveProgress createAchMenu(String title, int pgs, int type) {
        AchieveProgress achPgs = new AchieveProgress();
        achPgs.setType(type);
        achPgs.setCategoryName(title);
        achPgs.setCategoryId(0);
//        achPgs.setName(title);
        achPgs.setPercent(pgs);
        return achPgs;
    }

    @Override
    protected void initData() {

    }

    private void getMyAchieves(){
        GetMyAchievementsLogic logic = new GetMyAchievementsLogic(this, new BaseModel());
        logic.setCallback(new BaseLogic.LogicCallback<MyAchievements>() {
            @Override
            public void onSuccess(MyAchievements response) {
                response.getPercentList().add(0, createAchMenu("初识STEP", 0, achKind));
                adapter.setMyAchieve(response);
            }

            @Override
            public void onFailed(String code, String msg, MyAchievements localData) {

            }
        });
        logic.sendRequest();
    }
}
