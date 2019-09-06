package com.publit.publit_io.files;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.publit.publit_io.R;
import com.publit.publit_io.api.APIClient;
import com.publit.publit_io.api.ApiInterface;
import com.publit.publit_io.api.request.ProgressRequestBody;
import com.publit.publit_io.constant.Constant;
import com.publit.publit_io.utils.APIConfiguration;
import com.publit.publit_io.utils.FileUtils;
import com.publit.publit_io.utils.LogUtils;
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
 * It contains all the operations related to Files.
 */
public class PublitioFiles implements ProgressRequestBody.UploadCallbacks {

    private Context mContext;

    //Public Constructor
    public PublitioFiles(Context context) {
        mContext = context;
    }


    /**
     * This endpoint retrieves list of files.
     *
     * @param optionalParams List of Optional API Params.
     * @param callback       It is used to provide success or failure response.
     */
    public void filesList(Map<String, String> optionalParams, final PublitioCallback<JsonObject> callback) {

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

        if (optionalParams != null) {
            for (Map.Entry<String, String> entry : optionalParams.entrySet()) {
                apiParams.put(entry.getKey(), entry.getValue());
            }
        }

        if (NetworkService.isNetworkAvailable(mContext)) {
            Call<JsonObject> call = apiService.callFilesList(apiParams);
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
                    LogUtils.LOGD(PublitioFiles.class.getSimpleName(), t.getMessage());
                    callback.failure(t.getMessage());
                }
            });
        } else {
            callback.failure(mContext.getString(R.string.no_network_found));
        }
    }


    /**
     * This endpoint shows specific file info based on it's id.
     *
     * @param fileID   Unique alphanumeric name (id) of file.
     * @param callback It is used to provide success or failure response.
     */
    public void showFile(String fileID, final PublitioCallback<JsonObject> callback) {

        if (APIConfiguration.apiKey == null || APIConfiguration.apiKey.isEmpty())
            return;

        if (fileID == null) {
            callback.failure(mContext.getString(R.string.provide_file_id));
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

        if (NetworkService.isNetworkAvailable(mContext)) {

            Call<JsonObject> call = apiService.callShowFile(fileID, apiParams);
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
                    LogUtils.LOGD(PublitioFiles.class.getSimpleName(), new Gson().toJson(t.getMessage()));
                    callback.failure(new Gson().toJson(t.getMessage()));
                }
            });
        } else {
            callback.failure(mContext.getString(R.string.no_network_found));
        }
    }


    /**
     * This endpoint update specific file info based on it's id.
     *
     * @param fileID         Unique alphanumeric name (id) of file.
     * @param optionalParams List of Optional API Params.
     * @param callback       It is used to provide success or failure response.
     */
    public void updateFile(String fileID, Map<String, String> optionalParams, final PublitioCallback<JsonObject> callback) {

        if (APIConfiguration.apiKey == null || APIConfiguration.apiKey.isEmpty())
            return;

        if (fileID == null) {
            callback.failure(mContext.getString(R.string.provide_file_id));
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

            Call<JsonObject> call = apiService.callUpdateFile(fileID, apiParams);
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
                    LogUtils.LOGD(PublitioFiles.class.getSimpleName(), new Gson().toJson(t.getMessage()));
                    callback.failure(new Gson().toJson(t.getMessage()));
                }
            });
        } else {
            callback.failure(mContext.getString(R.string.no_network_found));
        }
    }


    /**
     * This endpoint deletes specific file info based on it's id.
     *
     * @param fileID   Unique alphanumeric name (id) of file.
     * @param callback It is used to provide success or failure response.
     */
    public void deleteFile(String fileID, final PublitioCallback<JsonObject> callback) {

        if (APIConfiguration.apiKey == null || APIConfiguration.apiKey.isEmpty())
            return;

        if (fileID == null) {
            callback.failure(mContext.getString(R.string.provide_file_id));
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

        if (NetworkService.isNetworkAvailable(mContext)) {

            Call<JsonObject> call = apiService.callDeleteFie(fileID, apiParams);
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
                    LogUtils.LOGD(PublitioFiles.class.getSimpleName(), new Gson().toJson(t.getMessage()));
                    callback.failure(new Gson().toJson(t.getMessage()));
                }
            });
        } else {
            callback.failure(mContext.getString(R.string.no_network_found));
        }
    }


    /**
     * This endpoint creates (uploads) new file.
     *
     * Right now you can upload following file types:
     * Images:- jpg, jpeg, jpe, png, gif, bmp, psd, webp, ai, tif and tiff
     * Videos:- mp4, webm, ogv, avi, mov, flv, 3gp, 3g2, wmv, mpeg and mkv
     * Audios:- mp3, wav, ogg, aac, aiff, amr, ac3, au, flac, m4a, aac, ra, voc and wma
     *
     * @param fileUri        Image or Video to upload as file.
     * @param optionalParams List of Optional API Params.
     * @param callback       It is used to provide success or failure response.
     */
    public void uploadFile(Uri fileUri, Map<String, String> optionalParams, final PublitioCallback<JsonObject> callback) {

        if (APIConfiguration.apiKey == null || APIConfiguration.apiKey.isEmpty())
            return;

        ApiInterface apiService = APIClient.getClient().create(ApiInterface.class);

        SHAGenerator shaGenerator = new SHAGenerator();

        if (fileUri == null) {
            callback.failure(mContext.getString(R.string.provide_file_uri));
            return;
        }

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

            Call<JsonObject> call = apiService.callCreateFile(fileData, apiParams);
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
                    LogUtils.LOGD(PublitioFiles.class.getSimpleName(), t.getMessage());
                    callback.failure(t.getMessage());
                }
            });
        } else {
            callback.failure(mContext.getString(R.string.no_network_found));
        }
    }


    @Override
    public void onProgressUpdate(int percentage) {
//        Log.d(TAG, "onProgressUpdate: " + percentage);
    }

    @Override
    public void onError() {

    }

    @Override
    public void onFinish() {
//        Log.d(TAG, "onFinish: " + "100%");
    }
}
