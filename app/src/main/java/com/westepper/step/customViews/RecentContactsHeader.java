package com.westepper.step.customViews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.netease.nim.uikit.common.ui.drop.DropFake;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.westepper.step.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Mikiller on 2017/11/20.
 */

public class RecentContactsHeader extends LinearLayout {
    private LinearLayout item_myFriend, item_systemMsg;
    private HeadImageView headerImg;
    private TextView menuTxt;
    private DropFake tip;

    private HeaderClickedListener headerListener;

    public RecentContactsHeader(Context context) {
        this(context, null, 0);
    }

    public RecentContactsHeader(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecentContactsHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr){
        LayoutInflater.from(context).inflate(R.layout.layout_recent_contacts_header, this);
        item_myFriend = (LinearLayout) findViewById(R.id.item_myFriend);
        item_systemMsg = (LinearLayout) findViewById(R.id.item_systemMsg);
        headerImg = (HeadImageView) findViewById(R.id.img_head);
        menuTxt = (TextView) findViewById(R.id.tv_nickname);
        tip = (DropFake) findViewById(R.id.unread_number_tip);

        headerImg.setImageResource(R.mipmap.ic_verif);
        menuTxt.setText("系统通知");

        item_myFriend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(headerListener != null)
                    headerListener.onMyFriendClick();
            }
        });
        item_systemMsg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(headerListener != null)
                    headerListener.onSystemMsgClick();
            }
        });
    }

    public void toggleTip(int unreadCount){
        tip.setVisibility(unreadCount > 0 ? VISIBLE : GONE);
        tip.setText("" + unreadCount);
        ((TextView)findViewById(R.id.tv_message)).setText(unreadCount > 0 ? "您有新的系统消息" : "");
        ((TextView)findViewById(R.id.tv_date_time)).setText(unreadCount > 0 ? new SimpleDateFormat("a hh:mm").format(new Date()) : "");
    }

    public void setHeaderListener(HeaderClickedListener headerListener) {
        this.headerListener = headerListener;
    }

    public interface HeaderClickedListener{
        void onMyFriendClick();
        void onSystemMsgClick();
    }
}
