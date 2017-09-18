package com.westepper.step.activities;

import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.uilib.mxgallery.utils.CameraGalleryUtils;
import com.uilib.utils.DisplayUtil;
import com.westepper.step.R;
import com.westepper.step.adapters.DisPhotoRcvAdapter;
import com.westepper.step.base.Constants;
import com.westepper.step.base.SuperActivity;
import com.westepper.step.customViews.TitleBar;

import java.io.File;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Mikiller on 2017/9/12.
 */

public class NewDiscoveryActivity extends SuperActivity {
    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.rcv_photo)
    RecyclerView rcv_photo;
    @BindView(R.id.ll_del)
    LinearLayout ll_del;

    int disKind;
    String tmpFile;
    List<File> fileList;
    DisPhotoRcvAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if(savedInstanceState != null)
            disKind = savedInstanceState.getInt(Constants.DIS_KIND, Constants.MOOD);
        else if(getIntent() != null) {
            disKind = getIntent().getIntExtra(Constants.DIS_KIND, Constants.MOOD);
            tmpFile = getIntent().getStringExtra(CameraGalleryUtils.TMP_FILE);
            fileList = (List<File>) getIntent().getSerializableExtra(CameraGalleryUtils.THUMB_FILE);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_discovery);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(Constants.DIS_KIND, disKind);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void initView() {
        titleBar.setTitleListener(new TitleBar.TitleListener() {
            @Override
            protected void onBackClicked() {
                back();
            }

            @Override
            protected void onSubClicked() {
                super.onSubClicked();
            }
        });
        titleBar.setTitle(disKind == Constants.MOOD ? "新心情" : "新约行");
//
        adapter = new DisPhotoRcvAdapter(this, true, 3, DisplayUtil.dip2px(this, 7));
        adapter.setDisKind(disKind);
        adapter.setItemMovedListener(new DisPhotoRcvAdapter.onItemMovedListener() {
            boolean needDel = false;

            @Override
            public void onPressed(int pos) {
                ll_del.setVisibility(View.VISIBLE);
            }

            @Override
            public void onMoved(int pos, int x, int y) {
                int dy = DisplayUtil.getScreenHeight(NewDiscoveryActivity.this) - 360 - titleBar.getHeight();
                needDel = y > dy;
            }

            @Override
            public void onReleased(int fPos, int tPos) {
                Log.e(TAG, "fpos: " + fPos + ", tPos: " + tPos);
                if(needDel) {
                    adapter.removeItem(fPos);
                    needDel = false;
                }else{

                    Collections.swap(adapter.getData(), adapter.getItemPos(fPos), adapter.getItemPos(tPos));
                }
                ll_del.setVisibility(View.GONE);
            }
        });

        rcv_photo.setAdapter(adapter);

        final GridLayoutManager glMgr = new GridLayoutManager(this, 3){
            @Override
            public boolean canScrollVertically() {
                return true;
            }
        };
        glMgr.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return (adapter.isHeadView(position) || adapter.isFootView(position)) ? glMgr.getSpanCount() : 1;
            }
        });
        rcv_photo.setLayoutManager(glMgr);
        rcv_photo.setHasFixedSize(true);
//        rcv_photo.addItemDecoration(new DisPhotoDecoration(3, 1, 1, DisplayUtil.dip2px(this, 4)));
        adapter.createTouchHelper().attachToRecyclerView(rcv_photo);

//        ll_goParam.setVisibility(disKind == Constants.MOOD ? View.GONE : View.VISIBLE);
    }

    @Override
    protected void initData() {
        if(!TextUtils.isEmpty(tmpFile)){
            adapter.addItem(tmpFile);
            tmpFile = null;
        }else{
            adapter.addItems(fileList);
            fileList.clear();
        }
    }
}
