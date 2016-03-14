package com.huangyezhaobiao.photomodule;

/**
 * Created by shenzhixin on 2015/12/3.
 */
public class CameraConstants {
    public static final int CAMERA_RUN_ERROR = 1016;
    public static final int INCREASE_SCREENLIGHT = 1018;
    public static final int INIT_CRASH_HANDLER = 1020;
    public static final int RESULT_LOAD_IMAGE = 1;
    public static final int REQUEST_GOTO_EDITOR = 2;
    public static final int RESULT_GOTO_TAKEPHOTO = 3;
    public static final int RESULT_TAKEN_DONE_PREVIEW = 1;
    public static final int RESULT_TAKEN_DONE_EDITOR = 2;
    public static final String ACTION_IMAGE_CAPTURE_SECURE = "android.media.action.IMAGE_CAPTURE_SECURE";
    public static final String SECURE_CAMERA_EXTRA = "secure_camera";
    public static final String WB_CAMERA_PHOTO_PATH = "save_path";
    public static final String UPDATE_STAT_PREFERENCE = "update_stat_preference";
    public static final String IS_CLOSE_FRONT_FILTER = "is_close_front_filter";

    public CameraConstants() {
    }
}
