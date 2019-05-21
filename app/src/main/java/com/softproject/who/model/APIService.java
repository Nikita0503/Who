package com.softproject.who.model;

import com.softproject.who.model.data.Userdata;
import com.softproject.who.model.data.instagram.InstagramData;
import com.softproject.who.model.data.instagram.InstagramUserdata;

import org.json.JSONObject;

import java.util.ArrayList;

import io.reactivex.Completable;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIService {

    @POST("users/")
    Completable sendNewUser(@Header("Authorization") String header, @Body Userdata userdata);

    @GET("users/?ordering=-auth_date")
    Single<ArrayList<Userdata>> getUsers();

    @GET("users/?ordering=-auth_date")
    Single<ArrayList<Userdata>> getUsers(@Query("search") String text);

    @GET("users/self/?")
    Single<InstagramData> getInstagramUserdata(@Query("access_token") String accessToken);

}
