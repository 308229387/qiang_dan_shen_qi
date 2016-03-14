package com.huangyezhaobiao.photomodule;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.eventbus.EventAction;
import com.huangyezhaobiao.eventbus.EventType;
import com.huangyezhaobiao.eventbus.EventbusAgent;
import com.huangyezhaobiao.utils.ToastUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by shenzhixin on 2015/9/24.
 */
public class GalleryPresenter {
    private List<MediaPicBean> allPhotoInfos;
    private List<GalleryDirInfo> dirInfos;
    private Context context;
    private ProgressDialog loadingProgress;
    private ScanTask        task;
    private GalleryPopup    popup;
    public GalleryPresenter(Context context){
        this.context    = context;
        allPhotoInfos   = new ArrayList<>();
        dirInfos        = new ArrayList<>();
        initLoadingProgress();
        task            = new ScanTask();
    }

    private void initLoadingProgress(){
        loadingProgress = ProgressDialog.show(context, null, "正在扫描图片");
    }




    public void scanPhotos(){
        if (!StorageUtils.hasSDCard()) {
            loadingProgress.dismiss();
            Toast.makeText(context, "没有存储设备", Toast.LENGTH_SHORT).show();
            return;
        }
        task.execute();
    }


    /**
     * 初始化popup
     */
    public void initPopupWindow(List<GalleryDirInfo> mImageFloders,GalleryPopup.OnPopupItemClickListener listener){
        popup = new GalleryPopup(context, mImageFloders);
        popup.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        popup.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                // 设置背景颜色变暗
                WindowManager.LayoutParams lp = ((Activity)context).getWindow().getAttributes();
                lp.alpha = 1.0f;
                ((Activity)context).getWindow().setAttributes(lp);
            }
        });
        popup.setOnPopupItemClickListener(listener);

    }



    public GalleryPopup getPopup(){
        return popup;
    }

    public void showPopupWindow(View anchor) {
        if(popup==null){
            Toast.makeText(context,"并没有图片",0).show();
            return;
        }
        popup.setAnimationStyle(R.style.anim_popup_dir);
        popup.showAsDropDown(anchor, 100, 100);
        // 设置背景颜色变暗
        WindowManager.LayoutParams lp = ((Activity)context).getWindow().getAttributes();
        lp.alpha = .3f;
        ((Activity)context).getWindow().setAttributes(lp);
    }

    /**
     * 根据路径信息得到photoInfo的数量
     * @param dirInfo
     * @return
     */
    public List<MediaPicBean> getPhotoListFromDir(GalleryDirInfo dirInfo) {
        File dir              = new File(dirInfo.getDir_path());
        List<MediaPicBean> infos = getPhotoInfos(dir, dirInfo.getDir_path());
        Collections.reverse(infos);
        return infos;
    }


    /**
     * 根据文件的dir得到这个文件夹下面的所有图片的一个bean集合
     * @param dir
     * @return
     */
    private List<MediaPicBean> getPhotoInfos(File dir,String dirNames){
        List<MediaPicBean> infos = new ArrayList<>();
        String[] list = dir.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                return GalleryScanHelper.isFileImage(filename);
            }
        });
        List<String> infoNames = Arrays.asList(list);
        int length = infoNames.size();
        for(int i=0;i<length;i++){
            MediaPicBean info = new MediaPicBean();
            info.setUrl(dirNames+"/"+infoNames.get(i));
            infos.add(info);
        }
        return infos;

    }

    public void dismissPopupWindow() {
        if(popup!=null && popup.isShowing()){
            popup.dismiss();
        }
    }

    private class ScanTask extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                loadingProgress.show();
            }catch (Exception e){

            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            allPhotoInfos = GalleryScanHelper.scanAllImages((Activity) context);
            dirInfos = GalleryScanHelper.scanDirImages((Activity) context);
            Log.e("shenzhixinUU","allPhotos:"+allPhotoInfos.size()+",dirs:"+dirInfos.size());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                loadingProgress.dismiss();
                sendAllPhotos();
                sendDirs();
            }catch (Exception e){

            }
        }
    }


    /**
     * 把各个装有图片的文件夹通过eventBus传递给GalleryActivity
     */
    private void sendDirs() {
        EventAction action = new EventAction(EventType.EVENT_GALLERY_DIRS);
        action.data        = dirInfos;
        EventbusAgent.getInstance().post(action);
    }


    /**
     * 把所有图片通过eventBus传递给GalleryActivity
     */
    private void sendAllPhotos(){
        EventAction action = new EventAction(EventType.EVENT_GALLERY_ALL_PHOTOS);
        action.data        = allPhotoInfos;
        EventbusAgent.getInstance().post(action);
        Log.e("shenzhixinUU", "send");
    }
}
