package com.westepper.step.responses;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mikiller on 2017/12/28.
 */

public class AreaRelation implements Parcelable {
    private List<DisArea> L1 = new ArrayList<>();
    private List<DisArea> L2 = new ArrayList<>();
    private List<DisArea> L3 = new ArrayList<>();

    protected AreaRelation(Parcel in) {
        L1 = in.createTypedArrayList(DisArea.CREATOR);
        L2 = in.createTypedArrayList(DisArea.CREATOR);
        L3 = in.createTypedArrayList(DisArea.CREATOR);
    }

    public static final Creator<AreaRelation> CREATOR = new Creator<AreaRelation>() {
        @Override
        public AreaRelation createFromParcel(Parcel in) {
            return new AreaRelation(in);
        }

        @Override
        public AreaRelation[] newArray(int size) {
            return new AreaRelation[size];
        }
    };

    public List<DisArea> getL1() {
        return L1;
    }

    public void setL1(List<DisArea> l1) {
        L1 = l1;
    }

    public List<DisArea> getL2() {
        return L2;
    }

    public void setL2(List<DisArea> l2) {
        L2 = l2;
    }

    public List<DisArea> getL3() {
        return L3;
    }

    public void setL3(List<DisArea> l3) {
        L3 = l3;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(L1);
        dest.writeTypedList(L2);
        dest.writeTypedList(L3);
    }
}
