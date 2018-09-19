package com.publit.publit_io.constant;

/**
 * Versions status params.
 */
public class VersionsStatusParams {

    private VersionsStatusParams() {
        // not to be instantiated in public
    }

    /**
     * Used for listing version with any status.
     */
    public static final String ALL = "all";

    /**
     * Used for listing file with creating status.
     */
    public static final String CREATING = "creating";

    /**
     * Used for listing file with ready status.
     */
    public static final String READY = "ready";

    /**
     * Used for listing file with failed status.
     */
    public static final String FAILED = "failed";

}
