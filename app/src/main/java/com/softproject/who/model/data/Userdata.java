package com.softproject.who.model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

public class Userdata {
    public boolean isNew;
    public boolean isHidden;
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("photo")
    @Expose
    public String photo;
    @SerializedName("location")
    @Expose
    public String location;
    @SerializedName("social")
    @Expose
    public Integer social;
    @SerializedName("social_id")
    @Expose
    public String socialId;
    @SerializedName("url")
    @Expose
    public String url;
    @SerializedName("age")
    @Expose
    public Integer age;
    @SerializedName("birthday")
    @Expose
    public String birthday;
    @SerializedName("gender")
    @Expose
    public String gender;
    @SerializedName("auth_date")
    @Expose
    public String authDate;

    public Userdata(Integer social) {
        this.social = social;
    }
}
