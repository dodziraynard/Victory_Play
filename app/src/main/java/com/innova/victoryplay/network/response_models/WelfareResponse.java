package com.innova.victoryplay.network.response_models;

import com.google.gson.annotations.SerializedName;
import com.innova.victoryplay.models.Audio;
import com.innova.victoryplay.models.Welfare;

import java.util.ArrayList;

public class WelfareResponse {
    private static final String TAG = "WelfareResponse";

    @SerializedName("transactions")
    private ArrayList<Welfare> results;

    public ArrayList<Welfare> getResults() {
        return results;
    }
}
