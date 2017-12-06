package com.westepper.step.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.mikiller.mkglidelib.imageloader.GlideImageLoader;
import com.uilib.joooonho.SelectableRoundedImageView;
import com.westepper.step.R;
import com.westepper.step.activities.UserDetailActivity;
import com.westepper.step.base.Constants;
import com.westepper.step.responses.RankList;
import com.westepper.step.utils.ActivityManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mikiller on 2017/9/6.
 */

public class PaihangRcvAdapter extends RecyclerView.Adapter<PaihangRcvAdapter.Holder> {
    private List<RankList.Rank> datas;
    private int userRank;
    private Context mContext;

    public PaihangRcvAdapter() {
    }

    public PaihangRcvAdapter(int userRank, List<RankList.Rank> datas) {
        this.datas = datas;
        this.userRank = userRank;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_paihang, null);
        return new Holder(root);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        final RankList.Rank rank = datas.get(position + 3);
        boolean isUserPos = rank.getPos() == userRank;
        holder.tv_num.setText(String.valueOf(rank.getPos()));
        holder.tv_nickName.setText(rank.getNickName());
        holder.tv_disNum.setText(rank.getReachecNum());
        holder.tv_achNum.setText(rank.getAchievedNum());
        GlideImageLoader.getInstance().loadImage(mContext, rank.getHeadUrl(), R.mipmap.ic_default_head, holder.iv_header, 0);
        holder.tv_num.setTextColor(isUserPos ? mContext.getResources().getColor(R.color.colorPrimary) : mContext.getResources().getColor(R.color.discovery_kind_unchecked));
        holder.tv_nickName.setTextColor(isUserPos ? mContext.getResources().getColor(R.color.colorPrimary) : mContext.getResources().getColor(R.color.text_color_black));
        holder.tv_disNum.setTextColor(isUserPos ? mContext.getResources().getColor(R.color.colorPrimary) : mContext.getResources().getColor(R.color.text_color_black));
        holder.tv_achNum.setTextColor(isUserPos ? mContext.getResources().getColor(R.color.colorPrimary) : mContext.getResources().getColor(R.color.text_color_black));
        holder.iv_dis.setChecked(isUserPos);
        holder.iv_ach.setChecked(isUserPos);
        holder.iv_header.setBorderWidthDP(isUserPos ? 2 : 0);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> args = new HashMap<>();
                args.put(Constants.USERINFO, rank.getUserId());
                ActivityManager.startActivity((Activity) mContext, UserDetailActivity.class, args);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (datas == null || datas.size() <= 3) ? 0 : datas.size() - 3;
    }

    public void setDatas(int userRank, List<RankList.Rank> datas){
        this.datas = datas;
        this.userRank = userRank;
        notifyDataSetChanged();
    }

    protected class Holder extends RecyclerView.ViewHolder{
        private TextView tv_num, tv_nickName, tv_hint, tv_disNum, tv_achNum;
        private SelectableRoundedImageView iv_header;
        private RadioButton iv_dis, iv_ach;
        public Holder(View itemView) {
            super(itemView);
            tv_num = (TextView) itemView.findViewById(R.id.tv_num);
            tv_nickName = (TextView) itemView.findViewById(R.id.tv_nickName);
            tv_disNum = (TextView) itemView.findViewById(R.id.tv_disNum);
            tv_achNum = (TextView) itemView.findViewById(R.id.tv_achNum);
            iv_header = (SelectableRoundedImageView) itemView.findViewById(R.id.iv_header);
            iv_dis = (RadioButton) itemView.findViewById(R.id.iv_dis);
            iv_ach = (RadioButton) itemView.findViewById(R.id.iv_ach);
        }
    }
}
