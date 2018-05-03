package com.recyclerviewapp.rest;

import com.recyclerviewapp.Helper.AppConstant;
import com.recyclerviewapp.Model.Model;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiInterface {

    @GET(AppConstant.URL)
    Call<List<Model>> getSongList();


    @GET("movie/{id}/list")
    Call<Model> getMovieDetails(@Path("id") String id);

    public void registration(@Field("name") String name,
                             @Field("email") String email,
                             @Field("password") String password,
                             @Field("logintype") String logintype,
                             Callback<Model> callback);

    @POST(AppConstant.URL)
    Call<Model> CreateUser(@Body Model user);


   // @Headers("Cache-Control: max-age=640000")
    @Headers({
            "Accept: application/vnd.github.v3.full+json",
            "User-Agent: Retrofit-Sample-App"
    })
    @POST(AppConstant.URL)
    Call<Model> CreateUser1(@Body Model user);

    @GET(AppConstant.URL)
    Call<Model> GetXWallActivities(@Header("token") String token, @Query("versionNumber") int VersionCode);


    @POST(AppConstant.URL)
    Call<Model> GetActUsers(@Header("token") String token, @Body Model homeModel);

    @POST(AppConstant.URL)
    Call<Model> GetPharmacyList(@Body Model pharmacyModel);

}
