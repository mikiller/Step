package com.uilib.mxgallery.widgets;

import android.content.ClipData;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;

import com.uilib.mxgallery.models.ItemModel;
import com.uilib.R;
import com.uilib.mxgallery.adapters.PreviewPagerAdapter;
import com.uilib.mxgallery.listeners.OnBottomBtnClickListener;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Mikiller on 2017/5/16.
 */

public class PreviewFragment extends Fragment {
    private final static String MODELS = "models";
    private final static String MODEL = "model";
    private final static String POS = "currentPos";
    private final static String ISSELECTED = "isSelected";
    private final static String MIMETYPE = "mimeType";
    private ViewPager vp_preview;
    private BottomBar bottomBar;
    private PreviewPagerAdapter adapter;
    private boolean isSelected;
    private OnSureListener listener;

    public static PreviewFragment newInstance(MediaCollection collection, ItemModel model, int mimeType, boolean isSelected){
        PreviewFragment fragment = new PreviewFragment();
        Bundle bundle = new Bundle();
        if(isSelected){
            bundle.putParcelableArrayList(MODELS, new ArrayList<ItemModel>(collection.modelSet));
            bundle.putInt(POS, collection.getMediaPos(model));
        }else{
            bundle.putParcelable(MODEL, model);
            bundle.putInt(POS, 0);
        }
        bundle.putInt(MIMETYPE, mimeType);
        bundle.putBoolean(ISSELECTED, isSelected);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void setOnSureListener(OnSureListener listener){
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_preview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final ArrayList<ItemModel> models = new ArrayList<>();
        isSelected = getArguments().getBoolean(ISSELECTED);
        if(!isSelected) {
            models.add((ItemModel) getArguments().getParcelable(MODEL));
        }else{
            models.addAll((ArrayList)(getArguments().getParcelableArrayList(MODELS)));
        }
        vp_preview = (ViewPager) view.findViewById(R.id.vp_preview);
        bottomBar = (BottomBar) view.findViewById(R.id.bottomBar);
        adapter = new PreviewPagerAdapter(getContext(), models, getArguments().getInt(MIMETYPE));
        vp_preview.setAdapter(adapter);
        vp_preview.setCurrentItem(getArguments().getInt(POS));
        bottomBar.updateBtnState(false, true);
        bottomBar.needNum(isSelected);
        if(isSelected)
            bottomBar.updateNum(models.size());
        else
            bottomBar.setRightText("选择");
        bottomBar.setBtnListener(new OnBottomBtnClickListener() {
            @Override
            public void onLeftClick() {
                getActivity().getSupportFragmentManager().beginTransaction().remove(PreviewFragment.this).commit();
                if(listener != null)
                    listener.onBack();
            }

            @Override
            public void onRightClick() {
                if(listener != null){
                    if (!isSelected){
                        listener.addNewMedia(models.get(0));
                    }else{
                        listener.confirmSelectedMedia();
                    }
                }
                getActivity().getSupportFragmentManager().beginTransaction().remove(PreviewFragment.this).commit();
            }
        });
    }

    @Override
    public void onDestroyView() {
        adapter.release();
        super.onDestroyView();
    }

    public interface OnSureListener{
        void onBack();
        void addNewMedia(ItemModel model);
        void confirmSelectedMedia();
    }
}
