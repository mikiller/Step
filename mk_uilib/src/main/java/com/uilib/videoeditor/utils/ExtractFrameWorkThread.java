package com.uilib.videoeditor.utils;

import android.os.Handler;

public class ExtractFrameWorkThread extends Thread {
    public static final int MSG_SAVE_SUCCESS = 0;
    private int thumbnailsCount;
    private double newWidth, newHeight;
    private ExtractVideoInfoUtil extractVideoInfoUtil;

    public ExtractFrameWorkThread(ExtractVideoInfoUtil extractVideoInfoUtil, double newWidth, double newHeight, int thumbnailsCount) {
        this.extractVideoInfoUtil = extractVideoInfoUtil;
        this.newWidth = newWidth;
        this.newHeight = newHeight;
        this.thumbnailsCount = thumbnailsCount;
    }

    @Override
    public void run() {
        super.run();
        if(extractVideoInfoUtil != null)
            extractVideoInfoUtil.extractFrame(0, newWidth, newHeight, thumbnailsCount);
    }

}
