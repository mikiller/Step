package com.netlib.mkokhttp.uploader;

import java.io.Serializable;

public class UploadInfo implements Serializable{

    private static final long serialVersionUID = 2296845736298945180L;
    private String url;                         //文件URL
    private String taskKey;                     //下载的标识键
    private int progress;                     //上传进度
    private long totalLength;                   //总大小
    private long uploadLength;                  //已上传大小
    private long networkSpeed;                  //上传速度
    private int state;                          //当前状态

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTaskKey() {
        return taskKey;
    }

    public void setTaskKey(String taskKey) {
        this.taskKey = taskKey;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public long getTotalLength() {
        return totalLength;
    }

    public void setTotalLength(long totalLength) {
        this.totalLength = totalLength;
    }

    public long getUploadLength() {
        return uploadLength;
    }

    public void setUploadLength(long uploadLength) {
        this.uploadLength = uploadLength;
    }

    public long getNetworkSpeed() {
        return networkSpeed;
    }

    public void setNetworkSpeed(long networkSpeed) {
        this.networkSpeed = networkSpeed;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }


    /** url 相同就认为是同一个任务 */
    @Override
    public boolean equals(Object o) {
        if(o != null && o instanceof UploadInfo){
            return ((UploadInfo) o).getTaskKey().equals(getTaskKey());
        }
//        if (o != null && o instanceof DownloadInfo) {
//            DownloadInfo info = (DownloadInfo) o;
//            return getUrl().equals(info.getUrl());
//        }
        return false;
    }
}