package com.example.admin.googleapi.interfaces;

import com.example.admin.googleapi.userinfo;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Admin on 5/21/2017.
 */

public interface API_interface {


    @FormUrlEncoded
    @POST("index.php/User/test_123")
    Call<userinfo>insertInfo(@Field("token_id") String token_id,
                             @Field("personName") String personName,
                             @Field("personEmail") String personEmail,
                             @Field("photoURL") String photoURL);


}
