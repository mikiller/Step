package com.westepper.step.utils;

import android.content.Context;

import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.session.SessionEventListener;
import com.netease.nimlib.sdk.avchat.model.AVChatAttachment;
import com.netease.nimlib.sdk.msg.attachment.FileAttachment;
import com.netease.nimlib.sdk.msg.model.IMMessage;
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
                //UserProfileActivity.start(context, message.getFromAccount());
            }

            @Override
            public void onAvatarLongClicked(Context context, IMMessage message) {
                // 一般用于群组@功能，或者弹出菜单，做拉黑，加好友等功能
            }
        };

        NimUIKit.setSessionListener(listener);
    }

}
