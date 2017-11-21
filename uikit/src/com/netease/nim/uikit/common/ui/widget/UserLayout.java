package com.netease.nim.uikit.common.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.netease.nim.uikit.R;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;

/**
 * Created by Mikiller on 2017/11/21.
 */

public class UserLayout extends RelativeLayout {
    HeadImageView imgHeader;
    TextView tv_name;
    ImageView owner, admin, delTag;
    public UserLayout(Context context) {
        super(context);
        initView(context, null);
    }

    public UserLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public UserLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs){
        LayoutInflater.from(context).inflate(R.layout.nim_team_member_item, this);
        imgHeader = (HeadImageView) findViewById(R.id.imageViewHeader);
        tv_name = (TextView) findViewById(R.id.textViewName);
        owner = (ImageView) findViewById(R.id.imageViewOwner);
        admin = (ImageView) findViewById(R.id.imageViewAdmin);
        delTag = (ImageView) findViewById(R.id.imageViewDeleteTag);

        if(attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.UserLayout);
            setHeaderByRes(ta.getResourceId(R.styleable.UserLayout_headerImg, NO_ID));
            setTvName(ta.getString(R.styleable.UserLayout_tvName));
            ta.recycle();
        }

    }

    public void setHeaderByAccount(String account){
        imgHeader.loadBuddyAvatar(account);
    }

    public void setHeaderByRes(int resId){
        if(resId != NO_ID) {
            imgHeader.setImageResource(resId);
        }
    }

    public void setTvName(String name){
        if(!TextUtils.isEmpty(name))
            tv_name.setText(name);
    }

    public void setOnHeaderClickListener(OnClickListener listener){
        imgHeader.setOnClickListener(listener);
    }
}
