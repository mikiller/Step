package com.westepper.step.responses;

/**
 * Created by Mikiller on 2017/11/8.
 */

public class L2Progress extends MyDiscovery {
    private double percent;
    private String point;

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }
}
