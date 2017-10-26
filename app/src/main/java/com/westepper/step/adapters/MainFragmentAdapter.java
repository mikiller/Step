package com.westepper.step.adapters;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.netease.nim.uikit.recent.RecentContactsFragment;
import com.westepper.step.R;
import com.westepper.step.base.BaseFragment;
import com.westepper.step.fragments.MineFragment;
import com.westepper.step.fragments.MsgFragment;
import com.westepper.step.fragments.MapFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mikiller on 2017/3/15.
 */

public class MainFragmentAdapter extends FragmentStatePagerAdapter {

    private List<BaseFragment> fragments;

    public MainFragmentAdapter(FragmentManager fm) {
        super(fm);
        fragments = new ArrayList<>();
        fragments.add(new MsgFragment());
        fragments.add(new MapFragment());
        //fragments.add(new DiscoveryFragment());
        fragments.add(new MineFragment());
    }

    @Override
    public BaseFragment getItem(int position) {
        return fragments.get(position);
    }

    public void fragmentCallback(int pos, int type, Intent data){
        getItem(pos).fragmentCallback(type, data);
    }

    @Override
    public int getCount() {
        return fragments == null ? 0 : fragments.size();
    }
}
