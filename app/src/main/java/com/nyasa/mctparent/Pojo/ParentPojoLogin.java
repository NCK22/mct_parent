package com.nyasa.mctparent.Pojo;

import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class ParentPojoLogin extends CommonParentPojo {


    @SerializedName("message")
    ArrayList<HashMap<String,String>> objProfile;

    public ArrayList<HashMap<String, String>> getObjProfile() {
        return objProfile;
    }

    public void setObjProfile(ArrayList<HashMap<String, String>> objProfile) {
        this.objProfile = objProfile;
    }

}
