package com.publit.publit_io.constant;

/**
 * The Constant used in Publitio SDK.
 */
public class Constant {


    private Constant() {
        // not to be instantiated in public
    }

    /**
     * The BASEURL for all the API's.
     */
    public static final String BASEURL = "https://api.publit.io/";

    /**
     * Used in User's Manifest to set api key.
     */
    public static final String API_KEY = "publitio_api_key";

    /**
     * Used in User's Manifest to set api secret.
     */
    public static final String API_SECRET = "publitio_api_secret";

    /**
     * The Algorithm used to generate api signature.
     */
    public static final String ALGORITHM = "SHA-1";

    /**
     * Used in all api calls to set value of api signature.
     */
    public static final String API_SIGNATURE = "api_signature";

    /**
     * Used in all api calls to set value of api key.
     */
    public static final String PUB_API_KEY = "api_key";

    /**
     * Used in all api calls to set value of api nonce.
     */
    public static final String API_NONCE = "api_nonce";

    /**
     * Used in all api calls to set value of api timestamp.
     */
    public static final String API_TIMESTAMP = "api_timestamp";

    /**
     * Used in all api calls to set value of api kit.
     */
    public static final String API_KIT = "api_kit";

    /**
     * Used in all api calls to set value of api kit.
     */
    public static final String SDK_TYPE = "android";

    /**
     * Used to set width in create version api.
     */
    public static final String WIDTH_CONSTANT = "w_";

    /**
     * Used to set height in create version api.
     */
    public static final String HEIGHT_CONSTANT = "h_";

    /**
     * Used to set watermark in create version api.
     */
    public static final String WATERMARK_CONSTANT = "wm_";

    /**
     * Used to set quality in create version api.
     */
    public static final String QUALITY_CONSTANT = "q_";

    /**
     * Used to set time in create version api.
     */
    public static final String TIME_CONSTANT = "t_";

    /**
     * Used to set start offset in create version api.
     */
    public static final String START_OFFSET = "so_";

    /**
     *Used to set end offset in create version api.
     */
    public static final String END_OFFSET = "eo_";

}
