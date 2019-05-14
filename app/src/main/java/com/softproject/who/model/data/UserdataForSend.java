package com.softproject.who.model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

public class UserdataForSend {

    @SerializedName("data")
    @Expose
    public JSONObject data;
    @SerializedName("social")
    @Expose
    public int social;


    public UserdataForSend(int social, JSONObject data) {
        this.social = social;
        this.data = data;
    }
}
