package com.innova.victoryplay.ui.welfare;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.innova.victoryplay.VictoryRoomDatabase;
import com.innova.victoryplay.models.Welfare;
import com.innova.victoryplay.network.ApiService;
import com.innova.victoryplay.network.HeaderInterceptor;
import com.innova.victoryplay.network.response_models.ArrearsResponse;
import com.innova.victoryplay.network.response_models.WelfareResponse;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.innova.victoryplay.utils.Constants.BASE_API_URL;

public class WelfareActivityViewModel extends AndroidViewModel {
    private static final String TAG = "WelfareFragmentViewModel";
    Application application;
    private static Retrofit retrofit = null;
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Welfare>> welfareList = new MutableLiveData<>();
    private MutableLiveData<Double> arrears = new MutableLiveData<>();
    private final ApiService apiService;

    public WelfareActivityViewModel(@NonNull Application application) {
        super(application);
        VictoryRoomDatabase db = VictoryRoomDatabase.getDatabase(application);
        this.application = application;
        isLoading.postValue(true);

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
        apiService = retrofit.create(ApiService.class);
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<ArrayList<Welfare>> getWelfareList() {
        return welfareList;
    }

    public LiveData<Double> getArrears() {
        return arrears;
    }

    public void loadWelfare() {
        isLoading.setValue(true);
        Call<WelfareResponse> call = apiService.getWelfare("welfare");
        call.enqueue(new Callback<WelfareResponse>() {
            @Override
            public void onResponse(Call<WelfareResponse> call, Response<WelfareResponse> response) {
                isLoading.setValue(false);
                if (response.body() != null) {
                    welfareList.setValue(response.body().getResults());
                    loadArrears();
                } else Toast.makeText(application, response.message(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<WelfareResponse> call, Throwable t) {
                isLoading.setValue(false);
                Toast.makeText(application, "Error: "+t.getMessage(), Toast.LENGTH_SHORT).show();
                //TODO: Get audios from data base
            }
        });
    }

    public void loadArrears() {
        isLoading.setValue(true);
        Call<ArrearsResponse> call = apiService.getArrears();
        call.enqueue(new Callback<ArrearsResponse>() {
            @Override
            public void onResponse(Call<ArrearsResponse> call, Response<ArrearsResponse> response) {
                isLoading.setValue(false);
                if (response.body() != null) {
                    arrears.setValue(response.body().getArrears());
                } else Toast.makeText(application, response.message(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ArrearsResponse> call, Throwable t) {
                isLoading.setValue(false);
                Toast.makeText(application, "Unable to connect to server", Toast.LENGTH_SHORT).show();
                //TODO: Get audios from data base
            }
        });
    }
}
