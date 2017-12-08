package com.westepper.step.utils;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.team.helper.TeamHelper;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.friend.FriendService;
import com.netease.nimlib.sdk.friend.FriendServiceObserve;
import com.netease.nimlib.sdk.friend.constant.VerifyType;
import com.netease.nimlib.sdk.friend.model.AddFriendData;
import com.netease.nimlib.sdk.friend.model.Friend;
import com.netease.nimlib.sdk.friend.model.FriendChangedNotify;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.CustomMessageConfig;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.constant.TeamBeInviteModeEnum;
import com.netease.nimlib.sdk.team.constant.TeamFieldEnum;
import com.netease.nimlib.sdk.team.constant.TeamTypeEnum;
import com.netease.nimlib.sdk.team.model.CreateTeamResult;
import com.netease.nimlib.sdk.team.model.Team;
import com.uilib.customdialog.CustomDialog;
import com.westepper.step.R;
import com.westepper.step.base.SuperActivity;
import com.westepper.step.logics.NewDiscoveryLogic;
import com.westepper.step.models.NewDiscoveryModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mikiller on 2017/11/22.
 */

public class ContactsHelper {
    private static final String TAG = ContactsHelper.class.getSimpleName();
    private static final int DEFAULT_TEAM_CAPACITY = 200;

    /**
     * 创建高级群
     */
    public static void createAdvancedTeam(final Context context, String teamName, List<String> memberAccounts, final NewDiscoveryModel model) {
        // 创建群
        TeamTypeEnum type = TeamTypeEnum.Advanced;
        HashMap<TeamFieldEnum, Serializable> fields = new HashMap<>();
        fields.put(TeamFieldEnum.Name, teamName);
        fields.put(TeamFieldEnum.BeInviteMode, model == null ? TeamBeInviteModeEnum.NoAuth : TeamBeInviteModeEnum.NeedAuth);
        NIMClient.getService(TeamService.class).createTeam(fields, type, "",
                memberAccounts).setCallback(
                new RequestCallback<CreateTeamResult>() {
                    @Override
                    public void onSuccess(CreateTeamResult result) {
                        onCreateSuccess(context, result, model);
                    }

                    @Override
                    public void onFailed(int code) {
                        String tip;
                        if (code == 801) {
                            tip = context.getString(com.netease.nim.uikit.R.string.over_team_member_capacity,
                                    DEFAULT_TEAM_CAPACITY);
                        } else if (code == 806) {
                            tip = context.getString(com.netease.nim.uikit.R.string.over_team_capacity);
                        } else {
                            tip = context.getString(com.netease.nim.uikit.R.string.create_team_failed) + ", code=" +
                                    code;
                        }

                        Toast.makeText(context, tip, Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onException(Throwable exception) {
                    }
                }
        );
    }

    /**
     * 群创建成功回调
     */
    private static void onCreateSuccess(final Context context, CreateTeamResult result, NewDiscoveryModel model) {
        if (result == null) {
            Log.e(TAG, "onCreateSuccess exception: team is null");
            return;
        }
        final Team team = result.getTeam();
        if (team == null) {
            Log.e(TAG, "onCreateSuccess exception: team is null");
            return;
        }

        Log.i(TAG, "create and update team success");

        // 检查有没有邀请失败的成员
        ArrayList<String> failedAccounts = result.getFailedInviteAccounts();
        if (failedAccounts != null && !failedAccounts.isEmpty()) {
            TeamHelper.onMemberTeamNumOverrun(failedAccounts, context);
        } else {
            Toast.makeText(context, com.netease.nim.uikit.R.string.create_team_success, Toast.LENGTH_SHORT).show();
        }

        // 演示：向群里插入一条Tip消息，使得该群能立即出现在最近联系人列表（会话列表）中，满足部分开发者需求
//        Map<String, Object> content = new HashMap<>(1);
//        content.put("content", "成功创建高级群");
//        IMMessage msg = MessageBuilder.createTipMessage(team.getId(), SessionTypeEnum.Team);
//        msg.setRemoteExtension(content);
//        CustomMessageConfig config = new CustomMessageConfig();
//        config.enableUnreadCount = false;
//        msg.setConfig(config);
//        msg.setStatus(MsgStatusEnum.success);
//        NIMClient.getService(MsgService.class).saveMessageToLocal(msg, true);
        SessionHelper.sendTipMessage("成功创建高级群", team.getId(), SessionTypeEnum.Team);

        // 发送后，稍作延时后跳转
        if (model == null) {
            new Handler(context.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    NimUIKit.startTeamSession(context, team.getId()); // 进入创建的群
                }
            }, 50);
        }else{
            model.setTeamId(team.getId());
            NewDiscoveryLogic logic = new NewDiscoveryLogic(context, model);
            logic.sendRequest();
        }
    }

    public static void inviteMembers(final Context context, final String teamId, List<String> accounts){
        NIMClient.getService(TeamService.class).addMembers(teamId, accounts).setCallback(new RequestCallback<List<String>>() {
            @Override
            public void onSuccess(List<String> failedAccounts) {
                if (failedAccounts == null || failedAccounts.isEmpty()) {
                    Toast.makeText(context, "添加群成员成功", Toast.LENGTH_SHORT).show();
                    NimUIKit.startTeamSession(context, teamId);
                } else {
                    TeamHelper.onMemberTeamNumOverrun(failedAccounts, context);
                }
            }

            @Override
            public void onFailed(int code) {
                if (code == ResponseCode.RES_TEAM_INVITE_SUCCESS) {
                    Toast.makeText(context, R.string.team_invite_members_success, Toast.LENGTH_SHORT).show();
                    NimUIKit.startTeamSession(context, teamId);
                } else {
                    Toast.makeText(context, "invite members failed, code=" + code, Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "invite members failed, code=" + code);
                }
            }

            @Override
            public void onException(Throwable exception) {

            }
        });
    }

    public static void addFriend(View addBtn, String account, boolean needVerif){
        if(needVerif)
            createVerifDlg(addBtn, account);
        else
            addFriend(addBtn, account, VerifyType.DIRECT_ADD, "");
    }

    private static void createVerifDlg(final View addBtn, final String account){
        final CustomDialog dlg = new CustomDialog(addBtn.getContext());
        dlg.setDlgEditable(true).setTitle("添加好友").setDlgButtonListener(new CustomDialog.onButtonClickListener() {
            @Override
            public void onCancel() {
                ((SuperActivity)addBtn.getContext()).hideInputMethod(dlg.getCurrentFocus());
            }

            @Override
            public void onSure() {
                addFriend(addBtn, account, VerifyType.VERIFY_REQUEST, dlg.getMsg());
            }
        }).show();
    }

    private static void addFriend(final View addBtn, String account, final VerifyType verifyType, String msg){
        NIMClient.getService(FriendService.class).addFriend(new AddFriendData(account, verifyType, msg))
                .setCallback(new RequestCallback<Void>() {
                    @Override
                    public void onSuccess(Void param) {
                        if (VerifyType.DIRECT_ADD == verifyType) {
                            Toast.makeText(addBtn.getContext(), "添加好友成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(addBtn.getContext(), "添加好友请求发送成功", Toast.LENGTH_SHORT).show();
                        }
                        addBtn.setEnabled(false);
                    }

                    @Override
                    public void onFailed(int code) {
                        if (code == 408) {
                            Toast.makeText(addBtn.getContext(), R.string.network_is_not_available, Toast
                                    .LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(addBtn.getContext(), "on failed:" + code, Toast
                                    .LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onException(Throwable exception) {
                    }
                });
    }

}
