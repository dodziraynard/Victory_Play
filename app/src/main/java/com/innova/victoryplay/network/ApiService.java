package com.innova.victoryplay.network;

import com.innova.victoryplay.network.response_models.ArrearsResponse;
import com.innova.victoryplay.network.response_models.AudioResponse;
import com.innova.victoryplay.network.response_models.LoginResponse;
import com.innova.victoryplay.network.response_models.PdfResponse;
import com.innova.victoryplay.network.response_models.PrayerRequestResponse;
import com.innova.victoryplay.network.response_models.TestimonyResponse;
import com.innova.victoryplay.network.response_models.TransactionResponse;
import com.innova.victoryplay.network.response_models.VideoResponse;
import com.innova.victoryplay.network.response_models.WelfareResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    @GET("audios")
    Call<AudioResponse> getAudios(@Query("query") String query);

    @GET("videos")
    Call<VideoResponse> getVideos(@Query("query") String query);

    @GET("pdfs")
    Call<PdfResponse> getPdfs(@Query("query") String query);

    @GET("transactions")
    Call<WelfareResponse> getWelfare(@Query("type") String transaction_type);

    @GET("welfare-arrears")
    Call<ArrearsResponse> getArrears();

    @FormUrlEncoded
    @POST("auth/login")
    Call<LoginResponse> logIn(@Field("username") String username, @Field("password") String password);

    @FormUrlEncoded
    @POST("testimonies/")
    Call<TestimonyResponse> postTestimony(@Field("testimony") String testimony);

    @FormUrlEncoded
    @POST("prayer-requests/")
    Call<PrayerRequestResponse> postPrayerRequest(@Field("request") String request);

    @FormUrlEncoded
    @POST("make-transaction/")
    Call<TransactionResponse> postTransaction(@Field("user_id") long userId,
                                              @Field("full_name") String fullName,
                                              @Field("type") String type,
                                              @Field("mobile") String mobile,
                                              @Field("amount") double amount);
}
