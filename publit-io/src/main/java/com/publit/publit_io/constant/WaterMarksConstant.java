package com.publit.publit_io.constant;

/**
 * Water marks Params.
 */
public class WaterMarksConstant {

    private WaterMarksConstant() {
        // not to be instantiated in public
    }

    /**
     * Position of watermark, valid center, top-left, top, top-right,
     * left, right, bottom-left, bottom, bottom-right.
     * Default bottom-right.
     */
    public static final String POSITION = "position";

    /**
     * Padding in pixels (ignored for Position center).
     * Default 0.
     */
    public static final String PADDING = "padding";

}
