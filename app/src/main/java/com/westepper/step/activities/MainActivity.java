package com.westepper.step.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.RadioGroup;

import com.westepper.step.R;
import com.westepper.step.adapters.MainFragmentAdapter;
import com.westepper.step.base.Constants;
import com.westepper.step.base.SuperActivity;
import com.westepper.step.customViews.UntouchableViewPager;
import com.westepper.step.fragments.MapFragment;

import butterknife.BindView;

/**
 * Created by Mikiller on 2017/9/1.
 */

public class MainActivity extends SuperActivity {
    @BindView(R.id.vp_content)
    UntouchableViewPager vp_content;
    @BindView(R.id.rdg_guideBar)
    RadioGroup rdg_guideBar;

    MainFragmentAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

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
                        adapter.getItem(1).fragmentCallback(checkedId, null);
//                        ((MapFragment)adapter.getItem(1)).setIsTrack(checkedId == R.id.rdb_track);
                        break;
                }
            }
        });
        vp_content.setTouchable(false);
        vp_content.setOffscreenPageLimit(1);
        vp_content.setAdapter(adapter = new MainFragmentAdapter(getSupportFragmentManager()));
        rdg_guideBar.check(R.id.rdb_track);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_OK)
            return;
        switch (requestCode){
            case Constants.CHEANGE_HEADER:
                adapter.getItem(2).fragmentCallback(requestCode, data);
                break;
        }
    }
}
