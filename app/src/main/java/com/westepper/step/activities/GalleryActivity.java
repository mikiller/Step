package com.westepper.step.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import com.uilib.mxgallery.listeners.GalleryTabListener;
import com.uilib.mxgallery.listeners.OnSelectItemListener;
import com.uilib.mxgallery.models.MimeType;
import com.uilib.mxgallery.widgets.GalleryTabGroup;
import com.uilib.mxgallery.widgets.MXGallery;
import com.westepper.step.R;
import com.westepper.step.base.SuperActivity;
import com.westepper.step.models.ReportResModel;
import com.westepper.step.utils.CameraGalleryUtils;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Mikiller on 2017/5/18.
 */

public class GalleryActivity extends SuperActivity {
//    @BindView(R.id.titleBar)
//    TitleBar titleBar;
    @BindView(R.id.gallery)
    MXGallery gallery;

    private String title;
    private boolean isMultiple = false;
    private Bundle savedBundle;
    private List<String> selectedPath = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //title = getIntent().getStringExtra("listType");
        isMultiple = getIntent().getBooleanExtra("isMultiple", false);
        savedBundle = savedInstanceState;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
    }

    @Override
    protected void initView() {
//        titleBar.setTitle(title);
//        titleBar.setOnLeftListener(new TitleBar.onLeftBtnClickListener() {
//            @Override
//            public void onBack() {
//                setResult(RESULT_CANCELED);
//                back();
//            }
//
//            @Override
//            public void onCreate() {
//
//            }
//
//            @Override
//            public void onChooseUser(CheckBox username) {
//
//            }
//        });
        gallery.setIsMultiple(isMultiple);
        gallery.setMimeType(MimeType.ofImage());
        gallery.setSelectListener(new OnSelectItemListener() {
            @Override
            public void onPreView(boolean isPreview) {
//                titleBar.setVisibility(isPreview ? View.GONE : View.VISIBLE);
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
//        gallery.setTabNames(new GalleryTabListener() {
//            @Override
//            public void onTabChecked(RadioButton tab, int id) {
//                gallery.updateItems(id == 1);
//            }
//
//            @Override
//            public void onTabUpdated(GalleryTabGroup galleryTab, int tabId, int itemCount) {
//                galleryTab.setTabName(tabId == 0 ? tabId : 0, String.format("未选择(%1$d)", itemCount - gallery.getSelectedItemCount() - selectedPath.size()));
//                galleryTab.setTabName(tabId == 0 ? tabId + 1 : tabId, String.format("全部(%1$d)", itemCount));
//            }
//        }, String.format("未选择(%1$d)", 0), String.format("全部(%1$d)", 0));
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


}
