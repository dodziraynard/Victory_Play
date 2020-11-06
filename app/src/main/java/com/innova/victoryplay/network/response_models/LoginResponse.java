package com.innova.victoryplay.network.response_models;

import com.google.gson.annotations.SerializedName;
import com.innova.victoryplay.models.Profile;

public class LoginResponse {
    private static final String TAG = "LoginResponse";

    @SerializedName("profile")
    private Profile profile;

    @SerializedName("token")
    private String token;

    public Profile getProfile() {
        return profile;
    }

    public String getToken() {
        return token;
    }
}
