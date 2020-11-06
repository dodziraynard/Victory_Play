package com.innova.victoryplay.ui.pdf;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.innova.victoryplay.VictoryRoomDatabase;
import com.innova.victoryplay.models.Pdf;
import com.innova.victoryplay.network.ApiService;
import com.innova.victoryplay.network.HeaderInterceptor;
import com.innova.victoryplay.network.response_models.PdfResponse;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.innova.victoryplay.utils.Constants.BASE_API_URL;

public class PdfActivityViewModel extends AndroidViewModel {
    private static final String TAG = "PdfFragmentViewModel";
    Application application;
    private static Retrofit retrofit = null;
    private final MutableLiveData<Boolean> isLoadingPdf = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Pdf>> pdfList = new MutableLiveData<>();

    public PdfActivityViewModel(@NonNull Application application) {
        super(application);
        VictoryRoomDatabase db = VictoryRoomDatabase.getDatabase(application);
        this.application = application;
        isLoadingPdf.postValue(true);
    }


    public LiveData<ArrayList<Pdf>> getPdfList() {
        return pdfList;
    }

    public LiveData<Boolean> getIsLoadingPdf() {
        return isLoadingPdf;
    }

    public void loadPdf(String query) {
        isLoadingPdf.setValue(true);
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
        Call<PdfResponse> call = apiService.getPdfs(query);
        call.enqueue(new Callback<PdfResponse>() {
            @Override
            public void onResponse(Call<PdfResponse> call, Response<PdfResponse> response) {
                isLoadingPdf.setValue(false);
                if (response.body() != null) {
                    pdfList.setValue(response.body().getResults());
                } else Toast.makeText(application, response.message(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<PdfResponse> call, Throwable t) {
                isLoadingPdf.setValue(false);
                Toast.makeText(application, "Unable to connect to server", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }
}
