package com.mikiller.mkglidelib.glidemodules;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.module.GlideModule;

/**
 * Created by Mikiller on 2016/10/8.
 */
public class CustomGlideModule implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        MemorySizeCalculator calculator = new MemorySizeCalculator(context);
        int defaultMemoryCacheSize = calculator.getMemoryCacheSize();
        int defaultBitmapPoolSize = calculator.getBitmapPoolSize();
//        int customMemoryCacheSize = (int) (0.8 * defaultMemoryCacheSize);
//        int customBitmapPoolSize = (int) (0.8 * defaultBitmapPoolSize);
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        int customMemoryCacheSize = (int) (activityManager.getMemoryClass() * 0.8);
        int customBitmapPoolSize = (int) (activityManager.getMemoryClass() * 0.8);
        builder.setMemoryCache( new LruResourceCache( customMemoryCacheSize ));
        builder.setBitmapPool( new LruBitmapPool( customBitmapPoolSize ));

        String downloadDirectoryPath = context.getExternalCacheDir().getPath().concat("/Jiefang/cache");
        builder.setDiskCache( new DiskLruCacheFactory( downloadDirectoryPath, 104857600 ) );
    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}
