package com.huangyezhaobiao.bean.mylist;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.activity.FetchDetailsActivity;
import com.huangyezhaobiao.bean.TelephoneBean;
import com.huangyezhaobiao.eventbus.EventAction;
import com.huangyezhaobiao.eventbus.EventType;
import com.huangyezhaobiao.eventbus.EventbusAgent;
import com.huangyezhaobiao.fragment.QiangDanBaseFragment;
import com.huangyezhaobiao.holder.MessCenIACIndividualHolder;
import com.huangyezhaobiao.inter.Constans;
import com.huangyezhaobiao.inter.MDConstans;
import com.huangyezhaobiao.lib.QDBaseBean;
import com.huangyezhaobiao.lib.ZBBaseAdapter;
import com.huangyezhaobiao.utils.ActivityUtils;
import com.huangyezhaobiao.utils.BDEventConstans;
import com.huangyezhaobiao.utils.BDMob;
import com.huangyezhaobiao.utils.HYEventConstans;
import com.huangyezhaobiao.utils.HYMob;
import com.huangyezhaobiao.utils.LogUtils;
import com.huangyezhaobiao.utils.MDUtils;
import com.huangyezhaobiao.view.ZhaoBiaoDialog;
import com.huangyezhaobiao.view.ZhaoBiaoDialog.onDialogClickListener;

import java.util.HashMap;
import java.util.Map;

/**
 * 我的订单中心页面的工商注册--个体的bean
 * @author shenzhixin
 *
 *
 *				"cateId":"4065",	
				"displayType":"2"					//【可取到】
				"orderId":"12312321",
				"title":"工商注册-个体",
				"time":"2015-05-15 18:49",
				"endTime":"2015年6月",				//标的的服务时间
				"location":"朝阳区北苑",
				"orderState":"0"					//同上
				"phone":"13564782223",
 */
public class MessCenIACIndividualBean extends QDBaseBean {
	private MessCenIACIndividualHolder holder;
	private int displayType;
	private String orderId;
	private String title;
	private String time;
	private String endTime;
	private String location;
	private String orderState;
	private String phone;
	//2015.12.8 add
	private String customerName;
	private String refundText;
	//2015.12.8 add end
	private ZhaoBiaoDialog dialog;

	//2015.10.21 add
	private String business;
	//2015.10.21 add end
	public MessCenIACIndividualHolder getHolder() {
		return holder;
	}


	public String getRefundText() {
		return refundText;
	}

	public void setRefundText(String refundText) {
		this.refundText = refundText;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public void setHolder(MessCenIACIndividualHolder holder) {
		this.holder = holder;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getOrderState() {
		return orderState;
	}

	public void setOrderState(String orderState) {
		this.orderState = orderState;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public String getCateId() {
		return cateId;
	}

	@Override
	public void setCateId(String cateId) {
		this.cateId = cateId;
	}


	public String getBusiness() {
		return business;
	}

	public void setBusiness(String business) {
		this.business = business;
	}

	@Override
	public int getLayoutId() {
		return R.layout.center_mine_indiviual;
	}

	@Override
	public void fillDatas() {
		if(!TextUtils.isEmpty(business)) {
			holder.tv_location_business_content.setText(business);
		}
		holder.tv_location_content.setText(location);
		LogUtils.LogE("ashensss", "location:" + location);
		holder.tv_project.setText(title);
		holder.tv_telephone.setText(phone);
		holder.tv_adapter_time.setText(time);
		holder.tv_domestic_personal_customerName_content.setText(customerName);
		holder.tv_time.setText(endTime);

		//判断状态
		if(TextUtils.equals(QiangDanBaseFragment.orderState, Constans.DONE_FRAGMENT)){//已完成
			holder.rl_maybe_not.setVisibility(View.VISIBLE);
			LogUtils.LogE("ashenasas", "lalala visible");
			if(TextUtils.equals(orderState, Constans.DONE_FRAGMENT_FINISH)){
				holder.tv_message.setText(R.string.over_done_service);
			}else if(TextUtils.equals(orderState, Constans.DONE_FRAGMENT_CANCEL)){
				holder.tv_message.setText(R.string.over_done_unservice);
			}
		}

		if(TextUtils.isEmpty(refundText) || TextUtils.equals("未退单",refundText)){
			//如果是done的话还是要显示
			if(TextUtils.equals(QiangDanBaseFragment.orderState, Constans.DONE_FRAGMENT)){//已完成
				holder.rl_maybe_not.setVisibility(View.VISIBLE);
			}else{//不是已完成
				holder.rl_maybe_not.setVisibility(View.GONE);
			}
		}else{//有字
			//如果是done的话还是要显示
			if(TextUtils.equals(QiangDanBaseFragment.orderState, Constans.DONE_FRAGMENT)){//已完成
				holder.rl_maybe_not.setVisibility(View.VISIBLE);
				StringBuilder sb = new StringBuilder();
				sb.append(holder.tv_message.getText().toString());
				holder.tv_message.setText(sb.append("      "+refundText));
			}else{//不是已完成
				holder.rl_maybe_not.setVisibility(View.VISIBLE);
				holder.tv_message.setText(refundText);
			}
		}



		//待服务的退单状态
		holder.ll.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				FetchDetailsActivity.orderState = orderState;
				Map<String, String> map = new HashMap<String, String>();
				LogUtils.LogE("ashenFetch", "orderid:" + orderId);
				map.put(Constans.ORDER_ID, orderId);
				ActivityUtils.goToActivityWithString(MessCenIACIndividualBean.this.context, FetchDetailsActivity.class, map);
				MDUtils.OrderListPageMD(QiangDanBaseFragment.orderState, cateId, orderId, MDConstans.ACTION_DETAILS);
			}
		});
		holder.btn_alreadry_contact.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//回调到OrderListActivity中去
				EventAction action = new EventAction(EventType.EVENT_TELEPHONE_FROM_LIST,new TelephoneBean(orderId,TelephoneBean.SOURCE_LIST));
				EventbusAgent.getInstance().post(action);
				//点击了打电话按钮
				BDMob.getBdMobInstance().onMobEvent(context, BDEventConstans.EVENT_ID_ORDER_LIST_PHONE);


				HYMob.getDataListByCall(context, HYEventConstans.EVENT_ID_ORDER_DETAIL_REFUND, orderId, "0");
				String  data= HYMob.dataBeanToJson(HYMob.dataList, "co","callStyle","orderId","serviceSate", "sa", "cq");
				HYMob.createMap(context, data, "0") ; //0表示正常日志，1表示崩溃日志

				initDialog(MessCenIACIndividualBean.this.context);
				dialog.show();
			}
		});
		FetchDetailsActivity.orderState = orderState;
	}

	@Override
	public View initView(View convertView, LayoutInflater inflater,
			ViewGroup parent, Context context,ZBBaseAdapter<QDBaseBean> adapter) {
		super.context = context;
		convertView = inflater.inflate(getLayoutId(), parent, false);
		initDialog(context);
		holder  = new MessCenIACIndividualHolder();
		holder.tv_domestic_personal_customerName_content = (TextView) convertView.findViewById(R.id.tv_domestic_personal_customerName_content);
		holder.btn_alreadry_contact = (ImageView) convertView.findViewById(R.id.btn_alreadry_contact);
		holder.rl_maybe_not = (RelativeLayout) convertView.findViewById(R.id.rl_maybe_not);
		holder.tv_location_business_content = (TextView) convertView.findViewById(R.id.tv_location_business_content);
		holder.tv_location_content = (TextView) convertView.findViewById(R.id.tv_location_content);
		holder.tv_project = (TextView) convertView.findViewById(R.id.tv_project);
		holder.tv_telephone = (TextView) convertView.findViewById(R.id.tv_telephone);
		holder.tv_adapter_time = (TextView) convertView.findViewById(R.id.tv_adapter_time);
		holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
		holder.tv_message = (TextView) convertView.findViewById(R.id.tv_message);
		holder.ll = (LinearLayout) convertView.findViewById(R.id.ll);

		convertView.setTag(holder);
		return convertView;
	}

	@Override
	public void converseView(View convertView,Context context,ZBBaseAdapter<QDBaseBean> adapter) {
		this.adapter = adapter;
		holder = (MessCenIACIndividualHolder) convertView.getTag();
		if(this.context == null){
			this.context = context;
		}
	}

	@Override
	public int getDisplayType() {
		return displayType;
	}
	
	public void setDisplayType(int displayType) {
		this.displayType = displayType;
	}

	private void initDialog(Context context) {
		if(dialog == null){
			dialog = new ZhaoBiaoDialog(context, "提示", "确定要拨打电话?");
			dialog.setOnDialogClickListener(new onDialogClickListener() {
				@Override
				public void onDialogOkClick() {
					ActivityUtils.goToDialActivity(MessCenIACIndividualBean.this.context, phone);
					MDUtils.OrderListPageMD(QiangDanBaseFragment.orderState, cateId, orderId, MDConstans.ACTION_UP_TEL);
					dialog.dismiss();
				}
				@Override
				public void onDialogCancelClick() {
					dialog.dismiss();
				}
			});
		}
	}


}
