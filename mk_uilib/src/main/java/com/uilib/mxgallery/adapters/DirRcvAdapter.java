package com.uilib.mxgallery.adapters;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mikiller.mkglidelib.imageloader.GlideImageLoader;
import com.uilib.R;
import com.uilib.joooonho.SelectableRoundedImageView;
import com.uilib.mxgallery.models.Album;
import com.uilib.utils.DisplayUtil;

import java.io.File;

/**
 * Created by Mikiller on 2017/9/11.
 */

public class DirRcvAdapter extends RecyclerViewCursorAdapter<DirRcvAdapter.Holder> {
    Context mContext;
    int[] size;

    private onItemClickListener listener;

    public DirRcvAdapter(Context context) {
        super(null);
        mContext = context;
    }

    public void setListener(onItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        size = new int[]{DisplayUtil.dip2px(mContext, 45), DisplayUtil.dip2px(mContext, 45)};
        return new Holder(LayoutInflater.from(mContext).inflate(R.layout.item_dirlist, parent, false));
    }

    @Override
    protected void onBindViewHolder(final Holder holder, Cursor cursor) {
        final Album album = Album.valueOf(cursor);
        GlideImageLoader.getInstance().loadLocalImage(mContext, Uri.fromFile(new File(album.getCoverPath())), size, R.mipmap.placeholder, holder.iv_img);
        holder.tv_dirName.setText(album.getDisplayName(mContext));
        holder.tv_count.setText(String.valueOf(album.getCount()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null)
                    listener.onItemClicked(album.getId());
            }
        });
    }

    @Override
    protected int getItemViewType(int position, Cursor cursor) {
        return 0;
    }

    public class Holder extends RecyclerView.ViewHolder{

        private SelectableRoundedImageView iv_img;
        private TextView tv_dirName, tv_count;
        public Holder(View itemView) {
            super(itemView);
            iv_img = (SelectableRoundedImageView) itemView.findViewById(R.id.iv_img);
            tv_dirName = (TextView) itemView.findViewById(R.id.tv_dirName);
            tv_count = (TextView) itemView.findViewById(R.id.tv_count);
        }
    }

    public interface onItemClickListener{
        void onItemClicked(String bucketId);
    }
}
