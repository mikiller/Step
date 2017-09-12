package com.westepper.step.activities;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.uilib.mxgallery.defaultloaders.AlbumLoader;
import com.uilib.mxgallery.defaultloaders.MediaLoader;
import com.uilib.mxgallery.listeners.OnSelectItemListener;
import com.uilib.mxgallery.models.Album;
import com.uilib.mxgallery.models.MimeType;
import com.uilib.mxgallery.utils.GalleryUtils;
import com.uilib.mxgallery.widgets.MXGallery;
import com.uilib.utils.DisplayUtil;
import com.westepper.step.R;
import com.uilib.mxgallery.adapters.DirRcvAdapter;
import com.westepper.step.base.SuperActivity;
import com.westepper.step.customViews.TitleBar;
import com.westepper.step.models.ReportResModel;
import com.westepper.step.utils.AnimUtils;
import com.westepper.step.utils.CameraGalleryUtils;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Mikiller on 2017/5/18.
 */

public class GalleryActivity extends SuperActivity {
    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.gallery)
    MXGallery gallery;
    @BindView(R.id.fl_pop)
    FrameLayout fl_pop;
    @BindView(R.id.rcv_dirList)
    RecyclerView rcv_dirList;

    private boolean isMultiple = false;
    private Bundle savedBundle;
    private List<String> selectedPath = new ArrayList<>();
    private DirRcvAdapter adapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        isMultiple = getIntent().getBooleanExtra("isMultiple", false);
        savedBundle = savedInstanceState;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
    }

    @Override
    protected void initView() {
        titleBar.setTitleListener(new TitleBar.TitleListener(){
            @Override
            protected void onBackClicked() {
                back();
            }

            @Override
            public void onMenuChecked(boolean isChecked){
                if(isChecked)
                    showDirList();
                else
                    hideDirList();
            }
        });
        gallery.setIsMultiple(isMultiple);
        gallery.setMimeType(MimeType.ofImage());
        gallery.setSelectListener(new OnSelectItemListener() {
            @Override
            public void onPreView(boolean isPreview) {
            }

            @Override
            public void onConfirm(List<File> fileList) {

//                if (title.equals("图片")) {
                    Intent intent = new Intent();
                    intent.putExtra(CameraGalleryUtils.THUMB_FILE, (Serializable) fileList);
                    //intent.putExtra("listType", title);
                    setResult(RESULT_OK, intent);
//                } else {
//                    CameraGalleryUtils.getInstance().openVideoEditor(fileList.get(0));
//                    CameraGalleryUtils.getInstance().openVideoEditor(CameraGalleryUtils.getInstance().createReportResModel(fileList.get(0)));

//                }
                back();
            }
        });
        gallery.onCreate(savedBundle);
        gallery.setSelectedPaths(getSelectPath(true));
        rcv_dirList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new DirRcvAdapter(this);
        adapter.setListener(new DirRcvAdapter.onItemClickListener() {
            @Override
            public void onItemClicked(String bucketId) {
                titleBar.callMenuCheck();
                gallery.setBucketId(bucketId);

            }
        });
        rcv_dirList.setAdapter(adapter);
    }

    private void showDirList(){
        GalleryUtils.initLoaderManager(this, AlbumLoader.LOADER_ID, new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                return new AlbumLoader(GalleryActivity.this);
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                adapter.swapCursor(data);
                fl_pop.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                adapter.swapCursor(null);
            }
        });
    }

    private void hideDirList(){
        adapter.swapCursor(null);
        fl_pop.setVisibility(View.GONE);
    }

    private List<String> getSelectPath(boolean isPic) {
        CameraGalleryUtils cgUtils = CameraGalleryUtils.getInstance();
        for (ReportResModel file : cgUtils.getThumbList()) {
            if (selectedPath.contains(file.getResFile().getAbsolutePath()))
                continue;
            if (isPic) {
                if (MimeType.isPic(cgUtils.getFileMimeType(file.getResFile().getPath())))
                    selectedPath.add(file.getResFile().getAbsolutePath());
            } else {
                if (MimeType.isVideo(cgUtils.getFileMimeType(file.getResFile().getPath())))
                    selectedPath.add(file.getResFile().getAbsolutePath());
            }
        }
        return selectedPath;
    }

    @Override
    protected void initData() {
        gallery.postDelayed(new Runnable() {
            @Override
            public void run() {
                gallery.checkTab(0);
            }
        }, 100l);
    }

    @Override
    protected void onDestroy() {
        GalleryUtils.destoryLoaderManager(MediaLoader.LOADER_ID,AlbumLoader.LOADER_ID);
        super.onDestroy();
    }
}
