package com.huangyezhaobiao.utils;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.huangyezhaobiao.application.BiddingApplication;
import com.huangyezhaobiao.bean.HYEventBean.CommonBean;
import com.huangyezhaobiao.bean.HYEventBean.DataBean;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * param：黄页埋点对应的操作以及值
 */
public class HYMob {

    public static List<DataBean> list = new ArrayList<>();
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

//    private String dataBeanToJson(Object object,String value1,String value2){
//        SimplePropertyPreFilter filter = new SimplePropertyPreFilter(object.getClass(),value1,value2);
//        return JSON.toJSONString(object,filter);
//    }
//
//    private String dataBeanToJson(Object object,String value1,String value2,String value3){
//
//        SimplePropertyPreFilter filter = new SimplePropertyPreFilter(object.getClass(),value1,value2,value3);
//        return JSON.toJSONString(object,filter);
//    }

    public static DataBean getBaseDataBean(Context context){
        String userId =UserUtils.getUserId(context);
        String time = String.valueOf(System.currentTimeMillis());
        DataBean bean = new DataBean();
        bean.setSa(userId);
        bean.setCq(time);
        return bean;
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
        return params_map;
    }


}
