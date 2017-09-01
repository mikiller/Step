package com.uilib.videoeditor.listeners;

/**
 * Created by Mikiller on 2017/5/5.
 */

public interface EditorSliderSeekListener {
    void onSliderClicked();
    void onSliderMoved(long seekValue);
    void onSliderUp(long startSeek);
}
