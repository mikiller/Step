package com.westepper.step.utils;

import android.content.Context;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Mikiller on 2017/9/1.
 */

public class FileUtils {

    public static String getFilePath(Context context, String fileName){
        return context.getFilesDir() + File.separator + fileName;
    }

    public static void saveToLocal(String str, String filePath) {
        File file = new File(filePath);
        if(file.exists()){
            file.delete();
        }
            FileOutputStream fos = null;
            try {
                file.createNewFile();
                fos = new FileOutputStream(file);
                fos.write(str.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {

                try {
                    if (fos != null)
                        fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


    }

    public static <T> T getDataFromLocal(String filePath, Class<?> T) {
        T target = null;
        FileInputStream fis = null;
        File file = new File(filePath);
        if (!file.exists()) {
            return target;
        }
        try {
            fis = new FileInputStream(file);
            byte[] str = new byte[fis.available()];
            fis.read(str);
            String areaStr = new String(str);
            target = (T) new Gson().fromJson(areaStr, T);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null)
                    fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return target;
        }
    }
}
