package com.westepper.step.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.widget.RadioGroup;

import com.westepper.step.R;
import com.westepper.step.adapters.MainFragmentAdapter;
import com.westepper.step.base.SuperActivity;

import butterknife.BindView;

/**
 * Created by Mikiller on 2017/9/1.
 */

public class MainActivity extends SuperActivity {
    @BindView(R.id.vp_content)
    ViewPager vp_content;
    @BindView(R.id.rdg_guideBar)
    RadioGroup rdg_guideBar;

//    private List<Integer> Ids;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        initIds();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

//    private void initIds() {
//        Ids = new ArrayList<>();
//        Ids.add(R.id.rdb_msg);
//        Ids.add(R.id.rdb_track);
//        Ids.add(R.id.rdb_discovery);
//        Ids.add(R.id.rdb_mine);
//    }

    @Override
    protected void initView() {
        rdg_guideBar.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rdb_msg:
                        vp_content.setCurrentItem(0);
                        break;
                    case R.id.rdb_mine:
                        vp_content.setCurrentItem(2);
                        break;
                    default:
                        vp_content.setCurrentItem(1);
                        break;
                }
//                vp_content.setCurrentItem(Ids.indexOf(checkedId));
            }
        });

        vp_content.setOffscreenPageLimit(1);
        vp_content.setAdapter(new MainFragmentAdapter(getSupportFragmentManager()));
//        vp_content.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                rdg_guideBar.check(Ids.get(position));
//                //setTitleBarOnPageSelected(position);
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
        vp_content.setCurrentItem(1);
    }

    @Override
    protected void initData() {
    }
}
