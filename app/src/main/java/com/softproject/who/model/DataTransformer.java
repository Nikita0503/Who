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

    public void transform(final int socialWebId, JSONObject jsonObject){
         switch (socialWebId){
             case APIUtils.FACEBOOK_ID:
                 facebookTransform(jsonObject);
                 break;
         }
    }

    private void facebookTransform(JSONObject jsonObject){
        Log.d("FACEBOOK", jsonObject.toString());
        Userdata userdata = new Userdata(APIUtils.FACEBOOK_ID);
        Iterator<String> keys = jsonObject.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            try {
                Object value = jsonObject.get(key);
                switch (key){
                    case "":

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
