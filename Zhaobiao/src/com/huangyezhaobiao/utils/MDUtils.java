package com.huangyezhaobiao.utils;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;

import com.huangyezhaobiao.application.BiddingApplication;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 埋点协议的实现工具
 * @author shenzhixin
 *
 */
public class MDUtils {
	/**
	 * 刷新余额:action为1
	 * 退出登录:action为2
	 */
	public static void myUserCenterMD(Context context,String action){
		/*HashMap<String, String> map = new HashMap<String, String>();
		map.put("userID", UserUtils.getUserId(context));
		map.put("pageType", "userCenter");
		map.put("action", action);
		map.put("time", getCurrentTime());
		writeCommonMDData(map);
		String json = mapToJson(map);
		writeJsonString(json);
		LogUtils.LogE("ashenUpload", "json:"+json);*/
	}
	
	/**
	 * 
	 * @param context
	 * @param cateId   类别id(如果多个类别怎么办)
	 * @param bidId    标的id
	 * @param action   执行了什么动作
	 */
	public static void servicePageMD(Context context,String cateId,String bidId,String action){
		/*Map<String, String> map = new HashMap<String, String>();
		map.put("userID", UserUtils.getUserId(context));
		String pageType = StateUtils.state==1?"serviceList":"restList";
		map.put("pageType", pageType);
		map.put("cateID", cateId);
		map.put("bidID", bidId);
		map.put("action",action);
		map.put("time", getCurrentTime());
		writeCommonMDData(map);
		String json = mapToJson(map);
		writeJsonString(json);
		LogUtils.LogE("ashenUpload", "json:"+json);*/
		
	}
	
	/**
	 * 推送弹窗界面的埋点
	 * @param context
	 * @param cateId 类别
	 * @param bidId  标的id
	 * @param action 行为
	 */
	public static void pushWindowPageMD(Context context,String cateId,String bidId,String action){
		/*Map<String, String> map = new HashMap<String, String>();
		map.put("userID", UserUtils.getUserId(context));
		map.put("pageType", "popView");
		map.put("cateID", cateId);
		map.put("bidID", bidId);
		map.put("action", action);
		map.put("time", getCurrentTime());
		writeCommonMDData(map);
		String json = mapToJson(map);
		writeJsonString(json);
		LogUtils.LogE("ashenUpload", "json:"+json);*/
	}
	
	
	/**
	 * 标的详情界面的埋点
	 */
	public static void bidDetailsPageMD(Context context,String bidState,String cateId,String bidId,String action){
		/*Map<String, String> map = new HashMap<String, String>();
		map.put("userID", UserUtils.getUserId(context));
		map.put("pageType", "bidDetail");
		map.put("bidState", bidState);
		map.put("cateID", cateId);
		map.put("bidID", bidId);
		map.put("action", action);
		map.put("time", getCurrentTime());
		writeCommonMDData(map);
		String json = mapToJson(map);
		writeJsonString(json);
		LogUtils.LogE("ashenUpload", "json:"+json);*/
	}
	
	/**
	 * 消息bar的title
	 */
	public static void messageBarPageMD(String messageType,String cateId,String bidId){
		/*Map<String, String> map = new HashMap<String, String>();
		map.put("userID", UserUtils.userId);
		map.put("pageType", "messageBar");
		map.put("messageType", messageType);
		map.put("cateID", cateId);
		map.put("bidID", bidId);
		map.put("action", "1");
		map.put("time", getCurrentTime());
		writeCommonMDData(map);
		String json = mapToJson(map);
		writeJsonString(json);
		LogUtils.LogE("ashenUpload", "json:"+json);*/
	}
	
	
	/**
	 * 抢单结果页面的md
	 */
	public static void QDResultPageMD(String pageType,String messageType,String cateId,String bidId,String action){
	/*	Map<String, String> map = new HashMap<String, String>();
		map.put("userID", UserUtils.userId);
		map.put("pageType", pageType);
		map.put("messageType",messageType);
		map.put("cateID",cateId);
		map.put("bidID",bidId);
		map.put("action", action);
		map.put("time",getCurrentTime());
		writeCommonMDData(map);
		String json = mapToJson(map);
		writeJsonString(json);
		LogUtils.LogE("ashenUpload", "json:"+json);*/
	}
	
	
	/**
	 * 订单列表页的埋点
	 */
	public static void OrderListPageMD(String orderState,String cateId,String orderId,String action){
		/*Map<String, String> map = new HashMap<String, String>();
		map.put("userID", UserUtils.userId);
		map.put("pageType", "oderList");
		map.put("orderState", orderState);
		map.put("cateID", cateId);
		map.put("orderID", orderId);
		map.put("action", action);
		map.put("time", getCurrentTime());
		writeCommonMDData(map);
		String json = mapToJson(map);
		writeJsonString(json);
		LogUtils.LogE("ashenUpload", "json:"+json);*/
	}
	
	
	/**
	 * 订单详情页的埋点
	 */
	public static void OrderDetailsPageMD(String orderState,String cateId,String orderId,String action,String tel){
		/*Map<String, String> map = new HashMap<String, String>();
		map.put("userID",UserUtils.userId);
		map.put("pageType", "oderDetails");
		map.put("sourceType", "oderList");
		map.put("orderState", orderState);
		map.put("cateID", cateId);
		map.put("orderID", orderId);
		map.put("action", action);
		map.put("tel", tel);
		map.put("time", getCurrentTime());
		writeCommonMDData(map);
		String json = mapToJson(map);
		writeJsonString(json);
		LogUtils.LogE("ashenUpload", "json:"+json);*/
	}
	
	
	/**
	 * 余额不足的埋点
	 */
	public static void YuENotEnough(String cateId,String orderId){
		/*Map<String, String> map = new HashMap<String, String>();
		map.put("userID", UserUtils.userId);
		map.put("pageType", "yuePopView");
		map.put("cateID", cateId);
		map.put("orderID", orderId);
		map.put("time", getCurrentTime());
		writeCommonMDData(map);
		String json = mapToJson(map);
		writeJsonString(json);
		LogUtils.LogE("ashenUpload", "json:"+json);*/
	}
	
	
	/**
	 * 抢单结果页面的埋点
	 */
	public static void QDResult(String pageType,String cateId,String bidId,String action){
		/*Map<String, String> map = new HashMap<String, String>();
		map.put("userID", UserUtils.userId);
		map.put("pageType", pageType);
		map.put("cateID", cateId);
		map.put("bidID", bidId);
		map.put("action", action);
		map.put("time", getCurrentTime());
		writeCommonMDData(map);
		String json = mapToJson(map);
		writeJsonString(json);
		LogUtils.LogE("ashenUpload", "json:"+json);*/
	}
	
	private static void writeCommonMDData(Map<String, String> map){
		/*if(map != null){
			map.put("platform", "2");//
			map.put("_time",getTimeOffcial());
			LogUtils.LogE("asasas", "time:"+String.valueOf(new Date().getTime()));
		}*/
	}
	
	
	/**
	 * 得到格式对应的string
	 * @return
	 */
	private static String getTimeOffcial(){
		/*long now = System.currentTimeMillis();
		
		StringBuffer logTimeBuffer = new StringBuffer(
				String.valueOf(now));
		if (logTimeBuffer.length() == 13) {
			logTimeBuffer.insert(10, ".");
	}*/
		return "";

	}
	  /**
     * 
    * map转换json.
    * <br>详细说明
    * @param map 集合
    * @return
    * @return String json字符串
    * @throws
    * @author slj
     */
    public static String mapToJson(Map<String, String> map) {
       /* Set<String> keys = map.keySet();
        String key = "";
        String value = "";
        StringBuffer jsonBuffer = new StringBuffer();
        jsonBuffer.append("{");
        for (Iterator<String> it = keys.iterator(); it.hasNext();) {
            key = (String) it.next();
            value = map.get(key);
            jsonBuffer.append("\""+key+"\"" + ":" +"\""+ value+"\"");
            if (it.hasNext()) {
                jsonBuffer.append(",");
            }
        }
        jsonBuffer.append("}");*/
        return "";
    }
    
    
    public static String getCurrentTime(){
    /*	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
    //	System.out.println(df.format(new Date()));// new Date()为获取当前系统时间
    	return df.format(new Date());*/
		return "";
    }
    
    
    public static void writeJsonString(String json){
    	/*Bundle data = new Bundle();
    	data.putString("json", json);
    	Message msg =   BiddingApplication.getLogHandler().obtainMessage();
    	msg.setData(data);
    	BiddingApplication.getLogHandler().sendMessage(msg);*/
    }
}
