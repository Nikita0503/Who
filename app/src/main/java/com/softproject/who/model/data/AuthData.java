package com.softproject.who.model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AuthData {

    @SerializedName("token")
    @Expose
    public String token;
    @SerializedName("social")
    @Expose
    public int social;


    public AuthData(int social, String token) {
        this.social = social;
        this.token = token;
    }
}
