package com.publit.publit_io.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.publit.publit_io.api.response.ErrorResponse;

import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Handles parsing error responses from API.
 */
public class ErrorResponseParser {

    private static final Gson gson = new GsonBuilder().create();

    public ErrorResponse parse(Response<?> response) {
        ResponseBody body = response.errorBody();
        if (body == null) {
            return new ErrorResponse("No error response provided.");
        }
        return gson.fromJson(body.charStream(), ErrorResponse.class);
    }

}
