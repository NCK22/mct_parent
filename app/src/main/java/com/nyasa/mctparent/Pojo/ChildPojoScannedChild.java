package com.nyasa.mctparent.Pojo;

import com.google.gson.annotations.SerializedName;

public class ChildPojoScannedChild {

    @SerializedName("scan_id")
    String scan_id;

    @SerializedName("scannedby")
    String scannedby;

    @SerializedName("scannedby_id")
    String scannedby_id;

    @SerializedName("child_mac_id")
    String child_mac_id;



    @SerializedName("dateTime")
    String dateTime;



    String found="false";

    public String getFound() {
        return found;
    }

    public void setFound(String found) {
        this.found = found;
    }

    public String getScannedby_id() {
        return scannedby_id;
    }

    public String getScan_id() {
        return scan_id;
    }

    public String getChild_mac_id() {
        return child_mac_id;
    }

    public String getScannedby() {
        return scannedby;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setScannedby_id(String scannedby_id) {
        this.scannedby_id = scannedby_id;
    }

    public void setScan_id(String scan_id) {
        this.scan_id = scan_id;
    }

    public void setScannedby(String scannedby) {
        this.scannedby = scannedby;
    }

    public void setChild_mac_id(String child_mac_id) {
        this.child_mac_id = child_mac_id;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
