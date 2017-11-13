package com.westepper.step.responses;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Mikiller on 2017/11/13.
 */

public class DiscoveredCities implements Serializable {
    private List<DiscoverCity> discoverCitys;

    public List<DiscoverCity> getDiscoverCitys() {
        return discoverCitys;
    }

    public void setDiscoverCitys(List<DiscoverCity> discoverCitys) {
        this.discoverCitys = discoverCitys;
    }

    public static class DiscoverCity {
        private int id;
        private String city_name;
        private String created_at;

        public String getCity_name() {
            return city_name;
        }

        public void setCity_name(String city_name) {
            this.city_name = city_name;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
