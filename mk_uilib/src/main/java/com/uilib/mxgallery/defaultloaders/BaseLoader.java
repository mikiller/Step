package com.uilib.mxgallery.defaultloaders;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.CursorLoader;

/**
 * Created by Mikiller on 2017/5/17.
 */

public abstract class BaseLoader extends CursorLoader {
    public static int LOADER_ID;
    protected static Uri QUERY_URI;
    protected static String[] PROJECTION;
    protected static String SELECTION_ALL;
    protected static String ORDER_BY;
    protected boolean mEnableCapture;

    public BaseLoader(Context context, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        super(context, uri, projection, selection, selectionArgs, sortOrder);
    }
}
