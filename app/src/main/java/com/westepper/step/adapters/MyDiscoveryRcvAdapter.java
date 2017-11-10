package com.westepper.step.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mikiller.mkglidelib.imageloader.GlideImageLoader;
import com.uilib.joooonho.SelectableRoundedImageView;
import com.uilib.mxgallery.models.ItemModel;
import com.uilib.utils.DisplayUtil;
import com.westepper.step.R;
import com.westepper.step.activities.DiscoveryDetailActivity;
import com.westepper.step.base.Constants;
import com.westepper.step.responses.Discovery;
import com.westepper.step.responses.DiscoveryList;
import com.westepper.step.responses.ImgDetail;
import com.westepper.step.responses.UserInfo;
import com.westepper.step.utils.ActivityManager;
import com.westepper.step.utils.MXPreferenceUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Mikiller on 2017/10/12.
 */

public class MyDiscoveryRcvAdapter extends RecyclerView.Adapter<MyDiscoveryRcvAdapter.DisHolder> {

    Context mContext;
    List<Discovery> discoveryList;
    int disKind;
    int[] ids = new int[]{R.id.iv_img1, R.id.iv_img2, R.id.iv_img3};

    public void setDiscoveryList(List<Discovery> discoveryList) {
        this.discoveryList = discoveryList;
        notifyDataSetChanged();
    }

    public void addDiscoveryList(List<Discovery> discoveryList){
        if(this.discoveryList == null)
            this.discoveryList = new ArrayList<>();
        int startPos = this.discoveryList.size();
        this.discoveryList.addAll(discoveryList);
        notifyItemRangeChanged(startPos, discoveryList.size());
    }

    public MyDiscoveryRcvAdapter(Context mContext, int disKind) {
        this.mContext = mContext;
        this.disKind = disKind;
    }

    @Override
    public DisHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_my_discovery, null);
        return new DisHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DisHolder holder, int position) {
        final Discovery discovery = discoveryList.get(position);
        if(isToday(discovery.getPushTime())){
            holder.tv_date1.setText("今天");
            holder.tv_date2.setText("");
        }else {
            holder.tv_date1.setText(getDate("dd", discovery.getPushTime())+"/");
            holder.tv_date2.setText(getDate("M", discovery.getPushTime()));
        }
        holder.tv_msg.setText(discovery.getInfo());
        holder.tv_pos.setText(discovery.getUserPos().getPoiTitle());
        holder.tv_good.setText(String.valueOf(discovery.getGoodNum()));
        holder.tv_commit.setText(String.valueOf(discovery.getCommitNum()));
        int i = 0;
        for(ImgDetail img : discovery.getImgList()){
            if(i >= 3)
                break;
            GlideImageLoader.getInstance().loadImage(mContext, img.getImg_url(), R.mipmap.placeholder, holder.iv_imgs[i++], 0);
        }
        if(disKind == Constants.OUTGO) {
            holder.ll_chat.setVisibility(View.VISIBLE );
            if(discovery.getTotalCount() == 0)
                holder.tv_joinNum.setText(String.format("邀约不限，已报名%1$s", discovery.getJoinCount()));
            else
                holder.tv_joinNum.setText(String.format("邀约%1$s人, 已报名%2$s人", discovery.getTotalCount(), discovery.getJoinCount()));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> args = new HashMap<>();
                String json = MXPreferenceUtils.getInstance().getString(Constants.USERINFO);
                discovery.setNickName(new Gson().fromJson(json, UserInfo.class).getNickName());
                discovery.setDiscoveryUserId(MXPreferenceUtils.getInstance().getString("account"));
                args.put(Constants.DISCOVERY_DETAIL, discovery);
                args.put(Constants.DIS_SCOPE, Constants.FRIEND);
                ActivityManager.startActivity((Activity) mContext, DiscoveryDetailActivity.class, args);
            }
        });

        holder.tv_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private String getDate(String format, long time){
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CHINA);
        return sdf.format(new Date(time));
    }

    private boolean isToday(long time){
        String d1 = getDate("dd", System.currentTimeMillis()),
        d2 = getDate("dd", time);
        return d1.equals(d2);
    }

    @Override
    public int getItemCount() {
        return discoveryList == null ? 0 : discoveryList.size();
    }

    protected class DisHolder extends RecyclerView.ViewHolder{

        private TextView tv_date1, tv_date2, tv_msg, tv_pos, tv_good, tv_commit, tv_joinNum, tv_chat;
        private SelectableRoundedImageView[] iv_imgs = new SelectableRoundedImageView[3];
        private LinearLayout ll_chat;

        public DisHolder(View itemView) {
            super(itemView);
            tv_date1 = (TextView) itemView.findViewById(R.id.tv_date1);
            tv_date2 = (TextView) itemView.findViewById(R.id.tv_date2);
            tv_msg = (TextView) itemView.findViewById(R.id.tv_msg);
            tv_pos = (TextView) itemView.findViewById(R.id.tv_pos);
            tv_good = (TextView) itemView.findViewById(R.id.tv_good);
            tv_commit = (TextView) itemView.findViewById(R.id.tv_commit);
            tv_joinNum = (TextView) itemView.findViewById(R.id.tv_joinNum);
            tv_chat = (TextView) itemView.findViewById(R.id.tv_chat);
            for(int i = 0; i < 3; i++) {
                iv_imgs[i] = (SelectableRoundedImageView) itemView.findViewById(ids[i]);
            }
            ll_chat = (LinearLayout) itemView.findViewById(R.id.ll_chat);
        }
    }
}
