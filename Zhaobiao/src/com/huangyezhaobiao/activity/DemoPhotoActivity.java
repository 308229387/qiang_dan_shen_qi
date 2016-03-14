package com.huangyezhaobiao.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.eventbus.EventAction;
import com.huangyezhaobiao.eventbus.EventbusAgent;
import com.huangyezhaobiao.photomodule.BaseMediaBean;
import com.huangyezhaobiao.photomodule.GalleryActivity;
import com.huangyezhaobiao.photomodule.MediaAdapter;
import com.huangyezhaobiao.photomodule.MediaPicBean;
import com.huangyezhaobiao.photomodule.PhotoBean;
import com.huangyezhaobiao.photomodule.PhotoDialog;
import com.huangyezhaobiao.photomodule.ViewSinglePhotoActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shenzhixin on 2015/12/3.
 */
public class DemoPhotoActivity extends QBBaseActivity implements PhotoDialog.OnPPViewClickListener {
    private GridView            gridView_comment_rating;
    private MediaAdapter        adapter;
    private List<BaseMediaBean> beans;
    private PhotoDialog         photoDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventbusAgent.getInstance().register(this);
        beans = new ArrayList<>();
        photoDialog = new PhotoDialog(this);
        photoDialog.setPPViewListener(this);
        initView();
        initListener();
    }

    @Override
    public void initView() {
        setContentView(getLayoutId());
        gridView_comment_rating = getView(R.id.gridView_comment_rating);
        adapter = new MediaAdapter(this,beans,true,9);
        gridView_comment_rating.setAdapter(adapter);
    }

    @Override
    public void initListener() {

    }

    private int getLayoutId(){
        return R.layout.activity_demo_photo;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventbusAgent.getInstance().unregister(this);
    }


    public void onEventMainThread(EventAction action) {
        switch (action.getType()) {
            case EVENT_GRIDVIEW_ITEM_CLOSE://点击了删除按钮
                BaseMediaBean baseMediaBean = (BaseMediaBean) action.data;
                this.beans.remove(baseMediaBean);
                adapter.refreshDatas(this.beans);
                break;
            case EVENT_GRIDVIEW_ITEM://点击了浏览
                BaseMediaBean bean1 = (BaseMediaBean) action.data;
                int index = findPosFromBean(bean1);
                Intent intents = ViewSinglePhotoActivity.newIntent(this,index,transformBaseToPic(this.beans));
                startActivity(intents);
                break;
            case EVENT_SELECTED_AVATAR://从相册回来了
                PhotoBean photoBean = (PhotoBean) action.data;
                ArrayList<MediaPicBean> beans = photoBean.getBeans();
                this.beans = transformPicToBase(beans);
                adapter.refreshDatas(transformPicToBase(beans));
                break;
            case EVENT_GRIDVIEW_ITEM_ADD://点击了加号
                photoDialog.show();
                break;

        }
    }



    private int findPosFromBean(BaseMediaBean bean1) {
        for(int i=0;i<beans.size();i++){
            if(bean1.getUrl().equals(beans.get(i).getUrl())){
                return i;
            }
        }
        return 0;
    }

    private ArrayList<MediaPicBean> transformBaseToPic(List<BaseMediaBean> picBeans){
        ArrayList<MediaPicBean> baseMediaBeans = new ArrayList<>();
        for(BaseMediaBean picBean : picBeans){
            MediaPicBean baseMediaBean = new MediaPicBean();
            baseMediaBean.setType(picBean.getType());
            baseMediaBean.setUrl(picBean.getUrl());
            if(!picBean.getUrl().startsWith("http:")){
                baseMediaBean.setLocal(true);
            }
            baseMediaBeans.add(baseMediaBean);
        }
        return baseMediaBeans;
    }


    private ArrayList<BaseMediaBean> transformPicToBase(List<MediaPicBean> picBeans){
        ArrayList<BaseMediaBean> baseMediaBeans = new ArrayList<>();
        for(MediaPicBean picBean : picBeans){
            BaseMediaBean baseMediaBean = new BaseMediaBean();
            baseMediaBean.setType(picBean.getType());
            baseMediaBean.setUrl(picBean.getUrl());
            baseMediaBeans.add(baseMediaBean);
        }
        return baseMediaBeans;
    }

    @Override
    public void onTakePhotoClick() {
        Log.e("shenzhixinUUU", "photo");
    }

    @Override
    public void onTakeGalleryClick() {
        Log.e("shenzhixinUUU", "onTakeGalleryClick");
        Intent intent = GalleryActivity.onNewIntent(this, 9, 1, transformBaseToPic(this.beans));
        startActivity(intent);
    }
}
