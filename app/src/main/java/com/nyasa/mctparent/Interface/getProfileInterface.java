package com.nyasa.mctparent.Interface;

import com.nyasa.mctparent.Pojo.ParentPojoLogin;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface getProfileInterface {

    String user_id="";
   /* @GET("Parents/list/{parent_id}")
    Call<ParentPojoLogin> doGetListResources(@Path("parent_id") String parent_id);
*/

   @FormUrlEncoded
   @POST("Parents/profile/")
   Call<ParentPojoLogin> doGetListResources(@Field("parent_id") String username);


}
