package com.softproject.who.model;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.softproject.who.model.data.Userdata;
import com.softproject.who.model.data.UserdataForList;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.Completable;
import io.reactivex.Single;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIUtils {

    public static final int FACEBOOK_ID = 1;
    public static final int TWITTER_ID = 2;
    public static final int INSTAGRAM_ID = 3;
    public static final String BASE_URL = "http://ec2-34-214-73-44.us-west-2.compute.amazonaws.com/api/";
    public static final String TOKEN = "token bbd674b17c9a13586d4729f28a5986d69076bf92";

    public Completable sendNewUser(int socialWebId, Userdata data){
        Retrofit retrofit = getClient(BASE_URL);
        APIService apiService = retrofit.create(APIService.class);
        return apiService.sendNewUser(TOKEN, new Userdata(socialWebId));
    }

    public Single<ArrayList<UserdataForList>> getUsers(){
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
