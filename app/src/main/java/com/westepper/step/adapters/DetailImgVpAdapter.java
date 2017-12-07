package com.westepper.step.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mikiller.mkglidelib.imageloader.GlideImageLoader;
import com.westepper.step.R;
import com.westepper.step.responses.ImgDetail;

import java.util.List;

/**
 * Created by Mikiller on 2017/9/24.
 */

public class DetailImgVpAdapter extends PagerAdapter {
    Context mContext;
    List<ImgDetail> imgs;

    public DetailImgVpAdapter(Context context, List<ImgDetail> imgList) {
        mContext = context;
        imgs = imgList;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        ImageView iv_img = new ImageView(mContext);
        iv_img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        GlideImageLoader.getInstance().loadImage(mContext, imgs.get(position).getImg_url(), R.mipmap.placeholder, iv_img, 0);
        container.addView(iv_img);
//        iv_img.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (listener != null)
//                    listener.onImgClick(imgs.get(position).getImg_url());
//            }
//        });
        return iv_img;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public List<ImgDetail> getImgs(){
        return imgs;
    }

    public String getCurrentImg(int pos){
        return imgs.get(pos).getImg_url();
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
