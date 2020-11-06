package com.innova.victoryplay.network.response_models;

import com.google.gson.annotations.SerializedName;
import com.innova.victoryplay.models.Welfare;

import java.util.ArrayList;

public class TransactionResponse {
    private static final String TAG = "TransactionResponse";

    @SerializedName("transactions")
    private ArrayList<Welfare> results;

    public ArrayList<Welfare> getResults() {
        return results;
    }
}
