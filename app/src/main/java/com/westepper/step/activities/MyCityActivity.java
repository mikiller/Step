package com.westepper.step.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.westepper.step.R;
import com.westepper.step.adapters.MyCityRcvAdapter;
import com.westepper.step.base.BaseLogic;
import com.westepper.step.base.Constants;
import com.westepper.step.base.SuperActivity;
import com.westepper.step.customViews.TitleBar;
import com.westepper.step.logics.GetMyAchieveBaseInfoLogic;
import com.westepper.step.logics.GetMyDiscoverBaseInfoLogic;
import com.westepper.step.models.BaseInfoModel;
import com.westepper.step.responses.AchievementBaseInfo;
import com.westepper.step.responses.AchCity;
import com.westepper.step.responses.DiscoveryBaseInfo;
import com.westepper.step.responses.MyAchieve;
import com.westepper.step.responses.MyDiscovery;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Mikiller on 2017/10/23.
 */

public class MyCityActivity extends SuperActivity {
    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.iv_city_badge)
    ImageView iv_city_badge;
    @BindView(R.id.rcv_cities)
    RecyclerView rcv_citier;

    int badge;
    int achKind;
    MyCityRcvAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if(savedInstanceState != null){
            badge = savedInstanceState.getInt(Constants.BADGE_KIND);
            achKind = savedInstanceState.getInt(Constants.ACH_KIND);
        }else if(getIntent() != null){
            badge = getIntent().getIntExtra(Constants.BADGE_KIND, 0);
            achKind = getIntent().getIntExtra(Constants.ACH_KIND, Constants.ACH_CITY);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dis_city);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(Constants.BADGE_KIND, badge);
        outState.putInt(Constants.ACH_KIND, achKind);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void initView() {
        titleBar.setTitleListener(new TitleBar.TitleListener() {
            @Override
            protected void onBackClicked() {
                back();
            }
        });

        rcv_citier.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rcv_citier.setAdapter(adapter = new MyCityRcvAdapter(achKind));
        switch (badge){
            case 1:
                titleBar.setTitle(achKind == Constants.ACH_CITY ? "发现城市" : "完成A1成就");
                iv_city_badge.setImageResource(achKind == Constants.ACH_CITY ? R.mipmap.ic_dis_city : R.mipmap.ic_ach_l1);
                break;
            case 2:
                titleBar.setTitle(achKind == Constants.ACH_CITY ? "点亮L1区域" : "完成A2成就");
                iv_city_badge.setImageResource(achKind == Constants.ACH_CITY ? R.mipmap.icon_dis_l1 : R.mipmap.ic_ach_l2);
                break;
            case 3:
                titleBar.setTitle(achKind == Constants.ACH_CITY ? "点亮L2区域" : "完成A3成就");
                iv_city_badge.setImageResource(achKind == Constants.ACH_CITY ? R.mipmap.icon_dis_l2 : R.mipmap.ic_ach_l3);
                break;
            case 4:
                titleBar.setTitle(achKind == Constants.ACH_CITY ? "点亮L3区域" : "完成A4成就");
                iv_city_badge.setImageResource(achKind == Constants.ACH_CITY ? R.mipmap.icon_dis_l3 : R.mipmap.ic_ach_l4);
                break;
        }

    }

    @Override
    protected void initData() {
        if (achKind == Constants.ACH_CITY)
            getDiscoverBaseInfoLogic();
        else
            getAchieveBaseInfoLogic();
    }

    private void getDiscoverBaseInfoLogic(){
        GetMyDiscoverBaseInfoLogic logic = new GetMyDiscoverBaseInfoLogic(this, new BaseInfoModel(badge));
        logic.setCallback(new BaseLogic.LogicCallback<DiscoveryBaseInfo>() {
            @Override
            public void onSuccess(DiscoveryBaseInfo response) {
                List<MyDiscovery> baseInfo = new ArrayList<MyDiscovery>();
                switch (badge){
                    case 1:
                        baseInfo = response.getDiscoverCityList();
                        break;
                    case 2:
                        baseInfo = response.getL1();
                        break;
                    case 3:
                        baseInfo = response.getL2();
                        break;
                    case 4:
                        baseInfo = response.getL3();
                        break;
                }

                adapter.setDataList(createMyDiscoveries(baseInfo));
            }

            @Override
            public void onFailed(String code, String msg, DiscoveryBaseInfo localData) {

            }
        });
        logic.sendRequest();
    }

    private List<AchCity> createMyDiscoveries(List<MyDiscovery> disList){
        List<AchCity> list = new ArrayList<>();
        for(MyDiscovery dis : disList){
            AchCity city = new AchCity();
            city.setType(badge);
            city.setTitle(badge == Constants.LEVEL1 ? dis.getCity_name() : dis.getAreaName());
            city.setDate(dis.getFormatCreated_at());
            list.add(city);
        }
        return list;
    }

    private void getAchieveBaseInfoLogic(){
        GetMyAchieveBaseInfoLogic logic = new GetMyAchieveBaseInfoLogic(this, new BaseInfoModel(badge));
        logic.setCallback(new BaseLogic.LogicCallback<AchievementBaseInfo>() {
            @Override
            public void onSuccess(AchievementBaseInfo response) {
                adapter.setDataList(createMyAchieves(response.getReachedList()));
            }

            @Override
            public void onFailed(String code, String msg, AchievementBaseInfo localData) {

            }
        });
        logic.sendRequest();
    }

    private List<AchCity> createMyAchieves(List<MyAchieve> achieveList){
        List<AchCity> list = new ArrayList<>();
        for(MyAchieve ach : achieveList){
            AchCity city = new AchCity();
            city.setType(badge);
            city.setTitle(ach.getAchievementName());
            city.setDate(ach.getFormatReached_at());
            list.add(city);
        }
        return list;
    }
}
