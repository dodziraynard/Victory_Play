package com.innova.victoryplay.models;

import com.google.gson.annotations.SerializedName;

public class Profile {
    private String id;

    private String username;
    private String email;
    private String image;
    private String date;
    @SerializedName("full_name")
    private String fullName;
    private String mobile;
    @SerializedName("is_admin")
    private boolean isAdmin;

    @SerializedName("user_id")
    private long userId;

    public Profile(String username, long userId) {
        this.username = username;
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public long getUserId() {
        return userId;
    }
}
