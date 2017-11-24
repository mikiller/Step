package com.westepper.step.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uilib.mxprogressbar.MXCircleProgressBar;
import com.westepper.step.R;
import com.westepper.step.base.Constants;
import com.westepper.step.responses.AchieveProgress;
import com.westepper.step.responses.CityProgress;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mikiller on 2017/11/24.
 */

public class UserDetailDisPgsAdapter extends RecyclerView.Adapter<UserDetailDisPgsAdapter.PgsHolder> {

    private int kind;
    private List<CityProgress> cityList;
    private List<AchieveProgress> achList;

    public UserDetailDisPgsAdapter(int kind) {
        this.kind = kind;
    }

    @Override
    public PgsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_userdetail_dispgs, null);
        return new PgsHolder(view);
    }

    @Override
    public void onBindViewHolder(PgsHolder holder, int position) {
        if(kind == Constants.ACH_CITY){
            CityProgress cityPgs = cityList.get(position);
            holder.setPgs(cityPgs.getCityName(), cityPgs.getReachedPercent());
        }else{
            AchieveProgress achPgs = achList.get(position);
            holder.setPgs(achPgs.getCategoryName(), achPgs.getPercent());
        }
    }

    public void setCityData(List<CityProgress> data){
        cityList = new ArrayList<>(data);
        notifyDataSetChanged();
    }

    public void setAchData(List<AchieveProgress> data){
        achList = new ArrayList<>(data);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return kind == Constants.ACH_CITY? (cityList == null ? 0 : cityList.size()) : (achList == null ? 0 : achList.size());
    }

    protected static class PgsHolder extends RecyclerView.ViewHolder{

        private MXCircleProgressBar pgs;
        private TextView tv_disName;
        public PgsHolder(View itemView) {
            super(itemView);
            pgs = (MXCircleProgressBar) itemView.findViewById(R.id.pgs);
            tv_disName = (TextView) itemView.findViewById(R.id.tv_disName);
        }

        public void setPgs(String name, double progress){
            tv_disName.setText(name);
            pgs.setProgress(progress);
        }
    }
}
