package com.publit.publit_io.folders;

import android.content.Context;

import com.google.gson.JsonObject;
import com.publit.publit_io.R;
import com.publit.publit_io.api.APIClient;
import com.publit.publit_io.api.ApiInterface;
import com.publit.publit_io.constant.Constant;
import com.publit.publit_io.utils.APIConfiguration;
import com.publit.publit_io.utils.LogUtils;
import com.publit.publit_io.utils.NetworkService;
import com.publit.publit_io.utils.PublitioCallback;
import com.publit.publit_io.utils.SHAGenerator;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * It contains all the operations related to Folders.
 */
public class PublitioFolders {

    private Context mContext;

    //Public Constructor
    public PublitioFolders(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * This endpoint creates new folder.
     *
     * @param folderName     Unique name (id) of folder.
     * @param optionalParams List of Optional API Params.
     * @param callback       It is used to provide success or failure response.
     */
    public void createFolder(String folderName, Map<String, String> optionalParams, final PublitioCallback<JsonObject> callback) {

        if (APIConfiguration.apiKey == null || APIConfiguration.apiKey.isEmpty())
            return;

        if (folderName == null || folderName.isEmpty()) {
            callback.failure(mContext.getString(R.string.provide_folder_name));
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
            Call<JsonObject> call = apiService.callCreateFolder(folderName, apiParams);
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
                    LogUtils.LOGD(PublitioFolders.class.getSimpleName(), t.getMessage());
                    callback.failure(t.getMessage());
                }
            });
        } else {
            callback.failure(mContext.getString(R.string.no_network_found));
        }
    }

    /**
     * This endpoint lists folders.
     *
     * @param optionalParams List of Optional API Params.
     * @param callback       It is used to provide success or failure response.
     */
    public void foldersList(Map<String, String> optionalParams, final PublitioCallback<JsonObject> callback) {

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
            Call<JsonObject> call = apiService.callFoldersList(apiParams);
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
                    LogUtils.LOGD(PublitioFolders.class.getSimpleName(), t.getMessage());
                    callback.failure(t.getMessage());
                }
            });
        } else {
            callback.failure(mContext.getString(R.string.no_network_found));
        }
    }

    /**
     * This endpoint shows specific folder info based on it's id.
     *
     * @param folderID Unique alphanumeric name (id) of folder.
     * @param callback It is used to provide success or failure response.
     */
    public void showFolder(String folderID, final PublitioCallback<JsonObject> callback) {

        if (APIConfiguration.apiKey == null || APIConfiguration.apiKey.isEmpty())
            return;

        if (folderID == null || folderID.isEmpty()) {
            callback.failure(mContext.getString(R.string.provide_folder_id));
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
            Call<JsonObject> call = apiService.callShowFolder(folderID, apiParams);
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
                    LogUtils.LOGD(PublitioFolders.class.getSimpleName(), t.getMessage());
                    callback.failure(t.getMessage());
                }
            });
        } else {
            callback.failure(mContext.getString(R.string.no_network_found));
        }
    }

    /**
     * This endpoint updates specific folder info based on it's id.
     *
     * @param folderID       Unique alphanumeric id of folder.
     * @param optionalParams List of Optional API Params.
     * @param callback       It is used to provide success or failure response.
     */
    public void updateFolder(String folderID, Map<String, String> optionalParams, final PublitioCallback<JsonObject> callback) {

        if (APIConfiguration.apiKey == null || APIConfiguration.apiKey.isEmpty())
            return;

        if (folderID == null || folderID.isEmpty()) {
            callback.failure(mContext.getString(R.string.provide_folder_id));
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

            Call<JsonObject> call = apiService.callUpdateFolder(folderID, apiParams);
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
                    LogUtils.LOGD(PublitioFolders.class.getSimpleName(), t.getMessage());
                    callback.failure(t.getMessage());
                }
            });
        } else {
            callback.failure(mContext.getString(R.string.no_network_found));
        }

    }

    /**
     * This endpoint deletes specific folder based on it's id.
     *
     * @param folderID Unique alphanumeric id of folder.
     * @param callback It is used to provide success or failure response.
     */
    public void deleteFolder(String folderID, final PublitioCallback<JsonObject> callback) {

        if (APIConfiguration.apiKey == null || APIConfiguration.apiKey.isEmpty())
            return;

        if (folderID == null || folderID.isEmpty()) {
            callback.failure(mContext.getString(R.string.provide_folder_id));
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

            Call<JsonObject> call = apiService.callDeleteFolder(folderID, apiParams);
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
                    LogUtils.LOGD(PublitioFolders.class.getSimpleName(), t.getMessage());
                    callback.failure(t.getMessage());
                }
            });
        } else {
            callback.failure(mContext.getString(R.string.no_network_found));
        }
    }

    /**
     * This endpoint lists entire folders tree for an account.
     *
     * @param callback It is used to provide success or failure response.
     */
    public void treeFolders(final PublitioCallback<JsonObject> callback) {

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

            Call<JsonObject> call = apiService.callTreeFolder(apiParams);
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
                    LogUtils.LOGD(PublitioFolders.class.getSimpleName(), t.getMessage());
                    callback.failure(t.getMessage());
                }
            });
        } else {
            callback.failure(mContext.getString(R.string.no_network_found));
        }
    }
}
