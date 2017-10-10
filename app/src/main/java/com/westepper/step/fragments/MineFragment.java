package com.westepper.step.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mikiller.mkglidelib.imageloader.GlideImageLoader;
import com.mikiller.mkglidelib.imageloader.ImageLoader;
import com.uilib.utils.BitmapUtils;
import com.westepper.step.R;
import com.westepper.step.activities.GalleryActivity;
import com.westepper.step.activities.PaihangActivity;
import com.westepper.step.base.BaseFragment;
import com.westepper.step.base.Constants;
import com.westepper.step.customViews.MyMenuItem;
import com.uilib.joooonho.SelectableRoundedImageView;
import com.uilib.utils.DisplayUtil;
import com.westepper.step.utils.ActivityManager;
import com.uilib.mxgallery.utils.CameraGalleryUtils;


import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;


/**
 * A simple {@link Fragment} subclass.
 */
public class MineFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.iv_header)
    SelectableRoundedImageView iv_header;
    @BindView(R.id.iv_header_bg)
    ImageView iv_header_bg;
    @BindView(R.id.tv_city)
    TextView tv_city;
    @BindView(R.id.btn_setting)
    ImageButton btn_setting;
    @BindView(R.id.tv_user_name)
    TextView tv_user_name;
    @BindView(R.id.tv_userId)
    TextView tv_userId;
    @BindView(R.id.tv_signature)
    TextView tv_signature;
    @BindView(R.id.iv_gender)
    ImageView iv_gender;
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
        lp.height = DisplayUtil.getScreenWidth(getActivity()) *9 / 16;
        iv_header_bg.setLayoutParams(lp);

//        lp = (RelativeLayout.LayoutParams) iv_header.getLayoutParams();
//        lp.width = lp.height = DisplayUtil.getScreenWidth(getActivity()) / 5;
//        iv_header.setLayoutParams(lp);
        iv_header_bg.setOnClickListener(this);
        iv_header.setOnClickListener(this);
        btn_setting.setOnClickListener(this);

        menu_paihang.setOnClickListener(this);
        menu_paihang.setSubText("NO.");
        menu_mood.setOnClickListener(this);
        menu_go.setOnClickListener(this);
        menu_discovery.setOnClickListener(this);
        menu_acheive.setOnClickListener(this);

    }

    @Override
    public void fragmentCallback(int type, Intent data) {
        if(type == Constants.CHANGE_HEADER){
            String filePath = "";
            if(data.getSerializableExtra(CameraGalleryUtils.TMP_FILE) != null)
                filePath = (String) data.getSerializableExtra(CameraGalleryUtils.TMP_FILE);
            else if( data.getSerializableExtra(CameraGalleryUtils.THUMB_FILE) != null) {
                List<File> files = (List<File>) data.getSerializableExtra(CameraGalleryUtils.THUMB_FILE);
                filePath = files.get(0).getPath();
            }

            GlideImageLoader.getInstance().loadImage(getActivity(), filePath, iv_header, 0, new ImageLoader.ImageLoadListener() {
                @Override
                public void onLoadSuccess(Bitmap drawable, ImageView imageView) {
                    imageView.setImageBitmap(drawable);
                    BitmapUtils.blur(getActivity(), drawable, iv_header_bg);
                }

                @Override
                public void onLoadFailed(ImageView imageView) {

                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_default_head);
//        BitmapUtils.blur(getActivity(), bmp, iv_header_bg);
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
        switch (v.getId()) {
            case R.id.menu_paihang:
                ActivityManager.startActivity(getActivity(), PaihangActivity.class);
                break;
            case R.id.iv_header:
                Map<String, Object> args = new HashMap<>();
                args.put(Constants.GALLERY_TYPE, Constants.CHANGE_HEADER);
                ActivityManager.startActivityforResult(getActivity(), GalleryActivity.class, Constants.CHANGE_HEADER, args);
//                ((BaseActivity) getActivity()).startToActivity(SettingActivity.class, "fragmentName", UserInfoFragment.class.getName());
                break;
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
        }
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
//        //Toast.makeText(getContext(),event.getTel(),Toast.LENGTH_LONG).showPolygon();
//        tv_user_name.setText(event.getTel());
//    }

}
