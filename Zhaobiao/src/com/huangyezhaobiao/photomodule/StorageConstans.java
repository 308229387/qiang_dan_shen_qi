package com.huangyezhaobiao.photomodule;

import android.net.Uri;
import android.provider.MediaStore;

/**
 * Created by shenzhixin on 2015/9/24.
 */
public class StorageConstans {
    public static final Uri EXTENAL_STORAGE = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

    public static final String RULES_SHOW ="date_modified DESC";

    public static  int MAX_COUNT = 0;


    public static final int REQUEST_GALLERY_2_SINGLE = 1;
    public static final int RESULT_CODE_SINGLE_2_GALLERY = 1;
    public static final int RESULT_CODE_SINGLE_2_GALLERY_2_OTHER = 2;

}
