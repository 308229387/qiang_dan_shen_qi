package com.huangyezhaobiao.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.huangye.commonlib.delegate.HttpRequestCallBack;
import com.huangye.commonlib.network.HTTPTools;
import com.huangyezhaobiao.bean.HYEventBean.CommonBean;
import com.huangyezhaobiao.bean.HYEventBean.DataBean;
import com.huangyezhaobiao.fragment.QiangDanBaseFragment;
import com.huangyezhaobiao.url.URLConstans;
import com.lidroid.xutils.http.ResponseInfo;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * param：黄页埋点对应的操作以及值
 */
public class HYMob {


    /**
     * 埋点data数据的list
     */
    public static List<DataBean> dataList = Collections.synchronizedList(new ArrayList<DataBean>());

    public static HashMap<String, String> params_map; //日志上传的参数信息
    /**
     * commonBean转换成json
     * @return
     */
    public static String commonBeanToJson(Context context) {
        try {
            CommonBean commonBean = new CommonBean();
            commonBean.setCa(LoggerUtils.getChannelId(context));
            commonBean.setCb(LoggerUtils.getAppVersionName(context));
            commonBean.setCc("android");
            commonBean.setCe(LoggerUtils.getScreenPixels(context));
            commonBean.setCh(LoggerUtils.getPhoneBrand());
            commonBean.setCi(LoggerUtils.getNetworkType(context));
            commonBean.setCk(LoggerUtils.getMacAddress(context));
            commonBean.setCl(LoggerUtils.getPhoneType());
            commonBean.setCm(LoggerUtils.getIMEI(context));
            commonBean.setCn(LoggerUtils.getPhoneNumber(context));
            String json = JSON.toJSONString(commonBean);
            LogUtils.LogV("wjl", json);
            return URLEncoder.encode(GzipUtil.gzip(json),"UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * dataBean转换成json
     * @param object
     * @param properties
     * @return
     */
    public static String dataBeanToJson(Object object,String... properties){
        try {
            SimplePropertyPreFilter filter = new SimplePropertyPreFilter(object.getClass(),properties);
            String json = JSON.toJSONString(object,filter);
            LogUtils.LogV("wjl", json);
            return URLEncoder.encode(GzipUtil.gzip(json),"UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 共有字段DataBean
     * @param context
     * @param co
     * @return
     */
    public static DataBean getBaseDataBean(Context context,String co){
        String userId =UserUtils.getUserId(context);
        String time = String.valueOf(System.currentTimeMillis());
        if(TextUtils.isEmpty(userId)){
            userId ="-";
        }
        if (TextUtils.isEmpty(time)){
            time = "-";
        }
        if (TextUtils.isEmpty(co)){
            co = "-";
        }
        DataBean bean = new DataBean();
        bean.setCo(co);
        bean.setSa(userId);
        bean.setCq(time);
        return bean;
    }


    public static DataBean getBaseDataBeanByModel(Context context,String co){
        int  state = StateUtils.state -1;
        String modelState = String.valueOf(state); //模式状态 服务0、休息1
        String userId =UserUtils.getUserId(context);
        String time = String.valueOf(System.currentTimeMillis());

        if(TextUtils.isEmpty(userId)){
            userId ="-";
        }
        if (TextUtils.isEmpty(time)){
            time = "-";
        }
        if (TextUtils.isEmpty(modelState) ){
            modelState = "-";
        }else if(state < 0 ){
            modelState = "0";
        }
        if (TextUtils.isEmpty(co)){
            co = "-";
        }
        DataBean bean = new DataBean();
        bean.setCo(co);
        bean.setSa(userId);
        bean.setCq(time);
        bean.setModelState(modelState);
        return bean;
    }

    public static DataBean getBaseDataBeanByState(Context context,String co){
        String userId =UserUtils.getUserId(context);
        String time = String.valueOf(System.currentTimeMillis());
        String serviceState = QiangDanBaseFragment.orderState;  //服务状态 待服务1/服务中2/已结束3
        if(TextUtils.isEmpty(userId)){
            userId ="-";
        }
        if (TextUtils.isEmpty(time)){
            time = "-";
        }
        if (TextUtils.isEmpty(serviceState) ){
            serviceState = "-";
        }
        if (TextUtils.isEmpty(co)){
            co = "-";
        }

        DataBean bean = new DataBean();
        bean.setCo(co);
        bean.setSa(userId);
        bean.setCq(time);
        bean.setServiceState(serviceState);
        return bean;
    }

    /**
     *
     * @param context
     * @param co
     * @return
     */
    public static void getDataList(Context context,String co){
        DataBean bean = getBaseDataBean(context, co);
        dataList.add(bean);
        createMap(context, dataBeanToJson(dataList, "co", "sa", "cq"), "0") ; //0表示正常日志，1表示崩溃日志
    }

    /**
     *
     * @param context
     * @param co
     * @param s1
     * @return
     */
    public static void getDataListByQiangDan(Context context,String co,String s1){
        if (TextUtils.isEmpty(s1)){
            s1 = "-";
        }
        DataBean bean = getBaseDataBean(context, co);
        bean.setS1(s1);
        dataList.add(bean);
        //0表示正常日志，1表示崩溃日志
        createMap(context, dataBeanToJson(dataList, "co", "sa", "s1", "cq"), "0");
    }

    /**
     * 登录
     * @param context
     * @param co
     * @param userName
     * @return
     */
    public static void getDataListByLoginSuccess(Context context,String co,String loginState,String userName){
        if(TextUtils.isEmpty(loginState)){
            loginState = "-";
        }
        if(TextUtils.isEmpty(userName)){
            userName = "-";
        }
        DataBean bean = getBaseDataBean(context, co);
        bean.setLoginState(loginState);
        bean.setUserName(userName);
        dataList.add(bean);
        createMap(context, dataBeanToJson(dataList, "co", "sa", "cq", "loginState", "userName"), "0") ; //0表示正常日志，1表示崩溃日志
    }

    /**
     * 登录失败
     * @param context
     * @param co
     * @param loginState
     * @param failureReason
     * @return
     */
    public static void getDataListByLoginError(Context context,String co,String loginState,String failureReason){
        if(TextUtils.isEmpty(loginState)){
            loginState = "-";
        }
        if(TextUtils.isEmpty(failureReason)){
            failureReason = "-";
        }
        DataBean bean = getBaseDataBean(context, co);
        bean.setLoginState(loginState);
        bean.setFailureReason(failureReason);
        dataList.add(bean);
        createMap(context, dataBeanToJson(dataList, "co", "sa", "cq", "loginState", "failureReason"), "0") ; //0表示正常日志，1表示崩溃日志
    }

    /**
     *
     * @param context
     * @param co 埋点类别
     * @return
     */
    public static void getDataListByModel(Context context,String co){
        DataBean bean = getBaseDataBeanByModel(context, co);
        dataList.add(bean);
        createMap(context, dataBeanToJson(dataList, "co", "sa", "modelState", "cq"), "0"); //0表示正常日志，1表示崩溃日志
    }

    /**
     * 列表页、详情页抢单
     * @param context
     * @param co
     * @param s1 标地id
     * @param grabOrderStyle 抢单入口 列表页1、详情页2、弹窗4
     * @return
     */
    public static void getDataListForQiangdan(Context context,String co,String s1,String grabOrderStyle){
        if (TextUtils.isEmpty(s1)){
            s1 = "-";
        }
        DataBean bean = getBaseDataBeanByModel(context, co);
        bean.setS1(s1);
        bean.setGrabOrderStyle(grabOrderStyle);
        dataList.add(bean);
        createMap(context, dataBeanToJson(dataList, "co", "s1", "modelState", "grabOrderStyle", "sa", "cq"), "0"); //0表示正常日志，1表示崩溃日志
    }
    /**
     * 弹窗抢单按钮
     * @param context
     * @param co
     * @param s1
     * @param lockScreenState 锁屏状态----安卓:锁屏0、未锁屏1
     * @param grabOrderStyle  抢单入口 列表页1、详情页2、弹窗4
     * @return
     */
    public static void getDataListByTanChuang(Context context,String co,String s1,String lockScreenState,String grabOrderStyle){
        if (TextUtils.isEmpty(s1)){
            s1 = "-";
        }
        if (TextUtils.isEmpty(lockScreenState)){
            lockScreenState = "-";
        }
        if (TextUtils.isEmpty(grabOrderStyle)){
            grabOrderStyle = "-";
        }
        DataBean bean = getBaseDataBean(context, co);
        bean.setS1(s1);
        bean.setLockScreenState(lockScreenState);
        bean.setGrabOrderStyle(grabOrderStyle);
        dataList.add(bean);
        createMap(context, dataBeanToJson(dataList, "co", "s1", "lockScreenState", "grabOrderStyle", "sa", "cq"), "0") ; //0表示正常日志，1表示崩溃日志
    }

    /**
     *
     * @param context
     * @param co
     * @param s1 标地id
     * @param grabOrderState 抢单状态 已抢0、可抢1
     * @return
     */
    public static void getDataListByState(Context context,String co,String s1,String grabOrderState){
        if (TextUtils.isEmpty(s1)){
            s1 = "-";
        }
        if (TextUtils.isEmpty(grabOrderState)){
            grabOrderState ="-";
        }
        DataBean bean = getBaseDataBean(context, co);
        bean.setS1(s1);
        bean.setGrabOrderState(grabOrderState);
        dataList.add(bean);
        createMap(context, dataBeanToJson(dataList, "co", "s1", "grabOrderState", "sa", "cq"), "0") ; //0表示正常日志，1表示崩溃日志
    }


    /**
     * 订单列表
     * @param context
     * @param co
     * @return
     */
    public static void getDataListByServiceState(Context context,String co){
        DataBean bean = getBaseDataBeanByState(context, co);
        dataList.add(bean);
        createMap(context, dataBeanToJson(dataList, "co", "serviceState", "sa", "cq"), "0") ; //0表示正常日志，1表示崩溃日志
    }

    /**
     * 电话
     * @param context
     * @param co
     * @param orderId  订单id
     * @param callStyle 电话入口 列表0、详情1
     * @return
     */
    public static void getDataListByCall(Context context,String co,String orderId,String callStyle){
        if (TextUtils.isEmpty(orderId)){
            orderId = "-";
        }
        if (TextUtils.isEmpty(callStyle)){
            callStyle = "-";
        }
        DataBean bean = getBaseDataBeanByState(context, co);
        bean.setOrderId(orderId);
        bean.setCallStyle(callStyle);
        dataList.add(bean);
        createMap(context, dataBeanToJson(dataList, "co", "callStyle", "orderId", "serviceSate", "sa", "cq"), "0") ; //0表示正常日志，1表示崩溃日志
    }
    /**
     * 退单
     * @param context
     * @param co
     * @param orderId
     * @return
     */
    public static void getDataListByRefund(Context context,String co,String orderId){
        if (TextUtils.isEmpty(orderId)){
            orderId = "-";
        }
        DataBean bean = getBaseDataBean(context, co);
        bean.setOrderId(orderId);
        dataList.add(bean);
        createMap(context, dataBeanToJson(dataList, "co", "orderId", "sa", "cq"), "0") ; //0表示正常日志，1表示崩溃日志
    }

    /**
     * 页面停留 共有字段DataBean
     * @param context
     * @param cr
     * @param cs
     * @return
     */
    public static void getBaseDataListForPage(Context context,String cr,long cs){
        if(cs <=0)cs =0;
        String userId =UserUtils.getUserId(context);
        String time = String.valueOf(cs);
        if(TextUtils.isEmpty(userId)){
            userId ="-";
        }
        if (TextUtils.isEmpty(cr)){
            cr = "-";
        }

        if (TextUtils.isEmpty(time)){
            time = "-";
        }

        DataBean bean = new DataBean();
        bean.setCr(cr);
        bean.setSa(userId);
        bean.setCs(time);
        dataList.add(bean);
        createMap(context, dataBeanToJson(dataList, "cr", "sa", "cs"), "0"); //0表示正常日志，1表示崩溃日志
    }

    /**
     * 生成字符串
     * @param context
     * @param data
     * @param t
     * @return
     */
    public static String createLogger(Context context,String data,String t){

        StringBuilder builder = new StringBuilder();
        builder.append("common=").append(commonBeanToJson(context));
        builder.append("&data=").append(data);
        builder.append("&t=").append(t);
        return builder.toString();
    }

    /**
     * 生成url参数map
     * @param context
     * @param data
     * @param t
     * @return
     */
    public static HashMap createMap(Context context,String data,String t){
        int num = UserUtils.getMobItem(context);
        if( num > 0){
            LogUtils.LogV("Upload", "sp upload");
            uploadMob(context,UserUtils.getMobCommon(context),UserUtils.getMobData(context),0,"sp");
        }

        if(dataList.size() > 5){
            LogUtils.LogV("Upload", "uploadeing");
            uploadMob(context,params_map.get("common"),params_map.get("data"),0,"");
        }

        params_map = new HashMap<String, String>();
        params_map.put("common",commonBeanToJson(context));
        params_map.put("data", data);
        params_map.put("t", t);
        LogUtils.LogV("wjl", createLogger(context, data, "0"));
        return params_map;
    }

    // 这里只能定义成static
    private synchronized static void uploadMob(final Context context,final String common,final String data,int t,final String from){
        if(NetUtils.isNetworkConnected(context)){
            HTTPTools.newHttpUtilsInstance().doGet(URLConstans.UPLOAD_URL +"?common=" + common + "&data=" + data +"&t=" + t, null, new HttpRequestCallBack() {
                @Override
                public void onLoadingFailure(String err) {
                    UserUtils.setMobItem(context,dataList.size());
                    UserUtils.setMobCommon(context,common);
                    UserUtils.setMobData(context,data);
                }

                @Override
                public void onLoadingSuccess(ResponseInfo<String> result) {
                    Log.v("Upload",result.result);
                    try {
                        JSONObject jsonResult = JSON.parseObject(result.result);
                        if (jsonResult.containsKey("status") && "0".equals(jsonResult.getString("status"))) {
                            if("sp".equals(from)){
                                LogUtils.LogV("Upload", "sp上传成功");
                                UserUtils.clearMob(context);
                            } else {
                                LogUtils.LogV("Upload", "上传成功");
                                dataList.clear();
                            }
                        }
                    } catch (Exception e) {
                        UserUtils.setMobItem(context,dataList.size());
                        UserUtils.setMobCommon(context,common);
                        UserUtils.setMobData(context,data);
                        e.printStackTrace();
                    }
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
        } else {
            UserUtils.setMobItem(context,dataList.size());
            UserUtils.setMobCommon(context,common);
            UserUtils.setMobData(context,data);
        }
    }
}
