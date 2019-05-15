package com.softproject.who.model;

import android.util.Log;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.softproject.who.model.data.Userdata;

import java.util.ArrayList;

import io.reactivex.Completable;
import io.reactivex.Single;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIUtils {

    public static final int FACEBOOK_ID = 1;
    public static final int TWITTER_ID = 2;
    public static final int INSTAGRAM_ID = 3;
    public static final String BASE_URL = "http://34.214.73.44:8000/api/";
    public static final String TOKEN = "token bbd674b17c9a13586d4729f28a5986d69076bf92";

    public Completable sendNewUser(Userdata userdata){
        Retrofit retrofit = getClient(BASE_URL);
        APIService apiService = retrofit.create(APIService.class);
        Log.d("FACEBOOK_USER", "name = " + userdata.name);
        Log.d("FACEBOOK_USER", "photo = " + userdata.photo);
        Log.d("FACEBOOK_USER", "location = " + userdata.location);
        Log.d("FACEBOOK_USER", "socialId = " + userdata.socialId);
        Log.d("FACEBOOK_USER", "url = " + userdata.url);
        Log.d("FACEBOOK_USER", "age = " + userdata.age);
        Log.d("FACEBOOK_USER", "gender = " + userdata.gender);
        Log.d("FACEBOOK_USER", "birthday = " + userdata.birthday);
        return apiService.sendNewUser(TOKEN, userdata);
    }

    public Single<ArrayList<Userdata>> getUsers(){
        Retrofit retrofit = getClient(BASE_URL);
        APIService apiService = retrofit.create(APIService.class);
        return apiService.getUsers();
    }

    public static Retrofit getClient(String baseUrl) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit;
    }
}
