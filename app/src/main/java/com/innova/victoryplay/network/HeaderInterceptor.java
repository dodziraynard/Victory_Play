package com.innova.victoryplay.network;

import android.content.Context;
import android.content.SharedPreferences;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import static com.innova.victoryplay.utils.Constants.SHARED_PREFS_FILE;
import static com.innova.victoryplay.utils.Constants.USER_TOKEN;


public class HeaderInterceptor implements Interceptor {
    private Context context;

    public HeaderInterceptor(Context context) {
        this.context = context;
    }

    public void removeContext(){
        this.context = null;
    }

    @NotNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        if (context != null) {
            SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);
            String token = prefs.getString(USER_TOKEN, "");

            Request request = chain.request()
                    .newBuilder()
                    .addHeader("Authorization", "Token " + token)
                    .build();
            return chain.proceed(request);
        }
        return chain.proceed(chain.request());
    }
}
