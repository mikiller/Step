package com.westepper.step.utils;

import android.provider.ContactsContract;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Mikiller on 2017/10/31.
 */

public class MXTimeUtils {
    public static final long DAY = 86400000, HOURS8 = 28800000;
    public static void getNextDates(String format, String[] dates, int firstPos){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        for(int i = firstPos; i < dates.length; i++) {
            calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 1);
            dates[i] = sdf.format(calendar.getTime());
        }
    }

    public static String getDiffTime(long time){
        long nowTime = System.currentTimeMillis();
        long dt = nowTime - time;
        int days = (int) (dt / 86400000);
        int hours = (int) ((dt - days * 86400000)/3600000);
        int minutes = (int) ((dt - days * 86400000 - hours * 3600000)/60000);
        int seconds = (int)(dt - days * 86400000 - hours * 3600000 - minutes * 600000) / 1000;
        if(days > 0){
            if(days >= 30){
                return getFormatTime("yyyy年MM月dd日", time);
            }
            return days + "天前";
        }else if(hours > 0){
            return hours + "小时前";
        }else if(minutes > 0){
            return minutes + "分钟前";
        }else{
            return seconds + "秒前";
        }

    }

    public static String getFormatTime(String format, long time){
        return new SimpleDateFormat(format).format(new Date(time));
    }

    public static boolean isOutofLimit(long time, long limit){
        return (time + limit < System.currentTimeMillis());
    }

    public static String getLeftTime(long time, long limit){
        return getLeftTime("HH:mm", time, limit);
    }

    public static String getLeftTime(String format, long time, long limit){
        long dt = limit - HOURS8 + time - System.currentTimeMillis();
        return getFormatTime(format, dt);
    }

    public static long getTimeFromFormat(String format, String time){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            Date date = sdf.parse(time);
            long timeMillis = date.getTime();
            return timeMillis / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
