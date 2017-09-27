package com.westepper.step.responses;

import android.os.Parcel;
import android.os.Parcelable;

import com.amap.api.maps.model.LatLng;

/**
 * Created by Mikiller on 2017/9/27.
 */

public class UserPos implements Parcelable {

    private String poiTitle;
    private LatLng latlng;

    public UserPos(LatLng latlng, String poiTitle) {
        this.latlng = latlng;
        this.poiTitle = poiTitle;
    }

    protected UserPos(Parcel in) {
        poiTitle = in.readString();
        latlng = in.readParcelable(LatLng.class.getClassLoader());
    }

    public static final Creator<UserPos> CREATOR = new Creator<UserPos>() {
        @Override
        public UserPos createFromParcel(Parcel in) {
            return new UserPos(in);
        }

        @Override
        public UserPos[] newArray(int size) {
            return new UserPos[size];
        }
    };

    public LatLng getLatlng() {
        return latlng;
    }

    public void setLatlng(LatLng latlng) {
        this.latlng = latlng;
    }

    public String getPoiTitle() {
        return poiTitle;
    }

    public void setPoiTitle(String poiTitle) {
        this.poiTitle = poiTitle;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(poiTitle);
        dest.writeParcelable(latlng, flags);
    }
}
