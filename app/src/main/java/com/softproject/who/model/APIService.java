package com.softproject.who.model;

import com.softproject.who.model.data.AuthData;

import io.reactivex.Completable;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface APIService {

    @POST("users")
    Completable singIn(@Header("Authorization") String header, @Body AuthData authData);
}
