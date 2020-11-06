package com.innova.victoryplay.network.response_models;

import com.google.gson.annotations.SerializedName;

public class PrayerRequestResponse {
    @SerializedName("request")
    String request;

    public String getRequest() {
        return request;
    }
}
