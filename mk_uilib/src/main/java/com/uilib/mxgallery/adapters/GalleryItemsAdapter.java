package com.uilib.mxgallery.adapters;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mikiller.mkglidelib.imageloader.GlideImageLoader;
import com.uilib.mxgallery.models.ItemModel;
import com.uilib.mxgallery.models.MimeType;
import com.uilib.mxgallery.widgets.MediaCollection;
import com.uilib.R;
import com.uilib.mxgallery.listeners.OnMediaItemClickListener;
import com.uilib.utils.DisplayUtil;

import java.io.File;
import java.net.URI;

/**
 * Created by Mikiller on 2017/5/11.
 */

public class GalleryItemsAdapter extends RecyclerViewCursorAdapter<GalleryItemsAdapter.MediaViewHolder> {

    private Context mContext;
    private int itemSize;
    private OnMediaItemClickListener listnener;
    private MediaCollection mediaCollection;

    public GalleryItemsAdapter(Context context, MediaCollection mediaCollection, int itemCount, int margin) {
        super(null);
        mContext = context;
        this.mediaCollection = mediaCollection;
        itemSize = (context.getResources().getDisplayMetrics().widthPixels - (itemCount+1) * margin) / 4;
    }

    public void setItemClickeListener(OnMediaItemClickListener listnener){
        this.listnener = listnener;
    }

    @Override
    public GalleryItemsAdapter.MediaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MediaViewHolder viewHolder = new MediaViewHolder(LayoutInflater.from(mContext).inflate(R.layout.layout_gallery_item, parent, false));
        return viewHolder;
    }

    @Override
    protected void onBindViewHolder(MediaViewHolder holder, final Cursor cursor) {
        ItemModel model = ItemModel.valueOf(cursor);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            GlideImageLoader.getInstance().loadLocalImage(mContext, model.getContentUri(), R.mipmap.placeholder, new Size(itemSize, itemSize), holder.iv_img);
        }else{
            GlideImageLoader.getInstance().loadLocalImage(mContext, model.getContentUri(), new int[]{itemSize, itemSize}, R.mipmap.placeholder, holder.iv_img);
        }
        holder.tv_time.setText(DateUtils.formatElapsedTime(model.duration / 1000));
//        int pdleft = DisplayUtil.px2dip(mContext, itemSize / 2), pdTop = DisplayUtil.dip2px(mContext, 5);
//        holder.ckb_isCheck.setPadding(pdleft, pdTop, pdTop, pdleft);
        holder.setMediaType(MimeType.isPic(model.mimeType));
        setItemStatus(holder, model);

    }

    private void setItemStatus(final GalleryItemsAdapter.MediaViewHolder holder, final ItemModel model){
        holder.ckb_isCheck.setEnabled(mediaCollection == null ? true : mediaCollection.canSelectModel(model));
        holder.ckb_isCheck.setOnCheckedChangeListener(null);
        holder.ckb_isCheck.setChecked(mediaCollection == null ? false : mediaCollection.isContainMedia(model));
        if(listnener != null){
            holder.ckb_isCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    listnener.onItemChecked(model, isChecked);
                    notifyDataSetChanged();
                }
            });
            holder.iv_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //listnener.onItemClicked(model, holder.ckb_isCheck.isChecked());
                    holder.ckb_isCheck.setChecked(!holder.ckb_isCheck.isChecked());
                }
            });
        }

    }

    @Override
    protected int getItemViewType(int position, Cursor cursor) {
        return 0;
    }

    public static class MediaViewHolder extends RecyclerView.ViewHolder{
        private ImageView iv_img;
        private CheckBox ckb_isCheck;
        private LinearLayout ll_time;
        private TextView tv_time;

        public MediaViewHolder(View itemView) {
            super(itemView);
            iv_img = (ImageView) itemView.findViewById(R.id.iv_img);
            iv_img.setAdjustViewBounds(true);
            ckb_isCheck = (CheckBox) itemView.findViewById(R.id.ckb_isCheck);
            ll_time = (LinearLayout) itemView.findViewById(R.id.ll_time);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
        }

        public void setMediaType(boolean isPic){
            if(isPic){
                ckb_isCheck.setVisibility(View.VISIBLE);
                ll_time.setVisibility(View.GONE);
            }else{
                ckb_isCheck.setVisibility(View.GONE);
                ll_time.setVisibility(View.VISIBLE);
            }
        }
    }
}
