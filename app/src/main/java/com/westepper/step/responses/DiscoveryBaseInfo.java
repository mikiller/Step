package com.westepper.step.responses;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by Mikiller on 2017/11/8.
 */

public class DiscoveryBaseInfo implements Serializable {
    private List<MyDiscovery> discoverCityList;
    private List<MyDiscovery> L1;
    private List<MyDiscovery> L2;
    private List<MyDiscovery> L3;
    private List<L2Progress> L2percent;

    public List<MyDiscovery> getDiscoverCityList() {
        return discoverCityList;
    }

    public void setDiscoverCityList(List<MyDiscovery> discoverCityList) {
        this.discoverCityList = discoverCityList;
    }

    public List<MyDiscovery> getL1() {
        return L1;
    }

    public void setL1(List<MyDiscovery> l1) {
        L1 = l1;
    }

    public List<MyDiscovery> getL2() {
        return L2;
    }

    public void setL2(List<MyDiscovery> l2) {
        L2 = l2;
    }

    public List<L2Progress> getL2percent() {
        return L2percent;
    }

    public void setL2percent(List<L2Progress> l2percent) {
        L2percent = l2percent;
    }

    public List<MyDiscovery> getL3() {
        return L3;
    }

    public void setL3(List<MyDiscovery> l3) {
        L3 = l3;
    }

//    public static class DiscoveryCity extends BaseInfo{
//        private String city_name;
//
//        public String getCity_name() {
//            return city_name;
//        }
//
//        public void setCity_name(String city_name) {
//            this.city_name = city_name;
//        }
//    }
}
