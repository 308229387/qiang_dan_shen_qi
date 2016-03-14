package com.huangyezhaobiao.photomodule;

        import android.app.Activity;
        import android.content.Context;
        import android.content.Intent;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.AbsListView;
        import android.widget.GridView;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.huangye.commonlib.activity.BaseActivity;
        import com.huangyezhaobiao.R;
        import com.huangyezhaobiao.eventbus.EventAction;
        import com.huangyezhaobiao.eventbus.EventType;
        import com.huangyezhaobiao.eventbus.EventbusAgent;
        import com.nostra13.universalimageloader.core.ImageLoader;

        import java.util.ArrayList;
        import java.util.List;

/**
 * Created by shenzhixin on 2015/9/24.
 * 照片选择的展示activity
 * 扫描神马的功能都是通过presenter来进行的
 */
public class GalleryActivity extends BaseActivity implements GalleryAdapter.OnGalleryClickListener, View.OnClickListener, GalleryPopup.OnPopupItemClickListener {
    TextView tv_back;
    TextView tv_title;
    TextView tv_right_btn;
    View ll_right_btn;
    TextView tv_choose_dir;
    TextView tv_chosen_total_view;
    GridView grid_photo;
    GalleryAdapter adapter;
    GalleryPresenter presenter;
    List<MediaPicBean> allPhotos;
    List<MediaPicBean> checkedPhotos;
    List<GalleryDirInfo> dirInfos;
    ArrayList<MediaPicBean> real_all_photos;//所有的图片，扫描过就不能动了
    ArrayList<String> tempPhotos = new ArrayList<>();//传递过来的照片的数量
    ArrayList<MediaPicBean> temp_mediaPicBean = new ArrayList<>();//传递过来的照片的bean
    public ArrayList<BaseMediaBean> mbeans = new ArrayList<>();
    int last_state;
    private PhotoBean photoBean;
    private int from_type = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        photoBean = new PhotoBean();
        initAndTransAdapt();
        presenter = new GalleryPresenter(this);
        presenter.scanPhotos();
        bindListener();
        EventbusAgent.getInstance().register(this);
    }

    @Override
    public void initView() {
        setContentView(R.layout.main_tuce_activity);
        tv_back              = getView(R.id.tv_back);
        tv_title             = getView(R.id.tv_title);
        tv_right_btn         = getView(R.id.tv_right_btn);
        ll_right_btn         = getView(R.id.ll_right_btn);
        tv_choose_dir        = getView(R.id.tv_choose_dir);
        tv_chosen_total_view = getView(R.id.tv_chosen_total_view);
        grid_photo           = getView(R.id.grid_photo);

    }

    @Override
    public void initListener() {

    }

    public static Intent onNewIntent(Context context, int maxCount, int type, ArrayList<MediaPicBean> beans) {
        Intent intent = new Intent(context, GalleryActivity.class);
        intent.putExtra(GalleryConstans.KEY_MAX_COUNT_CAN_CHECKED, maxCount);
        intent.putExtra(PhotoBean.KEY_TYPE, type);
        intent.putParcelableArrayListExtra(GalleryConstans.ALREADEY_CHECKED_PHOTOS_BEAN, beans);
        return intent;
    }

    /**
     * 主要进行变量的初始化操作，以及其他activity的数据的传递适配
     */
    private void initAndTransAdapt() {
        initView();
        tv_title.setText(getString(R.string.pick_photos_title));
        allPhotos = new ArrayList<>();
        checkedPhotos = new ArrayList<>();
        dirInfos = new ArrayList<>();
        real_all_photos = new ArrayList<>();
        StorageConstans.MAX_COUNT = getIntent().getIntExtra(GalleryConstans.KEY_MAX_COUNT_CAN_CHECKED, 9);
        temp_mediaPicBean = getIntent().getParcelableArrayListExtra(GalleryConstans.ALREADEY_CHECKED_PHOTOS_BEAN);
        from_type = getIntent().getIntExtra(PhotoBean.KEY_TYPE, -1);
        photoBean.setType(from_type);
        if (tempPhotos == null) {
            tempPhotos = new ArrayList<>();
        }
        if (temp_mediaPicBean == null) {
            temp_mediaPicBean = new ArrayList<>();

        }
        tempPhotos = GalleryScanHelper.transferBeanToString(temp_mediaPicBean);
        GalleryScanHelper.checked_photos.clear();
        GalleryScanHelper.checked_photos.addAll(tempPhotos);
        if (StorageConstans.MAX_COUNT == 1) {//头像时压缩如果不清除会有问题
            GalleryScanHelper.checked_photos.clear();
        }
        tv_right_btn.setText("确定(" + GalleryScanHelper.checkCounts() + ")");
        tv_chosen_total_view.setOnClickListener(this);
    }

    public void onEventMainThread(EventAction action) {
        switch (action.getType()) {
            case EVENT_GALLERY_ALL_PHOTOS://扫描所有图片完毕
                allPhotos = (List<MediaPicBean>) action.getData();
                real_all_photos.clear();
                real_all_photos.addAll(allPhotos);
                changeCurrentPhotoDatas(allPhotos);
                initAdapter();
                break;
            case EVENT_GALLERY_DIRS://扫描带图片的文件夹路径完毕
                dirInfos = (List<GalleryDirInfo>) action.getData();
                GalleryDirInfo info = new GalleryDirInfo("", allPhotos.get(0).getUrl(), "全部", allPhotos.size());
                dirInfos.add(0, info);
                if (presenter != null) {
                    presenter.initPopupWindow(dirInfos, this);
                }
                break;

        }
    }

    private void changeCurrentPhotoDatas(List<MediaPicBean> allPhotos) {
        PhotoHelper.current_photo_infos.clear();
        PhotoHelper.current_photo_infos.addAll(allPhotos);
    }


    private void initAdapter() {
        adapter = new GalleryAdapter(this, allPhotos);
        adapter.setOnGalleryClickLitener(this);
        adapter.setCheckedUrls(GalleryScanHelper.checked_photos);
        grid_photo.setAdapter(adapter);
    }

    @Override
    public void onGalleryAddClick() {
        Toast.makeText(this, "去掉用58照相api", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onGalleryCheckClicked(int count, MediaPicBean info) {
        tv_right_btn.setText("确定(" + GalleryScanHelper.checkCounts() + ")");
        checkedPhotos.add(info);
    }

    @Override
    public void onGalleryUnCheckClicked(int count, MediaPicBean info) {
        tv_right_btn.setText("确定(" + GalleryScanHelper.checkCounts() + ")");
        checkedPhotos.remove(info);
    }


    @Override
    public void onGalleryItemClicked(int pos, MediaPicBean info, int check_size) {
        PhotoHelper.current_photo_infos.clear();
        PhotoHelper.current_photo_infos.addAll(allPhotos);
        Intent intent = new Intent(this, SinglePhotoActivity.class);
        intent.putExtra("index", pos);
        intent.putExtra("checkedSize", GalleryScanHelper.checkCounts());
        startActivityForResult(intent, StorageConstans.REQUEST_GALLERY_2_SINGLE);

    }

    @Override
    public void onGalleryAlreaadyMax() {
        Toast.makeText(this, "最多选择" + StorageConstans.MAX_COUNT + "张", Toast.LENGTH_LONG).show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == StorageConstans.REQUEST_GALLERY_2_SINGLE) {
            switch (resultCode) {
                case StorageConstans.RESULT_CODE_SINGLE_2_GALLERY:
                    adapter.notifyDataSetChanged();
                    tv_right_btn.setText("确定(" + GalleryScanHelper.checkCounts() + ")");
                    break;
                case StorageConstans.RESULT_CODE_SINGLE_2_GALLERY_2_OTHER:
                    if (StorageConstans.MAX_COUNT == 1) {
                        ArrayList<MediaPicBean> beans = GalleryScanHelper.transferStringToBean(GalleryScanHelper.checked_photos);
                        clipPicture(beans.get(0).getUrl());

                    } else {
                        ArrayList<MediaPicBean> beans = GalleryScanHelper.transferStringToBean(GalleryScanHelper.checked_photos);
                        photoBean.setBeans(beans);
                        EventAction action = new EventAction(EventType.EVENT_SELECTED_AVATAR, photoBean);
                        EventbusAgent.getInstance().post(action);
                        onBackPressed();
                    }
                    break;
            }
        }else if (requestCode == CameraConstants.REQUEST_GOTO_EDITOR && Activity.RESULT_OK == resultCode) {
            //把这个赋给数组，传递走
            //Uri uri = data.getData();
            String path1 = data.getStringExtra(CameraConstants.WB_CAMERA_PHOTO_PATH);
            GalleryScanHelper.checked_photos.clear();
            GalleryScanHelper.checked_photos.add(path1);
            ArrayList<MediaPicBean> beans = GalleryScanHelper.transferStringToBean(GalleryScanHelper.checked_photos);
            photoBean.setBeans(beans);
            EventAction action = new EventAction(EventType.EVENT_SELECTED_AVATAR, photoBean);
            EventbusAgent.getInstance().post(action);
            onBackPressed();
        }
    }

    /**
     * E
     * 裁剪图片
     */
    private void clipPicture(String path) {
        /*if(path != null && new File(path).exists()){
            Uri uri = Uri.fromFile(new File(path));
            Intent intent = new Intent(this, WBEditorActivity.class);
            intent.setAction(EditorConstants.ACTION_PHOTO_EDITOR);
            intent.putExtra(EditorConstants.FILTER_PHOTO_URI, uri);
            startActivityForResult(intent, CameraConstants.REQUEST_GOTO_EDITOR);
        }*/



        /*ViewParam vp = new ViewParam();
        vp.objectType = ViewParam.OBJECT_STRING;
        vp.data = path;
        CropDialog cropDialog = new CropDialog(this, CropDialog.CropImageType.LOCAL);
        cropDialog.setViewParam(vp);
        cropDialog.setSetImageCallback(new SetImageCallback() {
            @Override
            public void onImageSave(String... path) {
                String path1 = path[0];
                //把这个赋给数组，传递走
                GalleryScanHelper.checked_photos.clear();
                GalleryScanHelper.checked_photos.add(path1);
                ArrayList<MediaPicBean> beans = GalleryScanHelper.transferStringToBean(GalleryScanHelper.checked_photos);
                photoBean.setBeans(beans);
                EventAction action = new EventAction(EventType.EVENT_SELECTED_AVATAR, photoBean);
                EventbusAgent.getInstance().post(action);
                onBackPressed();
            }
        });
        cropDialog.show();*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_right_btn:
            case R.id.tv_right_btn://确定
                if (GalleryScanHelper.checked_photos.size() == 0) {
                    Toast.makeText(this,"请选择至少一张图片",0).show();
                    return;
                }
                ArrayList<MediaPicBean> beans = GalleryScanHelper.transferStringToBean(GalleryScanHelper.checked_photos);
                if (StorageConstans.MAX_COUNT == 1) {
                    clipPicture(beans.get(0).getUrl());
                } else {
                    photoBean.setBeans(beans);
                    EventAction action = new EventAction(EventType.EVENT_SELECTED_AVATAR, photoBean);
                    EventbusAgent.getInstance().post(action);
                    onBackPressed();
                }
                break;
            case R.id.tv_choose_dir://选择文件夹
                presenter.showPopupWindow(tv_back);
                break;
            case R.id.tv_back://返回
                photoBean.setBeans(temp_mediaPicBean);
                EventAction action1 = new EventAction(EventType.EVENT_SELECTED_AVATAR, photoBean);
                EventbusAgent.getInstance().post(action1);
                onBackPressed();
                break;
            case R.id.tv_chosen_total_view://预览
                onGalleryItemClicked(0, null, 0);
                break;
        }
    }


    /**
     * 给各个控件绑定listener
     */
    private void bindListener() {
        grid_photo.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE
                        && last_state == SCROLL_STATE_FLING) {
                    //    adapter.unLock(0, 0);
                    //    adapter.notifyDataSetChanged();
                    ImageLoader.getInstance().resume();//加载
                } else if (scrollState == SCROLL_STATE_FLING) {
                    //   adapter.lock();
                    ImageLoader.getInstance().pause();//不加载
                }
                if (scrollState != SCROLL_STATE_FLING) {
                    //  adapter.unLock(0, 0);
                    ImageLoader.getInstance().resume();//加载
                }
                last_state = scrollState;
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        ll_right_btn.setOnClickListener(this);
        tv_choose_dir.setOnClickListener(this);
        tv_back.setOnClickListener(this);
    }

    @Override
    public void OnPopupItemClick(int pos) {
        GalleryDirInfo dirInfo = dirInfos.get(pos);
        if (pos == 0) {
            allPhotos.clear();
            allPhotos.addAll(real_all_photos);
        } else {
            allPhotos.clear();
            allPhotos.addAll(presenter.getPhotoListFromDir(dirInfo));
        }
        initAdapter();
        tv_choose_dir.setText(dirInfo.getName());
        presenter.dismissPopupWindow();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventbusAgent.getInstance().unregister(this);
        adapter.releaseSources();
        System.gc();
    }
}
