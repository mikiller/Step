package com.mkframe.step.utils;

import android.app.Activity;
import android.content.Intent;


import com.mkframe.step.base.SuperActivity;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by Mikiller on 2017/3/14.
 */

public class ActivityManager {
    public static Class<? extends SuperActivity> currentActivity;
    public static Activity lastActivity;

    public static void startActivity(Activity act, Class<? extends SuperActivity> act1){
        Intent intent = new Intent(act, act1);
        //act.getApplicationContext().startActivity(intent);
        act.startActivity(intent);
        lastActivity = act;
        currentActivity = act1;
    }

    public static void startActivity(Activity act, Class<? extends SuperActivity> act1, Map<String, Object> args){
        Intent intent = new Intent(act, act1);
        if(args != null){
            for(String key : args.keySet()){
                if(args.get(key) instanceof Serializable){
                    intent.putExtra(key, (Serializable)args.get(key));
                }else{
                    intent.putExtra(key, String.valueOf(args.get(key)));
                }
            }
        }
        //act.getApplicationContext().startActivity(intent);
        act.startActivity(intent);
        lastActivity = act;
        currentActivity = act1;
    }

    public static void startActivityforResult(Activity act, Class<? extends SuperActivity> act1, int requestCode, Map<String, Object> args){
        Intent intent = new Intent(act, act1);
        if(args != null){
            for(String key : args.keySet()){
                if(args.get(key) instanceof Serializable){
                    intent.putExtra(key, (Serializable)args.get(key));
                }else{
                    intent.putExtra(key, String.valueOf(args.get(key)));
                }
            }
        }
        act.startActivityForResult(intent, requestCode);
        lastActivity = act;
        currentActivity = act1;
    }

}
