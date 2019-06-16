package com.snubbull.app.repository.webservices;

import android.util.Log;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.snubbull.app.BuildConfig;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {

  private static final String TAG = ServiceGenerator.class.getSimpleName();
  private final static String BASE_API_URL = "http://10.0.2.2:8080";
  private static ServiceGenerator instance = new ServiceGenerator();
  private static Retrofit.Builder retrofit = null;
  private final ApiCalls service;

  private ServiceGenerator() {
    final Gson gson = new GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy
            .LOWER_CASE_WITH_UNDERSCORES)
        .create();

    retrofit = new Retrofit.Builder()
        .baseUrl(BASE_API_URL)
        .addConverterFactory(GsonConverterFactory.create(gson));

    if (BuildConfig.DEBUG) {
      final HttpLoggingInterceptor loggingInterceptor =
          new HttpLoggingInterceptor(new HttpLoggingInterceptor
              .Logger() {
            @Override
            public void log(String message) {
              Log.d(TAG, message);
            }
          });
      retrofit.callFactory(new OkHttpClient
          .Builder()
          .addNetworkInterceptor(loggingInterceptor)
          .build());

      loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    service = retrofit.build().create(ApiCalls.class);
  }

  public static ServiceGenerator getInstance() {
    return instance;
  }

  public ApiCalls getService() {
    return service;
  }


}
