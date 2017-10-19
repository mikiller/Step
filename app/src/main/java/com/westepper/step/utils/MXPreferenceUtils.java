package com.westepper.step.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

/**
 * Created by Mikiller on 2017/5/26.
 */

public class MXPreferenceUtils {
    public static final String REPORTS = "step";
    private SharedPreferences sp;
    private Context mContext;

    private MXPreferenceUtils() {
    }

    private static class CREATER {
        private static MXPreferenceUtils instance = new MXPreferenceUtils();
    }

    public static MXPreferenceUtils getInstance(Context context, String name) {
        CREATER.instance.mContext = context;
        CREATER.instance.sp = context.getSharedPreferences(name, Context.MODE_APPEND);
        return CREATER.instance;
    }

    public static MXPreferenceUtils getInstance(){
        return CREATER.instance;
    }

    public void setReport(String id, String reportJson){
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(id, reportJson).commit();
    }

    public void removeReport(String id){
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(id).commit();
    }

    public String getReprot(String id){
        return sp.getString(id, "{}");
    }

    public void setString(String key, String value){
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value).commit();
    }

    public void removeString(String key){
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key).commit();
    }

    public String getString(String key){
        return sp.getString(key, "");
    }

    public Map getAll(){
        return sp.getAll();
    }
}
