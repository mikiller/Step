package com.westepper.step.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.mikiller.mkglidelib.imageloader.GlideImageLoader;
import com.uilib.customdialog.CustomDialog;
import com.uilib.mxgallery.utils.CameraGalleryUtils;
import com.westepper.step.R;
import com.westepper.step.activities.GalleryActivity;
import com.westepper.step.base.Constants;
import com.westepper.step.customViews.MyMenuItem;
import com.westepper.step.utils.ActivityManager;
import com.westepper.step.utils.MapUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mikiller on 2017/9/13.
 */

public class DisPhotoRcvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int HEADER = 0, ITEM = 1, FOOTER = 2, FOOTER2 = 3;
    public int headCount = 1, footCount = 2, itemCount;
    private Context mContext;
    private List<String> pathList;
    private boolean needCamera = true;
    private int itemSize, margin;
    private int disKind;
    private onItemMovedListener listener;
    private View.OnClickListener footClickListener;

    private PoiItem poiItem;

    public DisPhotoRcvAdapter(Context context, boolean needCamera, int column, int margin) {
        mContext = context;
        this.needCamera = needCamera;
        itemSize = (int) ((context.getResources().getDisplayMetrics().widthPixels - (column+1) * margin) / column);
        this.margin = margin;
        LatLonPoint point = new LatLonPoint(MapUtils.getInstance().getMapLocation().getLatitude(), MapUtils.getInstance().getMapLocation().getLongitude());
        poiItem = new PoiItem("default", point, "上海市", "");
    }

    public DisPhotoRcvAdapter(Context context, boolean needCamera, List<String> pathList, int column, int margin) {
        this(context, needCamera, column, margin);
        this.pathList = pathList;
    }

    @Override
    public int getItemViewType(int position) {
        if(isHeadView(position))
            return HEADER;
        else if(isFootView(position)) {
            if(position == getItemCount() - footCount)
                return FOOTER;
            else
                return FOOTER2;
        }
        else
            return ITEM;
    }

    public void setDisKind(int disKind) {
        this.disKind = disKind;
        footCount = disKind == Constants.MOOD ? 1 : 2;
    }

    public void setItemMovedListener(onItemMovedListener listener){
        this.listener = listener;
    }

    public void setFootClickListener(View.OnClickListener listener){
        this.footClickListener = listener;
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
        else if(viewType == FOOTER2)
            viewHolder = new Foot2Holder(LayoutInflater.from(mContext).inflate(R.layout.layout_newdis_rcvfoot2, parent, false));
        else
            viewHolder = new ItemHolder(LayoutInflater.from(mContext).inflate(R.layout.layout_gallery_item, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof HeadHolder)
            bindHeadHolder((HeadHolder) holder);
        else if(holder instanceof ItemHolder)
            bindItemHolder((ItemHolder) holder, holder.getAdapterPosition() - headCount);
        else if(holder instanceof FootHolder)
            bindFootHolder((FootHolder) holder);
        else
            bindFootHolder2((Foot2Holder) holder);
    }

    private void bindHeadHolder(HeadHolder holder){
        holder.edt_msg.setHint(disKind == Constants.MOOD ? "记录你的心情..." : "描述你的约行...");
    }

    private void bindItemHolder(ItemHolder holder, int position){
        if(needCamera && position == pathList.size()){
            setLayoutParams(holder.ll_camera, margin);
            holder.ll_camera.setVisibility(View.VISIBLE);
            holder.ll_camera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map<String, Object> args = new HashMap<>();
                    args.put(Constants.GALLERY_TYPE, Constants.ADD_PHOTO);
                    args.put(Constants.ISMULTIPLE, true);
                    args.put(Constants.MAX_SELECT, 9 - pathList.size());
                    ActivityManager.startActivityforResult((Activity) mContext, GalleryActivity.class, Constants.ADD_PHOTO, args);
                }
            });
            holder.cv_img.setVisibility(View.GONE);
        }else{

            holder.ll_camera.setVisibility(View.GONE);
            holder.cv_img.setVisibility(View.VISIBLE);
            setLayoutParams(holder.cv_img, margin);
            GlideImageLoader.getInstance().loadImageWithoutBitmap(mContext, pathList.get(position), holder.iv_img);
            holder.setMediaType(true);
            holder.ckb_isCheck.setVisibility(View.GONE);
        }
    }

    private void bindFootHolder(FootHolder holder){
        String city = TextUtils.isEmpty(poiItem.getCityName()) ? poiItem.getTitle() : poiItem.getCityName() + ", " + poiItem.getTitle();
        holder.tv_pos.setText(city);
        if(footClickListener != null)
            holder.tv_pos.setOnClickListener(footClickListener);
    }

    private void bindFootHolder2(Foot2Holder holder){
        holder.menu_date.setSubText("不限");
        holder.menu_people.setSubText("不限");
        if(footClickListener != null){
            holder.menu_date.setOnClickListener(footClickListener);
            holder.menu_people.setOnClickListener(footClickListener);
        }
    }

    private void setLayoutParams(View view, int margins){
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) view.getLayoutParams();
        lp.width = lp.height = itemSize;
        lp.leftMargin = margins;
        lp.bottomMargin = margins;
        view.setLayoutParams(lp);
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

    public void removeItem(int pos){
        pathList.remove(pos - headCount);
        notifyItemRemoved(pos);
    }

    public List getData(){
        return pathList;
    }

    public PoiItem getPoiItem(){
        return poiItem;
    }

    public void setPoiItem(PoiItem item){
        poiItem = item;
        notifyItemChanged(getItemCount() - footCount);
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
                if(actionState == ItemTouchHelper.ACTION_STATE_DRAG && listener != null) {
                    listener.onPressed(viewHolder.getAdapterPosition());
                }
            }

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                if(listener != null)
                    listener.onReleased(viewHolder.getAdapterPosition());
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
                return true;
            }

            @Override
            public void onMoved(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, int fromPos, RecyclerView.ViewHolder target, int toPos, int x, int y) {
                super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y);
                if(getItemPos(toPos) >= pathList.size() || toPos < headCount) {
                    return;
                }
                Collections.swap(pathList, getItemPos(fromPos), getItemPos(toPos));
                notifyItemMoved(fromPos, toPos);
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                if(isCurrentlyActive && listener != null)
                    listener.onMove(viewHolder.getAdapterPosition(), (int) viewHolder.itemView.getY());
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

    public int getItemPos(int pos){
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

        public FootHolder(View itemView) {
            super(itemView);
            tv_pos = (TextView) itemView.findViewById(R.id.tv_pos);
        }
    }

    public static class Foot2Holder extends RecyclerView.ViewHolder{
        private LinearLayout ll_goParam;
        private MyMenuItem menu_people, menu_date;

        public Foot2Holder(View itemView) {
            super(itemView);
            ll_goParam = (LinearLayout) itemView.findViewById(R.id.ll_goParam);
            menu_people = (MyMenuItem) itemView.findViewById(R.id.menu_people);
            menu_date = (MyMenuItem) itemView.findViewById(R.id.menu_date);
        }
    }

    public interface onItemMovedListener{
        void onMove(int pos, int y);
        void onReleased(int fPos);
        void onPressed(int pos);
    }

}
