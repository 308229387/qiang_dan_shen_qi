package com.huangyezhaobiao.bean.mylist;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.activity.BiddingDetailsActivity;
import com.huangyezhaobiao.holder.QDZhuangXiuMessageHolder;
import com.huangyezhaobiao.inter.Constans;
import com.huangyezhaobiao.lib.QDBaseBean;
import com.huangyezhaobiao.lib.ZBBaseAdapter;
import com.huangyezhaobiao.utils.ActivityUtils;
import com.huangyezhaobiao.utils.HYEventConstans;
import com.huangyezhaobiao.utils.HYMob;
import com.huangyezhaobiao.utils.LogUtils;
import com.huangyezhaobiao.utils.TimeUtils;
import com.huangyezhaobiao.view.ZhaoBiaoDialog;

import java.util.HashMap;
import java.util.Map;

/**
 * 抢单信息的模型类----装修
 * @author 58
 *				"cateId":"4063",					//区分标的的业务类型 装修和工商注册
				"displayType":"1"					//分为装修，工商注册内资/外资，工商注册个人/海外（【可取到】）
				"orderId":"12312321",
				"time":"2015-05-15 18:49",			//抢单时间
				"title":"住宅装修-二手房",
				"space":"56平米",
				"budget":"预算3-5万",
				"endTime":"2015年6月",				//标的的服务时间
				"location":"朝阳区北苑",
				"type":"半包" 						//【可取到】
				"phone":"13564782223",				//发标用户手机
				"orderState":"0"	
 */
public class QDZhuangXiuMessageBean extends QDBaseBean {
	private ZhaoBiaoDialog dialog;
	  private int displayType;
	  private String orderId;
	  private String time;
	  private String title;
	  private String space;
	  private String budget;
	  private String endTime;
	  private String location;
	  private String type;
	  private String phone;
	  private String orderState;
		//2015.12.8 add
		private String customerName;
		private String refundText;
		//2015.12.8 add end

	private String detailAddress;

	  private QDZhuangXiuMessageHolder holder;

	public String getDetailAddress() {
		return detailAddress;
	}

	public void setDetailAddress(String detailAddress) {
		this.detailAddress = detailAddress;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getRefundText() {
		return refundText;
	}

	public void setRefundText(String refundText) {
		this.refundText = refundText;
	}

	public String getBudget() {
		return budget;
	}
	public void setBudget(String budget) {
		this.budget = budget;
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
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getSpace() {
		return space;
	}
	public void setSpace(String space) {
		this.space = space;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
	public void setCateId(String cateId){
		this.cateId = cateId;
	}
	
	public String getCateId(){
		return cateId;
	}
	@Override
	public String toString() {
		return "QiangDanBean [budget=" + budget + ", orderState=" + orderState
				+ ", phone=" + phone + ", title=" + title + ", time=" + time
				+ ", location=" + location + ", type=" + type + ", endTime="
				+ endTime + ", space=" + space + ", orderId=" + orderId + "]";
	}
	@Override
	public int getLayoutId() {
		return R.layout.center_mine_decorate;
	}
	
	@Override
	public void fillDatas() {
		holder.tv_project.setText(this.getTitle());
		holder.tv_time_content.setText(TimeUtils.formatDateTime(time));
		holder.tv_location.setText(this.getLocation());
		holder.tv_decorate_address_content.setText(detailAddress);
		holder.tv_customer_name_content.setText(customerName);

//		holder.tv_size.setText(this.getSpace());
//		holder.tv_cost.setText(this.getBudget());
//		holder.tv_mode.setText(this.getType());
//		holder.tv_telephone.setText(this.getPhone());

		BiddingDetailsActivity.time =time;

		//判断状态
		if(!TextUtils.isEmpty(orderState)){ //获取订单状态
			if(TextUtils.equals(orderState, Constans.DONE_FRAGMENT_FINISH)){ //已完成(成交)
				holder.iv_decorate_orderstate_line.setVisibility(View.GONE);
				holder.tv_decotrate_order_state.setText(R.string.over_done);
				holder.tv_decotrate_order_state.setTextColor(Color.parseColor("#C5C5C5"));
				if(TextUtils.isEmpty(refundText) ||TextUtils.equals(refundText,"未退单")){

				}else{
					StringBuilder sb = new StringBuilder();
					sb.append("已结束").append("(").append(refundText).append(")");
					holder.tv_decotrate_order_state.setText(sb.toString());
					holder.tv_decotrate_order_state.setTextColor(Color.parseColor("#C5C5C5"));
				}
			}else if(TextUtils.equals(orderState, Constans.DONE_FRAGMENT_CANCEL)){ //已完成(未成交)
				holder.iv_decorate_orderstate_line.setVisibility(View.GONE);
				holder.tv_decotrate_order_state.setText(R.string.over_undone);
				holder.tv_decotrate_order_state.setTextColor(Color.parseColor("#C5C5C5"));
				if(TextUtils.isEmpty(refundText) ||TextUtils.equals(refundText,"未退单")){

				}else{
					StringBuilder sb = new StringBuilder();
					sb.append("已结束").append("(").append(refundText).append(")");
					holder.tv_decotrate_order_state.setText(sb.toString());
					holder.tv_decotrate_order_state.setTextColor(Color.parseColor("#C5C5C5"));
				}
			}else if(TextUtils.equals(orderState, Constans.READY_SERVICE)){
				holder.iv_decorate_orderstate_line.setVisibility(View.VISIBLE);
				holder.iv_decorate_orderstate_line.setImageResource(R.drawable.onservice_order_state);
				holder.tv_decotrate_order_state.setText(R.string.unservice);
				holder.tv_decotrate_order_state.setTextColor(Color.parseColor("#4EC5BF"));
				if(TextUtils.isEmpty(refundText) ||TextUtils.equals(refundText,"未退单")){

				}else{
					StringBuilder sb = new StringBuilder();
					sb.append("待服务").append("(").append(refundText).append(")");
					holder.tv_decotrate_order_state.setText(sb.toString());
					holder.tv_decotrate_order_state.setTextColor(Color.parseColor("#4EC5BF"));
				}
			}else if(TextUtils.equals(orderState, Constans.ON_SERVICE)){
				holder.iv_decorate_orderstate_line.setVisibility(View.VISIBLE);
				holder.iv_decorate_orderstate_line.setImageResource(R.drawable.servicing_order_state);
				holder.tv_decotrate_order_state.setText(R.string.servicing);
				holder.tv_decotrate_order_state.setTextColor(Color.parseColor("#4EC5BF"));
				if(TextUtils.isEmpty(refundText) ||TextUtils.equals(refundText,"未退单")){

				}else{
					StringBuilder sb = new StringBuilder();
					sb.append("服务中").append("(").append(refundText).append(")");
					holder.tv_decotrate_order_state.setText(sb.toString());
					holder.tv_decotrate_order_state.setTextColor(Color.parseColor("#4EC5BF"));
				}
			}
		}


		holder.ll.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				BiddingDetailsActivity.orderState = orderState;
				Map<String, String> map = new HashMap<String, String>();
				Log.e("shenzhixin","click qdzx:"+ BiddingDetailsActivity.orderState+",click oriState:"+orderState);
				LogUtils.LogE("ashenFetch", "orderid:" + orderId);
				map.put(Constans.ORDER_ID, orderId);
				ActivityUtils.goToActivityWithString(QDZhuangXiuMessageBean.this.context, BiddingDetailsActivity.class, map);
//				MDUtils.OrderListPageMD(QiangDanBaseFragment.orderState, cateId, orderId, MDConstans.ACTION_DETAILS);
				HYMob.getDataListByServiceState(context, HYEventConstans.EVENT_ID_ORDER_DETAIL_PAGE);
			}
		});

//		holder.btn_alreadry_contact.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				//加请求
//				//回调到OrderListActivity中去
//				EventAction action = new EventAction(EventType.EVENT_TELEPHONE_FROM_LIST,new TelephoneBean(orderId,TelephoneBean.SOURCE_LIST));
//				EventbusAgent.getInstance().post(action);
//				//
//				//点击了打电话按钮
//				BDMob.getBdMobInstance().onMobEvent(context, BDEventConstans.EVENT_ID_ORDER_LIST_PHONE);
//
//				HYMob.getDataListByCall(context, HYEventConstans.EVENT_ID_ORDER_DETAIL_REFUND, orderId, "0");
//
//				initDialog(QDZhuangXiuMessageBean.this.context);
//				dialog.show();
//			}
//		});

	}
	@Override
	public View initView(View convertView, LayoutInflater inflater,
			ViewGroup parent,Context context,ZBBaseAdapter<QDBaseBean> adapter) {
		if(this.context == null) this.context = context;
		if(this.adapter==null) this.adapter = adapter;
//		initDialog(context);
		convertView = getLayoutView(inflater, parent);
		holder = new QDZhuangXiuMessageHolder();
		holder.tv_project           = (TextView) convertView.findViewById(R.id.tv_project);
		holder.tv_time_content      = (TextView) convertView.findViewById(R.id.tv_time_content);
		holder.tv_location          = (TextView) convertView.findViewById(R.id.tv_location);
		holder.tv_decorate_address_content = (TextView) convertView.findViewById(R.id.tv_decorate_address_content);
		holder.tv_customer_name_content = (TextView) convertView.findViewById(R.id.tv_customer_name_content);
		holder.iv_decorate_orderstate_line = (ImageView) convertView.findViewById(R.id.iv_decorate_orderstate_line);
		holder.tv_decotrate_order_state = (TextView) convertView.findViewById(R.id.tv_decotrate_order_state);
		holder.ll                   = (LinearLayout) convertView.findViewById(R.id.ll);
		convertView.setTag(holder);

//
//		holder.tv_message           = (TextView) convertView.findViewById(R.id.tv_message);
//		holder.tv_size              = (TextView) convertView.findViewById(R.id.tv_size);
//		holder.tv_telephone         = (TextView) convertView.findViewById(R.id.tv_telephone);
//		holder.tv_mode              = (TextView) convertView.findViewById(R.id.tv_mode);
//		holder.tv_cost              = (TextView) convertView.findViewById(R.id.tv_cost);
//		holder.btn_alreadry_contact = (ImageView) convertView.findViewById(R.id.btn_alreadry_contact);
//		holder.rl_maybe_not         = (RelativeLayout) convertView.findViewById(R.id.rl_maybe_not);
		//	holder.tv_time              = (TextView) convertView.findViewById(R.id.tv_time);
		return convertView;
		
	}
//	private void initDialog(Context context) {
//		if(dialog == null){
//			dialog = new ZhaoBiaoDialog(context,context.getString(R.string.hint), context.getString(R.string.make_sure_tel));
//			dialog.setOnDialogClickListener(new onDialogClickListener() {
//
//				@Override
//				public void onDialogOkClick() {
//					ActivityUtils.goToDialActivity(QDZhuangXiuMessageBean.this.context, phone);
//					MDUtils.OrderListPageMD(QiangDanBaseFragment.orderState, cateId, orderId, MDConstans.ACTION_UP_TEL);
//					dialog.dismiss();
//				}
//
//				@Override
//				public void onDialogCancelClick() {
//					dialog.dismiss();
//				}
//			});
//		}
//	}
	@Override
	public void converseView(View convertView,Context context,ZBBaseAdapter<QDBaseBean> adapter) {
		this.adapter = adapter;
		holder = (QDZhuangXiuMessageHolder) convertView.getTag();
		if(this.context == null) this.context = context;
	}
	@Override
	public int getDisplayType() {
		return displayType;
	}
	
	public void setDisplayType(int type){
		this.displayType = type;
	}
	
	 
}
