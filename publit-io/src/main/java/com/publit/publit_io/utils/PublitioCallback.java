package com.publit.publit_io.utils;

/**
 * Models a result from an api call.
 */
public interface PublitioCallback<T> {

    void success(T result);

    void failure(String message);

}
