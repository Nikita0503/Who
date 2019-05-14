package com.softproject.who.model;

import com.softproject.who.model.data.Userdata;
import com.softproject.who.model.data.UserdataForList;

import java.util.ArrayList;

import io.reactivex.Completable;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface APIService {

    @POST("users")
    Completable sendNewUser(@Header("Authorization") String header, @Body Userdata userdata);

    @GET("users")
    Single<ArrayList<UserdataForList>> getUsers();
}
