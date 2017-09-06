package com.westepper.step.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.uilib.joooonho.SelectableRoundedImageView;
import com.westepper.step.R;

import java.util.List;

/**
 * Created by Mikiller on 2017/9/6.
 */

public class PaihangRcvAdapter extends RecyclerView.Adapter<PaihangRcvAdapter.Holder> {
    private List<String> datas;

    public PaihangRcvAdapter() {
    }

    public PaihangRcvAdapter(List<String> datas) {
        this.datas = datas;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_paihang, null);
        return new Holder(root);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.tv_num.setText(String.valueOf(position + 4));
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    public class Holder extends RecyclerView.ViewHolder{
        private TextView tv_num, tv_nickName, tv_hint, tv_disNum, tv_achNum;
        private SelectableRoundedImageView iv_header;
        private ImageView iv_dis, iv_ach;
        public Holder(View itemView) {
            super(itemView);
            tv_num = (TextView) itemView.findViewById(R.id.tv_num);
        }
    }
}
