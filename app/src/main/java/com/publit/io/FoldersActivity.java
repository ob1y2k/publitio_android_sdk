package com.publit.io;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.publit.io.constant.Constants;
import com.publit.publit_io.constant.CreateFolderParams;
import com.publit.publit_io.constant.FoldersListParams;
import com.publit.publit_io.constant.FoldersTagsType;
import com.publit.publit_io.constant.OrderParams;
import com.publit.publit_io.constant.UpdateFolderParams;
import com.publit.publit_io.utils.Publitio;
import com.publit.publit_io.utils.PublitioCallback;

import java.util.HashMap;
import java.util.Map;

public class FoldersActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTextViewResponse;
    private ProgressDialog dialog;

    //Custom Dialog for User Inputs.
    private Dialog mDialogUserInput;

    private EditText mEditTextUserInput;
    private EditText mEditTextUserInputTitle;

    //Publitio class object to call Publit.io API's.
    Publitio mPublitio;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder);

        init();
    }

    /**
     * Initialize UI Components and Publitio object.
     */
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
        TextView mTextViewTree = findViewById(R.id.tree_tv);
        mTextViewResponse = findViewById(R.id.response_tv);

        //Initialize Click Listners.
        mTextViewCreate.setOnClickListener(this);
        mTextViewList.setOnClickListener(this);
        mTextViewShow.setOnClickListener(this);
        mTextViewUpdate.setOnClickListener(this);
        mTextViewDelete.setOnClickListener(this);
        mTextViewTree.setOnClickListener(this);
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

                dialog.setMessage(getString(R.string.call_api_message));
                dialog.show();

                Map<String, String> list = new HashMap<>();
                list.put(FoldersListParams.ORDER, OrderParams.DATE_DESC);
                list.put(FoldersListParams.TAGS, FoldersTagsType.TAG_ANY);

                mPublitio.folders().foldersList(list, new PublitioCallback<JsonObject>() {
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

            case R.id.tree_tv:

                dialog.setMessage(getString(R.string.call_api_message));
                dialog.show();

                mPublitio.folders().treeFolders(new PublitioCallback<JsonObject>() {
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
        mDialogUserInput.setContentView(inflaterUser.inflate(R.layout.dialog_user_input_folder, null));
        mDialogUserInput.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        mEditTextUserInput = mDialogUserInput.findViewById(R.id.user_input_tv);
        mEditTextUserInputTitle = mDialogUserInput.findViewById(R.id.user_input_title_tv);
        TextView mTextViewOk = mDialogUserInput.findViewById(R.id.profile_ok);

        if (operation.equals(Constants.UPDATE_API) || operation.equals(Constants.CREATE_API)) {
            mEditTextUserInput.setVisibility(View.VISIBLE);
        } else {
            mEditTextUserInput.setVisibility(View.GONE);
        }

        mTextViewOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDialogUserInput.dismiss();
                dialog.setMessage("Calling " + operation + " API.");
                dialog.show();

                switch (operation) {
                    case Constants.CREATE_API:
                        Map<String, String> createFolder = new HashMap<>();
                        if (!mEditTextUserInputTitle.getText().toString().isEmpty()) {
                            createFolder.put(CreateFolderParams.PARENT_ID, mEditTextUserInputTitle.getText().toString());
                        }
                        mPublitio.folders().createFolder(mEditTextUserInput.getText().toString(), createFolder, new PublitioCallback<JsonObject>() {
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

                    case Constants.SHOW_API:
                        mPublitio.folders().showFolder(mEditTextUserInputTitle.getText().toString(), new PublitioCallback<JsonObject>() {
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

                    case Constants.UPDATE_API:
                        Map<String, String> updateFolder = new HashMap<>();
                        if (!mEditTextUserInput.getText().toString().isEmpty()) {
                            updateFolder.put(UpdateFolderParams.NAME, mEditTextUserInput.getText().toString());
                        }
                        mPublitio.folders().updateFolder(mEditTextUserInputTitle.getText().toString(), updateFolder, new PublitioCallback<JsonObject>() {
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

                    case Constants.DELETE_API:
                        mPublitio.folders().deleteFolder(mEditTextUserInputTitle.getText().toString(), new PublitioCallback<JsonObject>() {
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
                }
            }
        });

        mDialogUserInput.show();
    }
}
