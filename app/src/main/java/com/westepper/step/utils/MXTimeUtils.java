package com.westepper.step.utils;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Mikiller on 2017/10/31.
 */

public class MXTimeUtils {
    public static String getDiffTime(long time){
        long nowTime = System.currentTimeMillis();
        long dt = nowTime - time;
        int days = (int) (dt / 86400000);
        int hours = (int) ((dt - days * 86400000)/3600000);
        int minutes = (int) ((dt - days * 86400000 - hours * 3600000)/60000);
        if(days > 0){
            if(days >= 30){
                return getFormatTime("yyyy年MM月dd日", time);
            }
            return days + "天前";
        }else if(hours > 0){
            return hours + "小时前";
        }else{
            return minutes + "分钟前";
        }

    }

    public static String getFormatTime(String format, long time){
        return new SimpleDateFormat(format).format(new Date(time));
    }
}
