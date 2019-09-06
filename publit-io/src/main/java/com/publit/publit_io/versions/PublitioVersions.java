package com.publit.publit_io.versions;

import android.content.Context;

import com.google.gson.JsonObject;
import com.publit.publit_io.R;
import com.publit.publit_io.api.APIClient;
import com.publit.publit_io.api.ApiInterface;
import com.publit.publit_io.constant.Constant;
import com.publit.publit_io.constant.CreateVersionParams;
import com.publit.publit_io.utils.APIConfiguration;
import com.publit.publit_io.utils.NetworkService;
import com.publit.publit_io.utils.PublitioCallback;
import com.publit.publit_io.utils.SHAGenerator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PublitioVersions {

    private Context mContext;

    //Public Constructor
    public PublitioVersions(Context activity) {
        mContext = activity;
    }

    /**
     * This endpoint creates new file version, out of your original file.
     * You can use it to transcode your files betwean formats, for resizing,
     * croping, watermarking and/or quality ajustment.
     *
     * @param fileId         Original file id you wish to transform.
     * @param outputFormat   Extension (output format) of desired file version.
     * @param optionalParams List of Optional API Params.
     * @param callback       It is used to provide success or failure response.
     */
    public void createVersion(String fileId, String outputFormat, Map<String, String> optionalParams, final PublitioCallback<JsonObject> callback) {

        if (APIConfiguration.apiKey == null || APIConfiguration.apiKey.isEmpty())
            return;

        if (fileId == null || outputFormat == null) {
            callback.failure(mContext.getString(R.string.provide_output_format));
            return;
        }

        List<String> strings = new ArrayList<>();

        ApiInterface apiService = APIClient.getClient().create(ApiInterface.class);

        SHAGenerator shaGenerator = new SHAGenerator();

        Map<String, String> apiParams = new HashMap<>();
        apiParams.put(Constant.API_SIGNATURE, shaGenerator.getApiSignature());
        apiParams.put(Constant.PUB_API_KEY, APIConfiguration.apiKey);
        apiParams.put(Constant.API_NONCE, shaGenerator.getApiNonce());
        apiParams.put(Constant.API_TIMESTAMP, shaGenerator.getApiTimeStamp());
        apiParams.put(Constant.API_KIT, Constant.SDK_TYPE);

        if (optionalParams != null) {
            if (optionalParams.get(CreateVersionParams.RESIZING_WIDTH) != null && !optionalParams.get(CreateVersionParams.RESIZING_WIDTH).isEmpty()) {
                strings.add(Constant.WIDTH_CONSTANT + optionalParams.get(CreateVersionParams.RESIZING_WIDTH));
            }

            if (optionalParams.get(CreateVersionParams.RESIZING_HEIGHT) != null && !optionalParams.get(CreateVersionParams.RESIZING_HEIGHT).isEmpty()) {
                strings.add(Constant.HEIGHT_CONSTANT + optionalParams.get(CreateVersionParams.RESIZING_HEIGHT));
            }

            if (optionalParams.get(CreateVersionParams.CROPPING) != null && !optionalParams.get(CreateVersionParams.CROPPING).isEmpty()) {
                strings.add(optionalParams.get(CreateVersionParams.CROPPING));
            }

            if (optionalParams.get(CreateVersionParams.WATERMARKING) != null && !optionalParams.get(CreateVersionParams.WATERMARKING).isEmpty()) {
                strings.add(Constant.WATERMARK_CONSTANT + optionalParams.get(CreateVersionParams.WATERMARKING));
            }

            if (optionalParams.get(CreateVersionParams.QUALITY) != null && !optionalParams.get(CreateVersionParams.QUALITY).isEmpty()) {
                strings.add(Constant.QUALITY_CONSTANT + optionalParams.get(CreateVersionParams.QUALITY));
            }

            if (optionalParams.get(CreateVersionParams.TRIMMING_TIME) != null && !optionalParams.get(CreateVersionParams.TRIMMING_TIME).isEmpty()) {
                strings.add(Constant.TIME_CONSTANT + optionalParams.get(CreateVersionParams.TRIMMING_TIME));
            }

            if (optionalParams.get(CreateVersionParams.TRIMMING_START) != null && !optionalParams.get(CreateVersionParams.TRIMMING_START).isEmpty()) {
                strings.add(Constant.START_OFFSET + optionalParams.get(CreateVersionParams.TRIMMING_START));
            }

            if (optionalParams.get(CreateVersionParams.TRIMMING_END) != null && !optionalParams.get(CreateVersionParams.TRIMMING_END).isEmpty()) {
                strings.add(Constant.END_OFFSET + optionalParams.get(CreateVersionParams.TRIMMING_END));
            }
        }

        if (strings.size() > 0) {
            Iterator<String> stringIterator = strings.iterator();
            if (stringIterator.hasNext()) {
                StringBuilder buffer = new StringBuilder(stringIterator.next());
                while (stringIterator.hasNext()) buffer.append(",").append(stringIterator.next());
                apiParams.put(CreateVersionParams.OPTIONS, buffer.toString());
            }
        }

        if (NetworkService.isNetworkAvailable(mContext)) {

            Call<JsonObject> call = apiService.callCreateVersion(fileId, outputFormat, apiParams);
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

        } else {
            callback.failure(mContext.getString(R.string.no_network_found));
        }
    }

    /**
     * This endpoint lists all versions for specific file.
     *
     * @param fileID         Original file id you wish to retrieve versions for.
     * @param optionalParams List of Optional API Params.
     * @param callback       It is used to provide success or failure response.
     */
    public void versionsList(String fileID, Map<String, String> optionalParams, final PublitioCallback<JsonObject> callback) {

        if (APIConfiguration.apiKey == null || APIConfiguration.apiKey.isEmpty())
            return;

        if (fileID == null) {
            callback.failure(mContext.getString(R.string.provide_file_id));
            return;
        }

        ApiInterface apiService = APIClient.getClient().create(ApiInterface.class);

        SHAGenerator shaGenerator = new SHAGenerator();

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

            Call<JsonObject> call = apiService.callVersionsList(fileID, apiParams);
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

        } else {
            callback.failure(mContext.getString(R.string.no_network_found));
        }
    }

    /**
     * This endpoint shows specific file version info based on it's id.
     *
     * @param versionID Unique alphanumeric name (id) of version.
     * @param callback  It is used to provide success or failure response.
     */
    public void showVersion(String versionID, final PublitioCallback<JsonObject> callback) {

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

            Call<JsonObject> call = apiService.callShowVersion(versionID, apiParams);
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
     * This endpoint updates specific file version info based on it's id.
     *
     * @param versionID Unique alphanumeric name (id) of version.
     * @param callback  It is used to provide success or failure response.
     */
    public void updateVersion(String versionID, final PublitioCallback<JsonObject> callback) {

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

            Call<JsonObject> call = apiService.callUpdateVersion(versionID, apiParams);
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
     * This endpoint re-converts specific file version info based on it's id.
     * Useful for failed versions re-creation.
     *
     * @param versionID Unique alphanumeric name (id) of version.
     * @param callback  It is used to provide success or failure response.
     */
    public void reconvertVersion(String versionID, final PublitioCallback<JsonObject> callback) {

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
            Call<JsonObject> call = apiService.callReconvertVersion(versionID, apiParams);
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
     * This endpoint deletes specific file version based on it's id.
     *
     * @param versionID Unique alphanumeric id of version.
     * @param callback  It is used to provide success or failure response.
     */
    public void deleteVersion(String versionID, final PublitioCallback<JsonObject> callback) {

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

            Call<JsonObject> call = apiService.callDeleteVersion(versionID, apiParams);
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
}
