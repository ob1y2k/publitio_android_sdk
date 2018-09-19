package com.publit.publit_io.constant;

/**
 * Version List Params.
 */
public class VersionsListParams {

    private VersionsListParams() {
        // not to be instantiated in public
    }

    /**
     * Specifies maximum number of file versions to return.
     * Default is 100. Maximum result limit is 1000.
     */
    public static final String LIMIT = "limit";

    /**
     * Specifies how many file versions should be skipped at the beginning of the result set.
     * Default is 0.
     */
    public static final String OFFSET = "offset";

    /**
     * Specifies parameters by which returned result should be ordered.
     */
    public static final String ORDER = "order";

    /**
     * Specifies which file versions should be returned based on transformation status.
     * Supported values are all, creating, ready and failed. Default is all.
     */
    public static final String FILTER_STATUS = "filter_status";

    /**
     * Specifies which file versions should be returned based on extension (output format).
     */
    public static final String FILTER_EXTENSION = "filter_extension";
}
