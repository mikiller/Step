package com.westepper.step.utils;

import android.content.Context;
import android.content.Intent;

import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.cache.FriendDataCache;
import com.netease.nim.uikit.session.SessionEventListener;
import com.netease.nim.uikit.session.module.MsgForwardFilter;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.avchat.model.AVChatAttachment;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.attachment.FileAttachment;
import com.netease.nimlib.sdk.msg.constant.AttachStatusEnum;
import com.netease.nimlib.sdk.msg.constant.MsgDirectionEnum;
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.CustomMessageConfig;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.robot.model.RobotAttachment;
import com.westepper.step.activities.UserDetailActivity;
import com.westepper.step.base.Constants;
import com.westepper.step.widgets.CustomP2PSessionCustomization;
import com.westepper.step.widgets.CustomTeamSessionCustomization;
import com.westepper.step.widgets.sessions.MsgViewHolderFile;
import com.westepper.step.widgets.sessions.MsgViewHolderTip;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mikiller on 2017/11/22.
 */

public class SessionHelper {

    public static void init(){
        initCustomSessionCustomization();
        registerViewHolders();
        setSessionListener();
        registerMsgForwardFilter();
        registFriendObserver();
    }

    private static void initCustomSessionCustomization(){
        NimUIKit.setCommonTeamSessionCustomization(new CustomTeamSessionCustomization());
        NimUIKit.setCommonP2PSessionCustomization(new CustomP2PSessionCustomization());
    }

    private static void registerViewHolders() {
        NimUIKit.registerMsgItemViewHolder(FileAttachment.class, MsgViewHolderFile.class);
        NimUIKit.registerTipMsgViewHolder(MsgViewHolderTip.class);
    }

    private static void setSessionListener() {
        SessionEventListener listener = new SessionEventListener() {
            @Override
            public void onAvatarClicked(Context context, IMMessage message) {
                // 一般用于打开用户资料页面
                Intent intent = new Intent(context, UserDetailActivity.class);
                intent.putExtra(Constants.USERINFO, message.getFromAccount());
                context.startActivity(intent);
            }

            @Override
            public void onAvatarLongClicked(Context context, IMMessage message) {
                // 一般用于群组@功能，或者弹出菜单，做拉黑，加好友等功能
            }
        };

        NimUIKit.setSessionListener(listener);
    }

    /**
     * 消息转发过滤器
     */
    private static void registerMsgForwardFilter() {
        NimUIKit.setMsgForwardFilter(new MsgForwardFilter() {
            @Override
            public boolean shouldIgnore(IMMessage message) {
                if (message.getDirect() == MsgDirectionEnum.In
                        && (message.getAttachStatus() == AttachStatusEnum.transferring
                        || message.getAttachStatus() == AttachStatusEnum.fail)) {
                    // 接收到的消息，附件没有下载成功，不允许转发
                    return true;
                }
                return false;
            }
        });
    }

    private static FriendDataCache.FriendDataChangedObserver observer = new FriendDataCache.FriendDataChangedObserver() {
        @Override
        public void onAddedOrUpdatedFriends(List<String> accounts) {
            for (String account : accounts)
                sendTipMessage("你们已经成为好友，开始聊天吧", account, SessionTypeEnum.P2P);
        }

        @Override
        public void onDeletedFriends(List<String> accounts) {
            for(String account : accounts) {
                MsgService msgService = NIMClient.getService(MsgService.class);
                msgService.deleteRecentContact2(account, SessionTypeEnum.P2P);
                msgService.clearChattingHistory(account, SessionTypeEnum.P2P);
            }
        }

        @Override
        public void onAddUserToBlackList(List<String> account) {

        }

        @Override
        public void onRemoveUserFromBlackList(List<String> account) {

        }
    };
    public static void registFriendObserver(){
        FriendDataCache.getInstance().registerFriendDataChangedObserver(observer, true);
    }

    public static void sendTipMessage(String tip, String id, SessionTypeEnum sessionType){
        Map<String, Object> content = new HashMap<>(1);
        content.put("content", tip);
        IMMessage msg = MessageBuilder.createTipMessage(id, sessionType);
        msg.setRemoteExtension(content);
        CustomMessageConfig config = new CustomMessageConfig();
        config.enableUnreadCount = false;
        msg.setConfig(config);
        msg.setStatus(MsgStatusEnum.success);
        NIMClient.getService(MsgService.class).saveMessageToLocal(msg, true);
    }

}
