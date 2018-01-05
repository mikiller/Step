package com.westepper.step.responses;

import com.westepper.step.utils.MapUtils;

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
        private String city_id;
        private String created_at;

        public String getCityName(){
            String name = "";
            if (MapUtils.getInstance().getDistrictArea(city_id) != null){
                name = MapUtils.getInstance().getDistrictArea(city_id).getName();
            }
            return name;
        }

        public String getCity_id() {
            return city_id;
        }

        public void setCity_id(String city_id) {
            this.city_id = city_id;
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
