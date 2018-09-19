package com.publit.publit_io.constant;

/**
 * Files privacy filter params.
 */
public class FilesPrivacyFilterParams {

    private FilesPrivacyFilterParams() {
        // not to be instantiated in public
    }

    /**
     * Used for listing file with any privacy status.
     */
    public static final String ALL = "all";

    /**
     * Used for listing file with private privacy status.
     */
    public static final String PRIVATE = "private";

    /**
     * Used for listing file with public privacy status.
     */
    public static final String PUBLIC = "public";

}
