package com.nyasa.mctparent.Interface;

import com.nyasa.mctparent.Pojo.ParentPojoLocation;
import com.nyasa.mctparent.Pojo.ParentPojoStudProf;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface getDriverLocInterface {

    String user_id="";
   /* @GET("Parents/list/{parent_id}")
    Call<ParentPojoLogin> doGetListResources(@Path("parent_id") String parent_id);
*/

   @FormUrlEncoded
   @POST("Location/driverLocation/")
   Call<ParentPojoLocation> doGetListResources(@Field("driver_id") String driver_id);

}
