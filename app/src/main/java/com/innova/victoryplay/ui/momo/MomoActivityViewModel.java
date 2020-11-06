package com.innova.victoryplay.ui.momo;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.innova.victoryplay.VictoryRoomDatabase;
import com.innova.victoryplay.network.ApiService;
import com.innova.victoryplay.network.HeaderInterceptor;
import com.innova.victoryplay.network.response_models.TransactionResponse;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.innova.victoryplay.utils.Constants.BASE_API_URL;

public class MomoActivityViewModel extends AndroidViewModel {
    private static final String TAG = "WelfareFragmentViewModel";
    Application application;
    private static Retrofit retrofit = null;
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final ApiService apiService;

    public MomoActivityViewModel(@NonNull Application application) {
        super(application);
        VictoryRoomDatabase db = VictoryRoomDatabase.getDatabase(application);
        this.application = application;
        isLoading.postValue(false);

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


    public void postTransaction(long userId, String fullName, String type, String mobile, double amount) {
        isLoading.setValue(true);
        Call<TransactionResponse> call = apiService.postTransaction(userId, fullName, type, mobile, amount);
        call.enqueue(new Callback<TransactionResponse>() {
            @Override
            public void onResponse(Call<TransactionResponse> call, Response<TransactionResponse> response) {
                isLoading.setValue(false);
                if (response.body() != null) {
//                    welfareList.setValue(response.body().getResults());
                } else Toast.makeText(application, response.message(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<TransactionResponse> call, Throwable t) {
                isLoading.setValue(false);
                Toast.makeText(application, "Unable to connect to server", Toast.LENGTH_SHORT).show();
                //TODO: Get audios from data base
            }
        });
    }
}
