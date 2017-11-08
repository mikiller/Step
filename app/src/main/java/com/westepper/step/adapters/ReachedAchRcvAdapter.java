package com.westepper.step.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.westepper.step.activities.MainActivity;
import com.westepper.step.base.Constants;
import com.westepper.step.base.SuperActivity;
import com.westepper.step.responses.AchieveArea;
import com.westepper.step.utils.ActivityManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Mikiller on 2017/10/24.
 */

public class ReachedAchRcvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int REACHTITLE = 0, UNREACHTITLE = 1, ACHIEVE = 2;
    private Context mContext;
    private List<AchieveArea> achAreaList;
    private List<String> reachIds = new ArrayList<>();
    private int dataOffset = 0;

    public ReachedAchRcvAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public List<AchieveArea> getAchAreaList() {
        return achAreaList;
    }

    public void setAchAreaList(List<AchieveArea> achAreaList) {
        this.achAreaList = new ArrayList<>(achAreaList);
        if(achAreaList.size() == 0)
            return;
        int j = 0;
        for(String id : reachIds){
            for(int i = 0; i < achAreaList.size(); i++){
                if(achAreaList.get(i).getAchieveAreaId().equals(id) && i != j){
                    Collections.swap(this.achAreaList, i, j++);
                    break;
                }
            }
        }
    }

    public void setReachIds(List<String> reachIds) {
        this.reachIds = reachIds;
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

        if(getItemViewType(position) == REACHTITLE || getItemViewType(position) == UNREACHTITLE){
            dataOffset++;
        }
        if(holder instanceof AchieveHolder){
            int pos = holder.getAdapterPosition() - dataOffset;
            final AchieveArea area = achAreaList.get(pos);
            ((AchieveHolder) holder).setIcon(pos < reachIds.size(), Integer.parseInt(area.getCredit_level()));
            ((AchieveHolder) holder).setTitle(area.getTitle());
            ((AchieveHolder) holder).setDesc(area.getDesc());
            ((AchieveHolder) holder).setName(area.getAchieveAreaName());
            ((AchieveHolder) holder).setScore(area.getScore());
            ((AchieveHolder) holder).setLocClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra(Constants.ACH_KIND, area.getAchieveAreaId());
                    ((SuperActivity)mContext).setResult(Activity.RESULT_OK, intent);
                    ((SuperActivity)mContext).back();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return achAreaList.size() + (reachIds.size() > 0 ? 2 : 1);
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0)
            return reachIds.size() == 0 ? UNREACHTITLE : REACHTITLE;
        else if(reachIds.size() > 0 && position == reachIds.size() + 1)
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
