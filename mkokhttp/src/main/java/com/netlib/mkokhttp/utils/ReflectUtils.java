package com.netlib.mkokhttp.utils;

import android.util.Log;

import com.google.gson.Gson;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Mikiller on 2016/9/17.
 */
public class ReflectUtils {
    Gson gson;
    private ReflectUtils(){ gson = new Gson();}

    private static class ReflectUtilsFactory{
        private static ReflectUtils instance = new ReflectUtils();
    }

    public static ReflectUtils getInstance(){
        return ReflectUtilsFactory.instance;
    }

    public Map<String, String> toMap(Object paramObj){
        Map<String, String> params = new LinkedHashMap<>();
        ArrayList<Field> fieldList = new ArrayList<>();
        getAllFields(paramObj.getClass(), fieldList, -1);
        Field[] fields = fieldList.toArray(new Field[fieldList.size()]);
        for(Field field : fields){
            try {
                field.setAccessible(true);
                if(field.get(paramObj) != null) {
                    params.put(field.getName(), (field.getType().isPrimitive() || field.getType().equals(String.class)) ? String.valueOf(field.get(paramObj)) : gson.toJson(field.get(paramObj)));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return params;
    }

    public void getAllFields(Class objClass, List<Field> fields, int exceptModifier){
        Class cls;
       if(!objClass.equals(Object.class)){
            cls = objClass.getSuperclass();
           Field[] tmp = objClass.getDeclaredFields();
            for(Field field : tmp){
                if(exceptModifier != -1 && field.getModifiers() == exceptModifier){
                    continue;
                }
                if(Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers()))
                    continue;
                fields.add(field);
            }
           getAllFields(cls, fields, Modifier.PRIVATE);
        }
    }
}
