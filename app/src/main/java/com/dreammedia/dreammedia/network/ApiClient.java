package com.dreammedia.dreammedia.network;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static com.dreammedia.dreammedia.network.ApiConstant.BASE_URL;


public class ApiClient {

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(getOkhttpClient())
                    .build();
        }
        return retrofit;
    }

    static Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    private static OkHttpClient getOkhttpClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        return new OkHttpClient.Builder()
                .addInterceptor(logging)
                .callTimeout(200, TimeUnit.SECONDS)
                .writeTimeout(200, TimeUnit.SECONDS) // write timeout
                .readTimeout(200, TimeUnit.SECONDS) // read timeout
                .build();
    }

}
