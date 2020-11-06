package com.innova.victoryplay.ui.video;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.innova.victoryplay.VictoryRoomDatabase;
import com.innova.victoryplay.models.Video;
import com.innova.victoryplay.network.ApiService;
import com.innova.victoryplay.network.HeaderInterceptor;
import com.innova.victoryplay.network.response_models.VideoResponse;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.innova.victoryplay.utils.Constants.BASE_API_URL;

public class VideoActivityViewModel extends AndroidViewModel {
    private static final String TAG = "VideoFragmentViewModel";
    Application application;
    private static Retrofit retrofit = null;
    private final MutableLiveData<Boolean> isLoadingVideo = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Video>> videoList = new MutableLiveData<>();

    public VideoActivityViewModel(@NonNull Application application) {
        super(application);
        VictoryRoomDatabase db = VictoryRoomDatabase.getDatabase(application);
        this.application = application;
        isLoadingVideo.postValue(true);
    }


    public LiveData<ArrayList<Video>> getVideoList() {
        return videoList;
    }

    public LiveData<Boolean> getIsLoadingVideo() {
        return isLoadingVideo;
    }

    public void loadVideo(String query) {
        isLoadingVideo.setValue(true);
        HeaderInterceptor headerInterceptor = new HeaderInterceptor(application);
        OkHttpClient okHttpClient = new OkHttpClient()
                .newBuilder().addInterceptor(headerInterceptor)
                .build();
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(BASE_API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        ApiService apiService = retrofit.create(ApiService.class);
        Call<VideoResponse> call = apiService.getVideos(query);
        call.enqueue(new Callback<VideoResponse>() {
            @Override
            public void onResponse(Call<VideoResponse> call, Response<VideoResponse> response) {
                isLoadingVideo.setValue(false);
                if (response.body() != null) {
                    videoList.setValue(response.body().getResults());
                } else Toast.makeText(application, response.message(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<VideoResponse> call, Throwable t) {
                isLoadingVideo.setValue(false);
                Toast.makeText(application, "Unable to connect to server", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }
}
