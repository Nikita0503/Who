package com.softproject.who.model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Userdata {
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
    @SerializedName("auth_date")
    @Expose
    public String authDate;
    @SerializedName("social")
    @Expose
    public Integer social;
    @SerializedName("social_id")
    @Expose
    public String socialId;
    @SerializedName("url")
    @Expose
    public String url;
    @SerializedName("token")
    @Expose
    public String token;
    @SerializedName("age")
    @Expose
    public Integer age;
    @SerializedName("birthday")
    @Expose
    public String birthday;
    @SerializedName("gender")
    @Expose
    public String gender;
}
