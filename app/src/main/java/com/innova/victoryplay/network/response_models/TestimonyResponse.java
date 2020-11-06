package com.innova.victoryplay.network.response_models;

import com.google.gson.annotations.SerializedName;

public class TestimonyResponse {
    @SerializedName("testimony")
    String testimony;

    public String getTestimony() {
        return testimony;
    }
}
