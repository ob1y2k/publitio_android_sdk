package com.publit.publit_io.files;

import android.content.ContentResolver;
import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.publit.publit_io.R;
import com.publit.publit_io.api.APIClient;
import com.publit.publit_io.api.ApiInterface;
import com.publit.publit_io.api.request.ProgressRequestBody;
import com.publit.publit_io.constant.Constant;
import com.publit.publit_io.constant.CreateFileParams;
import com.publit.publit_io.constant.FilesExtensionParams;
import com.publit.publit_io.exception.PublitioExceptions;
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

    private FFmpeg ffmpeg;

    private String fileName;

    //Public Constructor
    public PublitioFiles(Context context) {
        mContext = context;
        ffmpeg = FFmpeg.getInstance(mContext);
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
     * <p>
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
                            if (!performValidations(resolution, fileUri)) {
                                Constant.IS_COMPRESSED_VIDEO = false;
                                throw new PublitioExceptions(mContext.getResources().getString(R.string.validation_message));
                            }

                            Constant.IS_COMPRESSED_VIDEO = true;
                        }
                    }
                }
            }
        }


        if (Constant.IS_COMPRESSED_VIDEO) {
            loadFFMpegBinary(file, resolution, optionalParams, callback);
        } else {
            uploadFileOnServer(fileUri, optionalParams, callback);
        }

    }

    private boolean performValidations(String resolution, Uri fileUri) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(mContext, fileUri);
        int orignalFileWidth = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
        int orignalFileHeight = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
        retriever.release();

        String[] arr;
        arr = resolution.split(":");
        String selectedHeight = arr[0];
        String selectedWidth = arr[1];

        if (orignalFileHeight <= Integer.parseInt(selectedHeight) || orignalFileWidth <= Integer.parseInt(selectedWidth)) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * To check if device supports FFMPEG.
     *
     * @param file           The file to be convert.
     * @param resolution     The resolution to compress video.
     * @param optionalParams List of Optional API Params.
     * @param callback       It is used to provide success or failure response.
     */
    private void loadFFMpegBinary(final File file, final String resolution, final Map<String, String> optionalParams, final PublitioCallback<JsonObject> callback) {
        try {
            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {
                @Override
                public void onFailure() {
                    callback.failure(mContext.getResources().getString(R.string.device_not_supported));
                }

                @Override
                public void onSuccess() {
                    compressVideo(file, resolution, optionalParams, callback);
                }
            });
        } catch (FFmpegNotSupportedException e) {
            callback.failure(mContext.getResources().getString(R.string.device_not_supported));
        }
    }

    /**
     * To create video compressed command.
     *
     * @param file           The file to be convert.
     * @param resolution     The resolution to compress video.
     * @param optionalParams List of Optional API Params.
     * @param callback       It is used to provide success or failure response.
     */
    private void compressVideo(File file, String resolution, Map<String, String> optionalParams, PublitioCallback<JsonObject> callback) {
        File mydir = new File(mContext.getFilesDir(), "");
        fileName = file.getName();
        String cmd = "-y -i " + file.getAbsolutePath() + " -vf scale=" + resolution + " -c:v libx264 -preset veryslow -crf 24 " + mydir.getAbsolutePath() + "/" + fileName;
        String[] command = cmd.split(" ");
        executeCommand(command, mydir, file, optionalParams, callback);
    }

    /**
     * To execute FFMPEG command.
     *
     * @param command        FFMPEG command to compress video.
     * @param mydir          The file going to be create after conversion.
     * @param file           The file to be convert.
     * @param optionalParams List of Optional API Params.
     * @param callback       It is used to provide success or failure response.
     */
    private void executeCommand(final String[] command, final File mydir, final File file, final Map<String, String> optionalParams, final PublitioCallback<JsonObject> callback) {
        try {
            ffmpeg.execute(command, new ExecuteBinaryResponseHandler() {
                @Override
                public void onFailure(String s) {
                    callback.failure(s);
                }

                @Override
                public void onSuccess(String s) {
                    uploadFileOnServer(Uri.fromFile(new File(mydir.getAbsolutePath() + "/" + file.getName())), optionalParams, callback);
                }

                @Override
                public void onProgress(String s) {
                }

                @Override
                public void onStart() {
                }

                @Override
                public void onFinish() {
                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            // do nothing for now
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

        if (APIConfiguration.apiKey == null || APIConfiguration.apiKey.isEmpty())
            return;

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
}
