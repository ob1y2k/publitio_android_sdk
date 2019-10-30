package com.publit.io;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.publit.io.constant.Constants;
import com.publit.publit_io.constant.CreateFileParams;
import com.publit.publit_io.constant.FilesADParams;
import com.publit.publit_io.constant.FilesDownloadParams;
import com.publit.publit_io.constant.FilesListParams;
import com.publit.publit_io.constant.FilesPrivacyParams;
import com.publit.publit_io.constant.FilesResolutions;
import com.publit.publit_io.constant.FilesTransformationParams;
import com.publit.publit_io.constant.OrderParams;
import com.publit.publit_io.constant.UpdateFileParams;
import com.publit.publit_io.exception.PublitioExceptions;
import com.publit.publit_io.utils.Publitio;
import com.publit.publit_io.utils.PublitioCallback;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Class is design to test Publit.io Files Operations.
 */
public class FilesActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTextViewResponse;
    private ProgressDialog dialog;

    //Custom Dialog for selecting file to upload.
    private Dialog mDialogChooseFile;

    //Custom Dialog for selecting formate of file.
    private Dialog mDialogChooseFormate;

    //Custom Dialog for User Inputs.
    private Dialog mDialogUserInput;

    private EditText mEditTextUserInput;
    private EditText mEditTextUserInputTitle;

    //Publitio class object to call Publit.io API's.
    Publitio mPublitio;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);

        init();
    }

    /**
     * Initialize UI Components and Publitio object.
     */
    private void init() {

        //Initialize Publitio object.
        mPublitio = new Publitio(this);

        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);

        //Find views.
        TextView mTextViewCreate = findViewById(R.id.create_tv);
        TextView mTextViewList = findViewById(R.id.list_tv);
        TextView mTextViewShow = findViewById(R.id.show_tv);
        TextView mTextViewUpdate = findViewById(R.id.update_tv);
        TextView mTextViewDelete = findViewById(R.id.delete_tv);
        mTextViewResponse = findViewById(R.id.response_tv);

        //Initialize Click Listners.
        mTextViewCreate.setOnClickListener(this);
        mTextViewList.setOnClickListener(this);
        mTextViewShow.setOnClickListener(this);
        mTextViewUpdate.setOnClickListener(this);
        mTextViewDelete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.create_tv:

                mDialogChooseFile = new Dialog(this);
                mDialogChooseFile.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                mDialogChooseFile.setContentView(inflater.inflate(R.layout.dialog_choose_file, null));
                mDialogChooseFile.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                //Custom Dialog UI components.
                LinearLayout mLinearLayoutCamera = mDialogChooseFile.findViewById(R.id.profile_camera);
                LinearLayout mLinearLayoutGallery = mDialogChooseFile.findViewById(R.id.profile_gallery);
                TextView mTextViewCancel = mDialogChooseFile.findViewById(R.id.profile_cancel);
                mLinearLayoutCamera.setOnClickListener(this);
                mLinearLayoutGallery.setOnClickListener(this);

                mTextViewCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mDialogChooseFile.dismiss();

                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }
                });

                mDialogChooseFile.show();

                break;

            case R.id.show_tv:

                initializeUserInput(Constants.SHOW_API);

                break;

            case R.id.list_tv:

                dialog.setMessage(getString(R.string.call_api_message));
                dialog.show();

                Map<String, String> list = new HashMap<>();
                list.put(FilesListParams.ORDER, OrderParams.NAME_ASC);
//                list.put(FilesListParams.FILTER_PRIVACY, FilesPrivacyFilterParams.ALL);
//                list.put(FilesListParams.FILTER_EXTENSION, FilesExtensionParams.JPE);
//                list.put(FilesListParams.FILTER_TYPE, FilesTypeParams.IMAGE);
//                list.put(FilesListParams.FILTER_AD, FilesADFilterParams.ALL);

                //Calling Publit.io files list api.
                mPublitio.files().filesList(list, new PublitioCallback<JsonObject>() {
                    @Override
                    public void success(JsonObject result) {
                        mTextViewResponse.setText("");
                        mTextViewResponse.setText(result.toString());
                        dialog.dismiss();
                    }

                    @Override
                    public void failure(String message) {
                        mTextViewResponse.setText("");
                        mTextViewResponse.setText(message);
                        dialog.dismiss();
                    }
                });

                break;

            case R.id.update_tv:

                initializeUserInput(Constants.UPDATE_API);

                break;

            case R.id.delete_tv:

                initializeUserInput(Constants.DELETE_API);

                break;

            case R.id.profile_camera:

                mDialogChooseFormate = new Dialog(this);
                mDialogChooseFormate.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                LayoutInflater inflaters = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                mDialogChooseFormate.setContentView(inflaters.inflate(R.layout.dialog_choose_format, null));
                mDialogChooseFormate.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                TextView mTextViewImage = mDialogChooseFormate.findViewById(R.id.image_tv);
                TextView mTextViewVideo = mDialogChooseFormate.findViewById(R.id.video_tv);

                mTextViewImage.setOnClickListener(this);
                mTextViewVideo.setOnClickListener(this);

                mDialogChooseFormate.show();

                break;

            case R.id.profile_gallery:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.REQUEST_CODE_GALLERY);
                        mDialogChooseFile.dismiss();
                    } else {
                        dispatchChoosePictureIntent();
                    }

                } else {

                    dispatchChoosePictureIntent();

                }
                break;

            case R.id.image_tv:

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                        requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.REQUEST_CODE_CAMERA);

                    } else {

                        dispatchTakePictureIntent();

                    }
                } else {

                    dispatchTakePictureIntent();
                }
                break;

            case R.id.video_tv:

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                        requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.REQUEST_CODE_CAMERA);

                    } else {

                        dispatchTakeVideoIntent();

                    }
                } else {

                    dispatchTakeVideoIntent();
                }
                break;
        }
    }

    /**
     * Take User input and call API's.
     *
     * @param operation Describes the operation to perform.
     */
    private void initializeUserInput(final String operation) {

        mDialogUserInput = new Dialog(this);
        mDialogUserInput.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflaterUser = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mDialogUserInput.setContentView(inflaterUser.inflate(R.layout.dialog_user_input, null));
        mDialogUserInput.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        mEditTextUserInput = mDialogUserInput.findViewById(R.id.user_input_tv);
        mEditTextUserInputTitle = mDialogUserInput.findViewById(R.id.user_input_title_tv);
        TextView mTextViewOk = mDialogUserInput.findViewById(R.id.profile_ok);

        if (operation.equals(Constants.UPDATE_API)) {
            mEditTextUserInputTitle.setVisibility(View.VISIBLE);
        } else {
            mEditTextUserInputTitle.setVisibility(View.GONE);
        }

        mTextViewOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDialogUserInput.dismiss();
                dialog.setMessage("Calling " + operation + " API.");
                dialog.show();

                switch (operation) {

                    case Constants.SHOW_API:
                        if (!mEditTextUserInput.getText().toString().isEmpty()) {
                            //Calling Publit.io show file api.
                            mPublitio.files().showFile(mEditTextUserInput.getText().toString(), new PublitioCallback<JsonObject>() {
                                @Override
                                public void success(JsonObject result) {
                                    mTextViewResponse.setText("");
                                    mTextViewResponse.setText(result.toString());
                                    dialog.dismiss();
                                }

                                @Override
                                public void failure(String message) {
                                    mTextViewResponse.setText("");
                                    mTextViewResponse.setText(message);
                                    dialog.dismiss();
                                }
                            });
                        } else {
                            if (dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            Toast.makeText(FilesActivity.this, R.string.provide_id, Toast.LENGTH_LONG).show();
                        }
                        break;

                    case Constants.UPDATE_API:
                        if (!mEditTextUserInput.getText().toString().isEmpty() && !mEditTextUserInputTitle.getText().toString().isEmpty()) {

                            Map<String, String> update = new HashMap<>();
                            update.put(UpdateFileParams.PRIVACY, FilesPrivacyParams.PUBLIC);
                            update.put(UpdateFileParams.OPTION_DOWNLOAD, FilesDownloadParams.ENABLE);
                            update.put(UpdateFileParams.OPTION_TRANSFORM, FilesTransformationParams.ENABLE);
                            update.put(UpdateFileParams.OPTION_AD, FilesADParams.ENABLE);
                            update.put(UpdateFileParams.TITLE, mEditTextUserInputTitle.getText().toString());

                            //Calling Publit.io update file api.
                            mPublitio.files().updateFile(mEditTextUserInput.getText().toString(), update, new PublitioCallback<JsonObject>() {
                                @Override
                                public void success(JsonObject result) {
                                    mTextViewResponse.setText("");
                                    mTextViewResponse.setText(result.toString());
                                    dialog.dismiss();
                                }

                                @Override
                                public void failure(String message) {
                                    mTextViewResponse.setText("");
                                    mTextViewResponse.setText(message);
                                    dialog.dismiss();
                                }
                            });
                        } else {
                            if (dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            Toast.makeText(FilesActivity.this, R.string.provide_id, Toast.LENGTH_LONG).show();
                        }
                        break;

                    case Constants.DELETE_API:
                        if (!mEditTextUserInput.getText().toString().isEmpty()) {
                            //Calling Publit.io delete file api.
                            mPublitio.files().deleteFile(mEditTextUserInput.getText().toString(), new PublitioCallback<JsonObject>() {
                                @Override
                                public void success(JsonObject result) {
                                    mTextViewResponse.setText("");
                                    mTextViewResponse.setText(result.toString());
                                    dialog.dismiss();
                                }

                                @Override
                                public void failure(String message) {
                                    mTextViewResponse.setText("");
                                    mTextViewResponse.setText(message);
                                    dialog.dismiss();
                                }
                            });
                        } else {
                            if (dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            Toast.makeText(FilesActivity.this, R.string.provide_id, Toast.LENGTH_LONG).show();
                        }

                        break;
                }
            }
        });

        mDialogUserInput.show();

    }

    /**
     * Choose Images or Videos from Gallery.
     */
    public void dispatchChoosePictureIntent() {

        if (Build.VERSION.SDK_INT < 19) {
            Intent intent = new Intent();
            intent.setType("image/*,video/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), Constants.PICK_IMAGE);
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            intent.setType("video/*");
            startActivityForResult(intent, Constants.PICK_IMAGE);
        }

    }

    /**
     * Capture Photo from Camera.
     */
    public void dispatchTakePictureIntent() {
        //Capture Image
        Intent captureImageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (captureImageIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(captureImageIntent, Constants.REQUEST_IMAGE_CAPTURE);
        }
    }

    /**
     * Capture Video from Camera.
     */
    public void dispatchTakeVideoIntent() {
        //Capture Video
        Intent captureVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        if (captureVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(captureVideoIntent, Constants.REQUEST_IMAGE_CAPTURE);
        }
    }

    /**
     * Get URI of the Captured Image.
     *
     * @param inContext Activity context.
     * @param inImage   Bitmap of captured image.
     * @return URI of the Captured Image.
     */
    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        dialog.setMessage(getString(R.string.upload_api_call));
        dialog.show();

        if (data == null) {
            dialog.dismiss();
        }
        if (requestCode == Constants.PICK_IMAGE) {

            mDialogChooseFile.dismiss();

            if (data != null) {
                Uri uri = data.getData();

                Map<String, String> create = new HashMap<>();
                create.put(CreateFileParams.PRIVACY, FilesPrivacyParams.PUBLIC);
                create.put(CreateFileParams.OPTION_DOWNLOAD, FilesDownloadParams.DISABLE);
                create.put(CreateFileParams.OPTION_TRANSFORM, FilesTransformationParams.DISABLE);
                create.put(CreateFileParams.OPTION_AD, FilesADParams.ENABLE);
                create.put(CreateFileParams.RESOLUTION, FilesResolutions.RES_LOW);
//                create.put(FilesContants.CreateFile.TITLE, "PICK_IMAGE");

                //Calling Publit.io upload file api.
                try {
                    mPublitio.files().uploadFile(uri, create, new PublitioCallback<JsonObject>() {
                        @Override
                        public void success(JsonObject result) {
                            mTextViewResponse.setText("");
                            mTextViewResponse.setText(result.toString());
                            dialog.dismiss();
                        }

                        @Override
                        public void failure(String message) {
                            mTextViewResponse.setText("");
                            mTextViewResponse.setText(message);
                            dialog.dismiss();
                        }
                    });
                } catch (PublitioExceptions publitioExceptions) {
                    mTextViewResponse.setText("");
                    mTextViewResponse.setText(publitioExceptions.toString());
                    dialog.dismiss();
                }
            }

        } else if (requestCode == Constants.REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            if (data != null) {

                mDialogChooseFile.dismiss();
                mDialogChooseFormate.dismiss();

                Uri uri = data.getData();

                if (uri == null) {
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    uri = getImageUri(this, imageBitmap);
                }

                Map<String, String> create = new HashMap<>();
                create.put(CreateFileParams.PRIVACY, FilesPrivacyParams.PRIVATE);
                create.put(CreateFileParams.OPTION_DOWNLOAD, FilesDownloadParams.ENABLE);
                create.put(CreateFileParams.OPTION_TRANSFORM, FilesTransformationParams.ENABLE);
                create.put(CreateFileParams.OPTION_AD, FilesADParams.NEW);
                create.put(CreateFileParams.RESOLUTION, FilesResolutions.RES_LOW);

//                create.put(CreateFileParams.TITLE, "CAPTURE_IMAGE");


                //Calling Publit.io upload file api.
                try {
                    mPublitio.files().uploadFile(uri, create, new PublitioCallback<JsonObject>() {
                        @Override
                        public void success(JsonObject result) {
                            mTextViewResponse.setText("");
                            mTextViewResponse.setText(result.toString());
                            dialog.dismiss();
                        }

                        @Override
                        public void failure(String message) {
                            mTextViewResponse.setText("");
                            mTextViewResponse.setText(message);
                            dialog.dismiss();
                        }
                    });
                } catch (PublitioExceptions publitioExceptions) {
                    mTextViewResponse.setText("");
                    mTextViewResponse.setText(publitioExceptions.toString());
                    dialog.dismiss();
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
