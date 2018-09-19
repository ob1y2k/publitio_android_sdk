package com.publit.publit_io.constant;

/**
 * Update File Params.
 */
public class UpdateFileParams {

    private UpdateFileParams() {
        // not to be instantiated in public
    }

    /**
     * Unique alphanumeric public id of file.
     */
    public static final String PUBLIC_ID = "public_id";

    /**
     * Title of uploaded file.
     * Default taken from file name.
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
    public static final String OPTION_AD = "option_ad   ";
}
