package com.westepper.step.fragments;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.contact_selector.activity.ContactSelectActivity;
import com.netease.nim.uikit.recent.RecentContactsCallback;
import com.netease.nim.uikit.recent.RecentContactsFragment;
import com.netease.nim.uikit.team.helper.TeamHelper;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.SystemMessageService;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.uilib.customdialog.CustomDialog;
import com.westepper.step.R;
import com.westepper.step.activities.AddFriendActivity;
import com.westepper.step.activities.MyFriendsActivity;
import com.westepper.step.activities.SystemMessageActivity;
import com.westepper.step.base.BaseFragment;
import com.westepper.step.base.Constants;
import com.westepper.step.base.SuperActivity;
import com.westepper.step.customViews.RecentContactsHeader;
import com.westepper.step.customViews.TitleBar;
import com.westepper.step.utils.ActivityManager;
import com.westepper.step.widgets.CustomP2PSessionCustomization;
import com.westepper.step.widgets.CustomTeamSessionCustomization;

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
                showCreateChattingDlg();
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
        fragment.setCallback(new RecentContactsCallback() {
            @Override
            public void onRecentContactsLoaded() {

            }

            @Override
            public void onUnreadCountChange(int unreadCount) {

            }

            @Override
            public void onItemClick(RecentContact recent) {
                if (recent.getSessionType() == SessionTypeEnum.Team) {
                    NimUIKit.startTeamSession(getActivity(), recent.getContactId());
                } else if (recent.getSessionType() == SessionTypeEnum.P2P) {
                    NimUIKit.startP2PSession(getActivity(), recent.getContactId());
                }
            }

            @Override
            public String getDigestOfAttachment(RecentContact recent, MsgAttachment attachment) {
                return null;
            }

            @Override
            public String getDigestOfTipMsg(RecentContact recent) {
                return null;
            }
        });
        getChildFragmentManager().beginTransaction().replace(R.id.messages_fragment, fragment).commit();
    }

    private void showCreateChattingDlg(){
        final CustomDialog dlg = new CustomDialog(getActivity());
        dlg.setLayoutRes(R.layout.layout_newdis_dlg).setOnCustomBtnClickListener(new CustomDialog.onCustomBtnsClickListener() {
            @Override
            public void onBtnClick(int id) {
                switch (id){
                    case R.id.btn_mood:
                        //添加朋友
                        ActivityManager.startActivity(getActivity(), AddFriendActivity.class);
                        break;
                    case R.id.btn_outgo:
                        //发起群聊
                        ContactSelectActivity.Option advancedOption = TeamHelper.getCreateContactSelectOption(null, 50);
                        NimUIKit.startContactSelect(getActivity(), advancedOption, Constants.REQUEST_CODE_ADVANCED);
                        break;
                }
                dlg.dismiss();
            }
        }, R.id.btn_mood, R.id.btn_outgo).setCustomBtnText("添加朋友", "发起群聊").show();
    }

    @Override
    public void fragmentCallback(int type, Intent data) {

    }
}
