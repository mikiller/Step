package com.westepper.step.responses;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mikiller on 2017/8/29.
 */

public class AreaList implements Serializable {
    private static final long serialVersionUID = 1141207758735809623L;
    private List<Area> areaList = new ArrayList<>();

    public List<Area> getAreaList() {
        return areaList;
    }

    public void setAreaList(List<Area> areaList) {
        this.areaList = areaList;
    }

    public void setArea(Area area){
        areaList.add(area);
    }
}
