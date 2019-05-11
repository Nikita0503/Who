package com.softproject.who.model.data;

public class Userdata {
    public int number;
    public int socialWeb;
    public String username;
    public String socialWebId;
    public String imageURL;
    public String age;
    public String location;

    public Userdata(int socialWeb, String socialWebId, int number, String username) {
        this.socialWeb = socialWeb;
        this.socialWebId = socialWebId;
        this.number = number;
        this.username = username;

    }
}
