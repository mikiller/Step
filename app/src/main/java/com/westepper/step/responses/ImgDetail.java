package com.westepper.step.responses;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Mikiller on 2017/9/26.
 */

public class ImgDetail implements Parcelable {
    String img_url;
    int imgWidth, imgHeight;

    public ImgDetail(String url, int height, int width) {
        this.imgHeight = height;
        this.img_url = url;
        this.imgWidth = width;
    }

    protected ImgDetail(Parcel in) {
        img_url = in.readString();
        imgWidth = in.readInt();
        imgHeight = in.readInt();
    }

    public static final Creator<ImgDetail> CREATOR = new Creator<ImgDetail>() {
        @Override
        public ImgDetail createFromParcel(Parcel in) {
            return new ImgDetail(in);
        }

        @Override
        public ImgDetail[] newArray(int size) {
            return new ImgDetail[size];
        }
    };

    public int getImgHeight() {
        return imgHeight;
    }

    public void setImgHeight(int height) {
        this.imgHeight = height;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public int getImgWidth() {
        return imgWidth;
    }

    public void setImgWidth(int width) {
        this.imgWidth = width;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(img_url);
        dest.writeInt(imgWidth);
        dest.writeInt(imgHeight);
    }
}
