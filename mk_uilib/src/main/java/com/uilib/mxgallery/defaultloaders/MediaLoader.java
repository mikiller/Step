/*
 * Copyright (C) 2014 nohana, Inc.
 * Copyright 2017 Zhihu Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an &quot;AS IS&quot; BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.uilib.mxgallery.defaultloaders;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;

import com.uilib.mxgallery.models.Album;

import java.util.ArrayList;


/**
 * Load images and videos into a single cursor.
 */
public class MediaLoader extends BaseLoader {
    static{
        LOADER_ID = 1;
        QUERY_URI = MediaStore.Files.getContentUri("external");
        PROJECTION = new String[]{
                MediaStore.Files.FileColumns._ID,
                MediaStore.MediaColumns.DATA,
                MediaStore.MediaColumns.MIME_TYPE,
                MediaStore.MediaColumns.SIZE,
                "duration"};
        SELECTION_ALL = "(" + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                + " OR " + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                + " OR " + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?)"
                + " AND " + MediaStore.MediaColumns.SIZE + ">0";
        ORDER_BY = MediaStore.Files.FileColumns._ID + " DESC";
    }
    private static final String SELECTION_SINGLE = MediaStore.Files.FileColumns.MEDIA_TYPE + "=? AND "
            + MediaStore.MediaColumns.SIZE + ">0";

    private static final String SELECTION(int mediaType, int idCount){
        String selection = mediaType == MediaStore.Files.FileColumns.MEDIA_TYPE_NONE ? SELECTION_ALL : SELECTION_SINGLE;
        if(idCount > 0){
            selection = selection.concat(" AND ").concat(MediaStore.Files.FileColumns.DATA).concat(" not in(");
            for(int i = 0; i < idCount; i++){
                selection = selection.concat("?, ");
            }
            selection = selection.substring(0, selection.lastIndexOf(",")) + ")";
        }
        return selection;
    }

    private static final String[] SELECTION_ARGS(int meidaType, ArrayList<String> paths) {
        String[] args;
        String[] types = meidaType == 0 ?
                new String[]{String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE),
                        String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO),
                        String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_AUDIO)}
                : new String[]{String.valueOf(meidaType)};
        if(paths == null){
            args = types;
        }else{
            args = new String[paths.size() + types.length];
            for(int i = 0; i < types.length; i++){
                args[i] = types[i];
            }
            for (int i = 0; i < paths.size(); i++) {
                args[i + types.length] = String.valueOf(paths.get(i));
            }
        }
        return args;

    };

    private static final String[] SELECTION_ALL_ARGS(boolean isPic, ArrayList<Integer> ids) {
        String type = isPic ? String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE) : String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO);
        String[] args;
        if(ids == null){
            args = new String[]{type};
        } else{
            args = new String[ids.size() + 1];
            args[0] = type;
            for (int i = 0; i < ids.size(); i++) {
                args[i + 1] = String.valueOf(ids.get(i));
            }
        }
        return args;
    }

//    public static CursorLoader newInstance(Context context, int mediaType, boolean capture, ArrayList<Integer> ids){
//        return new MediaLoader(context,
//                QUERY_URI, PROJECTION,
//                SELECTION(mediaType, ids == null ? 0 : ids.size()),
//                SELECTION_ARGS(mediaType, ids),
//                ORDER_BY,
//                capture);
//    }

    public static CursorLoader newInstance(Context context, int mediaType, boolean capture, ArrayList<String> paths){
        return new MediaLoader(context,
                QUERY_URI, PROJECTION,
                SELECTION(mediaType, paths == null ? 0 : paths.size()),
                SELECTION_ARGS(mediaType, paths),
                ORDER_BY,
                capture);
    }

    private MediaLoader(Context context, Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder, boolean capture) {
        super(context, uri, projection, selection, selectionArgs, sortOrder);
        mEnableCapture = capture;
    }

    public void changeContent(){
    }

    @Override
    public Cursor loadInBackground() {
        Cursor result = super.loadInBackground();
//        if (!mEnableCapture || !MediaStoreCompat.hasCameraFeature(getContext())) {
//            return result;
//        }
//        MatrixCursor dummy = new MatrixCursor(PROJECTION);
//        dummy.addRow(new Object[]{Item.ITEM_ID_CAPTURE, Item.ITEM_DISPLAY_NAME_CAPTURE, "", 0, 0});
//        return new MergeCursor(new Cursor[]{dummy, result});
        return result;
    }

    @Override
    public void onContentChanged() {
        // FIXME a dirty way to fix loading multiple times
    }
}
