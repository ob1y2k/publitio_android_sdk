package com.publit.publit_io.constant;

/**
 * Create File API Params.
 */
public class CreateFileParams {

    private CreateFileParams() {
        // not to be instantiated in public
    }

    /**
     * Remote url to image or video if no file is present.
     */
    public static final String FILE_URL = "file_url";

    /**
     * Unique alphanumeric public id of file.
     */
    public static final String PUBLIC_ID = "public_id";

    /**
     * Title of uploaded file. Default taken from file name.
     */
    public static final String TITLE = "title";

    /**
     * Optional description of file.
     */
    public static final String DESCRIPTION = "description";

    /**
     * Optional tags for file. Used for search.
     */
    public static final String TAGS = "tags";

    /**
     * Privacy for file.
     * Supported values are private and public. Default private.
     */
    public static final String PRIVACY = "privacy";

    /**
     * Download option for a file.
     * Supported values are disabled and enabled. Default enabled.
     */
    public static final String OPTION_DOWNLOAD = "option_download";

    /**
     * Url-based Transformations option for a file.
     * Supported values are disabled and enabled. Default enabled.
     */
    public static final String OPTION_TRANSFORM = "option_transform";

    /**
     * Ad option for a file.
     * Supported values are disabled, enabled and new. Default enabled.
     */
    public static final String OPTION_AD = "option_ad";

    /**
     * Unique alphanumeric id of watermark.
     */
    public static final String WM = "wm";
}
