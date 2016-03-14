package com.huangyezhaobiao.photomodule;

import android.os.Environment;

/**
 * Created by shenzhixin on 2015/9/24.
 */
public class StorageUtils {
    public static boolean hasSDCard(){
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }
}
