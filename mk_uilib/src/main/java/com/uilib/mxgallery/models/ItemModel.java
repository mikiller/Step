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
package com.uilib.mxgallery.models;

import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.Nullable;

public class ItemModel implements Parcelable {
    public static final Creator<ItemModel> CREATOR = new Creator<ItemModel>() {
        @Override
        @Nullable
        public ItemModel createFromParcel(Parcel source) {
            return new ItemModel(source);
        }

        @Override
        public ItemModel[] newArray(int size) {
            return new ItemModel[size];
        }
    };
    public static final long ITEM_ID_CAPTURE = -1;
    public static final String ITEM_DISPLAY_NAME_CAPTURE = "Capture";
    public final long id;
    public final String path;
    public final String mimeType;
    public final Uri uri;
    public final long size;
    public final long duration; // only for video, in ms

    private ItemModel(long id, String path, String mimeType, long size, long duration) {
        this.id = id;
        this.path = path;
        this.mimeType = mimeType;
        Uri contentUri;
        if (MimeType.isPic(mimeType)) {
            contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        } else if (MimeType.isVideo(mimeType)) {
            contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        } else {
            // ?
            contentUri = MediaStore.Files.getContentUri("external");
        }
        this.uri = ContentUris.withAppendedId(contentUri, id);
        this.size = size;
        this.duration = duration;
    }

    private ItemModel(Parcel source) {
        id = source.readLong();
        path = source.readString();
        mimeType = source.readString();
        uri = source.readParcelable(Uri.class.getClassLoader());
        size = source.readLong();
        duration = source.readLong();
    }

    public static ItemModel valueOf(Cursor cursor) {
        return new ItemModel(cursor.getLong(cursor.getColumnIndex(MediaStore.Files.FileColumns._ID)),
                cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA)),
                cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.MIME_TYPE)),
                cursor.getLong(cursor.getColumnIndex(MediaStore.MediaColumns.SIZE)),
                cursor.getLong(cursor.getColumnIndex("duration")));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(path);
        dest.writeString(mimeType);
        dest.writeParcelable(uri, 0);
        dest.writeLong(size);
        dest.writeLong(duration);
    }

    public Uri getContentUri() {
        return uri;
    }

    public String getPath(){
        return path;
    }

    public boolean isCapture() {
        return id == ITEM_ID_CAPTURE;
    }

//    public boolean isImage() {
//        return mimeType.equals(MimeType.JPEG.toString())
//                || mimeType.equals(MimeType.PNG.toString())
//                || mimeType.equals(MimeType.GIF.toString())
//                || mimeType.equals(MimeType.BMP.toString())
//                || mimeType.equals(MimeType.WEBP.toString());
//    }
//
//    public boolean isGif() {
//        return mimeType.equals(MimeType.GIF.toString());
//    }
//
//    public boolean isVideo() {
//        return mimeType.equals(MimeType.MPEG.toString())
//                || mimeType.equals(MimeType.MP4.toString())
//                || mimeType.equals(MimeType.QUICKTIME.toString())
//                || mimeType.equals(MimeType.THREEGPP.toString())
//                || mimeType.equals(MimeType.THREEGPP2.toString())
//                || mimeType.equals(MimeType.MKV.toString())
//                || mimeType.equals(MimeType.WEBM.toString())
//                || mimeType.equals(MimeType.TS.toString())
//                || mimeType.equals(MimeType.AVI.toString());
//    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ItemModel)) {
            return false;
        }

        ItemModel other = (ItemModel) obj;
        return other.uri.equals(uri);
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + Long.valueOf(id).hashCode();
        result = 31 * result + mimeType.hashCode();
        result = 31 * result + uri.hashCode();
        result = 31 * result + Long.valueOf(size).hashCode();
        result = 31 * result + Long.valueOf(duration).hashCode();
        return result;
    }
}