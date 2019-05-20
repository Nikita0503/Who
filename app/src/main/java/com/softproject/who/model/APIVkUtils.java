package com.softproject.who.model;

import android.util.Log;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIVkUtils {
    private static final String BASE_URL = "https://api.vk.com/method/";


    public Single<JSONObject> getVkUserinfo(final String userId, final String accessToken){
        return Single.create(new SingleOnSubscribe<JSONObject>() {
            @Override
            public void subscribe(SingleEmitter<JSONObject> e) throws Exception {
                String url = BASE_URL + "users.get?user_id="+userId+"&fields=photo_100,sex,home_town,bdate&access_token="+accessToken+"&v=5.95";
                URL obj = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

                connection.setRequestMethod("GET");
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                JSONObject object = new JSONObject(response.toString());
                //Log.d("VK", object.toString(4));
                e.onSuccess(object);
            }
        });
    }


}
