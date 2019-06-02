package com.multilingual.rupali.accesspoints.config;

import com.multilingual.rupali.accesspoints.constants.Url;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClientMail {
    private static Retrofit retrofit = null;
    //    final static OkHttpClient okHttpClie
    public static Retrofit getClientMail() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Url.BASE_URL_MAIL)
                    .addConverterFactory(GsonConverterFactory.create())
//                    .client(okHttpClient)
                    .build();
        }
        return retrofit;
    }
}
