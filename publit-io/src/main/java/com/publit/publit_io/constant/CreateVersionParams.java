package com.publit.publit_io.constant;

/**
 * Create Version API Params.
 */
public class CreateVersionParams {

    private CreateVersionParams() {
        // not to be instantiated in public
    }

    /**
     * List of options you want to apply to this transformation.
     */
    public static final String OPTIONS = "options";

    /**
     * To resize width of image or video.
     */
    public static final String RESIZING_WIDTH = "resizing_width";

    /**
     * To resize height of image or video.
     */
    public static final String RESIZING_HEIGHT = "resizing_height";

    /**
     * To crop image or video.
     * Supported values are fit, ill and limit.
     */
    public static final String CROPPING = "cropping";

    /**
     * For watermarking of images & videos.
     */
    public static final String WATERMARKING = "watermarking";

    /**
     * For quality ajustment of images & videos.
     */
    public static final String QUALITY = "quality";

    /**
     * It should be used for video to image transformation.
     */
    public static final String TRIMMING_TIME = "trimming_time";

    /**
     * Defines start time from which to start trimming of the video,
     * and is used in video to video transfromation.
     */
    public static final String TRIMMING_START = "trimming_start";

    /**
     * Defines how long (in seconds) trimmed video should be.
     */
    public static final String TRIMMING_END = "trimming_end";
}
