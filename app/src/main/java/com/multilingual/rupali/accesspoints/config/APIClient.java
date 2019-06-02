package com.multilingual.rupali.accesspoints.config;


import com.multilingual.rupali.accesspoints.constants.Url;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {
    private static Retrofit retrofit = null;
//    final static OkHttpClient okHttpClient = new OkHttpClient.Builder()
//            .readTimeout(60, TimeUnit.SECONDS)
//            .connectTimeout(60, TimeUnit.SECONDS)
//            .build();
    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Url.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
//                    .client(okHttpClient)
                    .build();
        }
        return retrofit;
    }

}