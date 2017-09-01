package com.mkframe.step.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mkframe.R;
import com.mkframe.step.models.CityBean;

import java.util.List;

/**
 * Created by Mikiller on 2017/8/31.
 */

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder> {
    private Context mContext;
    private List<CityBean> mDatas;

    private CityClickListener listener;

    public void setListener(CityClickListener listener) {
        this.listener = listener;
    }

    public CityAdapter(Context mContext, List<CityBean> mDatas) {
        this.mContext = mContext;
        this.mDatas = mDatas;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_city,parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        CityBean city = mDatas.get(position);
        LinearLayout.LayoutParams lp = ((LinearLayout.LayoutParams)holder.ll_city.getLayoutParams());
        if(city.isTop()){
            holder.tv_fword.setText(city.getBaseIndexTag());
            holder.tv_fword.setVisibility(View.VISIBLE);
            lp.topMargin = 44;
        }else {
            holder.tv_fword.setVisibility(View.INVISIBLE);
            lp.topMargin = 24;
        }
        holder.ll_city.setLayoutParams(lp);
        holder.ll_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null)
                    listener.onCityClick(mDatas.get(holder.getAdapterPosition()).getTarget());
            }
        });
        holder.tv_city.setText(city.getTarget());

    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_fword, tv_city;
        private LinearLayout ll_city;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_fword = (TextView) itemView.findViewById(R.id.tv_fword);
            tv_city = (TextView) itemView.findViewById(R.id.tv_city);
            ll_city = (LinearLayout) itemView.findViewById(R.id.ll_city);
        }
    }

    public interface CityClickListener{
        void onCityClick(String city);
    }
}
