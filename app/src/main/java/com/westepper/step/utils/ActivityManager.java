package com.westepper.step.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.view.View;
import android.view.inputmethod.InputMethodManager;


import com.westepper.step.base.SuperActivity;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by Mikiller on 2017/3/14.
 */

public class ActivityManager {
    public static Class<? extends SuperActivity> currentActivity;
    public static SuperActivity lastActivity;

    public static void startActivity(Activity act, Class<? extends SuperActivity> act1){
        Intent intent = new Intent(act, act1);
        //act.getApplicationContext().startActivity(intent);
        act.startActivity(intent);
        lastActivity = (SuperActivity) act;
        currentActivity = act1;
    }

    public static void startActivity(Activity act, Class<? extends SuperActivity> act1, Map args){
        Intent intent = new Intent(act, act1);
        if(args != null){
            for(Object key : args.keySet()){
                if(args.get(key) instanceof Serializable){
                    intent.putExtra((String) key, (Serializable)args.get(key));
                }else if(args.get(key) instanceof Parcelable){
                    intent.putExtra((String) key, (Parcelable) args.get(key));
                }else{
                    intent.putExtra((String) key, String.valueOf(args.get(key)));
                }
            }
        }
        //act.getApplicationContext().startActivity(intent);
        act.startActivity(intent);
        lastActivity = (SuperActivity) act;
        currentActivity = act1;
    }

    public static void startActivityforResult(Activity act, Class<? extends SuperActivity> act1, int requestCode, Map<String, Object> args){
        Intent intent = new Intent(act, act1);
        if(args != null){
            for(String key : args.keySet()){
                if(args.get(key) instanceof Serializable){
                    intent.putExtra(key, (Serializable)args.get(key));
                }else if(args.get(key) instanceof Parcelable){
                    intent.putExtra(key, (Parcelable) args.get(key));
                } else{
                    intent.putExtra(key, String.valueOf(args.get(key)));
                }
            }
        }
        act.startActivityForResult(intent, requestCode);
        lastActivity = (SuperActivity) act;
        currentActivity = act1;
    }

    public static void startActivityWithTransAnim(Activity act, Class<? extends SuperActivity> act1, Map<String, View> transViews, Map<String, Object> args){
        Intent intent = new Intent(act, act1);
        if(args != null){
            for(String key : args.keySet()){
                if(args.get(key) instanceof Serializable){
                    intent.putExtra(key, (Serializable)args.get(key));
                }else if(args.get(key) instanceof Parcelable){
                    intent.putExtra(key, (Parcelable) args.get(key));
                } else{
                    intent.putExtra(key, String.valueOf(args.get(key)));
                }
            }
        }
        Pair[] transPairs = new Pair[transViews.size()];
        int i = 0;
        for(String key : transViews.keySet()){
            transPairs[i++] = Pair.create(transViews.get(key), key);
        }
        ActivityOptionsCompat optCmpt = ActivityOptionsCompat.makeSceneTransitionAnimation(act, transPairs);
        ActivityCompat.startActivity(act, intent, optCmpt.toBundle());
    }

}
