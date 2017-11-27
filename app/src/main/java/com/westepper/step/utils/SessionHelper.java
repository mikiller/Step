package com.westepper.step.utils;

import android.content.Context;
import android.content.Intent;

import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.session.SessionEventListener;
import com.netease.nim.uikit.session.module.MsgForwardFilter;
import com.netease.nimlib.sdk.avchat.model.AVChatAttachment;
import com.netease.nimlib.sdk.msg.attachment.FileAttachment;
import com.netease.nimlib.sdk.msg.constant.AttachStatusEnum;
import com.netease.nimlib.sdk.msg.constant.MsgDirectionEnum;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.robot.model.RobotAttachment;
import com.westepper.step.activities.UserDetailActivity;
import com.westepper.step.base.Constants;
import com.westepper.step.widgets.CustomP2PSessionCustomization;
import com.westepper.step.widgets.CustomTeamSessionCustomization;
import com.westepper.step.widgets.sessions.MsgViewHolderFile;
import com.westepper.step.widgets.sessions.MsgViewHolderTip;

/**
 * Created by Mikiller on 2017/11/22.
 */

public class SessionHelper {

    public static void init(){
        initCustomSessionCustomization();
        registerViewHolders();
        setSessionListener();
        registerMsgForwardFilter();
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

}
