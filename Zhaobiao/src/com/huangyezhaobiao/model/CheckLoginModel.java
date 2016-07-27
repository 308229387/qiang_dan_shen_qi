package com.huangyezhaobiao.model;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.huangye.commonlib.MyLoginBean.MineBean;
import com.huangye.commonlib.constans.ResponseConstans;
import com.huangye.commonlib.delegate.HttpRequestCallBack;
import com.huangye.commonlib.file.SharedPreferencesUtils;
import com.huangye.commonlib.model.NetWorkModel;
import com.huangye.commonlib.model.callback.NetworkModelCallBack;
import com.huangye.commonlib.network.HTTPTools;
import com.huangye.commonlib.network.HttpRequest;
import com.huangye.commonlib.utils.JsonUtils;
import com.huangye.commonlib.utils.NetBean;
import com.huangye.commonlib.utils.PhoneUtils;
import com.huangye.commonlib.utils.UserConstans;
import com.huangyezhaobiao.application.BiddingApplication;
import com.huangyezhaobiao.request.ZhaoBiaoRequest;
import com.huangyezhaobiao.url.URLConstans;
import com.huangyezhaobiao.utils.UserUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.wuba.loginsdk.external.LoginClient;

import java.util.List;

/**
 * author keyes
 * time 2016/4/21 10:08
 * email：1175426782@qq.com
 * param：
 * descript：验证ppu的可用性
 */
public class CheckLoginModel extends NetWorkModel {

    private static final String LOGIN_TYPE = "1";
    private static final String OTHER_TYPE = "2";
    private static final String TAG = CheckLoginModel.class.getName();
    public CheckLoginModel(NetworkModelCallBack baseSourceModelCallBack, Context context) {
        super(baseSourceModelCallBack, context);
    }

	@Override
    protected HttpRequest<String> createHttpRequest() {
        /** 登录招标 */
        return new ZhaoBiaoRequest<String>(ZhaoBiaoRequest.METHOD_POST, URLConstans.BASE_URL+"api/login", this);
//		return new ZhaoBiaoRequest<String>(ZhaoBiaoRequest.METHOD_POST, "http://10.252.158.8/api/login", this);
        /** 登录新版的passport */
        // return new ZhaoBiaoRequest<String>(ZhaoBiaoRequest.METHOD_POST,"https://passport.58.com/login/dologin",this);
    }

    @Override
    public void onLoadingSuccess(ResponseInfo<String> result) {
        Log.v(TAG,"onLoadingSuccess = " + jsonResult);
        boolean isLoginValidate = true;//登录是否合法，默认为是
		try {
			//这块需要做的是解析loginflag和token,如果loginflag为null，则表示是登录,存储token
			//如果loginflag不为null,则表示不是登录，就把token和存储出来的token进行比对
			//两个token一致,就表示正常,如果不一致,则表示退出登录,登录异常
			JSONObject jsonObject = JSON.parseObject(result.result);
			int loginflag = 0;
			//获得请求的flag
			try {
				loginflag = jsonObject.getInteger("loginFlag");
			}catch(Exception e){
				loginflag = -1;//如果解析不成功，不让他掉线
			}
//			setRequestURL(URLConstans.BASE_URL+"app/timestamp/sync");
//			getDatas();
			//获得返回的token
			String token     = jsonObject.getString("token");
			Log.e("shenzhixinHAHAHA","loginFlag:"+loginflag+",token:"+token);
			if(loginflag==1){//登录
				NetBean netBean = transformJsonToNetBean(result.result);
				MineBean mineBean = JsonUtils.jsonToObject(netBean.getData(), MineBean.class);
				UserConstans.USER_ID = mineBean.getUserId()+"";
				SharedPreferencesUtils.saveUserToken(context, ResponseConstans.SP_NAME,token);
				///api/testToken?userId=&apiType=&clientToken=&serverToken=&token=
//				String suffix_url = "api/testToken?userId="+UserConstans.USER_ID+"&apiType="+LOGIN_TYPE+"&clientToken="+token+"&serverToken="+token+"&platform=1";
//				HTTPTools.newHttpUtilsInstance().doGet(URLConstans.BASE_URL + suffix_url, new HttpRequestCallBack() {
//					@Override
//					public void onLoadingFailure(String err) {
//
//					}
//
//					@Override
//					public void onLoadingSuccess(ResponseInfo<String> result) {
//
//					}
//
//					@Override
//					public void onLoadingStart() {
//
//					}
//
//					@Override
//					public void onLoadingCancelled() {
//
//					}
//
//					@Override
//					public void onLoading(long total, long current) {
//
//					}
//				});
			}else if(loginflag == 0){
				//其他请求
				isLoginValidate = SharedPreferencesUtils.isLoginValidate(context,ResponseConstans.SP_NAME,token);
			}//-1就是退出登录，什么都不做
			if(!isLoginValidate){
				//	不合法
				baseSourceModelCallBack.onModelLoginInvalidate();
//				String suffix_url = "api/testToken?userId="+UserConstans.USER_ID+"&apiType="+OTHER_TYPE+"&clientToken="+SharedPreferencesUtils.getUserToken(context)+"&serverToken="+token+"&platform=1";
//				HTTPTools.newHttpUtilsInstance().doGet(URLConstans.BASE_URL + suffix_url, new HttpRequestCallBack() {
//					@Override
//					public void onLoadingFailure(String err) {
//
//					}
//
//					@Override
//					public void onLoadingSuccess(ResponseInfo<String> result) {
//
//					}
//
//					@Override
//					public void onLoadingStart() {
//
//					}
//
//					@Override
//					public void onLoadingCancelled() {
//
//					}
//
//					@Override
//					public void onLoading(long total, long current) {
//
//					}
//				});
			}else {
				Log.e("shenzhixin","json string:"+jsonObject.toJSONString());
				if (TextUtils.isEmpty(jsonObject.getString("result"))) {
					baseSourceModelCallBack.onLoadingFailure(jsonObject.getString("msg"));
				} else {
					baseSourceModelCallBack.onLoadingSuccess(transformJsonToNetBean(result.result), this);
				}
			}
		}catch(Exception e){//fix bug 185 on bugly
//			baseSourceModelCallBack.onLoadingFailure("服务器出现了问题，稍后再试呢");
			e.printStackTrace();
			//上传异常信息
			uploadException(e,result.result);
		}
    }

    private RequestParams getRequestParams(){
        RequestParams params = new RequestParams();
        List<RequestParams.HeaderItem> list = params.getHeaders();
        if(list!= null && list.size() != 0){
            list.clear();
        }
//        params.addHeader("ppu", UserUtils.getUserPPU(BiddingApplication.getAppInstanceContext()));
//        params.addHeader("userId",UserUtils.getPassportUserId(BiddingApplication.getAppInstanceContext()));

        params.addHeader("ppu", LoginClient.doGetPPUOperate(BiddingApplication.getAppInstanceContext()));
        params.addHeader("userId",LoginClient.doGetUserIDOperate(BiddingApplication.getAppInstanceContext()));
//        params.addHeader("userId","34675169722113");  //bigbang1
//        params.addHeader("userId","34680567140865");  //bigbang2
//        params.addHeader("userId","34680592616449");  //bigbang3
//        params.addHeader("userId","34964986925569");  //bigbang4
//        params.addHeader("userId","35606241334273");  //bigbang5
//        params.addHeader("userId","35606250707713");  //bigbang6
//        params.addHeader("userId","35606332708865");  //bigbang7
        params.addHeader("version", "6");
        params.addHeader("platform", "1");
        params.addHeader("UUID", PhoneUtils.getIMEI(BiddingApplication.getAppInstanceContext()));
        return params;
    }

    private void uploadException(Exception e, String result) {
        String url = com.huangye.commonlib.constans.URLConstans.BASE_URL + "app/exception/parseData?json="+result+"&exception="+e.getMessage()+"&userId="+UserConstans.USER_ID+"&token="+System.currentTimeMillis()+"&platform=1";
        HTTPTools.newHttpUtilsInstance().doGet(url, getRequestParams(), new HttpRequestCallBack() {
            @Override
            public void onLoadingFailure(String err) {

            }

            @Override
            public void onLoadingSuccess(ResponseInfo<String> result) {

            }

            @Override
            public void onLoadingStart() {

            }

            @Override
            public void onLoadingCancelled() {

            }

            @Override
            public void onLoading(long total, long current) {

            }
        });
    }

    /**
     * 先把json转成netBean
     * @param result
     * @return
     */
    public NetBean transformJsonToNetBean(String result){
        NetBean bean = JsonUtils.jsonToNetBean(result);
        return bean;
    }

    @Override
    public void onLoadingStart() {
        baseSourceModelCallBack.onLoadingStart();
    }

    @Override
    public void onLoadingCancelled() {
        baseSourceModelCallBack.onLoadingCancell();
    }

    @Override
    public void onLoading(long total, long current) {}

    @Override
    public void onLoadingFailure(String err) { baseSourceModelCallBack.onLoadingFailure("当前网络慢，请稍后重试"); }

    public enum TAG{ LOGIN,LOADMORE,REFRESH,CHECKVERSION }
}
