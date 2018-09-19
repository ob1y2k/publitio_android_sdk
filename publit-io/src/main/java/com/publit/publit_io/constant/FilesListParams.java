package com.publit.publit_io.constant;

/**
 * Files List Params.
 */
public class FilesListParams {

    private FilesListParams() {
        // not to be instantiated in public
    }

    /**
     * Specifies maximum number of files to return.
     * Default is 100. Maximum result limit is 1000
     */
    public static final String LIMIT = "limit";

    /**
     * Specifies how many files should be skipped at the beginning of the result set.
     * Default is 0
     */
    public static final String OFF_SET = "offset";

    /**
     * Specifies parameters by which returned result should be ordered.
     * Supported values are date, name, size and hits.
     * Parameter sort order can be specified by appending :asc for an ascending sort order
     * or :desc for a descending.
     */
    public static final String ORDER = "order";

    /**
     * Specifies which files should be returned based on privacy status.
     * Supported values are all, private and public. Default is all
     */
    public static final String FILTER_PRIVACY = "filter_privacy";

    /**
     * Specifies which files should be returned based on extension (output format).
     * Default is all
     */
    public static final String FILTER_EXTENSION = "filter_extension";

    /**
     * Specifies which files should be returned based on their type.
     * Supported values are all, image and video. Default is all
     */
    public static final String FILTER_TYPE = "filter_type";

    /**
     * Specifies which files should be returned based on option_ad status.
     * Supported values are all, enabled, disabled and new. Default is all
     */
    public static final String FILTER_AD = "filter_ad";

    /**
     * Specifies tags by whic to perform search via query.
     */
    public static final String TAGS = "tags";
}
