package com.softproject.who.model;

import android.util.Log;

import com.softproject.who.main.MainPresenter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.softproject.who.model.APIUtils;
import com.softproject.who.model.data.Userdata;
import com.softproject.who.model.data.instagram.InstagramUserdata;

import java.util.ArrayList;
import java.util.Iterator;

public class DataTransformer {
    private MainPresenter mPresenter;

    public DataTransformer(MainPresenter presenter) {
        this.mPresenter = presenter;
    }


    public Userdata facebookTransform(JSONObject jsonObject){
        Userdata userdata = new Userdata(APIUtils.FACEBOOK_ID);
        Iterator<String> keys = jsonObject.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            try {
                Object value = jsonObject.get(key);
                switch (key){
                    case "name":
                        userdata.name = String.valueOf(value);
                        break;
                    case "picture":
                        JSONObject objectPicture = jsonObject.getJSONObject("picture");
                        JSONObject objectData = objectPicture.getJSONObject("data");
                        userdata.photo = objectData.getString("url");
                        break;
                    case "hometown":
                        JSONObject objectHomeTown = jsonObject.getJSONObject("hometown");
                        userdata.location = objectHomeTown.getString("name");
                        break;
                    case "id":
                        userdata.socialId = jsonObject.getString("id");
                        break;
                    case "link":
                        userdata.url = jsonObject.getString("link");
                        break;
                    case "age_range":
                        JSONObject objectMin = jsonObject.getJSONObject("age_range");
                        userdata.age = objectMin.getInt("min");
                        break;
                    case "gender":
                        userdata.gender = jsonObject.getString("gender");
                        break;
                    case "birthday":
                        userdata.birthday = jsonObject.getString("birthday");
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return userdata;
    }

    public Userdata vkTransform(JSONObject vkObject) throws Exception {
        JSONArray jsonArray = vkObject.getJSONArray("response");
        JSONObject jsonObject = jsonArray.getJSONObject(0);
        Userdata userdata = new Userdata(APIUtils.VK_ID);
        Iterator<String> keys = jsonObject.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            try {
                Object value = jsonObject.get(key);
                switch (key){
                    case "first_name":
                        userdata.name = String.valueOf(value);
                        break;
                    case "photo_100":
                        userdata.photo = String.valueOf(value);
                        break;
                    case "home_town":
                        userdata.location = String.valueOf(value);
                        break;
                    case "id":
                        userdata.socialId = String.valueOf(value);
                        userdata.url = "https://vk.com/id" + String.valueOf(value);
                        break;
                    case "sex":
                        String gender = String.valueOf(value);
                        if(gender.equals("1")){
                            userdata.gender = "female";
                        }else if(gender.equals("2")){
                            userdata.gender = "male";
                        }
                        break;
                    case "bdate":
                        userdata.birthday = String.valueOf(value);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return userdata;
    }



    public Userdata instagramTransform(InstagramUserdata instagramUserdata){
        Userdata userdata = new Userdata(APIUtils.INSTAGRAM_ID);
        userdata.name = instagramUserdata.fullName;
        userdata.photo = instagramUserdata.profilePicture;
        userdata.socialId = instagramUserdata.id;
        userdata.url = "https://www.instagram.com/" + instagramUserdata.username;
        return userdata;
    }
}
