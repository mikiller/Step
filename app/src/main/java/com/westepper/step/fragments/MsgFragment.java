package com.westepper.step.fragments;

import android.content.Intent;
import android.view.View;

import com.netease.nim.uikit.recent.RecentContactsFragment;
import com.westepper.step.R;
import com.westepper.step.activities.MyFriendsActivity;
import com.westepper.step.base.BaseFragment;
import com.westepper.step.base.SuperActivity;
import com.westepper.step.customViews.TitleBar;
import com.westepper.step.utils.ActivityManager;

import butterknife.BindView;

/**
 * Created by Mikiller on 2017/9/1.
 */

public class MsgFragment extends BaseFragment {
    @BindView(R.id.titleBar)
    TitleBar titleBar;
    RecentContactsFragment fragment;
    @Override
    protected void setLayoutRes() {
        layoutRes = R.layout.fragment_msg;
    }

    @Override
    protected void initView() {
        titleBar.setSubImg(R.mipmap.ic_plus);
        titleBar.setTitleListener(new TitleBar.TitleListener() {

            @Override
            protected void onMoreClicked() {

            }
        });
        fragment = new RecentContactsFragment();
        fragment.setMyFirendClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityManager.startActivity(getActivity(), MyFriendsActivity.class);
            }
        });
        getChildFragmentManager().beginTransaction().replace(R.id.messages_fragment, fragment).commit();
    }

    @Override
    public void fragmentCallback(int type, Intent data) {

    }
}
