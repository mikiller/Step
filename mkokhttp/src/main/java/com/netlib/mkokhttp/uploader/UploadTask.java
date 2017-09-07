package com.netlib.mkokhttp.uploader;

import com.netlib.mkokhttp.request.OkHttpRequest;
import com.netlib.mkokhttp.uploader.internal.PriorityAsyncTask;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Mikiller on 2017/6/5.
 */

public class UploadTask<T> extends PriorityAsyncTask<Void, UploadInfo, UploadInfo> {

    private UploadInfo mUploadInfo;                  //当前任务的信息
    private File file;
    private UploadListener listener;
    private OkHttpRequest request;
    private boolean pause = true;
    Timer timer;
    TimerTask timerTask;

    public UploadTask(UploadInfo uploadInfo, OkHttpRequest request, UploadListener<T> uploadListener) {
        mUploadInfo = uploadInfo;
        this.request = request;
        listener = uploadListener;
        pause = uploadInfo.getState() != UploadManager.UPLOADING;
        //将当前任务在定义的线程池中执行
        executeOnExecutor(UploadThreadPool.getInstance().getExecutor());
    }

    public UploadTask(UploadInfo uploadInfo, File file, UploadListener<T> uploadListener) {
        mUploadInfo = uploadInfo;
        this.file = file;
        listener = uploadListener;
        pause = uploadInfo.getState() != UploadManager.UPLOADING;
        //将当前任务在定义的线程池中执行
        executeOnExecutor(UploadThreadPool.getInstance().getExecutor());
    }

    public void execute(){
        executeOnExecutor(UploadThreadPool.getInstance().getExecutor());
    }

    /** 每个任务进队列的时候，都会执行该方法 */
    @Override
    protected void onPreExecute() {
        if (listener != null) listener.onAdd(mUploadInfo);

        mUploadInfo.setNetworkSpeed(0);
    }

    @Override
    protected UploadInfo doInBackground(Void... params) {
        listener.doInBackground(mUploadInfo, params);
        //call net work to upload file
        //get progress and update mUploadInfo
//        OkHttpManager mgr = OkHttpManager.getInstance();
//        mgr.uploadFile(mUploadInfo.getUrl(), file, new Callback() {
//            @Override
//            public Object parseNetworkResponse(Response response, int id) throws Exception {
//                Log.e("upload task", response.message());
//                return null;
//            }
//
//            @Override
//            public void onError(Call call, Exception e, int id) {
//                Log.e("upload task", call.request().url() + "\n " + e.getMessage());
//            }
//
//            @Override
//            public void onCancel(Call call, int id) {
//
//            }
//
//            @Override
//            public void onResponse(Object response, int id) {
//
//            }
//        });
//        timer = new Timer();
//        timerTask = new TimerTask() {
//            int i = mUploadInfo.getProgress();
//            @Override
//            public void run() {
//                if(pause){
//                    synchronized (this){
//                        try {
//                            this.wait();
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//                if(i < 100) {
//                    mUploadInfo.setProgress(i+=10);
//                    if(listener != null)
//                        listener.onProgress(mUploadInfo);
//                }else{
//                    cancel();
//                }
//            }
//        };
//        timer.schedule(timerTask, 1000, 1000);
        return mUploadInfo;
    }

    public UploadInfo getUploadInfo(){
        return mUploadInfo;
    }

    public int getTaskState(){
        return mUploadInfo.getState();
    }

    public void pause(){
        pause = true;
        mUploadInfo.setState(UploadManager.PAUSE);
    }

    public void start(){
        pause = false;
        synchronized (timerTask) {
            timerTask.notify();
            mUploadInfo.setState(UploadManager.UPLOADING);
        }
    }

    public void cancel(){
        if(timer != null) {
            timer.cancel();
            timer.purge();
        }
    }

    @Override
    protected void onProgressUpdate(UploadInfo... values) {
        if(listener != null)
            listener.onProgress(mUploadInfo);
    }

}
