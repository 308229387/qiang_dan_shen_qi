package com.huangyezhaobiao.view;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.activity.OrderDetailActivity;
import com.huangyezhaobiao.activity.OtherDetailActivity;
import com.huangyezhaobiao.activity.SystemNotiListActivity;
import com.huangyezhaobiao.bean.push.PushBean;
import com.huangyezhaobiao.bean.push.PushToPassBean;
import com.huangyezhaobiao.db.DataBaseManager.TABLE_OTHER;
import com.huangyezhaobiao.enums.PopTypeEnum;
import com.huangyezhaobiao.enums.TitleBarType;
import com.huangyezhaobiao.inter.MDConstans;
import com.huangyezhaobiao.utils.ActivityUtils;
import com.huangyezhaobiao.utils.BDEventConstans;
import com.huangyezhaobiao.utils.BDMob;
import com.huangyezhaobiao.utils.HYEventConstans;
import com.huangyezhaobiao.utils.HYMob;
import com.huangyezhaobiao.utils.LogUtils;
import com.huangyezhaobiao.utils.MDUtils;
import com.huangyezhaobiao.utils.UnreadUtils;

import java.util.HashMap;
import java.util.Map;


public class TitleMessageBarLayout extends RelativeLayout{
	private boolean isDefaultHandle = true;//是否是交给控件自己处理
	private ImageView      iv_title_left_new_order;
	private ImageView      iv_title_left_yue;
	private ImageView      iv_title_right_new_array;
	private RelativeLayout rl_right_title_close;
	private TextView       tv_title_message;
	private View           title_root;
	private TitleBarType   type; 
	private String message;
	private OnTitleBarClickListener titleBarListener;
	private PushBean       bean;
	private long orderId;
	private int  tag;
	private PushToPassBean pushPassBean;
	public void setPushBean(PushBean bean){
		this.bean = bean;
		message = bean.toPushStorageBean().getStr();
		orderId = bean.toPushStorageBean().getOrderid();
		tag = bean.getTag();
		pushPassBean = bean.toPushPassBean();
		//TODO:根据bean来进行type的判断
		configTag(tag);
		Log.e("shenzhixinUUU", "message:" + message + ",tag:" + tag + ",orderId:" + orderId);
	}
	
	private void configTag(int tag) {
		switch (PopTypeEnum.getPopType(tag)) {
		case NEW_ORDER:
			type = TitleBarType.NEW_ORDER;
			break;
		case ORDERRESULT:
			int status = bean.toPushStorageBean().getStatus();
			LogUtils.LogE("ashenashen", "status:" + status);
			type = status==1?TitleBarType.QDRESULT_SUCCESS:TitleBarType.QDRESULT_FAILURE;
			break;
		case COUNTDOWN:
			type = TitleBarType.DAOJISHI;
			break;
		case SYSTEMMESSAGE:
			type = TitleBarType.SYS_NOTI;
			break;

		}
		operateViewwithType();
	}

	public void setDefaultHandle(boolean isDefaultHandle){
		this.isDefaultHandle = isDefaultHandle;
	}
	
	public void setTitleBarListener(OnTitleBarClickListener titleBarListener) {
		this.titleBarListener = titleBarListener;
		
	}

	public TitleMessageBarLayout(Context context, AttributeSet attrs) {
		this(context, attrs,-1);
	}

	public TitleMessageBarLayout(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView();
	}

	public TitleMessageBarLayout(Context context) {
		this(context,null);
	}

	
	
	public TitleBarType getType() {
		return type;
	}

	public void setType(TitleBarType type) {
		LogUtils.LogE("ashenPush", "setType");
		this.type = type;
		operateViewwithType();
	}
	
	public void setTitleMessage(String message){
		this.message = message;
		tv_title_message.setText(message);
	}

	/**
	 * 显示网络的错误
	 */
	public void showNetError(){
		type = TitleBarType.NETWORK_ERROR;
		operateViewwithType();
	}
	
	
	private void operateViewwithType() {
		LogUtils.LogE("ashenPush", "operateType");
		openTitle();
		switch (type) {
		case YUE://显示的是余额，第一版应该不做
			break;
		case DAOJISHI://倒计时
			showDaoJiShi();
			break;
		case QDRESULT_SUCCESS://抢单成功
			showResultSuccess();
			break;
		case QDRESULT_FAILURE://抢单失败
			showResultFailure();
			break;
		case NEW_ORDER://新订单
			showNewOrder();
			break;
		case NETWORK_ERROR://网络异常
			showNetworkError();
			break;
		case SYS_NOTI://系统通知
			showSysNoti();
			break;

		}
	}

	private void showSysNoti() {
		iv_title_left_yue.setVisibility(View.INVISIBLE);
		iv_title_left_new_order.setVisibility(View.VISIBLE);
		rl_right_title_close.setVisibility(View.VISIBLE);
		iv_title_right_new_array.setVisibility(View.INVISIBLE);
		tv_title_message.setText(message);
		MDUtils.messageBarPageMD(MDConstans.SYS_NOTI,bean.toPushPassBean().getCateId()+"",bean.toPushPassBean().getBidId()+"");
	}

	private void openTitle() {
		LogUtils.LogE("ashenPush", "openTitle");
	}

	/**
	 * 显示网络异常
	 */
	private void showNetworkError() {
		iv_title_left_yue.setVisibility(View.INVISIBLE);
		iv_title_left_new_order.setVisibility(View.VISIBLE);
		rl_right_title_close.setVisibility(View.INVISIBLE);
		iv_title_right_new_array.setVisibility(View.VISIBLE);
		tv_title_message.setText("网络异常，请检查您的网络设置");
	}

	/**
	 * 显示新订单
	 */
	private void showNewOrder() {
		iv_title_left_yue.setVisibility(View.INVISIBLE);
		iv_title_left_new_order.setVisibility(View.VISIBLE);
		rl_right_title_close.setVisibility(View.INVISIBLE);
		iv_title_right_new_array.setVisibility(View.VISIBLE);
		tv_title_message.setText("新订单!"+message);
	}

	/**
	 * 抢单失败的提示
	 */
	private void showResultFailure() {
		iv_title_left_new_order.setVisibility(View.INVISIBLE);
		iv_title_left_yue.setVisibility(View.VISIBLE);
		rl_right_title_close.setVisibility(View.VISIBLE);
		iv_title_right_new_array.setVisibility(View.INVISIBLE);
		tv_title_message.setText("抢单失败!"+message);
		MDUtils.messageBarPageMD(MDConstans.RESULT,bean.toPushPassBean().getCateId()+"",bean.toPushPassBean().getBidId()+"");
	}

	/**
	 * 抢单成功的提示
	 */
	private void showResultSuccess() {
		iv_title_left_new_order.setVisibility(View.INVISIBLE);
		iv_title_left_yue.setVisibility(View.VISIBLE);
		rl_right_title_close.setVisibility(View.VISIBLE);
		iv_title_right_new_array.setVisibility(View.INVISIBLE);
		tv_title_message.setText("抢单成功!"+message);
		MDUtils.messageBarPageMD(MDConstans.RESULT,bean.toPushPassBean().getCateId()+"",bean.toPushPassBean().getBidId()+"");
	}

	/**
	 * 当倒计时出来时的显示方式
	 */
	private void showDaoJiShi() {
		LogUtils.LogE("ashenPush", "125...");
		iv_title_left_new_order.setVisibility(View.INVISIBLE);
		iv_title_left_yue.setVisibility(View.VISIBLE);
		rl_right_title_close.setVisibility(View.VISIBLE);
		iv_title_right_new_array.setVisibility(View.INVISIBLE);
		tv_title_message.setText("您有一笔抢单尚未联系客户，请三小时内联系客户");
		MDUtils.messageBarPageMD(MDConstans.DAOJISHI,bean.toPushPassBean().getCateId()+"",bean.toPushPassBean().getBidId()+"");
	}

	/**
	 * 初始化这个view控件
	 */
	private void initView() {
		View.inflate(getContext(), R.layout.view_title_bar, this);
		iv_title_left_new_order  = (ImageView) findViewById(R.id.iv_title_left_new_order);
		iv_title_left_yue        = (ImageView) findViewById(R.id.iv_title_left_yue);
		iv_title_right_new_array = (ImageView) findViewById(R.id.iv_title_right_new_array);
		rl_right_title_close     = (RelativeLayout) findViewById(R.id.rl_right_title_close);
		tv_title_message         = (TextView) findViewById(R.id.tv_title_message);
		title_root               = findViewById(R.id.title_root);
		title_root.setOnClickListener(listener);
		rl_right_title_close.setOnClickListener(listener);
		closeTitle();
	}
	

	
	private OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.rl_right_title_close:
				if(titleBarListener!=null){
					titleBarListener.onTitleBarClosedClicked();
				}
				break;
			case R.id.title_root://根部信息
				//点击了消息bar
				BDMob.getBdMobInstance().onMobEvent(getContext(), BDEventConstans.EVENT_ID_MESSAGE_BAR);

				HYMob.getDataList(getContext(), HYEventConstans.EVENT_ID_MESSAGE_BAR);
				String data= HYMob.dataBeanToJson(HYMob.dataList, "co", "sa", "cq");
				HYMob.createMap(getContext(), data, "0") ; //0表示正常日志，1表示崩溃日志

				//点击了消息bar
					if(isDefaultHandle){
						switch (type) {
						case NEW_ORDER:	//新订单	+ orderId
							Intent intent = new Intent(getContext(),OrderDetailActivity.class);
							if(pushPassBean!=null){
								Bundle bundle = new Bundle();
								bundle.putSerializable("passBean", pushPassBean);
								LogUtils.LogE("pushBeanSzx", "pushBean:" + pushPassBean.getBidId());
								intent.putExtras(bundle);
								getContext().startActivity(intent);
							}else{
								ActivityUtils.goToActivity(getContext(), OrderDetailActivity.class);//这个还得加orderId
							}
							break;
						case QDRESULT_SUCCESS://抢单成功跳到抢单结果页面
							UnreadUtils.clearQDResult(getContext());
							Map<String, Integer> maps = new HashMap<String, Integer>();
							maps.put("type", TABLE_OTHER.KOUFEI);
							ActivityUtils.goToActivityWithInteger(
									getContext(),
									OtherDetailActivity.class, maps);
							break;
						case DAOJISHI://倒计时
							UnreadUtils.clearDaoJiShiResult(getContext());
							Map<String, Integer> map1 = new HashMap<String, Integer>();
							map1.put("type", TABLE_OTHER.DAOJISHI);
							ActivityUtils.goToActivityWithInteger(
									getContext(),
									OtherDetailActivity.class, map1);
							break;
						case SYS_NOTI:
							UnreadUtils.clearSysNotiNum(getContext());
							ActivityUtils.goToActivity(getContext(), SystemNotiListActivity.class);
							break;
						case NETWORK_ERROR:
							Intent wifiSettingsIntent = new Intent("android.settings.WIFI_SETTINGS");   
							getContext().startActivity(wifiSettingsIntent);  
							break;
						default:
							break;
						}
					}else if(titleBarListener!=null){
						titleBarListener.onTitleBarClicked(type);
					}
				LogUtils.LogE("ashen", "title_root");
				break;
			}
		}
	};
	
	protected void closeTitle() {
		
	}
	
	
	public interface OnTitleBarClickListener{
		
		/**
		 * 标题栏被点击了
		 */
		public void onTitleBarClicked(TitleBarType type);
		
		/**
		 * 标题栏的关闭按钮被点击
		 */
		public void onTitleBarClosedClicked();
	}
	
}
