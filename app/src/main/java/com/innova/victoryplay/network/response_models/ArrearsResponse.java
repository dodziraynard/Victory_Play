package com.innova.victoryplay.network.response_models;

import com.google.gson.annotations.SerializedName;

public class ArrearsResponse {
    @SerializedName("arrears")
    Double arrears;

    public Double getArrears() {
        return arrears;
    }
}
