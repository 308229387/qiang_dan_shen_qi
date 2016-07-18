package com.huangyezhaobiao.fragment.refund;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.huangye.commonlib.vm.callback.NetWorkVMCallBack;
import com.huangyezhaobiao.R;
import com.huangyezhaobiao.activity.MainActivity;
import com.huangyezhaobiao.activity.RefundActivity;
import com.huangyezhaobiao.adapter.RefundReasonAdapter;
import com.huangyezhaobiao.bean.tt.RefundFirstCommitBean;
import com.huangyezhaobiao.bean.tt.RefundFirstReasonEntity;
import com.huangyezhaobiao.constans.CommonValue;
import com.huangyezhaobiao.eventbus.EventAction;
import com.huangyezhaobiao.inter.ETInterface;
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
import com.huangyezhaobiao.utils.HYEventConstans;
import com.huangyezhaobiao.utils.HYMob;
import com.huangyezhaobiao.utils.KeyboardUtil;
import com.huangyezhaobiao.utils.NetUtils;
import com.huangyezhaobiao.utils.StringUtils;
import com.huangyezhaobiao.view.AttentionDialog;
import com.huangyezhaobiao.view.HorizontialListView;
import com.huangyezhaobiao.view.ResultDialog;
import com.huangyezhaobiao.view.UploadPicDialog;
import com.huangyezhaobiao.view.ZhaoBiaoDialog;
import com.huangyezhaobiao.vm.RefundCloseVM;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shenzhixin on 2015/12/9.
 * 第一次提交退单的操作
 * //展示,提交,这块很麻烦
 */
public class FirstRefundCommitFragment extends RefundBaseFragment implements NetWorkVMCallBack, View.OnClickListener, AttentionDialog.RequestOkListener, ETInterface {
    private TextView              tv_order_number;
    private TextView              tv_hint_text;
    private GridView              gridView_quit_reason;
    private EditText              et_refund_desc;
    private HorizontialListView   hlv_photo_container;
    private Button                btn_refund_submit;
    //照相
    private Button                btn_toggle_gallery;
    private Button                btn_toggle_camera;
    private View                  btn_toggle_select_pic;
    private View                  select_view;
    private View                  layout_no_internet;
    private TextView              tv_hint_count;
    private View                  rl_submit;
    //照相
    private RefundCloseVM         refundCloseVM;
    private RefundPresenter       refundPresenter;
    private RefundFirstCommitBean refundFirstCommitBean;
    private List<BaseMediaBean>   baseMediaBeans = new ArrayList<>();

    private AnimatePresenter      animatePresenter;
    private String photo_path;

    private ZhaoBiaoDialog  confirmDialog_upload;//上传图片的对话框
    private ResultDialog    resultDialog_success;//成功的结果反馈
    private ResultDialog    resultDialog_failure;//失败的结果反馈
    private AttentionDialog  refund_reason_dialog;//请选择退单原因
    private AttentionDialog  refund_desc_dialog;//请填写退单描述
    private UploadPicDialog uploadPicDialog;//图片的对话框



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        refundCloseVM    = new RefundCloseVM(this,getActivity());
        refundCloseVM.setEtInterface(this);
        refundPresenter  = new RefundPresenter(getActivity());
        animatePresenter = new AnimatePresenter(getActivity());
        initDialog();

    }

    private void initDialog() {
        confirmDialog_upload = new ZhaoBiaoDialog(getActivity(), StringUtils.getStringByResId(getActivity(),R.string.upload_pic_suggest));
        confirmDialog_upload.setPositiveText(StringUtils.getStringByResId(getActivity(),R.string.submit_direct));
        confirmDialog_upload.setNagativeText(StringUtils.getStringByResId(getActivity(), R.string.upload_evidence));
        confirmDialog_upload.setOnDialogClickListener(new ZhaoBiaoDialog.onDialogClickListener() {
            @Override
            public void onDialogOkClick() {
                //直接提交
                //TODO:上传
                confirmDialog_upload.dismiss();
                refundPresenter.submitRefund(getActivity(), refundFirstCommitBean.getOrderId(), RefundMediator.checkedId.get(0), et_refund_desc.getText().toString(), ((MediaAdapter) refundPresenter.getMediaAdapter()).getDataSources(), new UICallback() {
                    @Override
                    public void onUploadPicSuccess(String msg) {
                        uploadPicDialog.dismiss();
                        resultDialog_success.show();
                    }

                    @Override
                    public void onUploadPicFailure(String err) {
                        uploadPicDialog.dismiss();
                        resultDialog_failure.show();
                    }

                    @Override
                    public void onUploadPrecent(String precent) {
                        if(!uploadPicDialog.isShowing()) {
                            uploadPicDialog.show();
                        }
                        uploadPicDialog.setUploadProgress(precent);
                    }
                });
            }

            @Override
            public void onDialogCancelClick() {
                //上传证据
                confirmDialog_upload.dismiss();
            }


        });
        uploadPicDialog = new UploadPicDialog(getActivity());
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
        refund_reason_dialog = new AttentionDialog(getActivity(),StringUtils.getStringByResId(getActivity(),R.string.write_refund_reason),R.drawable.refund_attention);
        refund_desc_dialog = new AttentionDialog(getActivity(),StringUtils.getStringByResId(getActivity(), R.string.write_refund_desc),R.drawable.refund_attention);
//        refund_desc_dialog.setCancelButtonGone();
//        refund_reason_dialog.setCancelButtonGone();
        refund_reason_dialog.setListener(this);
        refund_desc_dialog.setListener(this);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_first_refund_commit;
    }

    @Override
    protected void inflateRootView() {
        layout_no_internet    = rootView.findViewById(R.id.layout_no_internet);
        tv_hint_text          = (TextView) rootView.findViewById(R.id.tv_hint_text);
        tv_order_number       = (TextView) rootView.findViewById(R.id.tv_order_number);
        rl_submit             = rootView.findViewById(R.id.rl_submit);
        gridView_quit_reason  = (GridView) rootView.findViewById(R.id.gridView_quit_reason);
        et_refund_desc        = (EditText) rootView.findViewById(R.id.et_refund_desc);
        hlv_photo_container   = (HorizontialListView) rootView.findViewById(R.id.hlv_photo_container);
        btn_refund_submit     = (Button) rootView.findViewById(R.id.btn_refund_submit);
        select_view           = rootView.findViewById(R.id.select_view);
        tv_hint_count         = (TextView) rootView.findViewById(R.id.tv_hint_count);
        btn_toggle_gallery    = (Button) rootView.findViewById(R.id.btn_toggle_gallery);
        btn_toggle_camera     = (Button) rootView.findViewById(R.id.btn_toggle_camera);
        btn_toggle_select_pic = rootView.findViewById(R.id.btn_toggle_select_pic);
        gridView_quit_reason.setAdapter(refundPresenter.getRefundResonAdapter());
        refundPresenter.initMediaAdapter(baseMediaBeans, CommonValue.MAX_COUNT);
        hlv_photo_container.setAdapter(refundPresenter.getMediaAdapter());
        initListener();
    }

    private void initListener() {
        btn_toggle_select_pic.setOnClickListener(this);
        btn_toggle_camera.setOnClickListener(this);
        btn_toggle_gallery.setOnClickListener(this);
        btn_refund_submit.setOnClickListener(this);
        et_refund_desc.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    ((EditText) v).setHint("");
                }

            }
        });
        et_refund_desc.addTextChangedListener(refundCloseVM.getEditTextChangedListener());
    }

    @Override
    protected void getDatas() {
        refundCloseVM.setRefundType(RefundMediator.TYPE_REFUND_FIRST_COMMIT);
        refundCloseVM.setOrderId(orderId);
        refundCloseVM.getDatas();
    }

    @Override
    public void onLoadingStart() {
        startLoading();
    }

    @Override
    public void onLoadingSuccess(Object t) {
        stopLoading();
        rl_submit.setVisibility(View.GONE);
        layout_no_internet.setVisibility(View.GONE);
        if(t instanceof RefundFirstCommitBean){
            refundFirstCommitBean = (RefundFirstCommitBean) t;
            tv_order_number.setText(refundFirstCommitBean.getOrderId());
            //刷adapter
            List<RefundFirstReasonEntity> reasonEntities = refundFirstCommitBean.getCancelReasons();
            ((RefundReasonAdapter)refundPresenter.getRefundResonAdapter()).refreshDataSources(reasonEntities);
            gridView_quit_reason.setAdapter(refundPresenter.getRefundResonAdapter());
        }
    }




    @Override
    public void onLoadingError(String msg) {
        stopLoading();
        rl_submit.setVisibility(View.GONE);
        layout_no_internet.setVisibility(View.GONE);

        if (!TextUtils.isEmpty(msg) && msg.equals("2001")) {
            RefundActivity ola = (RefundActivity) getActivity();
            ola.onLoadingError(msg);
        }else if(!TextUtils.isEmpty(msg)) {
            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
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
        //TODO:LOGIN
        stopLoading();
        rl_submit.setVisibility(View.GONE);
        ((RefundActivity)getActivity()).onLoginInvalidate();
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
                HYMob.getDataList(getActivity(), HYEventConstans.EVENT_ID_CAMERA);
                break;
            case R.id.btn_toggle_select_pic://收,什么都没选
                animatePresenter.hideRotateTranslateAnimation(select_view, btn_toggle_select_pic, btn_toggle_gallery, btn_toggle_camera);
                break;
            case R.id.btn_refund_submit://点击退单
                submit();
                break;
        }
    }

    //校验:原因/描述/图片
    private void submit() {
        //点击了退单按钮
        BDMob.getBdMobInstance().onMobEvent(getActivity(), BDEventConstans.EVENT_ID_REFUND_PAGE_SUBMIT);

        HYMob.getDataList(getActivity(), HYEventConstans.EVENT_ID_REFUND_PAGE_SUBMIT);

        //点击了退单按钮
        if(!NetUtils.isNetworkConnected(getActivity())){//没有网络
            Toast.makeText(getActivity(),"没有网络,请检查网络设置",Toast.LENGTH_SHORT).show();
            return;
        }
        if(RefundMediator.checkedId.size()==0){
            //退单原因
            refund_reason_dialog.show();
            return;
        }
        if(TextUtils.isEmpty(et_refund_desc.getText().toString())){
            //详细描述
            refund_desc_dialog.show();
            return;
        }
        if(java.nio.charset.Charset.forName("GBK").newEncoder().canEncode(et_refund_desc.getText().toString())){

        }else{
            Toast.makeText(getActivity(),"请不要输入例如表情等特殊字符",Toast.LENGTH_SHORT).show();
            return;
        }

        if(baseMediaBeans.size()==0){
            //有没有图片
            confirmDialog_upload.show();
            return;
        }

        rl_submit.setVisibility(View.VISIBLE);
            refundPresenter.submitRefund(getActivity(), refundFirstCommitBean.getOrderId(), RefundMediator.checkedId.get(0), et_refund_desc.getText().toString(), ((MediaAdapter) refundPresenter.getMediaAdapter()).getDataSources(), new UICallback() {
                        @Override
                        public void onUploadPicSuccess(String msg) {
                            uploadPicDialog.dismiss();
                            resultDialog_success.show();
                            rl_submit.setVisibility(View.GONE);
                }

                @Override
                public void onUploadPicFailure(final String err) {
                    if(isAdded()) { //getActivity（）！= null
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                uploadPicDialog.dismiss();
                                resultDialog_failure.show();
                                rl_submit.setVisibility(View.GONE);
                            }
                        });
                    }

                }

                @Override
                public void onUploadPrecent(String precent) {
                    Log.e("shenzhixin",precent);
                    if(!uploadPicDialog.isShowing()) {
                        uploadPicDialog.show();
                    }
                    uploadPicDialog.setUploadProgress(precent);

                }
            });
    }

    @Override
    public void fillDatas() {
        super.fillDatas();
        Log.e("shenzhixin", "hahaha:" + photo_path);
        BaseMediaBean mediaBean = new MediaPicBean(photo_path);
        baseMediaBeans.add(mediaBean);
        ((MediaAdapter)refundPresenter.getMediaAdapter()).refreshDatas(baseMediaBeans);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        releaseSources();
        System.gc();
    }

    private void releaseSources() {
        tv_hint_text.setBackgroundResource(0);
        btn_refund_submit.setBackgroundResource(0);
        refundPresenter.onDestroy();
    }

    @Override
    public void onChanged(String text) {
        tv_hint_count.setText(text);
    }

    @Override
    public void onRequestOkClick() {
        refund_reason_dialog.dismiss();
        refund_desc_dialog.dismiss();
    }
}
