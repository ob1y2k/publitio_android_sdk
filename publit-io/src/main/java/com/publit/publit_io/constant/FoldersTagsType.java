package com.publit.publit_io.constant;

public class FoldersTagsType {

    private FoldersTagsType() {
        // not to be instantiated in public
    }

    /**
     * A folder will only be listed if it has all tags specified in the tags parameter
     */
    public static final String TAG_ALL = ":all";

    /**
     * A folder will be listed if it has at least one tag specified in the tags parameter.
     */
    public static final String TAG_ANY = ":any";
}
