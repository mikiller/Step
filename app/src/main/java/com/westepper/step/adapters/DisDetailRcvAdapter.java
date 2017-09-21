package com.westepper.step.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.westepper.step.R;

import java.util.List;

/**
 * Created by Mikiller on 2017/9/21.
 */

public class DisDetailRcvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int DETAIL = 0, COMMITS = 1;
    private String name;
    private List<String> commits;

    @Override
    public int getItemViewType(int position) {
        if(position == 0)
            return DETAIL;
        else
            return COMMITS;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        View view;
        if(viewType == DETAIL) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dis_detail, null);
            viewHolder = new DetailHolder(view);
        }else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dis_commitlist, null);
            viewHolder = new CommitHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof DetailHolder)
            ((DetailHolder) holder).tv_name.setText(name);
        else
            ((CommitHolder)holder).tv_commit.setText(commits.get(holder.getAdapterPosition() - 1));
    }

    public void setCommits(List commits){
        this.commits = commits;
        notifyDataSetChanged();
    }

    public void setName(String name){
        this.name = name;
        notifyItemChanged(0);
    }

    @Override
    public int getItemCount() {
        return commits == null ? 1 : commits.size() + 1;
    }

    public class DetailHolder extends RecyclerView.ViewHolder{
        private TextView tv_name;
        public DetailHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
        }
    }

    public class CommitHolder extends RecyclerView.ViewHolder{

        private TextView tv_commit;
        public CommitHolder(View itemView) {
            super(itemView);
            tv_commit = (TextView) itemView.findViewById(R.id.tv_commit);
        }
    }
}
