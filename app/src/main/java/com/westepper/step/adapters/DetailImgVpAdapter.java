package com.westepper.step.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mikiller.mkglidelib.imageloader.GlideImageLoader;
import com.westepper.step.R;

/**
 * Created by Mikiller on 2017/9/24.
 */

public class DetailImgVpAdapter extends PagerAdapter {
    Context mContext;
    String[] imgPath;
    ImageView iv_img;

    public DetailImgVpAdapter(Context context, String[]imgPath) {
        mContext = context;
        this.imgPath = imgPath;
        iv_img = new ImageView(mContext);
        iv_img.setScaleType(ImageView.ScaleType.FIT_XY);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        GlideImageLoader.getInstance().loadImage(mContext, imgPath[position], R.mipmap.default_icon, iv_img, 0);
        container.addView(iv_img);
        return iv_img;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(iv_img);
    }

    @Override
    public int getCount() {
        return imgPath == null ? 0 : imgPath.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
