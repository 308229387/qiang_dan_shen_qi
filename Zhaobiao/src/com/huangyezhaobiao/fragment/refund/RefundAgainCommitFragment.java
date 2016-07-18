package com.huangyezhaobiao.fragment.refund;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.huangye.commonlib.vm.callback.NetWorkVMCallBack;
import com.huangyezhaobiao.R;
import com.huangyezhaobiao.activity.RefundActivity;
import com.huangyezhaobiao.bean.RefundResultBean;
import com.huangyezhaobiao.constans.CommonValue;
import com.huangyezhaobiao.eventbus.EventAction;
import com.huangyezhaobiao.mediator.RefundMediator;
import com.huangyezhaobiao.photomodule.BaseMediaBean;
import com.huangyezhaobiao.photomodule.CameraHelper;
import com.huangyezhaobiao.photomodule.MediaAdapter;
import com.huangyezhaobiao.photomodule.MediaPicBean;
import com.huangyezhaobiao.photomodule.PhotoBean;
import com.huangyezhaobiao.picupload.UICallback;
import com.huangyezhaobiao.presenter.AnimatePresenter;
import com.huangyezhaobiao.presenter.RefundPresenter;
import com.huangyezhaobiao.utils.BDEventConstans;
import com.huangyezhaobiao.utils.BDMob;
import com.huangyezhaobiao.utils.CharUtils;
import com.huangyezhaobiao.utils.HYEventConstans;
import com.huangyezhaobiao.utils.HYMob;
import com.huangyezhaobiao.utils.KeyboardUtil;
import com.huangyezhaobiao.utils.NetUtils;
import com.huangyezhaobiao.utils.StringUtils;
import com.huangyezhaobiao.utils.UserUtils;
import com.huangyezhaobiao.view.HorizontialListView;
import com.huangyezhaobiao.view.ResultDialog;
import com.huangyezhaobiao.view.UploadPicDialog;
import com.huangyezhaobiao.view.ZhaoBiaoDialog;
import com.huangyezhaobiao.vm.RefundResultVM;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shenzhixin on 2015/12/14.
 * 退单补交界面
 */
public class RefundAgainCommitFragment extends RefundBaseFragment implements NetWorkVMCallBack, View.OnClickListener {
    private TextView tv_order_id_content;
    private TextView tv_refund_reason_content;
    private TextView tv_refund_desc_content;
    private TextView tv_refund_evidence_content;
    private TextView tv_refund_result_content;
    private View     select_view;
    private View     layout_no_internet;
    private View     rl_submit;
    private Button   btn_toggle_gallery,btn_toggle_camera,btn_refund_submit;
    private View btn_toggle_select_pic;
    private HorizontialListView horizontialListView;
    private RefundPresenter refundPresenter;
    private RefundResultVM refundResultVM;
    private RefundResultBean refundResultBean;
    private MediaAdapter     mediaAdapter;
    private AnimatePresenter animatePresenter;
    private ZhaoBiaoDialog notUploadPicDialog;//没有上传图片的对话框
    private ZhaoBiaoDialog confirmAddPicDialog;//确定补交图片的对话框
    private List<BaseMediaBean> baseMediaBeans = new ArrayList<>();
    private String photo_path;
    private UploadPicDialog uploadPicDialog;//图片的对话框
    private ResultDialog resultDialog_success;//成功的结果反馈
    private ResultDialog resultDialog_failure;//失败的结果反馈
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        refundResultVM   = new RefundResultVM(this,getActivity());
        refundPresenter  = new RefundPresenter(getActivity());
        animatePresenter = new AnimatePresenter(getActivity());
       // Toast.makeText(getActivity(), "hahaha", Toast.LENGTH_SHORT).show();
        initDialog();
    }

    private void initDialog() {
        resultDialog_success = new ResultDialog(getActivity(),"提交成功",R.drawable.refund_result_success,StringUtils.getStringByResId(getActivity(),R.string.refund_submit_success));
        resultDialog_success.setListener(new ResultDialog.RequestOkListener() {
            @Override
            public void onRequestOkClick() {
                resultDialog_success.dismiss();
                getActivity().onBackPressed();
            }
        });
        resultDialog_failure = new ResultDialog(getActivity(),"提交失败",R.drawable.refund_result_fail,StringUtils.getStringByResId(getActivity(),R.string.refund_submit_failure));
        resultDialog_failure.setListener(new ResultDialog.RequestOkListener() {
            @Override
            public void onRequestOkClick() {
                resultDialog_failure.dismiss();
            }
        });

        uploadPicDialog    = new UploadPicDialog(getActivity());
        notUploadPicDialog = new ZhaoBiaoDialog(getActivity(),StringUtils.getStringByResId(getActivity(),R.string.not_upload_pic));
        notUploadPicDialog.setCancelButtonGone();
        notUploadPicDialog.setOnDialogClickListener(new ZhaoBiaoDialog.onDialogClickListener() {
            @Override
            public void onDialogOkClick() {
                notUploadPicDialog.dismiss();
            }

            @Override
            public void onDialogCancelClick() {

            }
        });
        confirmAddPicDialog = new ZhaoBiaoDialog(getActivity(),StringUtils.getStringByResId(getActivity(),R.string.confirm_add_evidence));
        confirmAddPicDialog.setOnDialogClickListener(new ZhaoBiaoDialog.onDialogClickListener() {
            @Override
            public void onDialogOkClick() {
                if(confirmAddPicDialog!=null && confirmAddPicDialog.isShowing()) {
                    confirmAddPicDialog.dismiss();
                }
                if(rl_submit!=null) {
                    rl_submit.setVisibility(View.VISIBLE);
                }
                refundPresenter.submitAddRefund(getActivity(), UserUtils.getUserId(getActivity()), refundResultBean.getOrderId(),"", ((MediaAdapter) refundPresenter.getMediaAdapter()).getDataSources(), new UICallback() {
                    @Override
                    public void onUploadPicSuccess(String msg) {
                        uploadPicDialog.dismiss();
                        resultDialog_success.show();
                        rl_submit.setVisibility(View.GONE);
                    }

                    @Override
                    public void onUploadPicFailure(String err) {
                        uploadPicDialog.dismiss();
                        resultDialog_failure.show();
                        rl_submit.setVisibility(View.GONE);
                    }

                    @Override
                    public void onUploadPrecent(String precent) {
                        Log.e("shenzhixin",precent);
                        if(!uploadPicDialog.isShowing()){
                            uploadPicDialog.show();
                        }
                        uploadPicDialog.setUploadProgress(precent);
                    }
                });
            }

            @Override
            public void onDialogCancelClick() {
                confirmAddPicDialog.dismiss();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_refund_add_commit;
    }

    @Override
    protected void inflateRootView() {
        layout_no_internet         = rootView.findViewById(R.id.layout_no_internet);
        tv_refund_result_content   = (TextView) rootView.findViewById(R.id.tv_refund_result_content);
        rl_submit                  = rootView.findViewById(R.id.rl_submit);
        tv_order_id_content        = (TextView) rootView.findViewById(R.id.tv_order_id_content);
        tv_refund_reason_content   = (TextView) rootView.findViewById(R.id.tv_refund_reason_content);
        tv_refund_desc_content     = (TextView) rootView.findViewById(R.id.tv_refund_desc_content);
        tv_refund_evidence_content = (TextView) rootView.findViewById(R.id.tv_refund_evidence_content);
        horizontialListView        = (HorizontialListView) rootView.findViewById(R.id.hlv_photo_container);
        select_view                = rootView.findViewById(R.id.select_view);
        btn_refund_submit          = (Button) rootView.findViewById(R.id.btn_refund_submit);
        btn_toggle_gallery         = (Button) rootView.findViewById(R.id.btn_toggle_gallery);
        btn_toggle_camera          = (Button) rootView.findViewById(R.id.btn_toggle_camera);
        btn_toggle_select_pic      =  rootView.findViewById(R.id.btn_toggle_select_pic);
        refundPresenter.initMediaAdapter(baseMediaBeans, CommonValue.MAX_COUNT);
        mediaAdapter               = (MediaAdapter) refundPresenter.getMediaAdapter();
        horizontialListView.setAdapter(mediaAdapter);
        initListener();
    }

    private void initListener() {
        btn_toggle_select_pic.setOnClickListener(this);
        btn_toggle_camera.setOnClickListener(this);
        btn_toggle_gallery.setOnClickListener(this);
        btn_refund_submit.setOnClickListener(this);

    }

    private void deleteExistJavaBean(BaseMediaBean baseMediaBean){
        for(int i=0;i<baseMediaBeans.size();i++){
            BaseMediaBean mediaBean = baseMediaBeans.get(i);
            if(mediaBean.getUrl().equals(baseMediaBean.getUrl())){
                baseMediaBeans.remove(baseMediaBean);
                break;
            }
        }
    }

    public int indexOfJavaBean(BaseMediaBean baseMediaBean){
        for(int i=0;i<baseMediaBeans.size();i++){
            BaseMediaBean mediaBean = baseMediaBeans.get(i);
            if(mediaBean.getUrl().equals(baseMediaBean.getUrl())){
                return i;
            }
        }
        return -1;
    }

    @Override
    public void onEventMainThread(EventAction action) {
        switch (action.type) {
            case  EVENT_GRIDVIEW_ITEM_CLOSE://点击了关闭按钮
                BaseMediaBean baseMediaBean = (BaseMediaBean) action.data;
                deleteExistJavaBean(baseMediaBean);
                ((MediaAdapter)refundPresenter.getMediaAdapter()).refreshDatas(baseMediaBeans);
                break;
            case  EVENT_GRIDVIEW_ITEM_ADD://点击了+号
                BDMob.getBdMobInstance().onMobEvent(getActivity(), BDEventConstans.EVENT_ID_REFUND_PAGE_ADD_PHOTO);

                HYMob.getDataList(getActivity(), HYEventConstans.EVENT_ID_REFUND_PAGE_ADD_PHOTO);

                KeyboardUtil.hideSoftInput(getActivity());
                //TODO:展示dialog是照相还是相册
                select_view.setVisibility(View.VISIBLE);
                animatePresenter.showRotateTranslateAnimation(btn_toggle_select_pic, btn_toggle_gallery, btn_toggle_camera);
                break;
            case EVENT_GRIDVIEW_ITEM://点击了浏览
                BaseMediaBean bean = (BaseMediaBean) action.data;
                refundPresenter.goToViewSinglePhotoActivity(getActivity(), indexOfJavaBean(bean), refundPresenter.transferToMediaPicBean(baseMediaBeans));
                break;
            case EVENT_SELECTED_AVATAR://浏览图片
                PhotoBean photoBean = (PhotoBean) action.data;
                baseMediaBeans = refundPresenter.transferToBaseMediaBean(photoBean.getBeans());
                ((MediaAdapter)refundPresenter.getMediaAdapter()).refreshDatas(baseMediaBeans);
                break;
            case EVENT_BACK_FROM_VIEW_SINGLE://从单张回来
                ArrayList<MediaPicBean> beans = (ArrayList<MediaPicBean>) action.data;
                baseMediaBeans = refundPresenter.transferToBaseMediaBean(beans);
                ((MediaAdapter)refundPresenter.getMediaAdapter()).refreshDatas(baseMediaBeans);
                break;

        }
    }


    @Override
    public void fillDatas() {
        super.fillDatas();
        Log.e("shenzhixin", "hahaha:" + photo_path);
        BaseMediaBean mediaBean = new MediaPicBean(photo_path);
        baseMediaBeans.add(mediaBean);
        ((MediaAdapter)refundPresenter.getMediaAdapter()).refreshDatas(baseMediaBeans);
    }

    @Override
    protected void getDatas() {
        refundResultVM.setOrderId(orderId);
        refundResultVM.fetchReundResult();
    }

    @Override
    public void onLoadingStart() {
        startLoading();
    }

    @Override
    public void onLoadingSuccess(Object t) {
        stopLoading();
        layout_no_internet.setVisibility(View.GONE);
        rl_submit.setVisibility(View.GONE);
        if(t instanceof  RefundResultBean){
            refundResultBean = (RefundResultBean) t;
            initDatas();
        }
    }

    private void initDatas() {
        tv_order_id_content.setText(refundResultBean.getOrderId());
        tv_refund_desc_content.setText(refundResultBean.getDetailDesc());
        tv_refund_evidence_content.setText(refundResultBean.getEvidence());
        tv_refund_reason_content.setText(refundResultBean.getCancelReason());
        tv_refund_result_content.setText(refundResultBean.getCancelResult());
    }

    @Override
    public void onLoadingError(String msg) {
        stopLoading();
        layout_no_internet.setVisibility(View.GONE);
        rl_submit.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(msg) && msg.equals("2001")) {
            RefundActivity ola = (RefundActivity) getActivity();
            ola.onLoadingError(msg);
        }else if(!TextUtils.isEmpty(msg)){
            Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onLoadingCancel() {
        stopLoading();
        rl_submit.setVisibility(View.GONE);
    }

    @Override
    public void onNoInterNetError() {
        stopLoading();
        layout_no_internet.setVisibility(View.VISIBLE);
        Toast.makeText(getActivity(),"没有网络",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoginInvalidate() {
        stopLoading();
        rl_submit.setVisibility(View.GONE);
        ((RefundActivity)getActivity()).onLoginInvalidate();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_toggle_gallery://去相册
                select_view.setVisibility(View.GONE);
                refundPresenter.goToGalleryActivity(getActivity(), CommonValue.MAX_COUNT, RefundMediator.REFUND_COMMIT_TYPE, refundPresenter.transferToMediaPicBean(baseMediaBeans));
                animatePresenter.hideRotateTranslateAnimation(select_view, btn_toggle_select_pic, btn_toggle_gallery, btn_toggle_camera);
                break;
            case R.id.btn_toggle_camera://去拍照
                select_view.setVisibility(View.GONE);
                animatePresenter.hideRotateTranslateAnimation(select_view, btn_toggle_select_pic, btn_toggle_gallery, btn_toggle_camera);
                takeCamera();
                break;
            case R.id.btn_toggle_select_pic://收,什么都没选
                animatePresenter.hideRotateTranslateAnimation(select_view, btn_toggle_select_pic, btn_toggle_gallery, btn_toggle_camera);
                break;
            case R.id.btn_refund_submit://提交
                submit();
                break;

        }
    }

    private void submit() {
        //点击了退单按钮
        BDMob.getBdMobInstance().onMobEvent(getActivity(),BDEventConstans.EVENT_ID_REFUND_PAGE_SUBMIT);

        HYMob.getDataList(getActivity(), HYEventConstans.EVENT_ID_REFUND_PAGE_SUBMIT);

        //点击了退单按钮
        if(!NetUtils.isNetworkConnected(getActivity())){//没有网络
            Toast.makeText(getActivity(),"没有网络,请检查网络设置",Toast.LENGTH_SHORT).show();
            return;
        }

        if(baseMediaBeans.size()==0){
            //有没有图片
            notUploadPicDialog.show();
            return;
        }
        confirmAddPicDialog.show();


    }


    /**
     * 去拍照
     */
    private void takeCamera() {
        //如果sd卡不存在，就返回，并弹出提示
        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            Toast.makeText(getActivity(), getString(R.string.sd_card_does_not_exist), Toast.LENGTH_LONG).show();
            return;
        }
        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//调用android自带的照相机
            String pathS = Environment.getExternalStorageDirectory().toString()
                    + "/Bidding";
            File path1 = new File(pathS);//建立这个路径的文件或文件夹
            if (!path1.exists()) {//如果不存在，就建立文件夹
                path1.mkdirs();
            }
            File file = new File(path1, "Bidding" + System.currentTimeMillis()
                    + ".JPEG");//在path1这个文件夹下面建立yuemeiDoctor的文件
            //得到这个文件的uri
            Uri mOutPutFileUri = Uri.fromFile(file);
            photo_path = file.getPath();//得到这个照片的路径
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mOutPutFileUri);//设置一个参数
            startActivityForResult(intent, CameraHelper.TAKE_CAMERA_REQUEST_CODE);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
