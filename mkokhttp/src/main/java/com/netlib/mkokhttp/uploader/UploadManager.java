package com.netlib.mkokhttp.uploader;

import android.util.Log;

import com.netlib.mkokhttp.request.OkHttpRequest;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class UploadManager {
    private final String TAG = this.getClass().getSimpleName();
    //定义上传状态常量
    public static final int NONE = -1, SUCCESS = 0, UPLOADING = 1, PAUSE = 2, FAILED = 3, WAITING = 4;

    private static UploadManager mInstance;         //使用单例模式
    private Map<String, UploadTask> uploadTasks;
    private int maxRunningCount = 1;
    private int runningCount = 0;

    public static UploadManager getInstance() {
        if (null == mInstance) {
            synchronized (UploadManager.class) {
                if (null == mInstance) {
                    mInstance = new UploadManager();
                }
            }
        }
        return mInstance;
    }

    private UploadManager() {
        uploadTasks = Collections.synchronizedMap(new LinkedHashMap<String, UploadTask>());
    }

    public <T> void addTask(UploadInfo info, OkHttpRequest request, UploadListener<T> listener) {
        UploadTask uploadTask = new UploadTask(info, request, listener);
        uploadTasks.put(info.getTaskKey(), uploadTask);
    }

    public <T> void addTask(UploadInfo info, File file, UploadListener<T> listener) {
        UploadTask uploadTask = new UploadTask(info, file, listener);
        uploadTasks.put(info.getTaskKey(), uploadTask);
    }

    public UploadTask getTask(String taskKey) {
        return uploadTasks.get(taskKey);
    }

    public int getTaskState(String taskKey) {
        return uploadTasks.containsKey(taskKey) ? uploadTasks.get(taskKey).getTaskState() : NONE;
    }

    public void startTask(String taskKey) {
        if (!uploadTasks.containsKey(taskKey))
            return;
        if (getTaskState(taskKey) == WAITING || getTaskState(taskKey) == PAUSE) {
            getTask(taskKey).start();
            runningCount++;
        }
    }

    public void pauseTask(String taskKey) {
        if (!uploadTasks.containsKey(taskKey))
            return;
        if(getTaskState(taskKey) == UPLOADING){
            getTask(taskKey).pause();
            runningCount--;
        }
    }

    public void deleteTask(String taskKey) {
        if (uploadTasks.containsKey(taskKey)) {
            if(getTaskState(taskKey) == UPLOADING)
                runningCount--;
            getTask(taskKey).cancel();
            uploadTasks.remove(taskKey);
        }
    }

    public void startNextTask(List<String> keyList) {
        Log.e(TAG, "max: " + maxRunningCount + ", runningcount: " + runningCount);
        if (uploadTasks.size() == 0)
            return;
        for(String key : keyList){
            if(uploadTasks.get(key).getTaskState() == WAITING){
                uploadTasks.get(key).start();
                runningCount++;
                break;
            }
        }
//        for (UploadTask task : uploadTasks.values()) {
//            if (task.getUploadInfo().getState() == WAITING) {
//                task.start();
//                runningCount++;
//                break;
//            }
//        }
    }

    public void taskSuccess(){
        runningCount--;
    }

    public List<UploadTask> getAllTask() {
        return new ArrayList<>(uploadTasks.values());
    }
}