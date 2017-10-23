package com.westepper.step.activities;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.westepper.step.R;
import com.westepper.step.adapters.MyCityRcvAdapter;
import com.westepper.step.base.Constants;
import com.westepper.step.base.SuperActivity;
import com.westepper.step.customViews.TitleBar;
import com.westepper.step.responses.DisCity;
import com.westepper.step.responses.Discovery;

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
    MyCityRcvAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if(savedInstanceState != null){
            badge = savedInstanceState.getInt(Constants.BADGE_KIND);
        }else if(getIntent() != null){
            badge = getIntent().getIntExtra(Constants.BADGE_KIND, 0);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dis_city);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(Constants.BADGE_KIND, badge);
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
        rcv_citier.setAdapter(adapter = new MyCityRcvAdapter());
        switch (badge){
            case 0:
                titleBar.setTitle("发现城市");
                iv_city_badge.setImageResource(R.mipmap.ic_dis_city);
                adapter.setDataList(createCitys(Constants.CITY, "发现上海", "发现杭州", "发现北京", "发现广州"));
                break;
            case 1:
                titleBar.setTitle("点亮L1区域");
                iv_city_badge.setImageResource(R.mipmap.ic_dis_l1);
                adapter.setDataList(createCitys(Constants.LEVEL, "点亮L1区域1", "点亮L1区域2", "点亮L1区域3", "点亮L1区域4"));
                break;
            case 2:
                titleBar.setTitle("点亮L2区域");
                iv_city_badge.setImageResource(R.mipmap.ic_dis_l2);
                adapter.setDataList(createCitys(Constants.LEVEL, "点亮L2区域1", "点亮L2区域2", "点亮L2区域3", "点亮L2区域4"));
                break;
            case 3:
                titleBar.setTitle("点亮L3区域");
                iv_city_badge.setImageResource(R.mipmap.ic_dis_l3);
                adapter.setDataList(createCitys(Constants.LEVEL, "点亮L3区域1", "点亮L3区域2", "点亮L3区域3", "点亮L3区域4"));
                break;
        }

    }

    private List<DisCity> createCitys(int type, String... titles){
        List<DisCity> list = new ArrayList<>();
        for(int i = 0; i < 4; i++){
            DisCity city = new DisCity();
            city.setType(type);
            city.setTitle(titles[i]);
            city.setDate("17/10/12");
            list.add(city);
        }
        return list;
    }

    @Override
    protected void initData() {

    }
}
