package com.westepper.step.base;

import android.accounts.NetworkErrorException;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.westepper.step.R;
import com.netlib.mkokhttp.OkHttpManager;
import com.netlib.mkokhttp.callback.Callback;
import com.westepper.step.base.BaseModel;

import java.io.File;
import java.lang.reflect.Type;
import java.net.ConnectException;
import java.net.SocketException;
import java.util.LinkedHashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Mikiller on 2016/7/21.
 */
public abstract class BaseLogic<P> extends Callback<P> {
    protected final String TAG = this.getClass().getSimpleName();
//    public static final String serviceIp = "http://218.1.109.5";
    private String webServiceIp = "http://180.166.160.22/api/";
    protected Context context;

    OkHttpManager httpMgr;

    private ProgressDialog networkDlg;

    protected Type responseType;

    protected BaseModel model;
    protected Map<String, String> params;
    protected Map<String, File> files;

    protected String url;

    protected LogicCallback callback;

    protected boolean needDlg = false;

    private String networkError, networkErrorNeedLogin, parseError;

    public BaseLogic(Context context) {
        httpMgr = OkHttpManager.getInstance();
        httpMgr.init(webServiceIp);
        if (context != null) {
            this.context = context;
            networkDlg = new ProgressDialog(context,
                    ProgressDialog.THEME_HOLO_LIGHT);
            networkDlg.setMessage("正在网络交互，请稍后...");
            networkDlg.setCanceledOnTouchOutside(false);
            networkDlg.setCancelable(true);
        }
        setUrl();
        setResponseType();
        setIsNeedDlg(isNeedDlg());
        this.files = new LinkedHashMap<>();

        networkError = context.getString(R.string.network_error);
        networkErrorNeedLogin = context.getString(R.string.network_error_needlogin);
        parseError = context.getString(R.string.parse_date_error);
    }

    public BaseLogic(Context context, BaseModel model) {
        this(context);
        this.model = model;
    }

    public BaseLogic(Context context, Map<String, String> params) {
        this(context);
        this.params = params;
    }

    public void setParams(String key, String value) {
        if (params == null)
            params = new LinkedHashMap<>();
        params.put(key, value);
    }

    protected abstract void setResponseType();

    protected abstract void setUrl();

    public void setFiles(Map<String, File> files){
        this.files = files;
    }

    public void setFile(String name, File file){
        files.put(name, file);
    }

    public LogicCallback getCallback() {
        return callback;
    }

    public BaseLogic<P> setCallback(LogicCallback callback) {
        this.callback = callback;
        return this;
    }

    protected void sendRequest(OkHttpManager.RequestType requestType) {
        if (needDlg)
            showProgressDialog();
        if (model != null)
            OkHttpManager.getInstance().sendRequest(webServiceIp.concat(url), requestType, model, files, this);
        else if (params != null) {
            OkHttpManager.getInstance().sendRequest(webServiceIp.concat(url), requestType, params, files, this);
        }
    }

    public abstract void sendRequest();

    public abstract boolean isNeedDlg();

    public void setIsNeedDlg(boolean isNeedDlg) {
        needDlg = isNeedDlg;
    }

    @Override
    public P parseNetworkResponse(Response response, int id) throws Exception {
        P result = null;
        String respStr = response.body().string();
        Log.d(TAG, respStr);
        if (respStr.contains("<!DOCTYPE html")) {
            throw new NetworkErrorException(networkErrorNeedLogin);
        }
        try {
            BaseResponse<P> logicResp = parse(respStr);
            if (!logicResp.getCode().equals("0")) {
                throw new Exception(logicResp.getCode().concat(", ").concat(logicResp.getMessage()));
            } else if (logicResp.getData() == null) {
                throw new JsonParseException("-2".concat(parseError));
            } else {
                result = logicResp.getData();
                //save response to local for offline work
                //SharePreferenceUtil.getInstance().saveData(url, respStr);
            }
        } catch (JsonParseException e) {
            e.printStackTrace();
            throw new JsonParseException("-2".concat(parseError));
        } catch (SocketException e) {
            e.printStackTrace();
            throw new ConnectException("-3".concat(networkError));
        }
        return result;

    }

    @Override
    public void onError(Call call, Exception e, int id) {
        if (networkDlg != null && networkDlg.isShowing())
            networkDlg.dismiss();
        BaseResponse<P> localData = null;
        String[] err = e.getMessage().split(",");
        String code = "-9", msg;
        if(err.length == 2){
            code = err[0];
            msg = err[1];
        }else{
            msg = err[0];
        }
        if(msg.contains(networkError) || msg.contains(networkErrorNeedLogin)) {
//            try {
                //get local data for offline works
//                localData = parse((String) SharePreferenceUtil.getInstance().getData(url, "{\"code\":\"-1\", \"message\":\"暂无最新数据\"}"));
                if(context != null){
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
//                    ToastUtils.netWorkErrToast(context, e.getMessage());
                }
//            }catch (JsonSyntaxException e1){
//                ToastUtils.ErrorToast(context, e.getMessage());
//            }finally {
                if(localData != null) {
                    onFailed(code, msg, localData.getData());
                    if (callback != null)
                        callback.onFailed(code, msg, localData.getData());
//                }
            }
        }else if(context != null){
//            ToastUtils.ErrorToast(context, e.getMessage());
            onFailed(code, msg, null);
            if (callback != null)
                callback.onFailed(code, msg, null);
        }
    }

    @Override
    public void onResponse(P response, int id) {
        if (networkDlg != null && networkDlg.isShowing())
            networkDlg.dismiss();
        onSuccess(response);
        if (callback != null)
            callback.onSuccess(response);
    }

    @Override
    public void onCancel(Call call, int id) {
        if (networkDlg != null && networkDlg.isShowing())
            networkDlg.dismiss();
        BaseResponse<P> localData = parse("{\"code\":\"-1\", \"message\":\"暂无可显示数据\"}");
        onFailed("-1", "user canceled!", localData.getData());
        if (callback != null)
            callback.onFailed("-1", "user canceled!", localData.getData());
    }

    protected BaseResponse<P> parse(String json) throws JsonSyntaxException {
//        if (!json.contains("\"code\":") && !json.contains("\"message\":"))
//            json = "{\"code\":\"\",\"message\":\"\",\"data\":" + json + "}";
//        else if (json.equals("{\"code\":\"\",\"message\":\"\"}"))
//            json = json.replace("}", ",\"data\":\"\"}");
//        else if (json.contains("{\"data\":[") || json.contains("\"message\":\"\",\"data\":[")) {
//            json = json.replace("\"data\":[", "\"data\":{\"data\":[");
//            if (json.contains("],\"code\""))
//                json = json.replace("],\"code\":", "]},\"code\":");
//            else if (json.endsWith("]}"))
//                json = json.concat("}");
//        }
//        if (json.contains("null"))
//            json = json.replace("null", "\"\"");
        return OkHttpManager.getInstance().getGson().fromJson(json, responseType);
    }

    public abstract void onSuccess(P response);

    public abstract void onFailed(String code, String msg, P localData);

    @SuppressLint("NewApi")
    private void showProgressDialog() {
        try {
            if (networkDlg != null
                    && !networkDlg.isShowing()
//                    && !ViewManager.INSTANCE.getCurActivity().isFinishing()
//                    && !ViewManager.INSTANCE.getCurActivity().isDestroyed()
                    && networkDlg.getOwnerActivity() == null) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        networkDlg.show();
                        Window window = networkDlg.getWindow();
                        WindowManager.LayoutParams lp = window.getAttributes();
                        // lp.alpha = 0.0f;// 透明度
                        lp.dimAmount = 0.0f;// 黑暗度
                        window.setAttributes(lp);
                    }
                });
            }
        } catch (Exception e) {
        }

    }

    public interface LogicCallback<P> {
        void onSuccess(P response);

        void onFailed(String code, String msg, P localData);
    }
}
