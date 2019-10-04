package com.publit.publit_io.constant;

public class FoldersListParams {

    private FoldersListParams() {
        // not to be instantiated in public
    }

    /**
     * Parent folder id you wish to retrieve (sub) folders for.
     * Default null - returns root (/) folders from account.
     */
    public static final String PARENT_ID = "parent_id";

    /**
     * Specifies parameters by which returned result should be ordered.
     * Supported values are date and name.
     * Parameter sort order can be specified by appending :asc for an ascending sort order
     * or :desc for a descending one to the parameter name (for example name:desc).
     * Default sort order is ascending and can be omitted. Default is date.
     */
    public static final String ORDER = "order";

    /**
     * Specifies tags by which to perform search via query.
     * Tag mode can be specified by appending :any (a folder will be listed if it has at least one tag specified in the tags parameter.)
     * or by appending :all (a folder will only be listed if it has all tags specified in the tags parameter).
     * Default mode is all.
     */
    public static final String TAGS = "tags";

}
