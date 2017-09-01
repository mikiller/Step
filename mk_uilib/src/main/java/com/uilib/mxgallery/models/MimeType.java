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

import android.content.ContentResolver;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import com.uilib.mxgallery.utils.GalleryUtils;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * MIME Type enumeration to restrict selectable media on the selection activity. Matisse only supports images and
 * videos.
 * <p>
 * Good example of mime types Android supports:
 * https://android.googlesource.com/platform/frameworks/base/+/refs/heads/master/media/java/android/media/MediaFile.java
 */
@SuppressWarnings("unused")
public enum MimeType {
    FILE(0X0, "file/*", new HashSet<String>(){
        private static final long serialVersionUID = -7084149762709340312L;

        {
            add("*");
        }
    }),
    // ============== images ==============
    JPEG(0x1, "image/jpeg", new HashSet<String>() {
        private static final long serialVersionUID = -7084149762709340312L;

        {
            add("jpg");
            add("jpeg");
        }
    }),
    PNG(0x2, "image/png", new HashSet<String>() {
        private static final long serialVersionUID = 1816480189651953429L;

        {
            add("png");
        }
    }),
    GIF(0x4, "image/gif", new HashSet<String>() {
        private static final long serialVersionUID = 8016009529405357624L;

        {
            add("gif");
        }
    }),
    BMP(0x8, "image/x-ms-bmp", new HashSet<String>() {
        private static final long serialVersionUID = 5777080511126172358L;

        {
            add("bmp");
        }
    }),
    WEBP(0x10, "image/webp", new HashSet<String>() {
        private static final long serialVersionUID = -6200857288259266858L;

        {
            add("webp");
        }
    }),

    // ============== videos ==============
    MPEG(0x20, "video/mpeg", new HashSet<String>() {
        private static final long serialVersionUID = 7997571284968244833L;

        {
            add("mpeg");
            add("mpg");
        }
    }),
    MP4(0x40, "video/mp4", new HashSet<String>() {
        private static final long serialVersionUID = 5811202373154006726L;

        {
            add("mp4");
            add("m4v");
        }
    }),
    QUICKTIME(0x80, "video/quicktime", new HashSet<String>() {
        private static final long serialVersionUID = 8483402338019948819L;

        {
            add("mov");
        }
    }),
    THREEGPP(0x100, "video/3gpp", new HashSet<String>() {
        private static final long serialVersionUID = -924435349411786446L;

        {
            add("3gp");
            add("3gpp");
        }
    }),
    THREEGPP2(0x200, "video/3gpp2", new HashSet<String>() {
        private static final long serialVersionUID = 977657521185331216L;

        {
            add("3g2");
            add("3gpp2");
        }
    }),
    MKV(0x400, "video/x-matroska", new HashSet<String>() {
        private static final long serialVersionUID = 4318280869746769729L;

        {
            add("mkv");
        }
    }),
    WEBM(0x800, "video/webm", new HashSet<String>() {
        private static final long serialVersionUID = 6391799202121839467L;

        {
            add("webm");
        }
    }),
    TS(0x1000, "video/mp2ts", new HashSet<String>() {
        private static final long serialVersionUID = 1816562133802428318L;

        {
            add("ts");
        }
    }),
    AVI(0x2000, "video/avi", new HashSet<String>() {
        private static final long serialVersionUID = -6472592338237557939L;

        {
            add("avi");
        }
    });

    private final long mMimeTypeId;
    private final String mMimeTypeName;
    private final Set<String> mExtensions;

    MimeType(int mimeTypeId, String mimeTypeName, Set<String> extensions) {
        mMimeTypeId = mimeTypeId;
        mMimeTypeName = mimeTypeName;
        mExtensions = extensions;
    }

    public static MimeType getMimeTypeWithTypeName(String typeName){
        if(typeName.equals(JPEG.toString()))
            return JPEG;
        else if(typeName.equals(PNG.toString()))
            return PNG;
        else if(typeName.equals(GIF.toString()))
            return GIF;
        else if(typeName.equals(BMP.toString()))
            return BMP;
        else if(typeName.equals(WEBP.toString()))
            return WEBP;
        else if(typeName.equals(MPEG.toString()))
            return MPEG;
        else if(typeName.equals(MP4.toString()))
            return MP4;
        else if(typeName.equals(QUICKTIME.toString()))
            return QUICKTIME;
        else if(typeName.equals(THREEGPP.toString()))
            return THREEGPP;
        else if(typeName.equals(THREEGPP2.toString()))
            return THREEGPP2;
        else if(typeName.equals(MKV.toString()))
            return MKV;
        else if(typeName.equals(WEBM.toString()))
            return WEBM;
        else if(typeName.equals(AVI.toString()))
            return AVI;
        else if(typeName.equals(TS.toString()))
            return TS;
        else
            return FILE;
    }

    public long getMimeTypeId(){
        return mMimeTypeId;
    }

    public static Set<MimeType> ofAll() {
        return EnumSet.allOf(MimeType.class);
    }

    public static Set<MimeType> of(MimeType type, MimeType... rest) {
        return EnumSet.of(type, rest);
    }

    public static Set<MimeType> ofImage() {
        return EnumSet.of(JPEG, PNG, GIF, BMP, WEBP);
    }

    public static boolean isPic(int mimeType){
        return mimeType == (JPEG.mMimeTypeId ^ PNG.mMimeTypeId ^ GIF.mMimeTypeId ^ BMP.mMimeTypeId ^ WEBP.mMimeTypeId);
    }

    public static boolean isPic(String mimeType) {
        return mimeType != null && (mimeType.equals(JPEG.toString())
                || mimeType.equals(PNG.toString())
                || mimeType.equals(GIF.toString())
                || mimeType.equals(BMP.toString())
                || mimeType.equals(WEBP.toString()));
    }

    public static boolean isGif(String mimeType) {
        return mimeType.equals(GIF.toString());
    }

    public static Set<MimeType> ofVideo() {
        return EnumSet.of(MPEG, MP4, QUICKTIME, THREEGPP, THREEGPP2, MKV, WEBM, TS, AVI);
    }

    public static boolean isVideo(int mimeType){
        return mimeType == (MPEG.mMimeTypeId ^ MP4.mMimeTypeId ^ QUICKTIME.mMimeTypeId ^ THREEGPP.mMimeTypeId ^ THREEGPP2.mMimeTypeId ^ MKV.mMimeTypeId ^ WEBM.mMimeTypeId ^ TS.mMimeTypeId ^ AVI.mMimeTypeId);
    }

    public static boolean isVideo(String mimeType) {
        return mimeType != null && (mimeType.equals(MPEG.toString())
                || mimeType.equals(MP4.toString())
                || mimeType.equals(QUICKTIME.toString())
                || mimeType.equals(THREEGPP.toString())
                || mimeType.equals(THREEGPP2.toString())
                || mimeType.equals(MKV.toString())
                || mimeType.equals(WEBM.toString())
                || mimeType.equals(TS.toString())
                || mimeType.equals(AVI.toString()));
    }

    @Override
    public String toString() {
        return mMimeTypeName;
    }

    public boolean checkType(ContentResolver resolver, Uri uri) {
        MimeTypeMap map = MimeTypeMap.getSingleton();
        if (uri == null) {
            return false;
        }
        String type = map.getExtensionFromMimeType(resolver.getType(uri));
        for (String extension : mExtensions) {
            if (extension.equals(type)) {
                return true;
            }
            String path = GalleryUtils.getPath(resolver, uri);
            if (path != null && path.toLowerCase(Locale.US).endsWith(extension)) {
                return true;
            }
        }
        return false;
    }
}
