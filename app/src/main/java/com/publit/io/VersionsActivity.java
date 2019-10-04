package com.publit.io;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.publit.io.constant.Constants;
import com.publit.publit_io.constant.CreateVersionParams;
import com.publit.publit_io.constant.CroppingParams;
import com.publit.publit_io.constant.OrderParams;
import com.publit.publit_io.constant.VersionsListParams;
import com.publit.publit_io.utils.Publitio;
import com.publit.publit_io.utils.PublitioCallback;

import java.util.HashMap;
import java.util.Map;

public class VersionsActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTextViewResponse;
    private ProgressDialog dialog;

    //Custom Dialog for User Inputs.
    private Dialog mDialogUserInput;

    private EditText mEditTextUserInput;
    private EditText mEditTextUserInputTitle;
    private TextView mTextViewOk;

    //Publitio class object to call Publit.io API's.
    Publitio mPublitio;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_version);

        init();
    }

    private void init() {

        //Initialize Publitio object.
        mPublitio = new Publitio(this);

        dialog = new ProgressDialog(this);

        //Find views.
        TextView mTextViewCreate = findViewById(R.id.create_tv);
        TextView mTextViewList = findViewById(R.id.list_tv);
        TextView mTextViewShow = findViewById(R.id.show_tv);
        TextView mTextViewUpdate = findViewById(R.id.update_tv);
        TextView mTextViewDelete = findViewById(R.id.delete_tv);
        TextView mTextViewReconvert = findViewById(R.id.reconvert_tv);
        mTextViewResponse = findViewById(R.id.response_tv);

        //Initialize Click Listners.
        mTextViewCreate.setOnClickListener(this);
        mTextViewList.setOnClickListener(this);
        mTextViewShow.setOnClickListener(this);
        mTextViewUpdate.setOnClickListener(this);
        mTextViewDelete.setOnClickListener(this);
        mTextViewReconvert.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.create_tv:
                initializeUserInput(Constants.CREATE_API);
                break;

            case R.id.show_tv:
                initializeUserInput(Constants.SHOW_API);
                break;

            case R.id.list_tv:
                initializeUserInput(Constants.LIST_API);
                break;

            case R.id.update_tv:
                initializeUserInput(Constants.UPDATE_API);
                break;

            case R.id.delete_tv:
                initializeUserInput(Constants.DELETE_API);
                break;

            case R.id.reconvert_tv:
                initializeUserInput(Constants.RECONVERT_API);
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
            mEditTextUserInputTitle.setHint(getResources().getString(R.string.enter_title));
        } else if (operation.equals(Constants.CREATE_API)) {
            mEditTextUserInputTitle.setHint(getResources().getString(R.string.enter_extension));
        }

        if (operation.equals(Constants.UPDATE_API) || operation.equals(Constants.CREATE_API)) {
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
                    case Constants.CREATE_API:
                        if (!mEditTextUserInput.getText().toString().isEmpty() || !mEditTextUserInputTitle.getText().toString().isEmpty()) {

                            Map<String, String> version = new HashMap<>();
                            version.put(CreateVersionParams.CROPPING, CroppingParams.C_FIT);

                            mPublitio.versions().createVersion(mEditTextUserInput.getText().toString(), mEditTextUserInputTitle.getText().toString(), version, new PublitioCallback<JsonObject>() {
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
                            Toast.makeText(VersionsActivity.this, R.string.extension_id, Toast.LENGTH_LONG).show();
                        }
                        break;

                    case Constants.LIST_API:
                        if (!mEditTextUserInput.getText().toString().isEmpty()) {

                            Map<String, String> list = new HashMap<>();
                            list.put(VersionsListParams.ORDER, OrderParams.NAME_ASC);

                            mPublitio.versions().versionsList(mEditTextUserInput.getText().toString(), list, new PublitioCallback<JsonObject>() {
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
                            Toast.makeText(VersionsActivity.this, R.string.provide_id, Toast.LENGTH_LONG).show();
                        }
                        break;

                    case Constants.SHOW_API:
                        if (!mEditTextUserInput.getText().toString().isEmpty()) {

                            mPublitio.versions().showVersion(mEditTextUserInput.getText().toString(), new PublitioCallback<JsonObject>() {
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
                            Toast.makeText(VersionsActivity.this, R.string.version_id, Toast.LENGTH_LONG).show();
                        }
                        break;

                    case Constants.UPDATE_API:

                        if (!mEditTextUserInput.getText().toString().isEmpty()) {
                            mPublitio.versions().updateVersion(mEditTextUserInput.getText().toString(), new PublitioCallback<JsonObject>() {
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
                            Toast.makeText(VersionsActivity.this, R.string.version_id, Toast.LENGTH_LONG).show();
                        }

                        break;

                    case Constants.DELETE_API:
                        if (!mEditTextUserInput.getText().toString().isEmpty()) {
                            mPublitio.versions().deleteVersion(mEditTextUserInput.getText().toString(), new PublitioCallback<JsonObject>() {
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
                            Toast.makeText(VersionsActivity.this, R.string.version_id, Toast.LENGTH_LONG).show();
                        }

                        break;

                    case Constants.RECONVERT_API:

                        if (!mEditTextUserInput.getText().toString().isEmpty()) {
                            mPublitio.versions().reconvertVersion(mEditTextUserInput.getText().toString(), new PublitioCallback<JsonObject>() {
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
                            Toast.makeText(VersionsActivity.this, R.string.version_id, Toast.LENGTH_LONG).show();
                        }

                        break;

                }
            }
        });

        mDialogUserInput.show();
    }
}
