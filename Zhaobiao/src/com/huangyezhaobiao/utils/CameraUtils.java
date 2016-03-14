package com.huangyezhaobiao.utils;

import android.hardware.Camera;

/**
 * Created by 58 on 2016/3/4.
 */
public class CameraUtils {
    public static void openCameraFront() {
        Camera c = null;
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                c = Camera.open(i);
                break;
            }
        }
    }
}
