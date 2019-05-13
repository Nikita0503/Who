package com.softproject.who.model;

import com.softproject.who.model.data.AuthData;
import com.softproject.who.model.data.Userdata;

import java.util.ArrayList;

import io.reactivex.Completable;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface APIService {

    @POST("users")
    Completable singIn(@Header("Authorization") String header, @Body AuthData authData);

    @GET("users")
    Single<ArrayList<Userdata>> getUsers();
}
