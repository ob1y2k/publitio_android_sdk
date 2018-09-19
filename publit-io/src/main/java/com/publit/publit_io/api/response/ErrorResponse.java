package com.publit.publit_io.api.response;

import com.google.gson.annotations.SerializedName;

/**
 * Models an API error response.
 */
public class ErrorResponse {

    @SerializedName("Message")
    private String message;

    public ErrorResponse() {
        super();
    }

    public ErrorResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}