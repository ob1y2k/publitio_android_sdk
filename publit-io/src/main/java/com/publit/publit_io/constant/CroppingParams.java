package com.publit.publit_io.constant;

/**
 * Cropping params.
 */
public class CroppingParams {

    private CroppingParams() {
        // not to be instantiated in public

    }

    /**
     * To resize image or video to fit within a width and height of image or video.
     */
    public static final String C_FIT = "c_fit";

    /**
     * It is same as the c_fit mode but only if the original image is larger than
     * the given limit (width and height), in which case the image is scaled down so that
     * it takes up as much space as possible within a bounding box defined by the given
     * width and height parameters.
     */
    public static final String C_LIMIT = "c_limit";

    /**
     * It create an file version with the exact given width and height
     * while retaining the original aspect ratio.
     */
    public static final String C_FILL = "c_fill";

}
