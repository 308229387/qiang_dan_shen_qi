package com.huangyezhaobiao.picupload;

/**
 * Created by shenzhixin on 2015/12/17.
 */
public interface UICallback {
    void onUploadPicSuccess(String msg);
    void onUploadPicFailure(String err);
    void onUploadPrecent(String precent);

}
