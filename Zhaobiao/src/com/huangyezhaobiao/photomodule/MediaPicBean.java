package com.huangyezhaobiao.photomodule;

import android.os.Parcel;
import android.os.Parcelable;



/**
 * Created by shenzhixin on 2015/9/24.
 */
public class MediaPicBean extends BaseMediaBean implements Parcelable {

    public MediaPicBean() {
        type = MultiMediaSD.MEDIA_TYPE_PIC;
        local = true;
    }

    protected MediaPicBean(Parcel in) {
        type = MultiMediaSD.MEDIA_TYPE_PIC;
        url = in.readString();
    }

    public MediaPicBean(String url) {
        type = MultiMediaSD.MEDIA_TYPE_PIC;
        this.url = url;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MediaPicBean> CREATOR = new Creator<MediaPicBean>() {
        @Override
        public MediaPicBean createFromParcel(Parcel in) {
            return new MediaPicBean(in);
        }

        @Override
        public MediaPicBean[] newArray(int size) {
            return new MediaPicBean[size];
        }
    };
}
