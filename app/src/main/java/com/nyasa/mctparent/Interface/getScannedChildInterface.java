package com.nyasa.mctparent.Interface;

import com.nyasa.mctparent.Pojo.ParentPojoLogin;
import com.nyasa.mctparent.Pojo.ParentPojoScannedChild;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface getScannedChildInterface {

    String user_id="";
   /* @GET("Parents/list/{parent_id}")
    Call<ParentPojoLogin> doGetListResources(@Path("parent_id") String parent_id);
*/

   @FormUrlEncoded
   @POST("parents/getScannedChild/")
   Call<ParentPojoScannedChild> doGetListResources(@Field("mac_id") String child_mac_id);


}
