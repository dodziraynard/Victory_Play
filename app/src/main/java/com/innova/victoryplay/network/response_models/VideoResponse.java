package com.innova.victoryplay.network.response_models;

import com.google.gson.annotations.SerializedName;
import com.innova.victoryplay.models.Video;

import java.util.ArrayList;

public class VideoResponse {
    private static final String TAG = "VideoResponse";

    @SerializedName("videos")
    private ArrayList<Video> results;

    public ArrayList<Video> getResults() {
        return results;
    }
}
