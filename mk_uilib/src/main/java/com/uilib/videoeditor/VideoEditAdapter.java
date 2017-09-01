package com.uilib.videoeditor;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.uilib.R;

import java.util.ArrayList;
import java.util.List;


public class VideoEditAdapter extends RecyclerView.Adapter {
    private List<Bitmap> lists = new ArrayList<>();

    private int itemW, itemH;
    private Context context;

    public VideoEditAdapter(Context context, int itemW, int itemH) {
        this.context = context;
        this.itemW = itemW;
        this.itemH = itemH;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new EditViewHolder(LayoutInflater.from(context).inflate(R.layout.video_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        EditViewHolder viewHolder = (EditViewHolder) holder;
        viewHolder.img.setImageBitmap(lists.get(position));
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    private final class EditViewHolder extends RecyclerView.ViewHolder {
        public ImageView img;

        EditViewHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.id_image);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) img.getLayoutParams();
            layoutParams.width = itemW;
            layoutParams.height = itemH;
            img.setLayoutParams(layoutParams);
        }
    }

    public void addItemVideoInfo(Bitmap bmp){
        lists.add(bmp);
        notifyItemInserted(lists.size());
    }
}
