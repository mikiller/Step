package com.mkframe.step.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.widget.RadioGroup;

import com.amap.api.maps.TextureMapView;
import com.mkframe.R;
import com.mkframe.step.adapters.MainFragmentAdapter;
import com.mkframe.step.base.SuperActivity;
import com.mkframe.step.utils.MapUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Mikiller on 2017/9/1.
 */

public class MainActivity extends SuperActivity {
    @BindView(R.id.vp_content)
    ViewPager vp_content;
    @BindView(R.id.rdg_guideBar)
    RadioGroup rdg_guideBar;

    private List<Integer> Ids;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        initIds();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void initIds() {
        Ids = new ArrayList<>();
        Ids.add(R.id.rdb_msg);
        Ids.add(R.id.rdb_track);
        Ids.add(R.id.rdb_discovery);
        Ids.add(R.id.rdb_mine);
    }

    @Override
    protected void initView() {
        rdg_guideBar.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                vp_content.setCurrentItem(Ids.indexOf(checkedId));
            }
        });

        vp_content.setOffscreenPageLimit(1);
        vp_content.setAdapter(new MainFragmentAdapter(getSupportFragmentManager()));
        vp_content.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                rdg_guideBar.check(Ids.get(position));
                //setTitleBarOnPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        vp_content.setCurrentItem(1);
    }

    @Override
    protected void initData() {
    }
}
