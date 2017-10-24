package com.westepper.step.adapters;

import android.content.Context;
import android.support.annotation.Dimension;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.uilib.utils.DisplayUtil;
import com.westepper.step.R;

import java.util.List;

/**
 * Created by Mikiller on 2017/10/24.
 */

public class ReachedAchRcvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int REACHTITLE = 0, UNREACHTITLE = 1, ACHIEVE = 2;
    private Context mContext;
    private List<String> reachIdList;

    public ReachedAchRcvAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public List<String> getReachIdList() {
        return reachIdList;
    }

    public void setReachIdList(List<String> reachIdList) {
        this.reachIdList = reachIdList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == ACHIEVE){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_myach_achieve, parent, false);
            return new AchieveHolder(view);
        }
        else{
            TextView view = new TextView(parent.getContext());
            RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            view.setLayoutParams(lp);
            view.setPadding(DisplayUtil.dip2px(mContext, 15), DisplayUtil.dip2px(mContext, 6), 0, DisplayUtil.dip2px(mContext, 6));
            view.setText(viewType == REACHTITLE ? "已完成" : "未完成");
            view.setTextColor(mContext.getResources().getColor(R.color.splash_label));
            view.setTextSize(Dimension.SP, 12);
            return new TitleHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return reachIdList.size() + 3;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0)
            return REACHTITLE;
        else if(position == reachIdList.size() + 1)
            return UNREACHTITLE;
        else
            return ACHIEVE;
    }

    protected class TitleHolder extends RecyclerView.ViewHolder{

        public TitleHolder(View itemView) {
            super(itemView);
        }
    }

    protected class AchieveHolder extends RecyclerView.ViewHolder{
        private ImageView iv_ach_icon;
        private TextView tv_ach_name, tv_ach_title, tv_ach_score, tv_ach_desc;
        private ImageButton btn_ach_loc;
        public AchieveHolder(View itemView) {
            super(itemView);
            iv_ach_icon = (ImageView) itemView.findViewById(R.id.iv_ach_icon);
            tv_ach_name = (TextView) itemView.findViewById(R.id.tv_ach_name);
            tv_ach_title = (TextView) itemView.findViewById(R.id.tv_ach_title);
            tv_ach_score = (TextView) itemView.findViewById(R.id.tv_ach_score);
            tv_ach_desc = (TextView) itemView.findViewById(R.id.tv_ach_desc);
            btn_ach_loc = (ImageButton) itemView.findViewById(R.id.btn_ach_loc);
        }

        public void setName(String name){
            tv_ach_name.setText(name);
        }

        public void setTitle(String title){
            tv_ach_title.setText(title);
        }

        public void setDesc(String desc){
            tv_ach_desc.setText(desc);
        }

        public void setScore(int score){
            tv_ach_score.setText(String.valueOf(score));
        }

        public void setIcon(boolean reached, int level){
            switch (level){
                case 1:
                    iv_ach_icon.setImageResource(reached ? R.mipmap.ic_l1 : R.mipmap.ic_l1_enable);
                    break;
                case 10:
                    iv_ach_icon.setImageResource(reached ? R.mipmap.ic_l2 : R.mipmap.ic_l2_enable);
                    break;
                case 100:
                    iv_ach_icon.setImageResource(reached ? R.mipmap.ic_l3 : R.mipmap.ic_l3_enable);
                    break;
                case 1000:
                    iv_ach_icon.setImageResource(reached ? R.mipmap.ic_l4 : R.mipmap.ic_l4_enable);
                    break;
            }
        }

        public void setLocClickListener(View.OnClickListener listener){
            btn_ach_loc.setOnClickListener(listener);
        }
    }
}
