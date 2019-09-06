package com.publit.publit_io.api;

import com.google.gson.JsonObject;

import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface ApiInterface {

    //Files Operations API.

    @GET("v1/files/list")
    Call<JsonObject> callFilesList(@QueryMap Map<String, String> options);

    @Multipart
    @POST("v1/files/create")
    Call<JsonObject> callCreateFile(@Part MultipartBody.Part file, @QueryMap Map<String, String> options);

    @GET("v1/files/show/{id}")
    Call<JsonObject> callShowFile(@Path("id") String id, @QueryMap Map<String, String> options);

    @PUT("v1/files/update/{id}")
    Call<JsonObject> callUpdateFile(@Path("id") String id, @QueryMap Map<String, String> options);

    @DELETE("v1/files/delete/{id}")
    Call<JsonObject> callDeleteFie(@Path("id") String id, @QueryMap Map<String, String> options);

    @GET("v1/files/player/{id}")
    Call<JsonObject> callPlayerFile(@Path("id") String id, @QueryMap Map<String, String> options);


    //Players Operations API.

    @POST("v1/players/create")
    Call<JsonObject> callCreatePlayer(@Query("name") String name, @QueryMap Map<String, String> options);

    @GET("v1/players/list")
    Call<JsonObject> callPlayersList(@QueryMap Map<String, String> options);

    @GET("v1/players/show/{id}")
    Call<JsonObject> callShowPlayer(@Path("id") String id, @QueryMap Map<String, String> options);

    @PUT("v1/players/update/{id}")
    Call<JsonObject> callUpdatePlayer(@Path("id") String id, @QueryMap Map<String, String> options);

    @DELETE("v1/players/delete/{id}")
    Call<JsonObject> callDeletePlayer(@Path("id") String id, @QueryMap Map<String, String> options);


    //Watermark Operations API.

    @Multipart
    @POST("v1/watermarks/create")
    Call<JsonObject> callCreateWaterMark(@Part MultipartBody.Part file, @Query("name") String name, @QueryMap Map<String, String> options);

    @GET("v1/watermarks/list")
    Call<JsonObject> callWatermarksList(@QueryMap Map<String, String> options);

    @GET("v1/watermarks/show/{id}")
    Call<JsonObject> callShowWatermark(@Path("id") String id, @QueryMap Map<String, String> options);

    @PUT("v1/watermarks/update/{id}")
    Call<JsonObject> callUpdateWatermark(@Path("id") String id, @QueryMap Map<String, String> options);

    @DELETE("v1/watermarks/delete/{id}")
    Call<JsonObject> callDeleteWatermark(@Path("id") String id, @QueryMap Map<String, String> options);


    //Versions Operations API

    @POST("v1/files/versions/create/{id}")
    Call<JsonObject> callCreateVersion(@Path("id") String id, @Query("extension") String outputFormat, @QueryMap Map<String, String> options);

    @GET("v1/files/versions/list/{id}")
    Call<JsonObject> callVersionsList(@Path("id") String id, @QueryMap Map<String, String> options);

    @GET("v1/files/versions/show/{id}")
    Call<JsonObject> callShowVersion(@Path("id") String id, @QueryMap Map<String, String> options);

    @PUT("v1/files/versions/update/{id}")
    Call<JsonObject> callUpdateVersion(@Path("id") String id, @QueryMap Map<String, String> options);

    @PUT("v1/files/versions/reconvert/{id}")
    Call<JsonObject> callReconvertVersion(@Path("id") String id, @QueryMap Map<String, String> options);

    @DELETE("v1/files/versions/delete/{id}")
    Call<JsonObject> callDeleteVersion(@Path("id") String id, @QueryMap Map<String, String> options);
}
