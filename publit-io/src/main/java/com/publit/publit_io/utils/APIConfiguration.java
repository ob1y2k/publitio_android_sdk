package com.publit.publit_io.utils;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import com.publit.publit_io.R;
import com.publit.publit_io.constant.Constant;
import com.publit.publit_io.exception.PublitioExceptions;

/**
 * Configure API key and API secret for Publit.io API calling.
 */
public class APIConfiguration {

    //API secret is used to generate SHA-1 digest.
    public static String apiSecret;

    //API key identifies the user to the API.
    public static String apiKey;

    private Activity mActivity;

    //Private Constructor
    protected APIConfiguration(Activity activity) {
        mActivity = activity;
        readMetaData();
    }

    /**
     * Read api-key and api-secret from Manifest.
     */
    private void readMetaData() {
        try {
            ApplicationInfo ai = mActivity.getPackageManager().getApplicationInfo(mActivity.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            apiKey = bundle.getString(Constant.API_KEY);
            apiSecret = bundle.getString(Constant.API_SECRET);

        } catch (PackageManager.NameNotFoundException e) {
            new PublitioExceptions(mActivity.getString(R.string.key_or_secret_not_found));
        } catch (NullPointerException e) {
            new PublitioExceptions(mActivity.getString(R.string.failed_to_load_meta_data));
        }

        if (apiSecret == null || apiSecret.isEmpty() || apiKey == null || apiKey.isEmpty()) {
            Toast.makeText(mActivity, mActivity.getString(R.string.key_or_secret_not_found), Toast.LENGTH_LONG).show();
        }
    }
}
