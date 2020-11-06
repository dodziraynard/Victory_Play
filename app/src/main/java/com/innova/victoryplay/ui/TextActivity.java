package com.innova.victoryplay.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.innova.victoryplay.R;
import com.innova.victoryplay.databinding.ActivityTextBinding;
import com.innova.victoryplay.network.ApiService;
import com.innova.victoryplay.network.HeaderInterceptor;
import com.innova.victoryplay.network.response_models.PrayerRequestResponse;
import com.innova.victoryplay.network.response_models.TestimonyResponse;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.innova.victoryplay.utils.Constants.ACTION_PRAYER_REQUEST;
import static com.innova.victoryplay.utils.Constants.BASE_API_URL;

public class TextActivity extends AppCompatActivity {

    private ActivityTextBinding binding;
    private static Retrofit retrofit = null;
    private HeaderInterceptor headerInterceptor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTextBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String action = getIntent().getAction();

        if (action.equals(ACTION_PRAYER_REQUEST)) {
            binding.etSubject.setText(R.string.prayer_request);
        } else {
            binding.etSubject.setText(R.string.testimony);
        }

        binding.fab.setOnClickListener(view -> {
            String message = binding.etMessage.getText().toString();

            if (action.equals(ACTION_PRAYER_REQUEST)) {
                sendPrayerRequest(message);
            } else {
                sendTestimony(message);
            }
        });

        headerInterceptor = new HeaderInterceptor(this);
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
    }

    private void sendTestimony(String message) {
        binding.progressBar.setVisibility(View.VISIBLE);

        ApiService apiService = retrofit.create(ApiService.class);
        Call<TestimonyResponse> call = apiService.postTestimony(message);
        call.enqueue(new Callback<TestimonyResponse>() {
            @Override
            public void onResponse(Call<TestimonyResponse> call, Response<TestimonyResponse> response) {
                binding.progressBar.setVisibility(View.GONE);
                if (response.body() != null) {
                    Toast.makeText(TextActivity.this, "Testimony Sent", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                } else
                    Toast.makeText(TextActivity.this, response.message(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<TestimonyResponse> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(TextActivity.this, "Unable to connect to server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendPrayerRequest(String message) {
        binding.progressBar.setVisibility(View.VISIBLE);

        ApiService apiService = retrofit.create(ApiService.class);
        Call<PrayerRequestResponse> call = apiService.postPrayerRequest(message);
        call.enqueue(new Callback<PrayerRequestResponse>() {
            @Override
            public void onResponse(Call<PrayerRequestResponse> call, Response<PrayerRequestResponse> response) {
                binding.progressBar.setVisibility(View.GONE);
                if (response.body() != null) {
                    Toast.makeText(TextActivity.this, "Prayer Request Sent", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                } else
                    Toast.makeText(TextActivity.this, response.message(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<PrayerRequestResponse> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(TextActivity.this, "Unable to connect to server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        headerInterceptor.removeContext();
    }
}