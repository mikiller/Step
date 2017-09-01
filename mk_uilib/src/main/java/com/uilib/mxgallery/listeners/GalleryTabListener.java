package com.uilib.mxgallery.listeners;

import android.widget.RadioButton;

import com.uilib.mxgallery.widgets.GalleryTabGroup;

/**
 * Created by Mikiller on 2017/5/15.
 */

public interface GalleryTabListener {
     void onTabChecked(RadioButton tab, int id);
     void onTabUpdated(GalleryTabGroup galleryTab, int tabCount, int itemCount);
}
