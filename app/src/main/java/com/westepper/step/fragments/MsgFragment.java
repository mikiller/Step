package com.westepper.step.fragments;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.netease.nim.uikit.recent.RecentContactsFragment;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.SystemMessageService;
import com.westepper.step.R;
import com.westepper.step.activities.MyFriendsActivity;
import com.westepper.step.activities.SystemMessageActivity;
import com.westepper.step.base.BaseFragment;
import com.westepper.step.base.SuperActivity;
import com.westepper.step.customViews.RecentContactsHeader;
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
        final RecentContactsHeader header = new RecentContactsHeader(getActivity());
        header.setHeaderListener(new RecentContactsHeader.HeaderClickedListener() {
            @Override
            public void onMyFriendClick() {
                ActivityManager.startActivity(getActivity(), MyFriendsActivity.class);
            }

            @Override
            public void onSystemMsgClick() {
                NIMClient.getService(SystemMessageService.class).resetSystemMessageUnreadCount();
                Intent intent = new Intent(getActivity(), SystemMessageActivity.class);
                startActivity(intent);
            }
        });
        fragment.setRecentContactsHeader(header);
        fragment.setUpdateSysCountListener(new RecentContactsFragment.UpdateSystemVerifCountListener() {
            @Override
            public void updateSystemVerifCount(int count) {
                header.toggleTip(count);
            }
        });
        getChildFragmentManager().beginTransaction().replace(R.id.messages_fragment, fragment).commit();
    }

    @Override
    public void fragmentCallback(int type, Intent data) {

    }
}
