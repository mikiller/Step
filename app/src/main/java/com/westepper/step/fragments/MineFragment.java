package com.westepper.step.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.westepper.step.R;
import com.westepper.step.base.BaseFragment;
import com.westepper.step.customViews.MyMenuItem;
import com.uilib.joooonho.SelectableRoundedImageView;
import com.uilib.utils.DisplayUtil;


import butterknife.BindView;


/**
 * A simple {@link Fragment} subclass.
 */
public class MineFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.rl_header_icon)
    RelativeLayout rl_header_icon;
    @BindView(R.id.iv_header)
    SelectableRoundedImageView iv_header;
    @BindView(R.id.iv_header_bg)
    ImageView iv_header_bg;
    @BindView(R.id.iv_user_edit)
    ImageView iv_user_edit;
    @BindView(R.id.tv_user_name)
    TextView tv_user_name;
    @BindView(R.id.menu_paihang)
    MyMenuItem menu_paihang;
    @BindView(R.id.menu_mood)
    MyMenuItem menu_mood;
    @BindView(R.id.menu_go)
    MyMenuItem menu_go;
    @BindView(R.id.menu_discovery)
    MyMenuItem menu_discovery;
    @BindView(R.id.menu_acheive)
    MyMenuItem menu_acheive;
    @BindView(R.id.menu_setting)
    MyMenuItem menu_setting;

    public MineFragment() {
        // Required empty public constructor
    }

    public static MineFragment newInstance(int num) {
        MineFragment fragment = new MineFragment();
        Bundle args = new Bundle();
        args.putInt("num", num);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected void setLayoutRes() {
        layoutRes = R.layout.fragment_mine;
    }

    @Override
    protected void initView() {
        RelativeLayout.LayoutParams lp;
        lp = (RelativeLayout.LayoutParams) iv_header_bg.getLayoutParams();
        lp.height = DisplayUtil.getScreenHeight(getActivity()) / 3;
        iv_header_bg.setLayoutParams(lp);

        tv_user_name.setOnClickListener(this);
        lp = (RelativeLayout.LayoutParams) iv_header.getLayoutParams();
        lp.width = lp.height = DisplayUtil.getScreenWidth(getActivity()) / 5;
        iv_header.setLayoutParams(lp);
        iv_header.setOnClickListener(this);

        tv_user_name.setOnClickListener(this);

        menu_paihang.setOnClickListener(this);
        menu_paihang.setSubText("NO.");
        menu_mood.setOnClickListener(this);
        menu_go.setOnClickListener(this);
        menu_discovery.setOnClickListener(this);
        menu_acheive.setOnClickListener(this);
        menu_setting.setOnClickListener(this);

    }

    @Override
    public void fragmentCallback(int type, Intent data) {

    }

    @Override
    public void onResume() {
        super.onResume();
//        user = ((BaseActivity) getActivity()).fetchUser();
//        if (!((BaseActivity) getActivity()).isLogin() || TextUtils.isEmpty(user.getAccesstoken())) {
//            iv_user_edit.setVisibility(View.GONE);
//            tv_user_name.setText("点击登录");
//            tv_user_name.setTextColor(Color.BLACK);
//            tv_user_name.setEnabled(true);
//            tv_user_name.setClickable(true);
//            iv_header.setImageResource(R.mipmap.icon_user_header);
//            iv_header.setEnabled(false);
//            iv_header_bg.setImageResource(R.mipmap.my);
//        } else {
//            iv_user_edit.setVisibility(View.VISIBLE);
//            tv_user_name.setText(TextUtils.isEmpty(user.getShowname()) ? user.getTel() : user.getShowname());
//            tv_user_name.setTextColor(Color.WHITE);
//            tv_user_name.setEnabled(false);
//            tv_user_name.setClickable(false);
//            iv_header.setEnabled(true);
//            tv_user_name.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_user_name));
//            GlideImageLoader.getInstance().loadImage(mContext, user.getAvator(), R.mipmap.icon_user_header, iv_header, 0l);
//            GlideImageLoader.getInstance().loadImage(mContext, user.getAvator(), R.mipmap.my, iv_header_bg, 0l, new ImageLoader.ImageLoadListener() {
//
//                @Override
//                public void onLoadFailed(ImageView imageView) {
//                    if(!TextUtils.isEmpty(user.localPath) && new File(user.localPath).exists()){
//                        GlideImageLoader.getInstance().loadImage(mContext, user.localPath, R.mipmap.icon_user_header, iv_header, 0l);
//                        GlideImageLoader.getInstance().loadImage(mContext, user.localPath, R.mipmap.my, imageView, 0l, this);
//                    }
//                }
//
//                @Override
//                public void onLoadSuccess(Bitmap bmp, ImageView imageView) {
//                    BitmapUtils.blur(mContext, bmp, imageView);
//                }
//            });
//        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.iv_header:
//                ((BaseActivity) getActivity()).startToActivity(SettingActivity.class, "fragmentName", UserInfoFragment.class.getName());
//                break;
//            case R.id.tv_user_name:
//                ((BaseActivity) getActivity()).startToActivity(UserRegisterActivity.class);
//                break;
//            case R.id.menu_mood:
//                if (((BaseActivity) getActivity()).isUserLogin(true, BaseListActivity.class.getName()))
//                    ((BaseActivity) getActivity()).startToActivity(BaseListActivity.class, BaseListFragment.class.getSimpleName(), OrderListFragment.class.getName());
//                break;
//            case R.id.menu_advice:
//                ((BaseActivity) getActivity()).startToActivity(AdviceActivity.class);
//                break;
//            case R.id.menu_contact_us:
//                ((MainActivity)getActivity()).setEmailMaskVisible(true);
//                break;
//            case R.id.menu_setting:
//                ((BaseActivity) getActivity()).startToActivity(SettingActivity.class, "fragmentName", SettingFragment.class.getName());
//                break;
//        }
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onChangeAvatorEvent(User event) {
//        //iv_header.setImageURI(Uri.parse("file://" + event.getAvator()));
//        //iv_header.setImageBitmap(decodeSampledBitmapFromFd(event.getAvator(), DensityUtils.dp2Px(60), DensityUtils.dp2Px(60)));
//        iv_header.setImageBitmap(doRotate(event.getAvator(), decodeSampledBitmapFromFd(event.getAvator(), DensityUtils.dp2Px(60), DensityUtils.dp2Px(60))));
//    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onGetCodeEvent(User event) {
//        /* Do something */
//        //Toast.makeText(getContext(),event.getTel(),Toast.LENGTH_LONG).show();
//        tv_user_name.setText(event.getTel());
//    }

}
