package com.westepper.step.responses;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Mikiller on 2017/9/4.
 */

public class DiscoveryList implements Serializable {
    private static final long serialVersionUID = 8461422688901691119L;

    private List<Discovery> discoveryList;

    public List<Discovery> getDiscoveryList() {
        return discoveryList;
    }

    public void setDiscoveryList(List<Discovery> discoveryList) {
        this.discoveryList = discoveryList;
    }
}
