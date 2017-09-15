package com.westepper.step.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mikiller.mkglidelib.imageloader.GlideImageLoader;
import com.uilib.mxgallery.utils.CameraGalleryUtils;
import com.westepper.step.R;
import com.westepper.step.customViews.MyMenuItem;
import com.westepper.step.utils.AnimUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Mikiller on 2017/9/13.
 */

public class DisPhotoRcvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int HEADER = 0, ITEM = 1, FOOTER = 2;
    public int headCount = 1, footCount = 1, itemCount;
    private Context mContext;
    private List<String> pathList;
    private boolean needCamera = true;
    private int itemSize;

    public DisPhotoRcvAdapter(Context context, boolean needCamera, int column, int margin) {
        mContext = context;
        this.needCamera = needCamera;
        itemSize = (int) ((context.getResources().getDisplayMetrics().widthPixels - (column+1) * margin) / column);
    }

    public DisPhotoRcvAdapter(Context context, boolean needCamera, List<String> pathList, int column, int margin) {
        this(context, needCamera, column, margin);
        this.pathList = pathList;
    }

    @Override
    public int getItemViewType(int position) {
        if(isHeadView(position))
            return HEADER;
        else if(isFootView(position))
            return FOOTER;
        else
            return ITEM;
    }

    public boolean isHeadView(int pos){
        return headCount != 0 && pos < headCount;
    }

    public boolean isFootView(int pos){
        return footCount != 0 && pos >= getItemCount() - footCount;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        if(viewType == HEADER)
            viewHolder = new HeadHolder(LayoutInflater.from(mContext).inflate(R.layout.layout_newdis_rcvhead, parent, false));
        else if(viewType == FOOTER)
            viewHolder = new FootHolder(LayoutInflater.from(mContext).inflate(R.layout.layout_newdis_rcvfoot, parent, false));
        else
            viewHolder = new ItemHolder(LayoutInflater.from(mContext).inflate(R.layout.layout_gallery_item, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ItemHolder)
            bindItemHolder((ItemHolder) holder, holder.getAdapterPosition() - headCount);

    }

    private void bindItemHolder(ItemHolder holder, int position){
        if(needCamera && position == pathList.size()){
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.ll_camera.getLayoutParams();
            lp.width = lp.height = itemSize;
            holder.ll_camera.setLayoutParams(lp);
            holder.ll_camera.setVisibility(View.VISIBLE);
            holder.ll_camera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CameraGalleryUtils.getInstance(mContext).openSysCamera();
                }
            });
            holder.cv_img.setVisibility(View.GONE);
        }else{
            holder.ll_camera.setVisibility(View.GONE);
            holder.cv_img.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.iv_img.getLayoutParams();
            lp.width = lp.height = itemSize;
            holder.iv_img.setLayoutParams(lp);
//            GlideImageLoader.getInstance().loadImage(mContext, pathList.get(position - pos), R.mipmap.placeholder, holder.iv_img, 0);
            GlideImageLoader.getInstance().loadImageWithoutBitmap(mContext, pathList.get(position), holder.iv_img);
//            holder.tv_time.setText(DateUtils.formatElapsedTime(model.duration / 1000));
//        int pdleft = DisplayUtil.px2dip(mContext, itemSize / 2), pdTop = DisplayUtil.dip2px(mContext, 5);
//        holder.ckb_isCheck.setPadding(pdleft, pdTop, pdTop, pdleft);
            holder.setMediaType(true);
            holder.ckb_isCheck.setVisibility(View.GONE);
//            setItemStatus(holder, model);
        }
    }


    public void addItem(String newPath){
        if(pathList == null)
            pathList = new ArrayList<String>();
        pathList.add(newPath);
        notifyDataSetChanged();
    }

    public void addItems(List<File> files){
        for(File file : files){
            addItem(file.getPath());
        }
    }

    public List getData(){
        return pathList;
    }

    public ItemTouchHelper createTouchHelper(){
        return new ItemTouchHelper(new ItemTouchHelper.Callback() {

            @Override
            public boolean isItemViewSwipeEnabled() {
                return false;
            }

            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                super.onSelectedChanged(viewHolder, actionState);
                if(viewHolder != null)
                    AnimUtils.startObjectAnim(((ItemHolder)viewHolder).cv_img, "translationZ", 0, 10, 400);
            }

            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                // 拖拽的标记，这里允许上下左右四个方向
                int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT |
                        ItemTouchHelper.RIGHT;
                if(!canMove(viewHolder.getAdapterPosition()))
                    dragFlags = ItemTouchHelper.ACTION_STATE_IDLE;
                // 滑动的标记，这里允许左右滑动
                int swipeFlags = ItemTouchHelper.ACTION_STATE_IDLE;
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                if(getItemPos(target.getAdapterPosition()) >= pathList.size() || target.getAdapterPosition() < headCount)
                    return false;
                Collections.swap(pathList, getItemPos(viewHolder.getAdapterPosition()), getItemPos(target.getAdapterPosition()));
                return true;
            }

            @Override
            public void onMoved(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, int fromPos, RecyclerView.ViewHolder target, int toPos, int x, int y) {
                super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y);
                notifyItemMoved(fromPos, toPos);
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

            }
        });
    }

    private boolean canMove(int pos){
        boolean rst = true;
        if(pos < headCount)
            rst = false;
        else if(pos >= pathList.size() + headCount)
            rst = false;
        return rst;
    }

    private int getItemPos(int pos){
        return pos - headCount;
    }

    @Override
    public int getItemCount() {
        itemCount = (needCamera ? 1 : 0) + headCount + footCount;
        return pathList == null ? itemCount : (pathList.size() < 9 ? pathList.size() + itemCount : pathList.size() + headCount + footCount);
    }

    public static class ItemHolder extends RecyclerView.ViewHolder{
        private LinearLayout ll_camera;
//        private RelativeLayout rl_img;
        private CardView cv_img;
        private ImageView iv_img;
        private CheckBox ckb_isCheck;
        private LinearLayout ll_time;
        private TextView tv_time;

        public ItemHolder(View itemView) {
            super(itemView);
            ll_camera = (LinearLayout) itemView.findViewById(R.id.ll_camera);
//            rl_img = (RelativeLayout) itemView.findViewById(com.uilib.R.id.rl_img);
            cv_img = (CardView) itemView.findViewById(R.id.cv_img);
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

    public static class HeadHolder extends RecyclerView.ViewHolder{

        private EditText edt_msg;
        public HeadHolder(View itemView) {
            super(itemView);
            edt_msg = (EditText) itemView.findViewById(R.id.edt_msg);
        }
    }

    public static class FootHolder extends RecyclerView.ViewHolder{

        private TextView tv_pos;
        private LinearLayout ll_goParam;
        private MyMenuItem menu_people, menu_date;
        public FootHolder(View itemView) {
            super(itemView);
            tv_pos = (TextView) itemView.findViewById(R.id.tv_pos);
            ll_goParam = (LinearLayout) itemView.findViewById(R.id.ll_goParam);
            menu_people = (MyMenuItem) itemView.findViewById(R.id.menu_people);
            menu_date = (MyMenuItem) itemView.findViewById(R.id.menu_date);
        }
    }
}
