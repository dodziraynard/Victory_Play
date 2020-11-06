package com.innova.victoryplay.ui.audio;

import android.app.Application;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.innova.victoryplay.VictoryRoomDatabase;
import com.innova.victoryplay.models.Audio;
import com.innova.victoryplay.network.ApiService;
import com.innova.victoryplay.network.response_models.AudioResponse;
import com.innova.victoryplay.network.HeaderInterceptor;
import com.innova.victoryplay.services.AudioPlayerService;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.innova.victoryplay.utils.Constants.BASE_API_URL;

public class AudioActivityViewModel extends AndroidViewModel {
    private static final String TAG = "AudioFragmentViewModel";
    Application application;
    private static Retrofit retrofit = null;
    private final MutableLiveData<AudioPlayerService.LocalBinder> binder = new MutableLiveData<>();
    private final MutableLiveData<Boolean> handlerHasCallback = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoadingAudio = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Audio>> audioList = new MutableLiveData<>();

    public AudioActivityViewModel(@NonNull Application application) {
        super(application);
        VictoryRoomDatabase db = VictoryRoomDatabase.getDatabase(application);
        this.application = application;
        handlerHasCallback.postValue(false);
        isLoadingAudio.postValue(true);
    }

    public LiveData<AudioPlayerService.LocalBinder> getBinder() {
        return binder;
    }

    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            AudioPlayerService.LocalBinder b = (AudioPlayerService.LocalBinder) service;
            binder.postValue(b);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            binder.setValue(null);
        }
    };

    public ServiceConnection getServiceConnection() {
        return serviceConnection;
    }

    public boolean getHandlerHasCallback() {
        return handlerHasCallback.getValue();
    }

    public void setHandlerHasCallback(boolean handlerHasCallback) {
        this.handlerHasCallback.postValue(handlerHasCallback);
    }

    public LiveData<Boolean> getIsLoadingAudio() {
        return isLoadingAudio;
    }

    public LiveData<ArrayList<Audio>> getAudioList() {
        return audioList;
    }

    public void loadAudio(String query){
        isLoadingAudio.setValue(true);
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
        Call<AudioResponse> call = apiService.getAudios(query);
        call.enqueue(new Callback<AudioResponse>() {
            @Override
            public void onResponse(Call<AudioResponse> call, Response<AudioResponse> response) {
                isLoadingAudio.setValue(false);
                if (response.body() != null) {
                    audioList.setValue(response.body().getResults());
                } else Toast.makeText(application, response.message(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<AudioResponse> call, Throwable t) {
                isLoadingAudio.setValue(false);
                Toast.makeText(application, "Unable to connect to server", Toast.LENGTH_SHORT).show();
                //TODO: Get audios from data base
            }
        });
    }
}
