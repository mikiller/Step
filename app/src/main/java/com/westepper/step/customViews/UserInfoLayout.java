package com.westepper.step.customViews;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mikiller.mkglidelib.imageloader.GlideImageLoader;
import com.netease.nim.uikit.NimUIKit;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.friend.FriendService;
import com.netease.nimlib.sdk.friend.constant.VerifyType;
import com.netease.nimlib.sdk.friend.model.AddFriendData;
import com.uilib.customdialog.CustomDialog;
import com.uilib.joooonho.SelectableRoundedImageView;
import com.uilib.utils.DisplayUtil;
import com.westepper.step.R;
import com.westepper.step.activities.GalleryActivity;
import com.westepper.step.activities.SettingActivity;
import com.westepper.step.activities.UserInfoActivity;
import com.westepper.step.base.Constants;
import com.westepper.step.base.SuperActivity;
import com.westepper.step.responses.UserInfo;
import com.westepper.step.utils.ActivityManager;
import com.westepper.step.utils.ContactsHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mikiller on 2017/11/23.
 */

public class UserInfoLayout extends RelativeLayout implements View.OnClickListener {
    SelectableRoundedImageView iv_header;
    ImageView iv_header_bg, iv_gender, iv_user_edit;
    RelativeLayout rl_opt;
    TextView tv_city, tv_user_name, tv_userId, tv_signature, btn_goSession;
    ImageButton btn_setting, btn_info;

    boolean canEdit = true;

    public UserInfoLayout(Context context) {
        this(context, null, 0);
    }

    public UserInfoLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UserInfoLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr){
        LayoutInflater.from(context).inflate(R.layout.layout_userinfo, this);
        iv_header = (SelectableRoundedImageView) findViewById(R.id.iv_header);
        iv_header_bg = (ImageView) findViewById(R.id.iv_header_bg);
        iv_user_edit = (ImageView) findViewById(R.id.iv_user_edit);
        iv_gender = (ImageView) findViewById(R.id.iv_gender);
        rl_opt = (RelativeLayout) findViewById(R.id.rl_opt);
        tv_city = (TextView) findViewById(R.id.tv_city);
        tv_user_name = (TextView) findViewById(R.id.tv_user_name);
        tv_userId = (TextView) findViewById(R.id.tv_userId);
        tv_signature = (TextView) findViewById(R.id.tv_signature);
        btn_goSession = (TextView) findViewById(R.id.btn_goSession);
        btn_setting = (ImageButton) findViewById(R.id.btn_setting);
        btn_info = (ImageButton) findViewById(R.id.btn_info);

        if(attrs != null){
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.UserInfoLayout);
            rl_opt.setVisibility(ta.getBoolean(R.styleable.UserInfoLayout_needOpt, true) ? VISIBLE : GONE);
            btn_goSession.setVisibility(ta.getBoolean(R.styleable.UserInfoLayout_needGoSession, false) ? VISIBLE : GONE);
            canEdit = ta.getBoolean(R.styleable.UserInfoLayout_canEdit, true);
            ta.recycle();
        }

        RelativeLayout.LayoutParams lp;
        lp = (RelativeLayout.LayoutParams) iv_header_bg.getLayoutParams();
        lp.height = DisplayUtil.getScreenWidth(getContext()) *9 / 16;
        iv_header_bg.setLayoutParams(lp);
        iv_header_bg.setImageResource(canEdit ? R.mipmap.ic_addcover : R.color.discovery_kind_unchecked);
        iv_user_edit.setVisibility(canEdit ? VISIBLE : GONE);

        if(canEdit){
            iv_header_bg.setOnClickListener(this);
            iv_header.setOnClickListener(this);
            btn_setting.setOnClickListener(this);
            btn_info.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        Map<String, Object> args;
        switch (v.getId()) {
            case R.id.iv_header_bg:
                args = new HashMap<>();
                args.put(Constants.GALLERY_TYPE, Constants.CHANGE_USER_BG);
                ActivityManager.startActivityforResult((Activity) getContext(), GalleryActivity.class, Constants.CHANGE_USER_BG, args);
                break;
            case R.id.iv_header:
//                args = new HashMap<>();
//                args.put(Constants.USERINFO, userInfo);
                ActivityManager.startActivityforResult((Activity) getContext(), UserInfoActivity.class, Constants.CHANGE_HEADER, null);
                break;
            case R.id.btn_setting:
                ActivityManager.startActivity((Activity) getContext(), SettingActivity.class);
                break;
            case R.id.btn_info:
                break;
        }
    }

    public void setUserInfo(UserInfo userInfo){
        if(userInfo == null)
            return;
        tv_city.setText(userInfo.getCity());
        tv_user_name.setText(userInfo.getNickName());
        tv_userId.setText("ID: ".concat(userInfo.getUserId()));
        tv_signature.setText(userInfo.getSign());
        iv_gender.setImageResource(userInfo.getGender() == 1 ? R.mipmap.male : R.mipmap.female);
        GlideImageLoader.getInstance().loadImage(getContext(), userInfo.getHeadImg(), R.mipmap.ic_default_head, iv_header, 100);
        if(TextUtils.isEmpty(userInfo.getCover())){
            iv_header_bg.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            iv_header_bg.setImageResource(canEdit ? R.mipmap.ic_addcover : R.color.discovery_kind_unchecked);
        }else if(!userInfo.getCover().contains("data:image/jpeg;base64,")){
            setCover(userInfo.getCover());
        }
    }

    public void setCover(String path){
        iv_header_bg.setScaleType(ImageView.ScaleType.CENTER_CROP);
        GlideImageLoader.getInstance().loadImage(getContext(), path, R.mipmap.ic_addcover, iv_header_bg, 0);
    }

    public boolean setBtnState(final String account, final int needVerif){
        final boolean isFriend = NIMClient.getService(FriendService.class).isMyFriend(account);
        btn_goSession.setText(isFriend ? "发消息" : "加为好友");
        btn_goSession.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFriend) {
                    NimUIKit.startP2PSession(getContext(), account);
                    ((SuperActivity) getContext()).back();
                }else{
                    ContactsHelper.addFriend(btn_goSession, account, needVerif == 1);
                }
            }
        });
        return isFriend;
    }

    public int getBgWidth(){
        return iv_header_bg.getMeasuredWidth();
    }

    public int getBgHeight(){
        return iv_header_bg.getMeasuredHeight();
    }
}
