package com.publit.publit_io.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.publit.publit_io.constant.Constant;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * The type Api client.
 */
public class APIClient {

    private static Retrofit retrofit = null;

    /**
     * Gets the client.
     *
     * @return the client
     */
    public static Retrofit getClient() {
        Gson gson = new GsonBuilder().setLenient().create();

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.connectTimeout(20, TimeUnit.SECONDS);
        httpClient.readTimeout(120, TimeUnit.SECONDS);
        retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASEURL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
                .build();
        return retrofit;
    }

    /**
     * Create service s.
     *
     * @param <S>          the type parameter
     * @param serviceClass the service class
     * @return the s
     */
    public static <S> S createService(
            Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }
}
