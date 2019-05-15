package com.softproject.who.model;

import android.util.Log;

import com.softproject.who.main.MainPresenter;

import org.json.JSONException;
import org.json.JSONObject;

import com.softproject.who.model.APIUtils;
import com.softproject.who.model.data.Userdata;

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
}
