package com.publit.publit_io.watermarks;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import com.google.gson.JsonObject;
import com.publit.publit_io.R;
import com.publit.publit_io.api.APIClient;
import com.publit.publit_io.api.ApiInterface;
import com.publit.publit_io.api.request.ProgressRequestBody;
import com.publit.publit_io.constant.Constant;
import com.publit.publit_io.utils.APIConfiguration;
import com.publit.publit_io.utils.FileUtils;
import com.publit.publit_io.utils.NetworkService;
import com.publit.publit_io.utils.PublitioCallback;
import com.publit.publit_io.utils.SHAGenerator;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * It contains all the operations related to Watermarks.
 */
public class PublitioWaterMarks implements ProgressRequestBody.UploadCallbacks {

    private Context mContext;

    //Public Constructor
    public PublitioWaterMarks(Context activity) {
        mContext = activity;
    }


    /**
     * This endpoint creates new watermark,
     * that you can later use on Files & Versions in order to protect your images and videos.
     * Right now you can upload png or jpg images as your watermarks
     * and Publitio API will securely stored them for future use.
     *
     * @param fileUri        Image to upload as watermark (png or jpg supported) .
     * @param watermarkName  Unique alphanumeric name (id) of watermark.
     * @param optionalParams List of Optional API Params.
     * @param callback       It is used to provide success or failure response.
     */
    public void uploadWatermark(Uri fileUri, String watermarkName, Map<String, String> optionalParams, final PublitioCallback<JsonObject> callback) {

        if (APIConfiguration.apiKey == null || APIConfiguration.apiKey.isEmpty())
            return;

        if (fileUri == null || watermarkName == null) {
            callback.failure(mContext.getString(R.string.provide_watermark_uri));
            return;
        }

        ApiInterface apiService = APIClient.getClient().create(ApiInterface.class);

        SHAGenerator shaGenerator = new SHAGenerator();

        // use the FileUtils to get the actual file by uri
        File file = FileUtils.getFile(mContext, fileUri);


        ContentResolver cR = mContext.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        String type = mime.getExtensionFromMimeType(cR.getType(fileUri));

        ProgressRequestBody fileBody = new ProgressRequestBody(file, type, this);

        MultipartBody.Part fileData =
                MultipartBody.Part.createFormData("file", file.getName(), fileBody);

        Map<String, String> apiParams = new HashMap<>();
        apiParams.put(Constant.API_SIGNATURE, shaGenerator.getApiSignature());
        apiParams.put(Constant.PUB_API_KEY, APIConfiguration.apiKey);
        apiParams.put(Constant.API_NONCE, shaGenerator.getApiNonce());
        apiParams.put(Constant.API_TIMESTAMP, shaGenerator.getApiTimeStamp());
        apiParams.put(Constant.API_KIT, Constant.SDK_TYPE);

        if (optionalParams != null) {
            for (Map.Entry<String, String> entry : optionalParams.entrySet()) {
                apiParams.put(entry.getKey(), entry.getValue());
            }
        }

        if (NetworkService.isNetworkAvailable(mContext)) {

//            if (FileUtils.getExtension(String.valueOf(fileUri)).contains("jpg") || FileUtils.getExtension(String.valueOf(fileUri)).contains("png")) {
            Call<JsonObject> call = apiService.callCreateWaterMark(fileData, watermarkName, apiParams);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.body() != null) {
                        callback.success(response.body().getAsJsonObject());
                    } else if (response.errorBody() != null) {
                        try {
                            callback.failure(response.errorBody().string());
                        } catch (IOException e) {
                            callback.failure(e.getMessage());
                        }
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    callback.failure(t.getMessage());
                }
            });
            /*} else {
                callback.failure(mActivity.getString(R.string.choose_png_or_jpg));
            }*/
        } else {
            callback.failure(mContext.getString(R.string.no_network_found));
        }
    }


    /**
     * This endpoint lists all watermark under the account.
     *
     * @param callback It is used to provide success or failure response.
     */
    public void watermarksList(final PublitioCallback<JsonObject> callback) {

        if (APIConfiguration.apiKey == null || APIConfiguration.apiKey.isEmpty())
            return;

        SHAGenerator shaGenerator = new SHAGenerator();

        ApiInterface apiService = APIClient.getClient().create(ApiInterface.class);

        Map<String, String> apiParams = new HashMap<>();
        apiParams.put(Constant.API_SIGNATURE, shaGenerator.getApiSignature());
        apiParams.put(Constant.PUB_API_KEY, APIConfiguration.apiKey);
        apiParams.put(Constant.API_NONCE, shaGenerator.getApiNonce());
        apiParams.put(Constant.API_TIMESTAMP, shaGenerator.getApiTimeStamp());
        apiParams.put(Constant.API_KIT, Constant.SDK_TYPE);

        if (NetworkService.isNetworkAvailable(mContext)) {
            Call<JsonObject> call = apiService.callWatermarksList(apiParams);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.body() != null) {
                        callback.success(response.body().getAsJsonObject());
                    } else if (response.errorBody() != null) {
                        try {
                            callback.failure(response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    callback.failure(t.getMessage());
                }
            });
        } else {
            callback.failure(mContext.getString(R.string.no_network_found));
        }
    }


    /**
     * This endpoint shows specific watermark info based on it's id.
     *
     * @param watermarkID Unique alphanumeric name (id) of watermark.
     * @param callback    It is used to provide success or failure response.
     */
    public void showWatermark(String watermarkID, final PublitioCallback<JsonObject> callback) {

        if (APIConfiguration.apiKey == null || APIConfiguration.apiKey.isEmpty())
            return;

        SHAGenerator shaGenerator = new SHAGenerator();

        ApiInterface apiService = APIClient.getClient().create(ApiInterface.class);

        Map<String, String> apiParams = new HashMap<>();
        apiParams.put(Constant.API_SIGNATURE, shaGenerator.getApiSignature());
        apiParams.put(Constant.PUB_API_KEY, APIConfiguration.apiKey);
        apiParams.put(Constant.API_NONCE, shaGenerator.getApiNonce());
        apiParams.put(Constant.API_TIMESTAMP, shaGenerator.getApiTimeStamp());
        apiParams.put(Constant.API_KIT, Constant.SDK_TYPE);

        if (NetworkService.isNetworkAvailable(mContext)) {

            Call<JsonObject> call = apiService.callShowWatermark(watermarkID, apiParams);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.body() != null) {
                        callback.success(response.body().getAsJsonObject());
                    } else if (response.errorBody() != null) {
                        try {
                            callback.failure(response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    callback.failure(t.getMessage());
                }
            });
        } else {
            callback.failure(mContext.getString(R.string.no_network_found));
        }
    }


    /**
     * This endpoint update specific watermark info based on it's id.
     *
     * @param watermarkID    Unique alphanumeric name (id) of watermark.
     * @param optionalParams List of Optional API Params.
     * @param callback       It is used to provide success or failure response.
     */
    public void updateWatermark(String watermarkID, Map<String, String> optionalParams, final PublitioCallback<JsonObject> callback) {

        if (APIConfiguration.apiKey == null || APIConfiguration.apiKey.isEmpty())
            return;

        if (watermarkID == null) {
            callback.failure(mContext.getString(R.string.provide_watermark_id));
            return;
        }
        SHAGenerator shaGenerator = new SHAGenerator();

        ApiInterface apiService = APIClient.getClient().create(ApiInterface.class);

        Map<String, String> apiParams = new HashMap<>();
        apiParams.put(Constant.API_SIGNATURE, shaGenerator.getApiSignature());
        apiParams.put(Constant.PUB_API_KEY, APIConfiguration.apiKey);
        apiParams.put(Constant.API_NONCE, shaGenerator.getApiNonce());
        apiParams.put(Constant.API_TIMESTAMP, shaGenerator.getApiTimeStamp());
        apiParams.put(Constant.API_KIT, Constant.SDK_TYPE);

        if (optionalParams != null) {
            for (Map.Entry<String, String> entry : optionalParams.entrySet()) {
                apiParams.put(entry.getKey(), entry.getValue());
            }
        }

        if (NetworkService.isNetworkAvailable(mContext)) {
            Call<JsonObject> call = apiService.callUpdateWatermark(watermarkID, apiParams);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.body() != null) {
                        callback.success(response.body().getAsJsonObject());
                    } else if (response.errorBody() != null) {
                        try {
                            callback.failure(response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    callback.failure(t.getMessage());
                }
            });
        } else {
            callback.failure(mContext.getString(R.string.no_network_found));
        }
    }


    /**
     * This endpoint deletes specific watermark info based on it's id.
     *
     * @param watermarkID Unique alphanumeric name (id) of watermark.
     * @param callback    It is used to provide success or failure response.
     */
    public void deleteWatermark(String watermarkID, final PublitioCallback<JsonObject> callback) {

        if (APIConfiguration.apiKey == null || APIConfiguration.apiKey.isEmpty())
            return;

        SHAGenerator shaGenerator = new SHAGenerator();

        ApiInterface apiService = APIClient.getClient().create(ApiInterface.class);

        Map<String, String> apiParams = new HashMap<>();
        apiParams.put(Constant.API_SIGNATURE, shaGenerator.getApiSignature());
        apiParams.put(Constant.PUB_API_KEY, APIConfiguration.apiKey);
        apiParams.put(Constant.API_NONCE, shaGenerator.getApiNonce());
        apiParams.put(Constant.API_TIMESTAMP, shaGenerator.getApiTimeStamp());
        apiParams.put(Constant.API_KIT, Constant.SDK_TYPE);

        if (NetworkService.isNetworkAvailable(mContext)) {
            Call<JsonObject> call = apiService.callDeleteWatermark(watermarkID, apiParams);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.body() != null) {
                        callback.success(response.body().getAsJsonObject());
                    } else if (response.errorBody() != null) {
                        callback.failure(response.message());
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    callback.failure(t.getMessage());
                }
            });
        } else {
            callback.failure(mContext.getString(R.string.no_network_found));
        }
    }

    @Override
    public void onProgressUpdate(int percentage) {
//        LogUtils.LOGD("Progress: ", " " +percentage);
    }

    @Override
    public void onError() {
//        LogUtils.LOGD("Progress: ", " Error");
    }

    @Override
    public void onFinish() {
//        LogUtils.LOGD("Progress: ", " Completed");
    }
}
