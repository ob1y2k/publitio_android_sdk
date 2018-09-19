package com.publit.publit_io.api;

import com.publit.publit_io.api.response.ErrorResponse;
import com.publit.publit_io.utils.LogUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Base callback for future Retrofit implementation.
 */
public abstract class AbstractCallback<T> implements Callback<T> {

    private final String TAG = LogUtils.makeLogTag(this.getClass());

    private ErrorResponseParser errorResponseParser = new ErrorResponseParser();

    public void onResponse(/*@Nonnull*/  Call<T> call, /*@Nonnull*/ Response<T> response) {
        if (response.code() >= 400) {
            onResponseError(call, response);
        } else {
            this.handleSuccess(response);
        }
        this.handleComplete(call);
    }

    public void onFailure(/*@Nonnull*/ Call<T> call, /*@Nonnull*/ Throwable t) {
        LogUtils.LOGE(TAG, t.getMessage(), t);
        this.handleFailure(call, t);
        this.handleComplete(call);
    }

    private ErrorResponse doParseError(Response<T> response) {
        return errorResponseParser.parse(response);
    }

    protected void onResponseError(Call<T> call, Response<T> response) {
        ErrorResponse errorResponse = doParseError(response);

        LogUtils.LOGD(AbstractCallback.class.getSimpleName(), call.request().url().toString());

        switch (response.code()) {
            case 400:
                this.handleClientError(errorResponse);
                break;
            default:
                this.handleError(call, errorResponse);
                break;
        }
    }

    protected void handleClientError(ErrorResponse errorResponse) {

    }

    protected void handleComplete(Call<T> call) {

    }

    protected abstract void handleFailure(Call<T> call, Throwable throwable);

    protected abstract void handleError(Call<T> call, ErrorResponse errorResponse);

    protected abstract void handleSuccess(Response<T> response);

}
