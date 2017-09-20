package com.westepper.step.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.westepper.step.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mikiller on 2017/9/19.
 */

public class PoiRcvAdapter extends RecyclerView.Adapter<PoiRcvAdapter.Holder> {

    List<PoiItem> datas = new ArrayList<>();
    PoiItem choosedItem;
    onItemClickListener listener;
    @Override
    public PoiRcvAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        Holder holder = new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_poi, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(PoiRcvAdapter.Holder holder, int position) {
        final PoiItem item = datas.get(position);
        holder.tv_poi.setText(item.getTitle());
        holder.iv_poi.setVisibility((choosedItem != null && item.getPoiId().equals(choosedItem.getPoiId())) ? View.VISIBLE : View.GONE);
        if(!TextUtils.isEmpty(item.getSnippet())) {
            holder.tv_adr.setText(item.getSnippet());
            holder.tv_adr.setVisibility(View.VISIBLE);
        }else
            holder.tv_adr.setVisibility(View.GONE);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setChoosedItem(item);
                notifyDataSetChanged();
                if(listener != null)
                    listener.onItemClick(choosedItem);
            }
        });

    }

    public void setDatas(List<PoiItem> newData){
        datas.clear();
        datas.add(newData.get(0));
        newData.remove(0);
        if(!choosedItem.getPoiId().equals("default")){
            datas.add(choosedItem);
        }
        for(PoiItem item : newData){
            if(item.getPoiId().equals(choosedItem.getPoiId())){
                newData.remove(item);
                break;
            }
        }
        datas.addAll(newData);
        notifyDataSetChanged();
    }

    public void setItemClickListener(onItemClickListener listener){
        this.listener = listener;
    }

    public void setChoosedItem(PoiItem item){
        choosedItem = item;
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    public static class Holder extends RecyclerView.ViewHolder{

        private TextView tv_poi, tv_adr;
        private ImageView iv_poi;
        public Holder(View itemView) {
            super(itemView);
            tv_poi = (TextView) itemView.findViewById(R.id.tv_poi);
            iv_poi = (ImageView) itemView.findViewById(R.id.iv_choose);
            tv_adr = (TextView) itemView.findViewById(R.id.tv_adr);
        }
    }

    public interface onItemClickListener{
        void onItemClick(PoiItem item);
    }
}
