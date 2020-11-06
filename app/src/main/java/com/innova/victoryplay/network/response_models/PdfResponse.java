package com.innova.victoryplay.network.response_models;

import com.google.gson.annotations.SerializedName;
import com.innova.victoryplay.models.Pdf;

import java.util.ArrayList;

public class PdfResponse {
    private static final String TAG = "PdfResponse";

    @SerializedName("pdfs")
    private ArrayList<Pdf> results;

    public ArrayList<Pdf> getResults() {
        return results;
    }
}
