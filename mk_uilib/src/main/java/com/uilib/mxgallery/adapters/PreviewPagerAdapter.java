/*
 * Copyright 2017 Zhihu Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.uilib.mxgallery.adapters;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.support.v4.net.ConnectivityManagerCompat;
import android.support.v4.view.PagerAdapter;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.mikiller.mkglidelib.imageloader.GlideImageLoader;
import com.uilib.mxgallery.models.ItemModel;
import com.uilib.R;
import com.uilib.mxgallery.models.MimeType;
import com.uilib.mxgallery.utils.GalleryUtils;
import com.uilib.utils.DisplayUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PreviewPagerAdapter extends PagerAdapter {
    private Context mContext;
    private ArrayList<ItemModel> mItems = new ArrayList<>();
    private ImageView iv_preview;
    private FrameLayout fl_vieoPreview;
    private VideoView viewVideo;
    private ImageButton btn_start;
    private TextView tv_time;
    private int mimeType;
    private Timer timer;

    public PreviewPagerAdapter(Context context, List<ItemModel> items, int mimeType) {
        mContext = context;
        this.mimeType = mimeType;
        if (items != null)
            mItems.addAll(items);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View page = LayoutInflater.from(mContext).inflate(R.layout.layout_preview_page, null, false);
//        page.setTag(mItems.get(position).getContentUri());
        page.setTag(mItems.get(position).getPath());
        iv_preview = (ImageView) page.findViewById(R.id.iv_preview);
        fl_vieoPreview = (FrameLayout) page.findViewById(R.id.fl_vieoPreview);
        viewVideo = (VideoView) page.findViewById(R.id.viewVideo);
        btn_start = (ImageButton) page.findViewById(R.id.btn_start);
        tv_time = (TextView) page.findViewById(R.id.tv_time);

        boolean isPic = MimeType.isPic(mimeType);
        iv_preview.setVisibility(isPic ? View.VISIBLE : View.GONE);
        fl_vieoPreview.setVisibility(isPic ? View.GONE : View.VISIBLE);
        if(!isPic && fl_vieoPreview != null) {
            viewVideo.setVideoPath(mItems.get(position).getPath());
            viewVideo.seekTo(1);
            viewVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    timer.cancel();
                    viewVideo.seekTo(1);
                    btn_start.setVisibility(View.VISIBLE);
                }
            });
            tv_time.setText(DateUtils.formatElapsedTime(mItems.get(position).duration / 1000));

            btn_start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!viewVideo.isPlaying()){
                        viewVideo.seekTo(0);
                        viewVideo.start();
                        v.setVisibility(View.GONE);
                        timer = new Timer();
                        timer.schedule(new TimerTask() {
                            long time = 0;
                            @Override
                            public void run() {
                                updateTime(time++);
                            }
                        }, 0, 1000);
                    }

                }
            });
        }else if(iv_preview != null) {
            GlideImageLoader.getInstance().loadLocalImage(mContext, mItems.get(position).getContentUri(), R.mipmap.placeholder, iv_preview);
        }
        container.addView(page);
        return page;
    }

    private void updateTime(final long time){
        if(tv_time == null)
            return;
        tv_time.post(new Runnable() {
            @Override
            public void run() {
                tv_time.setText(DateUtils.formatElapsedTime(time));
            }
        });
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public void release(){
        if(MimeType.isVideo(mimeType)){
            viewVideo.stopPlayback();
            viewVideo.destroyDrawingCache();
            if(timer != null) {
                timer.cancel();
                timer = null;
            }
        }
        mItems.clear();
        mItems = null;
        mContext = null;
        System.gc();
    }

//    public ItemModel getMediaItem(int position) {
//        return mItems.get(position);
//    }
//
//    public void addAll(List<ItemModel> items) {
//        mItems.addAll(items);
//    }

    interface OnPrimaryItemSetListener {

        void onPrimaryItemSet(int position);
    }

}
