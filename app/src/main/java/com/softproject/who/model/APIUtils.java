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
    public static final int VK_ID = 4;
    public static final String BASE_URL = "http://34.214.73.44:8000/api/";
    public static final String TOKEN = "token a1372269eca148a1412f45cb4ca7fdc88b5d15e8";

    public Completable sendNewUser(Userdata userdata){
        Retrofit retrofit = getClient(BASE_URL);
        APIService apiService = retrofit.create(APIService.class);
        return apiService.sendNewUser(TOKEN, userdata);
    }

    public Single<ArrayList<Userdata>> getUsers(String text){
        Retrofit retrofit = getClient(BASE_URL);
        APIService apiService = retrofit.create(APIService.class);
        if(text.equals("")) {
            return apiService.getUsers();
        }else{
            return apiService.getUsers(text);
        }
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
