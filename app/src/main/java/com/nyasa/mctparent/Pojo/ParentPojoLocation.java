package com.nyasa.mctparent.Pojo;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ParentPojoLocation {

    @SerializedName("status")
    public
    String status;

  /*  @SerializedName("message")
    String msg;*/

    @SerializedName("message")
    ArrayList<ChildPojoLocation> objProfile;




    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public ArrayList<ChildPojoLocation> getObjProfile() {
        return objProfile;
    }

    public void setObjProfile(ArrayList<ChildPojoLocation> objProfile) {
        this.objProfile = objProfile;
    }
}
