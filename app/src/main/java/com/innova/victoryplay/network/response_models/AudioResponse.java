package com.innova.victoryplay.network.response_models;

import com.google.gson.annotations.SerializedName;
import com.innova.victoryplay.models.Audio;

import java.util.ArrayList;

public class AudioResponse {
    private static final String TAG = "AudioResponse";

    @SerializedName("audios")
    private ArrayList<Audio> results;

    public ArrayList<Audio> getResults() {
        return results;
    }
}
