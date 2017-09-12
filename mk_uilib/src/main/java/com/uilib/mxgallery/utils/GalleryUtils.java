package com.uilib.mxgallery.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.FileProvider;
import android.support.v4.content.Loader;
import android.webkit.MimeTypeMap;

import java.io.File;

/**
 * Created by Mikiller on 2017/5/11.
 */

public class GalleryUtils {

    private static final String SCHEME_CONTENT = "content";

    private static LoaderManager mLoaderManager;

    public static String getPath(ContentResolver resolver, Uri uri) {
        if (uri == null) {
            return null;
        }

        if (SCHEME_CONTENT.equals(uri.getScheme())) {
            Cursor cursor = null;
            try {
                cursor = resolver.query(uri, new String[]{MediaStore.Images.ImageColumns.DATA},
                        null, null, null);
                if (cursor == null || !cursor.moveToFirst()) {
                    return null;
                }
                return cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        return uri.getPath();
    }

    public static Uri getFileUri(Context mContext, File file){
        return (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) ? Uri.fromFile(file) : FileProvider.getUriForFile(mContext, mContext.getString(mContext.getResources().getIdentifier("file_provider", "string", mContext.getPackageName())), file);
    }

    public String getFileMimeType(String path){
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(path.trim()));
    }

    public static void initLoaderManager(Context context, int id, LoaderManager.LoaderCallbacks<Cursor> callback){
        if(mLoaderManager == null)
            mLoaderManager = ((FragmentActivity) context).getSupportLoaderManager();
        mLoaderManager.restartLoader(id, null, callback);
    }

    public static void destoryLoaderManager(int... ids){
        if(mLoaderManager != null) {
            for(int id : ids)
                mLoaderManager.destroyLoader(id);
            mLoaderManager = null;
        }
    }
}
