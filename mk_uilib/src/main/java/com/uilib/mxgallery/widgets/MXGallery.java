package com.uilib.mxgallery.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.uilib.mxgallery.listeners.OnSelectItemListener;
import com.uilib.mxgallery.models.ItemModel;
import com.uilib.mxgallery.models.MimeType;
import com.uilib.R;
import com.uilib.mxgallery.adapters.GalleryItemsAdapter;
import com.uilib.mxgallery.defaultloaders.MediaLoader;
import com.uilib.mxgallery.listeners.GalleryTabListener;
import com.uilib.mxgallery.listeners.OnBottomBtnClickListener;
import com.uilib.mxgallery.listeners.OnMediaItemClickListener;

import java.io.File;
import java.util.List;
import java.util.Set;

/**
 * Created by Mikiller on 2017/5/11.
 */

public class MXGallery extends RelativeLayout implements LoaderManager.LoaderCallbacks<Cursor> {
    private final String TAG = this.getClass().getSimpleName();

    private GalleryTabGroup tabGroup;
    private RecyclerView rcv_gallery;
    private BottomBar bottomBar;

    private GalleryItemsAdapter itemsAdapter;
    private OnSelectItemListener selectListener;
    private MediaCollection mediaCollection;
    @NonNull
    private FragmentManager fgmtMgr;
    private Loader<Cursor> contentLoader;

    private int mimeType = 0;
    private int columnNum = 4, itemMargin = 8, maxSelectionCount = 9;
    private boolean needEdge = true, isMultiple = true, needCapture = false;

    public MXGallery(Context context) {
        super(context);
        initView(context, null, 0);
    }

    public MXGallery(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs, 0);
    }

    public MXGallery(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    private void initView(final Context context, AttributeSet attrs, int defStyleAttr) {
        LayoutInflater.from(context).inflate(R.layout.layout_mxgallery, this, true);
        tabGroup = (GalleryTabGroup) findViewById(R.id.tabGroup);
        rcv_gallery = (RecyclerView) findViewById(R.id.rcv_gallery);
        bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MXGallery);
            columnNum = ta.getInt(R.styleable.MXGallery_columnNum, 4);
            itemMargin = ta.getInt(R.styleable.MXGallery_itemMargin, 8);
            maxSelectionCount = ta.getInt(R.styleable.MXGallery_maxSltCount, 9);
            needEdge = ta.getBoolean(R.styleable.MXGallery_needEdge, true);
            isMultiple = ta.getBoolean(R.styleable.MXGallery_isMultiple, true);
            needCapture = ta.getBoolean(R.styleable.MXGallery_needCapture, false);
            tabGroup.setVisibility(ta.getBoolean(R.styleable.MXGallery_needTab, true) ? VISIBLE : GONE);
            ta.recycle();
        }
        rcv_gallery.setLayoutManager(new GridLayoutManager(getContext(), columnNum));
        rcv_gallery.setHasFixedSize(true);
        rcv_gallery.addItemDecoration(new MediaItemDecoration(columnNum, itemMargin, needEdge));

        fgmtMgr = ((FragmentActivity) getContext()).getSupportFragmentManager();
    }

    public void onCreate(Bundle bundle) {
        if(bundle != null)
            mediaCollection = bundle.getParcelable(MediaCollection.SET_KEY);
        else
            mediaCollection = new MediaCollection(isMultiple, maxSelectionCount);
        itemsAdapter = new GalleryItemsAdapter(getContext(), mediaCollection, columnNum, itemMargin);
        itemsAdapter.setItemClickeListener(new OnMediaItemClickListener() {
            @Override
            public void onItemChecked(ItemModel item, boolean isChecked) {
                if (isChecked)
                    checkItem(item);
                else
                    unCheckItem(item);
                updateCollectionNum(mediaCollection.getSelectedCount());
            }
        });
        rcv_gallery.setAdapter(itemsAdapter);
        bottomBar.setBtnListener(new OnBottomBtnClickListener() {
            @Override
            public void onLeftClick() {
                openPreviewPage(null, true);
            }

            @Override
            public void onRightClick() {
                if(selectListener != null)
                    selectListener.onConfirm(mediaCollection.getModelFiles());
            }
        });
    }

    public void onSaveInstanceState(Bundle outState) {
        mediaCollection.onSaveInstanceState(outState);
    }

    public void setSelectedPaths(List<String > paths){
        mediaCollection.setSelectedModelPath(paths);
    }

    private void checkItem(ItemModel item) {
        if (mediaCollection.isMultiple) {
            mediaCollection.addMedia(item);
        } else {
            mediaCollection.removeLastMedia();
            mediaCollection.addMedia(item);
        }
    }

    private void unCheckItem(ItemModel item) {
        if (mediaCollection.isMultiple) {
            mediaCollection.removeMedia(item);
        } else
            mediaCollection.removeLastMedia();
    }

    private void updateCollectionNum(int selectedNum) {
        tabGroup.updateTab(mediaCollection.totalImage);
        bottomBar.updateBtnState(true, mediaCollection.getSelectedCount() > 0);
        bottomBar.updateBtnState(false, selectedNum > 0);
        bottomBar.updateNum(selectedNum);
    }

    private void openPreviewPage(final ItemModel item, boolean isCheck) {
        final PreviewFragment fragment = PreviewFragment.newInstance(mediaCollection, item, mimeType, isCheck);
        fragment.setOnSureListener(new PreviewFragment.OnSureListener() {
            @Override
            public void addNewMedia(ItemModel model) {
                if (mediaCollection.canSelectModel(model)) {
                    checkItem(model);
                    updateCollectionNum(mediaCollection.getSelectedCount());
                    if(MimeType.isPic(mimeType))
                        itemsAdapter.notifyDataSetChanged();
                    else if(selectListener != null) {
                        selectListener.onPreView(false);
                        selectListener.onConfirm(mediaCollection.getModelFiles());
                    }
                }
            }

            @Override
            public void confirmSelectedMedia() {
                if(selectListener != null) {
                    selectListener.onPreView(false);
                    selectListener.onConfirm(mediaCollection.getModelFiles());
                }
            }

            @Override
            public void onBack(){
                if(selectListener != null){
                    selectListener.onPreView(false);
                }
            }
        });
        fgmtMgr.beginTransaction().add(R.id.fl_preview, fragment, "preview").commitAllowingStateLoss();
        if(selectListener != null)
            selectListener.onPreView(true);
    }

    public void setIsMultiple(boolean isMultiple){
        this.isMultiple = isMultiple;
    }

    public void setTabNames(GalleryTabListener listener, String... names) {
        tabGroup.setTabNames(listener, names);
    }

    public void setMimeType(Set<MimeType> mimeType) {
        for (MimeType type : mimeType) {
            this.mimeType ^= type.getMimeTypeId();
        }
        bottomBar.setVisibility(MimeType.isPic(this.mimeType) ? VISIBLE : GONE);
    }

    public void setContentLoader(Loader<Cursor> loader) {
        contentLoader = loader;
    }

    public void setMaxSelectionCount(int max) {
        mediaCollection.setMaxSelectionCount(max);
    }

    public void setSelectListener(OnSelectItemListener selectListener) {
        this.selectListener = selectListener;
    }

    public void checkTab(int tab){
        tabGroup.checkTab(tab);
    }

    public void updateItems(boolean isNeedShowSelected) {
        mediaCollection.isNeedShowSelected = isNeedShowSelected;
        tabGroup.initLoaderManager(this);
    }

    public int getSelectedItemCount() {
        return mediaCollection.getSelectedCount();
    }

    public boolean onBackClicked() {
        return fgmtMgr == null ? true : !(fgmtMgr.getFragments().size() > 0);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        tabGroup.initLoaderManager(this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        int type = MediaStore.Files.FileColumns.MEDIA_TYPE_NONE;
        if (MimeType.isPic(mimeType))
            type = MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
        else if (MimeType.isVideo(mimeType))
            type = MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;
        return contentLoader == null ? MediaLoader.newInstance(getContext(),
                type,
                needCapture,
                mediaCollection.isNeedShowSelected ? null : mediaCollection.getSelectedModelPath()) : contentLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (mediaCollection.isNeedShowSelected)
            mediaCollection.totalImage = data.getCount();
        updateCollectionNum(mediaCollection.getSelectedCount());
        itemsAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        itemsAdapter.swapCursor(null);
    }
}
