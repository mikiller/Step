package com.uilib.netresdisplaylayout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.uilib.R;
import com.uilib.uploadimageview.MXProgressImageView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

/**
 * Created by Mikiller on 2016/8/24.
 */
public class NetResAdapter extends RecyclerView.Adapter<NetResAdapter.ResViewHolder> {
    Context mContext;
    List<String> data;
    Map<String, Bitmap> bmgList = new HashMap<>();
    int itemWidth;
    boolean hasLoad = false;

    OnItemClickListener itemClickListener;

    public OnItemClickListener getItemClickListener() {
        return itemClickListener;
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public NetResAdapter(Context mContext, List<String> data) {
        this.data = data;
        this.mContext = mContext;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public int getItemWidth() {
        return itemWidth;
    }

    public void setItemWidth(int itemWidth) {
        this.itemWidth = itemWidth;
    }

    @Override
    public ResViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.net_res_item_layout, parent, false);
        ResViewHolder viewHolder = new ResViewHolder(itemWidth, itemView);
        if(!hasLoad)
            loadNetBmp(viewHolder);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ResViewHolder holder, int position) {
        if(data == null || data.get(position) == null)
            return;
        if(data.get(position).endsWith(".caf")){
            //holder.iv_pic.setScaleType(ImageView.ScaleType.FIT_XY);
            holder.iv_pic.setImageResource(R.mipmap.audio);
        }else {
           // holder.iv_pic.setScaleType(ImageView.ScaleType.CENTER_CROP);
            holder.iv_pic.setImageBitmap(bmgList.get(data.get(position)));
            holder.iv_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(itemClickListener != null)
                        itemClickListener.onItemClick(v, holder.getLayoutPosition());
                }
            });
        }
    }

    private void loadNetBmp(final ResViewHolder holder){
        for(final String path : data){
            if(path == null)
                continue;
            if(path.endsWith("caf"))
                continue;
            Executors.newCachedThreadPool().execute(new BmpRunnable(holder.iv_pic, path));
        }
        hasLoad = true;
    }

    private class BmpRunnable implements Runnable {
        String path;
        View view;

        public BmpRunnable(View view, String path) {
            this.path = path;
            this.view = view;
        }

        @Override
        public void run() {
            try {
//                        final int pos = holder.getLayoutPosition();
                URL url = new URL(path);
                final Bitmap bmp = BitmapFactory.decodeStream(url.openStream());
                Log.e("netrcvadapter2", path);
                bmgList.put(path, bmp);
                try {
                    NetResAdapter.this.notifyItemChanged(data.indexOf(path));
                }catch (Exception e){
                    e.printStackTrace();
                    Executors.newCachedThreadPool().execute(this);
                }
//                        holder.iv_pic.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                if(data == null || path == null)
//                                    return;
//
//                            }
//                        });
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public static class ResViewHolder extends RecyclerView.ViewHolder{
        private ImageView iv_pic;
        public ResViewHolder(int itemWidth, View itemView) {
            super(itemView);
            iv_pic = (ImageView) itemView.findViewById(R.id.iv_pic);
            RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) itemView.getLayoutParams();
            if(lp == null){
                lp = new RecyclerView.LayoutParams(itemWidth, itemWidth);
            }
            lp.width = itemWidth;
            lp.height = itemWidth;
            itemView.setLayoutParams(lp);
        }

    }

    public void recyler(){
        for(String path : data){
            if(bmgList.get(path) != null && !bmgList.get(path).isRecycled()){
                bmgList.get(path).recycle();
            }
        }
        bmgList.clear();
        bmgList = null;
    }

    public interface OnItemClickListener{
        void onItemClick(View v, int pos);
    }
}
