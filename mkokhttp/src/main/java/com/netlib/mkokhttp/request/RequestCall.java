package com.netlib.mkokhttp.request;

import com.netlib.mkokhttp.utils.OkHttpUtils;
import com.netlib.mkokhttp.callback.Callback;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by zhy on 15/12/15.
 * 对OkHttpRequest的封装，对外提供更多的接口：cancel(),readTimeOut()...
 */
public class RequestCall
{
    private OkHttpRequest okHttpRequest;
    private Request request;
    private Call call;

    private long readTimeOut;
    private long writeTimeOut;
    private long connTimeOut;

    private OkHttpClient clone;

    public RequestCall(OkHttpRequest request) {
        this.okHttpRequest = request;
    }

    public RequestCall readTimeOut(long readTimeOut) {
        this.readTimeOut = readTimeOut;
        return this;
    }

    public RequestCall writeTimeOut(long writeTimeOut) {
        this.writeTimeOut = writeTimeOut;
        return this;
    }

    public RequestCall connTimeOut(long connTimeOut) {
        this.connTimeOut = connTimeOut;
        return this;
    }

    public boolean buildCall(Callback callback) {
        request = generateRequest(callback);
        if (request == null) {
            call = null;
            return false;
        }

        if (readTimeOut > 0 || writeTimeOut > 0 || connTimeOut > 0) {
            readTimeOut = readTimeOut > 0 ? readTimeOut : OkHttpUtils.DEFAULT_MILLISECONDS;
            writeTimeOut = writeTimeOut > 0 ? writeTimeOut : OkHttpUtils.DEFAULT_MILLISECONDS;
            connTimeOut = connTimeOut > 0 ? connTimeOut : OkHttpUtils.DEFAULT_MILLISECONDS;

            clone = OkHttpUtils.getInstance().getOkHttpClient().newBuilder()
                    .readTimeout(readTimeOut, TimeUnit.MILLISECONDS)
                    .writeTimeout(writeTimeOut, TimeUnit.MILLISECONDS)
                    .connectTimeout(connTimeOut, TimeUnit.MILLISECONDS)
                    .build();

            call = clone.newCall(request);
        } else {
            call = OkHttpUtils.getInstance().getOkHttpClient().newCall(request);
        }
        return true;
    }

    private Request generateRequest(Callback callback) {
        return okHttpRequest.generateRequest(callback);
    }

    public void execute(Callback callback) {
        if (buildCall(callback)) {
            if (callback == null)
                callback = Callback.CALLBACK_DEFAULT;
            callback.onBefore(request, getOkHttpRequest().getId());
            final Callback finalCallback = callback;
            final int id = okHttpRequest.getId();

            call.enqueue(new okhttp3.Callback()
            {
                @Override
                public void onFailure(Call call, final IOException e)
                {
                    OkHttpUtils.getInstance().sendFailResultCallback(call,
                            new IOException("网络无连接"),
                            finalCallback,
                            id);
                }

                @Override
                public void onResponse(final Call call, final Response response)
                {
                    try
                    {
                        if (call.isCanceled())
                        {
                            OkHttpUtils.getInstance().sendCancelResultCallback(call, finalCallback, id);
                            return;
                        }

                        if (!finalCallback.validateReponse(response, id))
                        {
                            OkHttpUtils.getInstance().sendFailResultCallback(call, new IOException("request failed , reponse's code is : " + response.code()), finalCallback, id);
                            return;
                        }

                        Object o = finalCallback.parseNetworkResponse(response, id);
                        if(o != null)
                            OkHttpUtils.getInstance().sendSuccessResultCallback(o, finalCallback, id);
                    } catch (Exception e)
                    {
                        OkHttpUtils.getInstance().sendFailResultCallback(call, e, finalCallback, id);
                    } finally
                    {
                        if (response.body() != null)
                            response.body().close();
                    }

                }
            });
        }
    }

    public Call getCall() {
        return call;
    }

    public Request getRequest() {
        return request;
    }

    public OkHttpRequest getOkHttpRequest() {
        return okHttpRequest;
    }

    public Response execute() throws IOException {
        buildCall(null);
        return call.execute();
    }

    public void cancel() {
        if (call != null) {
            call.cancel();
        }
    }

}
