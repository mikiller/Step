package com.westepper.step.responses;

import android.os.Parcel;
import android.os.Parcelable;

import com.amap.api.maps.AMap;

/**
 * Created by Mikiller on 2017/12/29.
 */

public abstract class Graphy implements Parcelable {
    protected Graphics graphics;
    private boolean isReached;

    public void setReached(boolean reached) {
        isReached = reached;
        graphics.setReached(reached);
    }

    public boolean isReached(){
        return isReached;
    }

    public void setGraphicsType(int type){
        graphics.setGraphicsType(type);
        //graphics.setReached(reached);
    }

    public abstract void createGraphics(AMap aMap, int graphicType);

    public void hide(){
        graphics.hide();
    }

    public void show(){
        graphics.show();
    }
}
