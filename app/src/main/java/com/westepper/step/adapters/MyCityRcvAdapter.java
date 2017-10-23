package com.westepper.step.adapters;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.westepper.step.R;
import com.westepper.step.base.Constants;
import com.westepper.step.responses.DisCity;

import java.util.List;

/**
 * Created by Mikiller on 2017/10/23.
 */

public class MyCityRcvAdapter extends RecyclerView.Adapter<MyCityRcvAdapter.CityHolder> {
    private List<DisCity> dataList;

    public MyCityRcvAdapter() {
    }

    public void setDataList(List<DisCity> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    @Override
    public CityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CityHolder viewHolder = new CityHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dis_city, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CityHolder holder, int position) {
        DisCity city = dataList.get(position);
        holder.setTv_dis_city(city.getTitle());
        holder.setTv_date(city.getDate());
        holder.setType(city.getType());
        if(position < getItemCount() - 1){
            holder.showLine();
        }
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    public class CityHolder extends RecyclerView.ViewHolder{
        private ImageView iv_city_logo;
        private View line;
        private TextView tv_dis_city, tv_date;
        public CityHolder(View itemView) {
            super(itemView);
            iv_city_logo = (ImageView) itemView.findViewById(R.id.iv_city_logo);
            line = itemView.findViewById(R.id.line);
            tv_dis_city = (TextView) itemView.findViewById(R.id.tv_dis_city);
            tv_date = (TextView) itemView.findViewById(R.id.tv_date);
        }

        public void setType(int type){
            iv_city_logo.setImageResource(type == Constants.CITY ? R.mipmap.ic_city_logo : R.mipmap.ic_level_logo);
            GradientDrawable gd = (GradientDrawable) line.getBackground();
            gd.setColor(type == Constants.CITY ? Color.parseColor("#00a8ff"):Color.parseColor("#fab446"));
            line.setBackgroundDrawable(gd);
        }

        public void showLine(){
            line.setVisibility(View.VISIBLE);
        }

        public void setTv_dis_city(String title){
            tv_dis_city.setText(title);
        }

        public void setTv_date(String date){
            tv_date.setText(date);
        }
    }
}
