package com.nyasa.mctparent.Pojo;

import com.google.gson.annotations.SerializedName;

public class ChildPojoLocation {

    @SerializedName("loc_id")
    String loc_id;

    @SerializedName("latitude")
    String latitude;

    @SerializedName("longitude")
    String longitude;

    @SerializedName("date")
    String date;

    @SerializedName("driver_id")
    String driver_id;

    @SerializedName("driverMac_id")
    String driverMac_id;

    @SerializedName("name")
    String name;

    @SerializedName("mac_id")
    String mac_id;

    @SerializedName("stauts")
    String stauts;


    public String getDate() {
        return date;
    }

    public String getDriver_id() {
        return driver_id;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLoc_id() {
        return loc_id;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getName() {
        return name;
    }

    public String getDriverMac_id() {
        return driverMac_id;
    }

    public String getStauts() {
        return stauts;
    }

    public String getMac_id() {
        return mac_id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDriver_id(String driver_id) {
        this.driver_id = driver_id;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setDriverMac_id(String driverMac_id) {
        this.driverMac_id = driverMac_id;
    }

    public void setLoc_id(String loc_id) {
        this.loc_id = loc_id;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setStauts(String stauts) {
        this.stauts = stauts;
    }

    public void setMac_id(String mac_id) {
        this.mac_id = mac_id;
    }

    public void setName(String name) {
        this.name = name;
    }
}

