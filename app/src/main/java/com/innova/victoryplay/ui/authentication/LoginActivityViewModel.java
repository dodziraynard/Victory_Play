package com.innova.victoryplay.ui.authentication;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.innova.victoryplay.VictoryRoomDatabase;
import com.innova.victoryplay.models.Profile;
import com.innova.victoryplay.network.ApiService;
import com.innova.victoryplay.network.response_models.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.innova.victoryplay.utils.Constants.BASE_API_URL;
import static com.innova.victoryplay.utils.Constants.FULL_USERNAME;
import static com.innova.victoryplay.utils.Constants.SHARED_PREFS_FILE;
import static com.innova.victoryplay.utils.Constants.USER_ID;
import static com.innova.victoryplay.utils.Constants.USER_TOKEN;

public class LoginActivityViewModel extends AndroidViewModel {
    private static final String TAG = "LoginActivityViewModel";
    Application application;
    private static Retrofit retrofit = null;
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<Profile> profile = new MutableLiveData<>();

    public LoginActivityViewModel(@NonNull Application application) {
        super(application);
        VictoryRoomDatabase db = VictoryRoomDatabase.getDatabase(application);
        this.application = application;
        isLoading.postValue(true);
    }


    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }


    public void logIn(String username, String password) {
        isLoading.setValue(true);
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        ApiService apiService = retrofit.create(ApiService.class);
        Call<LoginResponse> call = apiService.logIn(username, password);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                isLoading.setValue(false);
                if (response.body() != null) {
                    profile.setValue(response.body().getProfile());

                    String token = response.body().getToken();

                    SharedPreferences prefs = application.getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString(USER_TOKEN, token);
                    editor.putString(FULL_USERNAME, response.body().getProfile().getFullName());
                    editor.putString(FULL_USERNAME, response.body().getProfile().getFullName());
                    editor.putLong(USER_ID, response.body().getProfile().getUserId());
                    editor.apply();

                    Toast.makeText(application, token, Toast.LENGTH_SHORT).show();
                } else Toast.makeText(application, response.message(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                isLoading.setValue(false);
                Toast.makeText(application, "Error: "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
