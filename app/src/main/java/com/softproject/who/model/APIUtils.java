package com.softproject.who.model;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.softproject.who.model.data.AuthData;
import com.softproject.who.model.data.Userdata;

import java.util.ArrayList;

import io.reactivex.Completable;
import io.reactivex.Single;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIUtils {

    public static final int FACEBOOK_ID = 1;
    public static final String BASE_URL = "http://ec2-54-191-215-22.us-west-2.compute.amazonaws.com/api/";
    public static final String TOKEN = "token 90aedfe273129b27ea1676011db99864f2e12514";

    public Completable singIn(int socialWeb, String socialWebToken){
        Retrofit retrofit = getClient(BASE_URL);
        APIService apiService = retrofit.create(APIService.class);
        return apiService.singIn(TOKEN, new AuthData(socialWeb, socialWebToken));
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
