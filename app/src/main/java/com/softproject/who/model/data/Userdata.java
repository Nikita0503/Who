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
    //public Userdata(int socialWeb, String socialWebId, int number, String username) {
    //    this.socialWeb = socialWeb;
    //    this.socialWebId = socialWebId;
    //    this.number = number;
    //    this.username = username;
//
    //}

}
