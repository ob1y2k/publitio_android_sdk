package com.publit.publit_io.utils;


import android.app.Activity;

import com.publit.publit_io.files.PublitioFiles;
import com.publit.publit_io.players.PublitioPlayers;
import com.publit.publit_io.versions.PublitioVersions;
import com.publit.publit_io.watermarks.PublitioWaterMarks;

/**
 * Generate API signature.
 */
public class Publitio {

    private Activity mActivity;


    /**
     * Instantiates a new Publitio object.
     *
     * @param activity the activity
     */
    public Publitio(Activity activity) {
        mActivity = activity;
        if (APIConfiguration.apiKey == null || APIConfiguration.apiKey.isEmpty()
                || APIConfiguration.apiSecret == null || APIConfiguration.apiSecret.isEmpty()) {
            new APIConfiguration(mActivity);
        }
    }

    /**
     * Gives the water mark object to call publit.io water mark api's.
     *
     * @return the publitio water marks object.
     */
    public PublitioWaterMarks waterMark() {
        return new PublitioWaterMarks(mActivity.getApplicationContext());
    }

    /**
     * Gives the files object to call publit.io files api's.
     *
     * @return the publitio files object.
     */
    public PublitioFiles files() {
        return new PublitioFiles(mActivity.getApplicationContext());
    }

    /**
     * Gives the players object to call publit.io players api's.
     *
     * @return the publitio players object.
     */
    public PublitioPlayers players() {
        return new PublitioPlayers(mActivity.getApplicationContext());
    }

    /**
     * Gives the versions object to call publit.io versions api's.
     *
     * @return the publitio versions object.
     */
    public PublitioVersions versions() {
        return new PublitioVersions(mActivity.getApplicationContext());
    }
}
