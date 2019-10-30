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
import com.publit.publit_io.constant.CreateFileParams;
import com.publit.publit_io.constant.FilesExtensionParams;
import com.publit.publit_io.constant.FilesResolutions;
import com.publit.publit_io.exception.PublitioExceptions;
import com.publit.publit_io.utils.APIConfiguration;
import com.publit.publit_io.utils.FileUtils;
import com.publit.publit_io.utils.LogUtils;
import com.publit.publit_io.utils.NetworkService;
import com.publit.publit_io.utils.PublitioCallback;
import com.publit.publit_io.utils.SHAGenerator;
import com.vincent.videocompressor.VideoCompress;

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

    private String fileName;

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

        if (!isValidated(callback)) {
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

        if (!isValidated(callback)) {
            return;
        }

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

        if (!isValidated(callback)) {
            return;
        }

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

        if (!isValidated(callback)) {
            return;
        }

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
     * Right now you can upload following file types:
     * Images:- jpg, jpeg, jpe, png, gif, bmp, psd, webp, ai, tif and tiff
     * Videos:- mp4, webm, ogv, avi, mov, flv, 3gp, 3g2, wmv, mpeg and mkv
     * Audios:- mp3, wav, ogg, aac, aiff, amr, ac3, au, flac, m4a, aac, ra, voc and wma
     *
     * @param fileUri        Image or Video to upload as file.
     * @param optionalParams List of Optional API Params.
     * @param callback       It is used to provide success or failure response.
     */
    public void uploadFile(Uri fileUri, Map<String, String> optionalParams, final PublitioCallback<JsonObject> callback) throws PublitioExceptions {

        if (fileUri == null) {
            callback.failure(mContext.getString(R.string.provide_file_uri));
            return;
        }

        // use the FileUtils to get the actual file by uri
        File file = FileUtils.getFile(mContext, fileUri);
        String resolution = "";

        ContentResolver cR = mContext.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        String type = mime.getExtensionFromMimeType(cR.getType(fileUri));

        if (optionalParams != null) {
            for (Map.Entry<String, String> entry : optionalParams.entrySet()) {
                if (entry.getKey().equals(CreateFileParams.RESOLUTION)) {
                    if (type != null && !type.isEmpty()) {
                        if (type.equals(FilesExtensionParams.MP4) ||
                                type.equals(FilesExtensionParams.WEBM) ||
                                type.equals(FilesExtensionParams.OGV) ||
                                type.equals(FilesExtensionParams.AVI) ||
                                type.equals(FilesExtensionParams.MOV) ||
                                type.equals(FilesExtensionParams.FLV) ||
                                type.equals(FilesExtensionParams.THREE_GP) ||
                                type.equals(FilesExtensionParams.THREE_G2) ||
                                type.equals(FilesExtensionParams.WMV) ||
                                type.equals(FilesExtensionParams.MPEG) ||
                                type.equals(FilesExtensionParams.MKV)) {
                            resolution = entry.getValue();
                            Constant.IS_COMPRESSED_VIDEO = true;
                        }
                    }
                }
            }
        }


        if (Constant.IS_COMPRESSED_VIDEO) {
            compressVideo(file, resolution, optionalParams, callback);
        } else {
            uploadFileOnServer(fileUri, optionalParams, callback);
        }

    }

    /**
     * To compress video.
     *
     * @param file           The file to be convert.
     * @param resolution     The resolution to compress video.
     * @param optionalParams List of Optional API Params.
     * @param callback       It is used to provide success or failure response.
     */
    private void compressVideo(final File file, final String resolution, final Map<String, String> optionalParams, final PublitioCallback<JsonObject> callback) {
        final File mydir = new File(mContext.getFilesDir(), "");
        fileName = file.getName();

        if (resolution.equals(FilesResolutions.RES_LOW)) {

            VideoCompress.compressVideoLow(file.getAbsolutePath(), mydir.getAbsolutePath() + "/" + fileName, new VideoCompress.CompressListener() {
                @Override
                public void onStart() {

                }

                @Override
                public void onSuccess() {
                    uploadFileOnServer(Uri.fromFile(new File(mydir.getAbsolutePath() + "/" + file.getName())), optionalParams, callback);
                }

                @Override
                public void onFail() {

                }

                @Override
                public void onProgress(float percent) {

                }
            });
        } else {

            VideoCompress.compressVideoMedium(file.getAbsolutePath(), mydir.getAbsolutePath() + "/" + fileName, new VideoCompress.CompressListener() {
                @Override
                public void onStart() {

                }

                @Override
                public void onSuccess() {
                    uploadFileOnServer(Uri.fromFile(new File(mydir.getAbsolutePath() + "/" + file.getName())), optionalParams, callback);
                }

                @Override
                public void onFail() {

                }

                @Override
                public void onProgress(float percent) {

                }
            });
        }
    }

    /**
     * To upload file on the server.
     *
     * @param fileUri        Image or Video to upload as file.
     * @param optionalParams List of Optional API Params.
     * @param callback       It is used to provide success or failure response.
     */
    private void uploadFileOnServer(Uri fileUri, Map<String, String> optionalParams, final PublitioCallback<JsonObject> callback) {

        if (!isValidated(callback)) {
            return;
        }

        ApiInterface apiService = APIClient.getClient().create(ApiInterface.class);

        SHAGenerator shaGenerator = new SHAGenerator();

        // use the FileUtils to get the actual file by uri
        File file = FileUtils.getFile(mContext, fileUri);

        String type = getFileExtention(fileUri);

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
                        if (Constant.IS_COMPRESSED_VIDEO) {
                            Constant.IS_COMPRESSED_VIDEO = false;
                        }

                        deleteCompressedFile();
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

    /**
     * Get the extention of the file.
     *
     * @param fileUri uri of the file.
     * @return extention of the file.
     */
    private String getFileExtention(Uri fileUri) {
        String[] arr = new String[0];
        if (fileName != null && !fileName.isEmpty())
            arr = fileName.split("\\.");
        String type;
        if (Constant.IS_COMPRESSED_VIDEO) {
            type = arr[1];
        } else {
            ContentResolver cR = mContext.getContentResolver();
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            type = mime.getExtensionFromMimeType(cR.getType(fileUri));
        }
        return type;
    }

    /**
     * Delete the compressed file.
     */
    private void deleteCompressedFile() {
        if (fileName != null && !fileName.isEmpty()) {
            File dir = mContext.getFilesDir();
            File file = new File(dir, fileName);
            file.delete();
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

    /**
     * To validate api key and api secret.
     *
     * @param callback It is used to provide success or failure response.
     * @return validation before api call.
     */
    private boolean isValidated(PublitioCallback<JsonObject> callback) {
        if (APIConfiguration.apiSecret == null || APIConfiguration.apiSecret.isEmpty() || APIConfiguration.apiKey == null || APIConfiguration.apiKey.isEmpty()) {
            callback.failure(mContext.getResources().getString(R.string.key_or_secret_not_found));
            return false;
        }
        return true;
    }
}
