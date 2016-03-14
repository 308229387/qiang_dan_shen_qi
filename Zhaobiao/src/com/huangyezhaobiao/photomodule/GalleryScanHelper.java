package com.huangyezhaobiao.photomodule;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shenzhixin on 2015/9/24.
 */
public class GalleryScanHelper {
    /**
     * 相册中被选中的photo的Path，这个东西在哪都可以用的上的啊亲
     */
    public static ArrayList<String> checked_photos = new ArrayList<>();

/*//    public static List<PhotoInfo> getPhotos(GalleryDirInfo info){
        return null;
    }*/

    public static List<MediaPicBean> scanAllImages(Activity activity) {
        List<MediaPicBean> allPhotoInfos = new ArrayList<>();
        Cursor cursor = findCursor(activity);
        File picPath;
        while (cursor != null && cursor.moveToNext()) {
            String path = getPhotoPath(cursor);
            String size = getPhotoSize(cursor);
            if (!path.contains("%")) {//防止fresco加载不出来带%的路径的图片
                picPath = new File(path);
                if (picPath.exists()) {
                    MediaPicBean info = createPhotoInfo(path, size, getDisplayMetrics(activity));
                    allPhotoInfos.add(info);
                }
            }
        }
        cursor.close();
        return allPhotoInfos;
    }


    /**
     * 扫描路径的文件件
     *
     * @param activity
     */
    public static List<GalleryDirInfo> scanDirImages(Activity activity) {
        List<GalleryDirInfo> dirInfos = new ArrayList<>();
        Cursor cursor = findCursor(activity);
        String firstImage = null;
        List<String> dirPaths = new ArrayList<>();
        while (cursor.moveToNext()) {
            String path = getPhotoPath(cursor);
            Log.e("downloading", "path:" + path);
            if (firstImage == null) {
                firstImage = path;
            }
            File parentFile = getDirByImagePath(path);
            if (parentFile == null) {
                continue;
            }
            String dirPath = parentFile.getAbsolutePath();
            if (dirPaths.contains(dirPath)) {
                continue;
            } else {
                dirPaths.add(dirPath);
                //新的文件夹
                GalleryDirInfo info = generateDirInfo(dirPath, path, parentFile);
                dirInfos.add(info);
            }
        }
        cursor.close();
        return dirInfos;
    }

    public static void getListImages() {

    }


    /**
     * 根据图片名得到图片所在的文件夹
     *
     * @param imagePath
     * @return
     */
    private static File getDirByImagePath(String imagePath) {
        return new File(imagePath).getParentFile();
    }


    private static GalleryDirInfo generateDirInfo(String dirPath, String path, File file) {
        GalleryDirInfo info = new GalleryDirInfo();
        info.setDir_path(dirPath);
        info.setFirst_pic_path(path);
        int count = getImageCounts(file);
        info.setCount(count);
        return info;
    }

    private static int getImageCounts(File file) {
        if (file == null || !file.exists()) {
            return 0;
        }
        if (file.isDirectory()) {
            String[] filelist = file.list(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String filename) {
                    return isFileImage(filename);
                }
            });
            if (filelist != null) {
                return filelist.length;
            }
        }
        return 0;
    }

    /**
     * 根据文件名来判断这个文件是不是图片
     *
     * @param filename
     * @return
     */
    public static boolean isFileImage(String filename) {
        return (filename.endsWith(".jpg")
                || filename.endsWith(".png")
                || filename.endsWith(".jpeg")
                || filename.endsWith(".JPEG")
                || filename.endsWith(".PNG")
                || filename.endsWith(".JPG"));
    }

    private static MediaPicBean createPhotoInfo(String path, String size, DisplayMetrics displayMetrics) {
        //  boolean needCompress = checkNeedCompress(size, displayMetrics);
        MediaPicBean bean = new MediaPicBean();
        bean.setUrl(path);
        return bean;
    }

    private static boolean checkNeedCompress(String size, DisplayMetrics dm) {
        float size_stand = Integer.valueOf(size) / 1024.f;
        return size_stand > dm.widthPixels * dm.heightPixels / 1024 * 2.5f;
    }


    /**
     * 获取屏幕的像素点的对象
     *
     * @param activity
     * @return
     */
    private static DisplayMetrics getDisplayMetrics(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm;
    }

    private static String getPhotoPath(Cursor mCursor) {
        return mCursor.getString(mCursor
                .getColumnIndex(MediaStore.Images.Media.DATA));
    }


    private static String getPhotoSize(Cursor mCursor) {
        return mCursor.getString(mCursor
                .getColumnIndex(MediaStore.Images.Media.SIZE));
    }


    private static Cursor findCursor(Activity context) {
        ContentResolver mContentResolver = context
                .getContentResolver();
        Cursor cursor = mContentResolver.query(
                StorageConstans.EXTENAL_STORAGE, null, null, null,
                StorageConstans.RULES_SHOW);
        return cursor;
    }


    /**
     * 判断这个路径的照片有没有被选中
     *
     * @param path
     * @return
     */
    public static boolean isChecked(String path) {
        return checked_photos.contains(path);
    }

    /**
     * 设置这个照片为选中的状态
     *
     * @param path
     */
    public static void setChecked(String path) {
        checked_photos.add(path);
    }

    /**
     * 设置这个照片为不选中的状态
     *
     * @param path
     */
    public static void setUnChecked(String path) {
        checked_photos.remove(path);
    }

    /**
     * 返回选中的数量
     *
     * @return
     */
    public static int checkCounts() {
        return checked_photos == null ? 0 : checked_photos.size();
    }

    /**
     * 将传递过来的bean转化给string
     *
     * @param temp_mediaPicBean
     */
    public static ArrayList<String> transferBeanToString(ArrayList<MediaPicBean> temp_mediaPicBean) {
        ArrayList<String> checkPoints = new ArrayList<>();
        int length = temp_mediaPicBean.size();
        for (int i = 0; i < length; i++) {
            String path = temp_mediaPicBean.get(i).getUrl();
            checkPoints.add(path);
        }
        return checkPoints;

    }

    /**
     * 根据路径再转化成Bean传递给调用者
     *
     * @param checkPoints
     * @return
     */
    public static ArrayList<MediaPicBean> transferStringToBean(ArrayList<String> checkPoints) {
        ArrayList<MediaPicBean> beans = new ArrayList<>();
        int length = checkPoints.size();
        for (int i = 0; i < length; i++) {
            String path = checkPoints.get(i);
            MediaPicBean bean = new MediaPicBean();
            bean.setUrl(path);
            beans.add(bean);
        }
        return beans;

    }
}
