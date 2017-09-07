package com.uilib.mxgallery.listeners;

import com.uilib.mxgallery.models.ItemModel;

/**
 * Created by Mikiller on 2017/5/12.
 */

public interface OnMediaItemClickListener {
    void onItemChecked(ItemModel item, boolean isChecked);
}
