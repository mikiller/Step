package com.westepper.step.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.cache.NimUserInfoCache;
import com.netease.nim.uikit.common.ui.widget.UserLayout;
import com.netease.nim.uikit.common.util.sys.NetworkUtil;
import com.netease.nim.uikit.contact_selector.activity.ContactSelectActivity;
import com.netease.nim.uikit.session.helper.MessageListPanelHelper;
import com.netease.nim.uikit.team.helper.TeamHelper;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.friend.FriendService;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.uilib.customdialog.CustomDialog;
import com.uilib.mxmenuitem.MyMenuItem;
import com.westepper.step.R;
import com.westepper.step.base.Constants;
import com.westepper.step.base.SuperActivity;
import com.westepper.step.customViews.TitleBar;
import com.westepper.step.customViews.ToggleBox;
import com.westepper.step.utils.ContactsHelper;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by Mikiller on 2017/11/21.
 */

public class P2PSessionDetailActivity extends SuperActivity implements View.OnClickListener{
    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.menu_find_history)
    MyMenuItem menu_find_history;
    @BindView(R.id.menu_clear_history)
    MyMenuItem menu_clear_history;
    @BindView(R.id.ckb_newNotify)
    ToggleBox ckb_newNotify;
    @BindView(R.id.layout_user)
    UserLayout layout_user;
    @BindView(R.id.layout_createTeam)
    UserLayout layout_createTeam;

    String account;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if(savedInstanceState != null)
            account = savedInstanceState.getString(Constants.USERINFO);
        else if(getIntent() != null)
            account = getIntent().getStringExtra(Constants.USERINFO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p2p_session_detail);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(Constants.USERINFO, account);
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

        layout_user.setHeaderByAccount(account);
        layout_user.setTvName(NimUserInfoCache.getInstance().getUserDisplayName(account));
        layout_createTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> accounts = new ArrayList<String>();
                accounts.add(account);
                ContactSelectActivity.Option advancedOption = TeamHelper.getCreateContactSelectOption(accounts, 50);
                NimUIKit.startContactSelect(P2PSessionDetailActivity.this, advancedOption, Constants.REQUEST_CODE_ADVANCED);
            }
        });

        ckb_newNotify.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                if (!NetworkUtil.isNetAvailable(P2PSessionDetailActivity.this)) {
                    Toast.makeText(P2PSessionDetailActivity.this, R.string.network_is_not_available, Toast.LENGTH_SHORT).show();
                    ckb_newNotify.setChecked(!isChecked);
                    return;
                }

                NIMClient.getService(FriendService.class).setMessageNotify(account, isChecked).setCallback(new RequestCallback<Void>() {
                    @Override
                    public void onSuccess(Void param) {
                        if (isChecked) {
                            Toast.makeText(P2PSessionDetailActivity.this, "开启消息提醒成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(P2PSessionDetailActivity.this, "关闭消息提醒成功", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailed(int code) {
                        if (code == 408) {
                            Toast.makeText(P2PSessionDetailActivity.this, R.string.network_is_not_available, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(P2PSessionDetailActivity.this, "on failed:" + code, Toast.LENGTH_SHORT).show();
                        }
                        ckb_newNotify.setChecked(!isChecked);
                    }

                    @Override
                    public void onException(Throwable exception) {

                    }
                });
            }
        });
        menu_clear_history.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.menu_clear_history:
                showClearHistoryDlg();
                break;
        }
    }

    private void showClearHistoryDlg(){
        CustomDialog dlg = new CustomDialog(this);
        dlg.setTitle("清除聊天记录").setMsg("确定要清除聊天记录吗？").setDlgButtonListener(new CustomDialog.onButtonClickListener() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onSure() {
                NIMClient.getService(MsgService.class).clearChattingHistory(account, SessionTypeEnum.P2P);
                MessageListPanelHelper.getInstance().notifyClearMessages(account);
            }
        }).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_OK)
            return;
        switch (requestCode){
            case Constants.REQUEST_CODE_ADVANCED:
                final ArrayList<String> selected = data.getStringArrayListExtra(ContactSelectActivity.RESULT_DATA);
                ContactsHelper.createAdvancedTeam(this, selected, null);
                break;

        }
    }
}
