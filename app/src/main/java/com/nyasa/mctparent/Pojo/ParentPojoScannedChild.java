package com.nyasa.mctparent.Pojo;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ParentPojoScannedChild {

    @SerializedName("status")
    public
    String status;

  /*  @SerializedName("message")
    String msg;*/

    @SerializedName("message")
    ArrayList<ChildPojoScannedChild> objProfile;




    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<ChildPojoScannedChild> getObjProfile() {
        return objProfile;
    }

    public void setObjProfile(ArrayList<ChildPojoScannedChild> objProfile) {
        this.objProfile = objProfile;
    }
}
