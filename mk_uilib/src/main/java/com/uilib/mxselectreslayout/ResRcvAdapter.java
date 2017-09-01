package com.uilib.mxselectreslayout;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.uilib.R;
import com.uilib.uploadimageview.MXProgressImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mikiller on 2016/8/12.
 */
public class ResRcvAdapter extends RecyclerView.Adapter<ResRcvAdapter.ResViewHolder> {
    Context mContext;
//    List<String> imgs;
    List<ResItemData> data;
    int itemWidth;
    onItemStateListener onItemStateListener;

    boolean canDelete = true;

    public onItemStateListener getOnItemDeleteListener() {
        return onItemStateListener;
    }

    public void setOnItemStateListener(onItemStateListener onItemDeleteListener) {
        this.onItemStateListener = onItemDeleteListener;
    }

    public boolean isCanDelete() {
        return canDelete;
    }

    public void setCanDelete(boolean canDelete) {
        this.canDelete = canDelete;
        notifyDataSetChanged();
    }

    //    public List<String> getImgs() {
//        return imgs;
//    }
//
//    public void setImgs(List<String> imgs) {
//        this.imgs = new ArrayList<>(imgs);
//        notifyDataSetChanged();
//    }


    public List<ResItemData> getData() {
        return data;
    }

    public void setData(List<ResItemData> data) {
        this.data = new ArrayList<>(data);
        onItemStateListener.setData(this.data);
        notifyDataSetChanged();
    }

    public void updateItemData(ResItemData itemData, int position){
        data.get(position).update(itemData);
        notifyItemChanged(position);
    }

    public int getItemWidth() {
        return itemWidth;
    }

    public void setItemWidth(int itemWidth) {
        this.itemWidth = itemWidth;
    }

//    public ResRcvAdapter(Context context, List<String> imgs) {
//        mContext = context;
//        this.imgs = imgs;
//    }


    public ResRcvAdapter(Context mContext, List<ResItemData> datas) {
        this.mContext = mContext;
        if(datas != null)
            this.data = new ArrayList<>(datas);
    }

    @Override
    public ResViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.select_res_item_layout, parent, false);
        ResViewHolder viewHolder = new ResViewHolder(itemWidth, itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ResViewHolder holder, final int position) {
        final int pos = holder.getAdapterPosition();
        holder.iv_pic.setListener(onItemStateListener.setPos(pos));
        setIvPicData(holder, pos);

        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getLayoutPosition();
                data.remove(pos);
                onItemStateListener.setData(data);
                notifyItemRemoved(pos);

                if(onItemStateListener != null)
                    onItemStateListener.onDelete(pos);

            }
        });
//            }
//        });

    }

    private void setIvPicData(ResViewHolder holder, final int position){
//        List<String> pathList = new ArrayList<>(imgs);
        if(data == null || data.size() == 0)
            return;
        final ResItemData itemData = data.get(position);
            if (itemData.imgPath.endsWith(".aac")) {
                holder.iv_pic.setBgImage(R.mipmap.audio);
            } else{
                holder.iv_pic.setFilePath(itemData.imgPath);
            }
        holder.iv_pic.setProgress(itemData.progress);
//        if(holder.iv_pic.getUploadState() == MXProgressImageView.ImageState.PAUSE)
//            itemData.setUploadState(MXProgressImageView.ImageState.PAUSE);
//        if(itemData.uploadState == MXProgressImageView.ImageState.SUCCESS && itemData.progress == 0)
//            itemData.setUploadState(MXProgressImageView.ImageState.STOP);
        holder.iv_pic.setUploadState(itemData.uploadState);
//        holder.iv_delete.setVisibility(canDelete ? View.VISIBLE : View.GONE);
        holder.iv_delete.setVisibility(isPicStop(holder.iv_pic) ? View.VISIBLE : View.GONE);
    }

    private boolean isPicStop(MXProgressImageView pic){
        return pic.getUploadState() == MXProgressImageView.ImageState.STOP || pic.getUploadState() == MXProgressImageView.ImageState.FAILED;
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public static class ResViewHolder extends RecyclerView.ViewHolder{
        private ImageView iv_delete;
        private MXProgressImageView iv_pic;

        public ResViewHolder(int itemWidth, View itemView) {
            super(itemView);
            iv_delete = (ImageView) itemView.findViewById(R.id.iv_delete);
            iv_pic = (MXProgressImageView) itemView.findViewById(R.id.iv_pic);
            iv_pic.setUploadState(MXProgressImageView.ImageState.STOP);
            RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) itemView.getLayoutParams();
            if(lp == null){
                lp = new RecyclerView.LayoutParams(itemWidth, itemWidth);
            }
            iv_pic.setAuto(true);
            lp.width = itemWidth;
            lp.height = itemWidth;
            itemView.setLayoutParams(lp);
        }
    }

//    public interface onItemStateListener{
//        void onDelete(int position);
//        void onStop(int position);
//        void onStart(int position);
//        void onPause(int position);
//        void onFailed(int position);
//        void onSuccess(int position);
//    }

    public static abstract class onItemStateListener implements MXProgressImageView.onViewStateListener {
        int pos = 0;
        List<ResItemData> data;
        public MXProgressImageView.onViewStateListener setPos(int pos){
            this.pos = pos;
            return this;
        }

        public int getPos(){
            return pos;
        }

        public List<ResItemData> getData() {
            return data;
        }

        public void setData(List<ResItemData> data) {
            this.data = data;
        }

        @Override
        public void onStop() {
            data.get(pos).setUploadState(MXProgressImageView.ImageState.STOP);
            onStop(pos);
//            if(onItemStateListener != null)
//                onItemStateListener.onStop(pos);
        }

        @Override
        public void onStart() {
            data.get(pos).setUploadState(MXProgressImageView.ImageState.START);
            onStart(pos);
//            if(onItemStateListener != null)
//                onItemStateListener.onStart(pos);
        }

        @Override
        public void onPause() {
            data.get(pos).setUploadState(MXProgressImageView.ImageState.PAUSE);
            onPause(pos);
//            if(onItemStateListener != null)
//                onItemStateListener.onPause(pos);
        }

        @Override
        public void onFailed() {
            data.get(pos).setUploadState(MXProgressImageView.ImageState.FAILED);
            onFailed(pos);
//            if(onItemStateListener != null)
//                onItemStateListener.onFailed(pos);
        }

        @Override
        public void onSuccess() {
            data.get(pos).setUploadState(MXProgressImageView.ImageState.SUCCESS);
            onSuccess(pos);
//            if(onItemStateListener != null)
//                onItemStateListener.onSuccess(pos);
        }

        public abstract void onDelete(int position);
        public abstract void onStop(int position);
        public abstract void onStart(int position);
        public abstract void onPause(int position);
        public abstract void onFailed(int position);
        public abstract void onSuccess(int position);
    };
}
