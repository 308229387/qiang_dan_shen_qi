package com.huangyezhaobiao.utils;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.huangye.commonlib.file.SqlUtils;
import com.huangye.commonlib.utils.JsonUtils;
import com.huangyezhaobiao.R;
import com.huangyezhaobiao.activity.OrderDetailActivity;
import com.huangyezhaobiao.activity.OtherDetailActivity;
import com.huangyezhaobiao.bean.push.PushBean;
import com.huangyezhaobiao.bean.push.PushToStorageBean;
import com.huangyezhaobiao.bean.push.bar.CountdownPushBean;
import com.huangyezhaobiao.bean.push.bar.SystemPushBean;
import com.huangyezhaobiao.bean.push.pop.AffiliatePopBean;
import com.huangyezhaobiao.bean.push.pop.CleaningPopBean;
import com.huangyezhaobiao.bean.push.pop.DomesticRegisterPopBean;
import com.huangyezhaobiao.bean.push.pop.NannyPopBean;
import com.huangyezhaobiao.bean.push.pop.PersonalRegisterPopBean;
import com.huangyezhaobiao.bean.push.pop.PopBaseBean;
import com.huangyezhaobiao.bean.push.pop.RenovationPopBean;
import com.huangyezhaobiao.db.DataBaseManager.TABLE_OTHER;
import com.huangyezhaobiao.enums.PopTypeEnum;
import com.lidroid.xutils.exception.DbException;
import com.xiaomi.mipush.sdk.MiPushMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Push json信息转化成 Bean 的工具类
public class PushUtils {

	public static List<PopBaseBean> pushList = new ArrayList<PopBaseBean>();

	public static Map<String, Class<? extends PopBaseBean>> popMap = new HashMap<String, Class<? extends PopBaseBean>>();

	static {
		popMap.put("1", RenovationPopBean.class);
		popMap.put("2", PersonalRegisterPopBean.class);
		popMap.put("3", DomesticRegisterPopBean.class);
		//2015.8.17 add
		popMap.put("4", AffiliatePopBean.class);
		//2015.12.7 add
		popMap.put("5", NannyPopBean.class);
		popMap.put("6", CleaningPopBean.class);
	}

	private static JSONObject obj;
	private static JSONObject extMap;
	private static JSONObject detail;
	private static JSONObject info;
	
	private static PushBean pushBean;



	private static void init(String content){
		try {
			if(content != null){
				obj = JSON.parseObject(content);
			}

			if(obj.getString("extMap") != null){
				extMap = JSON.parseObject(obj.getString("extMap"));
			}

			if(extMap.getString("detail") != null){
				detail = JSON.parseObject(extMap.getString("detail"));
			}

			if (detail.getString("info") != null){
				info = JSON.parseObject(detail.getString("info"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	public static void initJson(MiPushMessage message) {

		if(message != null){
			Log.e("miui","content:"+message.getContent());
			init(message.getContent());
		}
//		try {
//			Log.e("miui","content:"+message.getContent());
//			obj = JSON.parseObject(message.getContent());
//			extMap = JSON.parseObject(obj.getString("extMap"));
//
//			detail = JSON.parseObject(extMap.getString("detail"));
//			info = JSON.parseObject(detail.getString("info"));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}


	public static void initGPushJson(String gePushJson){

		if(gePushJson != null){
			Log.e("miui","content:"+gePushJson);
			init(gePushJson);
		}
//		try {
//			Log.e("miui","content:"+gePushJson);
//			obj = JSON.parseObject(gePushJson);
//			extMap = JSON.parseObject(obj.getString("extMap"));
//
//			detail = JSON.parseObject(extMap.getString("detail"));
//			info = JSON.parseObject(detail.getString("info"));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

	public static PushBean dealGePushMessage(Context context,String gePushContent){
		initGPushJson(gePushContent);
		return getPushBean(context);
	}

	/**
	 * 根据现在的协议传递把MiPushMessage 根据类别 转换成我们要的数据
	 * 
	 * @param message
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static PushBean dealPushMessage(Context context, MiPushMessage message) {
		initJson(message);
		return getPushBean(context);

	}

	@Nullable
	private static PushBean getPushBean(Context context) {
		PushBean bean = null;

		// 这里拿到Push的基本信息 类型和推送时间
		int type = 0;
		String pushTime = null;
		long pushId = 0;
		int pushTurn = 0;

		try {

			if (extMap.getInteger("type") != null) {
                type = extMap.getInteger("type");//推送类型，新订单/推送结果...
                Log.e("GetuiSdkDemo", "type:" + type);
            }
			if (extMap.getString("pushTime") != null){
                pushTime = extMap.getString("pushTime");//推送时间
             }
			if( extMap.getLong("id") != null){
                pushId = extMap.getLong("id");//推送id
            }
			if("0".equals(info.getString("displayType"))){
                return null;
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 100:新标的 101:抢单结果,成功or失败在detail中体现 102:倒计时提醒 103:系统消息
		switch (PopTypeEnum.getPopType(type)) {

		case NEW_ORDER:
			try {
				Log.v("PushUtils","接收到New_Order" + detail);
				if(detail.getInteger("pushTurn") != null) {
					pushTurn = detail.getInteger("pushTurn");//推送轮次
				}

				if( detail.getString("voice")!= null) {
					String voice = detail.getString("voice");
					info.put("voice", voice);//声音播报内容
					Log.v("PushUtils", "接收到New_Order" + voice);
				}

				if(info.getString("displayType")!= null && popMap.containsKey(info.getString("displayType"))) {
					Class clazz0 = popMap.get(info.getString("displayType"));//PopBaseBean
					PopBaseBean popBean0 = (PopBaseBean) JsonUtils.jsonToObject(info.toString(), clazz0);
					pushList.add(popBean0);//加到队列中，弹窗啊啥的用
					//	}
					bean = popBean0;
					Log.v("PushUtils", "接收到New_Order" + bean.pushId);
				}

				UnreadUtils.saveNewOrder(context);

			} catch (Exception e) {
				Log.v("PushUtils","接收到New_Order异常");
			}

			break;
		case ORDERRESULT:
			String status = detail.getString("status");
			info.put("status", status);//结果时就需要status
			Class clazz1 = popMap.get(info.getString("displayType"));
			PopBaseBean popBean1 = (PopBaseBean) JsonUtils.jsonToObject(info.toString(), clazz1);
			bean = popBean1;
			UnreadUtils.saveQDResult(context);
			break;

		case COUNTDOWN:
			String deadLine = detail.getString("deadLine");
			info.put("deadLine", deadLine);

			CountdownPushBean countdownPushBean = JsonUtils.jsonToObject(info.toString(), CountdownPushBean.class);
			bean = countdownPushBean;
			UnreadUtils.saveDaoJiShi(context);
			break;
		case SYSTEMMESSAGE:
			SystemPushBean systemPushBean = JsonUtils.jsonToObject(info.toString(), SystemPushBean.class);
			bean = systemPushBean;
			UnreadUtils.saveSysNoti(context);
			break;
		default:
			break;
		}
		// 写入extMap级信息
		if(bean==null){
			return bean;
		}
		bean.setTag(type);//titleBar需要
		bean.setPushTime(pushTime);
		bean.setPushId(pushId);
		bean.setPushTurn(pushTurn);

		pushBean = bean;
		if(BidUtils.bidLists.contains(bean.getPushId())){
			return null;
		}
		showNotification(bean);
		PushToStorageBean pushToStorageBean = bean.toPushStorageBean();
		pushToStorageBean.setTag(type);
		int tag = pushToStorageBean.getTag();
		try {
			SqlUtils.getInstance(context);
			if(tag!=100) {
				SqlUtils.dbUtils.save(pushToStorageBean);
			}

		} catch (DbException e) {
			e.printStackTrace();
		}
		return bean;
	}

	/**
	 * 设置推送
	 * @param bean
	 */
	private static void showNotification(PushBean bean) {
		Notification notification = new Notification(R.drawable.ic_launcher,"您有一条新消息",1);
	}

	public static void notify(Context context, MiPushMessage message) {

		int type = 0;
		try {
			type = Integer.parseInt(extMap.getString("type"));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		switch (PopTypeEnum.getPopType(type)) {

		case NEW_ORDER:
			UnreadUtils.clearNewOder(context);
			Intent intent = new Intent(context, OrderDetailActivity.class);
			// 程序在后台运行的时候必须要加的标志，新建activity队列
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
			intent.setClass(context, OrderDetailActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("passBean", pushBean.toPushPassBean());
			intent.putExtras(bundle);
			context.startActivity(intent);
			break;
		case ORDERRESULT:
			UnreadUtils.clearQDResult(context);
			Intent intent1 = new Intent(context, OtherDetailActivity.class);
			// 程序在后台运行的时候必须要加的标志，新建activity队列
			intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);

			intent1.putExtra("type", TABLE_OTHER.KOUFEI);
			// intent1.putExtra("bidId", info.getString("bidId"));
			context.startActivity(intent1);
			break;
		case COUNTDOWN:

			UnreadUtils.clearDaoJiShiResult(context);
			Intent intent2 = new Intent(context, OtherDetailActivity.class);
			// 程序在后台运行的时候必须要加的标志，新建activity队列
			intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
			intent2.putExtra("type", TABLE_OTHER.DAOJISHI);
			context.startActivity(intent2);
			break;
		case SYSTEMMESSAGE:
			UnreadUtils.clearSysNotiNum(context);
			Intent intent3 = new Intent(context, OtherDetailActivity.class);
			// 程序在后台运行的时候必须要加的标志，新建activity队列
			intent3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
			intent3.putExtra("type", TABLE_OTHER.SYSNOTIF);
			context.startActivity(intent3);
			break;
		default:
			break;
		}

	}

}
