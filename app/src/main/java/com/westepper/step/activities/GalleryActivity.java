package com.westepper.step.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.uilib.mxgallery.defaultloaders.AlbumLoader;
import com.uilib.mxgallery.defaultloaders.MediaLoader;
import com.uilib.mxgallery.listeners.OnSelectItemListener;
import com.uilib.mxgallery.models.MimeType;
import com.uilib.mxgallery.utils.GalleryUtils;
import com.uilib.mxgallery.widgets.MXGallery;
import com.westepper.step.R;
import com.uilib.mxgallery.adapters.DirRcvAdapter;
import com.westepper.step.base.Constants;
import com.westepper.step.base.SuperActivity;
import com.westepper.step.customViews.TitleBar;
import com.uilib.mxgallery.models.ReportResModel;
import com.uilib.mxgallery.utils.CameraGalleryUtils;
import com.westepper.step.utils.ActivityManager;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private CameraGalleryUtils cgUtils;
    private int galleryKind, galleryType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        isMultiple = getIntent().getBooleanExtra(Constants.ISMULTIPLE, false);
        galleryType = getIntent().getIntExtra(Constants.GALLERY_TYPE, Constants.NEW_DISCOVERY);
        galleryKind = getIntent().getIntExtra(Constants.DIS_KIND, Constants.MOOD);
        savedBundle = savedInstanceState;
        super.onCreate(savedInstanceState);
        cgUtils = CameraGalleryUtils.getInstance(this);
        setContentView(R.layout.activity_gallery);
    }

    @Override
    protected void initView() {
        titleBar.setTitleListener(new TitleBar.TitleListener() {
            @Override
            protected void onBackClicked() {
                back();
            }

            @Override
            public void onMenuChecked(boolean isChecked) {
                if (isChecked)
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
                onGetImgFiles(CameraGalleryUtils.THUMB_FILE, fileList);
//                if(galleryType == Constants.CHANGE_HEADER) {
//                    Intent intent = new Intent();
//                    intent.putExtra(CameraGalleryUtils.THUMB_FILE, (Serializable) fileList);
//                    setResult(RESULT_OK, intent);
//                    back();
//                }else{
//                    Map<String, Object> args = new HashMap<String, Object>();
//                    args.put(CameraGalleryUtils.THUMB_FILE, (Serializable) fileList);
//                    args.put(Constants.DIS_KIND, galleryKind);
//                    ActivityManager.startActivity(GalleryActivity.this, NewDiscoveryActivity.class, args);
//                }
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

    private void showDirList() {
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

    private void hideDirList() {
        adapter.swapCursor(null);
        fl_pop.setVisibility(View.GONE);
    }

    private void onGetImgFiles(String key, Object value){
        if(galleryType == Constants.CHANGE_HEADER) {
            Intent intent = new Intent();
            if(value instanceof String)
                intent.putExtra(key, (String)value);
            else
                intent.putExtra(key, (Serializable)value);
            setResult(RESULT_OK, intent);
        }else{
            Map<String, Object> args = new HashMap<String, Object>();
            args.put(key, value);
            args.put(Constants.DIS_KIND, galleryKind);
            ActivityManager.startActivity(GalleryActivity.this, NewDiscoveryActivity.class, args);
        }
        back();
    }

    private List<String> getSelectPath(boolean isPic) {
//        CameraGalleryUtils cgUtils = CameraGalleryUtils.getInstance();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;
        onGetImgFiles(CameraGalleryUtils.TMP_FILE, cgUtils.tmpFile.getResFile().getPath());
    }

    @Override
    protected void onDestroy() {
        GalleryUtils.destoryLoaderManager(MediaLoader.LOADER_ID, AlbumLoader.LOADER_ID);
        super.onDestroy();
    }
}
