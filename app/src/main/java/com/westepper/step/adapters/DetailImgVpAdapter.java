package com.westepper.step.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mikiller.mkglidelib.imageloader.GlideImageLoader;
import com.westepper.step.R;
import com.westepper.step.responses.DisDetailImg;

import java.util.List;

/**
 * Created by Mikiller on 2017/9/24.
 */

public class DetailImgVpAdapter extends PagerAdapter {
    Context mContext;
    List<DisDetailImg> imgs;


    public DetailImgVpAdapter(Context context, List<DisDetailImg> imgList) {
        mContext = context;
        imgs = imgList;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        ImageView iv_img = new ImageView(mContext);
        iv_img.setScaleType(ImageView.ScaleType.FIT_XY);
        GlideImageLoader.getInstance().loadImage(mContext, imgs.get(position).getUrl(), R.mipmap.default_icon, iv_img, 0);
        container.addView(iv_img);
        return iv_img;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public List<DisDetailImg> getImgs(){
        return imgs;
    }

    @Override
    public int getCount() {
        return imgs == null ? 0 : imgs.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
