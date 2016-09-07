package wuba.zhaobiao.respons;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by SongYongmeng on 2016/9/7.
 */
public class BusinessCityRespons implements Serializable {
    public ArrayList<City> data;

    public ArrayList<City> getData() {
        return data;
    }

    public void setData(ArrayList<City> data) {
        this.data = data;
    }

    public class City implements Serializable {
        private String cityId;
        private String cityName;
        public ArrayList<Areas> areas;

        public String getCityId() {
            return cityId;
        }

        public void setCityId(String cityId) {
            this.cityId = cityId;
        }

        public ArrayList<Areas> getAreas() {
            return areas;
        }

        public void setAreas(ArrayList<Areas> areas) {
            this.areas = areas;
        }

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }
    }

    public class Areas {
        private String areaName;
        private String areaId;

        public String getAreaName() {
            return areaName;
        }

        public void setAreaName(String areaName) {
            this.areaName = areaName;
        }

        public String getAreaId() {
            return areaId;
        }

        public void setAreaId(String areaId) {
            this.areaId = areaId;
        }
    }
}
