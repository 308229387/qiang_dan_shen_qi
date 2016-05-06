package com.huangyezhaobiao.presenter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.widget.BaseAdapter;

import com.huangye.commonlib.constans.ResponseConstans;
import com.huangye.commonlib.utils.JsonUtils;
import com.huangye.commonlib.utils.NetBean;
import com.huangye.commonlib.utils.PhoneUtils;
import com.huangyezhaobiao.adapter.RefundReasonAdapter;
import com.huangyezhaobiao.application.BiddingApplication;
import com.huangyezhaobiao.bean.tt.RefundFirstReasonEntity;
import com.huangyezhaobiao.constans.AppConstants;
import com.huangyezhaobiao.mediator.RefundMediator;
import com.huangyezhaobiao.photomodule.BaseMediaBean;
import com.huangyezhaobiao.photomodule.GalleryActivity;
import com.huangyezhaobiao.photomodule.MediaAdapter;
import com.huangyezhaobiao.photomodule.MediaPicBean;
import com.huangyezhaobiao.photomodule.MultiMediaSD;
import com.huangyezhaobiao.photomodule.ViewSinglePhotoActivity;
import com.huangyezhaobiao.picupload.UICallback;
import com.huangyezhaobiao.picupload.XHttpWrapper;
import com.huangyezhaobiao.url.URLConstans;
import com.huangyezhaobiao.utils.TaskUtil;
import com.huangyezhaobiao.utils.UploadPicUtil;
import com.huangyezhaobiao.utils.UserUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by shenzhixin on 2015/12/9.
 */
public class RefundPresenter {
    private Context context;
    //退单原因的适配器
    private RefundReasonAdapter refundReasonAdapter;
    //媒介的适配器
    private MediaAdapter        mediaAdapter;
    public RefundPresenter(Context context){
        this.context = context;
        refundReasonAdapter = new RefundReasonAdapter(context,new ArrayList<RefundFirstReasonEntity>());
    }

    public void initMediaAdapter(List<BaseMediaBean> baseMediaBeans,int maxCount){
        mediaAdapter = new MediaAdapter(context,baseMediaBeans,true,maxCount);
    }

    public BaseAdapter getMediaAdapter(){
        return mediaAdapter;
    }
    public BaseAdapter getRefundResonAdapter(){
        return refundReasonAdapter;
    }
    public void onDestroy(){
        mediaAdapter.releaseAllSources();
    }

    /**
     * 去相册
     * @param context
     * @param maxCount
     * @param type
     * @param beans
     */
    public void goToGalleryActivity(Context context,int maxCount,int type,ArrayList<MediaPicBean> beans){
        Intent intent = GalleryActivity.onNewIntent(context,maxCount,type,beans);
        context.startActivity(intent);
    }



    public ArrayList<MediaPicBean> transferToMediaPicBean(List<BaseMediaBean> baseMediaBeans) {
        ArrayList<MediaPicBean> mediaPicBeans = new ArrayList<>();
        for (int index = 0;index <baseMediaBeans.size();index++){
            MediaPicBean mediaPicBean = new MediaPicBean(baseMediaBeans.get(index).getUrl());
            mediaPicBeans.add(mediaPicBean);
        }
        return mediaPicBeans;
    }


    public ArrayList<BaseMediaBean> transferToBaseMediaBean(List<MediaPicBean> mediaPicBeans){
        ArrayList<BaseMediaBean> baseMediaBeans = new ArrayList<>();
        for(int index=0;index<mediaPicBeans.size();index++){
            BaseMediaBean baseMediaBean = new BaseMediaBean(MultiMediaSD.MEDIA_TYPE_PIC,mediaPicBeans.get(index).getUrl());
            baseMediaBeans.add(baseMediaBean);
        }
        return baseMediaBeans;
    }

    public void goToViewSinglePhotoActivity(Context context,int index,ArrayList<MediaPicBean> mediaPicBeans) {
        Intent intent = ViewSinglePhotoActivity.newIntent(context, index, mediaPicBeans);
        context.startActivity(intent);
    }


    //去提交退单评价

    /**
     * userId=
     orderId=
     cancelReasons="2" //退单原因
     detailDesc=""
     image1=
     image2=
     * @param context
     * @param orderId
     * @param refundReasonId
     * @param refundDesc
     * @param dataSources
     */
    public void submitRefund(final Context context,String orderId,String refundReasonId, String refundDesc, List<BaseMediaBean> dataSources,final UICallback callBack) {
        List<MediaPicBean> mediaPicBeans       = getSubmitBeans(dataSources);
        final List<File>         imgFiles      = transferToFiles(mediaPicBeans);
        final HashMap<String,String> refundMaps    = new HashMap<>();
       // refundMaps.put("userId",UserUtils.getUserId(context));
        refundMaps.put("orderId",orderId);
        refundMaps.put("cancelReasons",refundReasonId);
        refundMaps.put("detailDesc",refundDesc);
        SharedPreferences sharedPreferences = context.getSharedPreferences(ResponseConstans.SP_NAME, Context.MODE_PRIVATE);
        String myToken = sharedPreferences.getString(ResponseConstans.KEY_LOGIN_TOKEN, "");
        refundMaps.put("loginToken", myToken);
        TaskUtil.executeAsyncTask(new AsyncTask<Void, Void, RequestParams>() {
            @Override
            protected RequestParams doInBackground(Void... params) {
                //配置参数
                return getAddCommentRequestParams(context, refundMaps, imgFiles, callBack);
            }

            @Override
            protected void onPostExecute(RequestParams requestParams) {
                //执行
                if (isCancelled())
                    return;
                if (requestParams != null) {
                    submitCommentApi(URLConstans.URL_FIRST_SUBMIT_REFUND,context, requestParams, callBack);
                } else {
                    if (callBack != null) {
                        callBack.onUploadPicFailure("上传的参数是空的");
                    }
                }
            }
        });
    }

    private List<File> transferToFiles(List<MediaPicBean> mediaPicBeans) {
        List<File> fileList = new ArrayList<>();
        for(int index=0;index<mediaPicBeans.size();index++){
            File file = new File(mediaPicBeans.get(index).getUrl());
            fileList.add(file);
        }
        return fileList;

    }

    /**
     * 发送这个添加评价的请求
     */
    private static void submitCommentApi(String url,final Context context, final RequestParams params, final UICallback listener) {
        XHttpWrapper<String> http = new XHttpWrapper<>();
        http.post(url, params, new RequestCallBack<String>() {
            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
                if (listener != null) {
                    listener.onUploadPrecent("已上传:" + ((current / total) * 100) + "%");
                }
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (responseInfo == null) {
                    if (listener != null) {
                        listener.onUploadPicFailure("上传图片并没有返回结果");
                    }
                    return;
                }
                NetBean netBean = JsonUtils.jsonToNetBean(responseInfo.result);
                Map<String, String> map = JsonUtils.jsonToMap(netBean.getData());
                if ("0".equals(map.get("status"))) {
                    listener.onUploadPicSuccess(map.get("msg"));
                    RefundMediator.checkedId.clear();
                } else {
                    listener.onUploadPicFailure(map.get("msg"));
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.e("shenzhixin", "upload error:" + s);
                if (listener != null) {
                    if (!TextUtils.isEmpty(s)) {
                        listener.onUploadPicFailure(s);
                    } else {
                        listener.onUploadPicFailure("未知错误");
                    }
                }
            }
        });
    }

    /**
     * 添加通用headder
     * @return
     */
    private static RequestParams getRequestParams(){
        RequestParams params = new RequestParams();
        List<RequestParams.HeaderItem> list = params.getHeaders();
        if(list!= null && list.size() != 0){
            list.clear();
        }

        params.addHeader("ppu", UserUtils.getUserPPU(BiddingApplication.getAppInstanceContext()));
        params.addHeader("userId",UserUtils.getPassportUserId(BiddingApplication.getAppInstanceContext()));
        Log.e("sdf", "ppu:" + UserUtils.getUserPPU(BiddingApplication.getAppInstanceContext()));
        Log.e("sdf", "userId:" + UserUtils.getPassportUserId(BiddingApplication.getAppInstanceContext()));
//        try {
//            params.addHeader("version", VersionUtils.getVersionCode(BiddingApplication.getAppInstanceContext()));
        params.addHeader("version", "5");
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
        params.addHeader("platform", "1");
        params.addHeader("UUID", PhoneUtils.getIMEI(BiddingApplication.getAppInstanceContext()));
        return params;
    }


    private static RequestParams getAddCommentRequestParams(final Context context, HashMap<String, String> map, List<File> files, final UICallback listener) {
        RequestParams params = getRequestParams();
        Set<Map.Entry<String, String>> entries = map.entrySet();
        Iterator<Map.Entry<String, String>> iterator = entries.iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> next = iterator.next();
            String key = next.getKey();
            String value = next.getValue();
            params.addBodyParameter(key, value);
        }
        int size = files.size();
        for (int index = 0; index < size; index++) {
            File file = files.get(index);
            if (!file.exists()) {
                if (listener != null) {
                    //在子线程，不能这么回调，或者就直接在主activity里runonuithread
                   listener.onUploadPicFailure("上传的第"+(index+1)+"个文件并不存在!");
                }
                return null;
            }
            try {
                String headUrl = file.getAbsolutePath();
                if (file.length() / 1024 <= AppConstants.UPLOAD_PIC_MAX_SIZE) {
                    params.addBodyParameter(AppConstants.UPLOAD_REFUND_EVIDENCE + (index + 1), file,"image/*");
                } else {
                    String url = AppConstants.Directorys.SDCARD + "/tmp"  + index + SystemClock.currentThreadTimeMillis() + ".jpg";
                    File emp_file = new File(url);
                    if (emp_file.exists()) {
                        emp_file.delete();
                   }
                    UploadPicUtil.compressImage(headUrl, url);
                    params.addBodyParameter(AppConstants.UPLOAD_REFUND_EVIDENCE + (index + 1), new File(url),"image/*");
                }
                //params.addHeader("Content-Type", "image/*");
            } catch (Exception e) {
                e.printStackTrace();
                if (listener != null) {
                   listener.onUploadPicFailure("添加参数时出现问题了");
                }
                return null;
            }
        }

        return params;
    }


    private List<MediaPicBean> getSubmitBeans(List<BaseMediaBean> dataSources){
        List<MediaPicBean> mediaPicBeans = new ArrayList<>();
        for(int index=0;index<dataSources.size();index++){
            BaseMediaBean baseMediaBean = dataSources.get(index);
            if(baseMediaBean.getType()==MultiMediaSD.MEDIA_TYPE_ADD){
                continue;
            }
            MediaPicBean mediaPicBean = new MediaPicBean(baseMediaBean.getUrl());
            mediaPicBeans.add(mediaPicBean);
        }
        return mediaPicBeans;
    }

    /**
     * /app/addEvidenceOrder?
     userId=
     orderId=
     cancelOrderId=
     image1=
     image2=
     *
     */
    public void submitAddRefund(final Context context,String userId,String orderId,String cancelOrderId, List<BaseMediaBean> dataSources,final UICallback callBack){
        List<MediaPicBean> mediaPicBeans       = getSubmitBeans(dataSources);
        final List<File>         imgFiles      = transferToFiles(mediaPicBeans);
        final HashMap<String,String> refundMaps    = new HashMap<>();
       // refundMaps.put("userId",userId);
        refundMaps.put("orderId",orderId);
       // refundMaps.put("cancelOrderId",cancelOrderId);
        SharedPreferences sharedPreferences = context.getSharedPreferences(ResponseConstans.SP_NAME, Context.MODE_PRIVATE);
        String myToken = sharedPreferences.getString(ResponseConstans.KEY_LOGIN_TOKEN, "");
        refundMaps.put("loginToken", myToken);
        TaskUtil.executeAsyncTask(new AsyncTask<Void, Void, RequestParams>() {
            @Override
            protected RequestParams doInBackground(Void... params) {
                //配置参数
                return getAddCommentRequestParams(context, refundMaps, imgFiles, callBack);
            }

            @Override
            protected void onPostExecute(RequestParams requestParams) {
                //执行
                if (isCancelled())
                    return;
                if (requestParams != null) {
                    submitCommentApi(URLConstans.URL_ADD_SUBMIT_REFUND,context, requestParams, callBack);
                } else {
                    if (callBack != null) {
                        callBack.onUploadPicFailure("上传的参数是空的");
                    }
                }
            }
        });
    }
}
