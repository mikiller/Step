package com.uilib.mxselectreslayout;

import android.util.Log;

import com.uilib.uploadimageview.MXProgressImageView;

import java.io.Serializable;

/**
 * Created by Mikiller on 2016/8/17.
 */
public class ResItemData implements Serializable{
    String imgPath;
    MXProgressImageView.ImageState uploadState;
    int progress;

    public ResItemData() {
    }

    public ResItemData(String imgPath, int progress, MXProgressImageView.ImageState uploadState) {
        this.imgPath = imgPath;
        this.progress = progress;
        this.uploadState = uploadState;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public MXProgressImageView.ImageState getUploadState() {
        return uploadState;
    }

    public void setUploadState(MXProgressImageView.ImageState uploadState) {
        this.uploadState = uploadState;
    }

    public void update(ResItemData itemData){
        setProgress(itemData.getProgress());
        setUploadState(itemData.getUploadState());
        setImgPath(itemData.getImgPath());
    }
}
