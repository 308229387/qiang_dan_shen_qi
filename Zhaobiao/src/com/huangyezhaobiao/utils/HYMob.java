package com.huangyezhaobiao.utils;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.huangyezhaobiao.bean.HYEventBean.CommonBean;
import com.huangyezhaobiao.bean.HYEventBean.DataBean;
import com.huangyezhaobiao.fragment.QiangDanBaseFragment;

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
    public static List<DataBean> dataList= Collections.synchronizedList(new ArrayList<DataBean>());

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

    /**
     *
     * @param context
     * @param co
     * @return
     */
    public static List<DataBean> getDataList(Context context,String co){
        dataList.add(getBaseDataBean(context, co));
        return dataList;
    }

    /**
     *
     * @param context
     * @param co
     * @param sl
     * @return
     */
    public static List<DataBean> getDataListByQiangDan(Context context,String co,String sl){
        if (TextUtils.isEmpty(sl)){
            sl = "-";
        }
        DataBean bean = getBaseDataBean(context, co);
        bean.setSl(sl);
        dataList.add(bean);
        return dataList;
    }

    /**
     * 登录
     * @param context
     * @param co
     * @param userName
     * @return
     */
    public static List<DataBean> getDataListByLogin(Context context,String co,String userName){
        if(TextUtils.isEmpty(userName)){
            userName = "-";
        }
        DataBean bean = getBaseDataBean(context, co);
        bean.setUserName(userName);
        dataList.add(bean);
        return dataList;
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


    /**
     *
     * @param context
     * @param co 埋点类别
     * @return
     */
    public static List<DataBean> getDataListByModel(Context context,String co){
        dataList.add(getBaseDataBeanByModel(context, co));
        return dataList;
    }

    /**
     * 列表页、详情页抢单
     * @param context
     * @param co
     * @param sl  标地id
     * @param grabOrderStyle 抢单入口 列表页1、详情页2、弹窗4
     * @return
     */
    public static List<DataBean> getDataList(Context context,String co,String sl,String grabOrderStyle){
        if (TextUtils.isEmpty(sl)){
            sl = "-";
        }
        DataBean bean = getBaseDataBeanByModel(context, co);
        bean.setSl(sl);
        bean.setGrabOrderStyle(grabOrderStyle);
        dataList.add(bean);
        return dataList;
    }

    /**
     * 弹窗抢单按钮
     * @param context
     * @param co
     * @param sl
     * @param lockScreenState 锁屏状态----安卓:锁屏0、未锁屏1
     * @param grabOrderStyle  抢单入口 列表页1、详情页2、弹窗4
     * @return
     */
    public static List<DataBean> getDataListByTanChuang(Context context,String co,String sl,String lockScreenState,String grabOrderStyle){
        if (TextUtils.isEmpty(sl)){
            sl = "-";
        }
        if (TextUtils.isEmpty(lockScreenState)){
            lockScreenState = "-";
        }
        if (TextUtils.isEmpty(grabOrderStyle)){
            grabOrderStyle = "-";
        }
        DataBean bean = getBaseDataBean(context, co);
        bean.setSl(sl);
        bean.setLockScreenState(lockScreenState);
        bean.setGrabOrderStyle(grabOrderStyle);
        dataList.add(bean);
        return dataList;
    }

    /**
     *
     * @param context
     * @param co
     * @param sl 标地id
     * @param grabOrderState 抢单状态 已抢0、可抢1
     * @return
     */
    public static List<DataBean> getDataListByState(Context context,String co,String sl,String grabOrderState){
        if (TextUtils.isEmpty(sl)){
            sl = "-";
        }
        if (TextUtils.isEmpty(grabOrderState)){
            grabOrderState ="-";
        }
        DataBean bean = getBaseDataBean(context, co);
        bean.setSl(sl);
        bean.setGrabOrderState(grabOrderState);
        dataList.add(bean);
        return dataList;
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
    public static List<DataBean> getDataListByServiceState(Context context,String co){
        DataBean bean = getBaseDataBeanByState(context, co);
        dataList.add(bean);
        return dataList;
    }

    /**
     * 电话
     * @param context
     * @param co
     * @param orderId  订单id
     * @param callStyle 电话入口 列表0、详情1
     * @return
     */
    public static List<DataBean> getDataListByCall(Context context,String co,String orderId,String callStyle){
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
        return dataList;
    }

    /**
     * 退单
     * @param context
     * @param co
     * @param orderId
     * @return
     */
    public static List<DataBean> getDataListByRefund(Context context,String co,String orderId){
        if (TextUtils.isEmpty(orderId)){
            orderId = "-";
        }
        DataBean bean = getBaseDataBean(context, co);
        bean.setOrderId(orderId);
        dataList.add(bean);
        return dataList;
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
        builder.append("common=").append(HYMob.commonBeanToJson(context));
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

        params_map = new HashMap<String, String>();
        params_map.put("common",commonBeanToJson(context));
        params_map.put("data", data);
        params_map.put("t", t);
        LogUtils.LogV("wjl", HYMob.createLogger(context, data, "0"));
        return params_map;
    }


}
