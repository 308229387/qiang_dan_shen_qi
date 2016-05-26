package com.huangyezhaobiao.bean.mylist;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
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
import com.huangyezhaobiao.holder.QDZhuangXiuMessageHolder;
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
	  private QDZhuangXiuMessageHolder holder;

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
		holder.tv_time_content.setText(this.getTime());
		holder.tv_cost.setText(this.getBudget());
		holder.tv_location.setText(this.getLocation());
		holder.tv_mode.setText(this.getType());
		holder.tv_project.setText(this.getTitle());
		holder.tv_telephone.setText(this.getPhone());
		holder.tv_customer_name_content.setText(customerName);
		holder.tv_size.setText(this.getSpace());
		Log.e("shenzhixin","current:"+QiangDanBaseFragment.orderState);
		if(TextUtils.equals(QiangDanBaseFragment.orderState, Constans.DONE_FRAGMENT)){//已完成
			holder.rl_maybe_not.setVisibility(View.VISIBLE);
			LogUtils.LogE("ashenasas", "lalala visible");
			if(TextUtils.equals(orderState, Constans.DONE_FRAGMENT_FINISH)){
				holder.tv_message.setText(R.string.over_done_service);
			}else if(TextUtils.equals(orderState, Constans.DONE_FRAGMENT_CANCEL)){
				holder.tv_message.setText(R.string.over_done_unservice);
			}
		}
		Log.e("shenzhixinjjj","refundText:"+refundText);
			if(TextUtils.isEmpty(refundText) || TextUtils.equals("未退单",refundText)){
				//如果是done的话还是要显示
				if(TextUtils.equals(QiangDanBaseFragment.orderState, Constans.DONE_FRAGMENT)){//已完成
					holder.rl_maybe_not.setVisibility(View.VISIBLE);
				}else{//不是已完成
					holder.rl_maybe_not.setVisibility(View.GONE);
				}
			}else{//有字
			//如果是done的话还是要显示
				Log.e("shenzhixinjjj","refundText:"+refundText);
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
			LogUtils.LogE("ashenasas", "lalala gone");


		holder.ll.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				FetchDetailsActivity.orderState = orderState;
				Map<String, String> map = new HashMap<String, String>();
				Log.e("shenzhixin","click qdzx:"+FetchDetailsActivity.orderState+",click oriState:"+orderState);
				LogUtils.LogE("ashenFetch", "orderid:" + orderId);
				map.put(Constans.ORDER_ID, orderId);
				ActivityUtils.goToActivityWithString(QDZhuangXiuMessageBean.this.context, FetchDetailsActivity.class, map);
				MDUtils.OrderListPageMD(QiangDanBaseFragment.orderState, cateId, orderId, MDConstans.ACTION_DETAILS);
			}
		});
		holder.btn_alreadry_contact.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//加请求
				//回调到OrderListActivity中去
				EventAction action = new EventAction(EventType.EVENT_TELEPHONE_FROM_LIST,new TelephoneBean(orderId,TelephoneBean.SOURCE_LIST));
				EventbusAgent.getInstance().post(action);
				//
				//点击了打电话按钮
				BDMob.getBdMobInstance().onMobEvent(context, BDEventConstans.EVENT_ID_ORDER_LIST_PHONE);

				HYMob.getDataListByCall(context, HYEventConstans.EVENT_ID_ORDER_DETAIL_REFUND, orderId, "0");

				initDialog(QDZhuangXiuMessageBean.this.context);
				dialog.show();
			}
		});

		Log.e("shenzhixin","qdzx:"+FetchDetailsActivity.orderState+",oriState:"+orderState);
	}
	@Override
	public View initView(View convertView, LayoutInflater inflater,
			ViewGroup parent,Context context,ZBBaseAdapter<QDBaseBean> adapter) {
		if(this.context == null) this.context = context;
		if(this.adapter==null) this.adapter = adapter;
		initDialog(context);
		convertView = getLayoutView(inflater, parent);
		holder = new QDZhuangXiuMessageHolder();
		holder.tv_customer_name_content = (TextView) convertView.findViewById(R.id.tv_customer_name_content);
		holder.btn_alreadry_contact = (ImageView) convertView.findViewById(R.id.btn_alreadry_contact);
		holder.rl_maybe_not         = (RelativeLayout) convertView.findViewById(R.id.rl_maybe_not);
		holder.tv_time_content      = (TextView) convertView.findViewById(R.id.tv_time_content);
		holder.tv_cost              = (TextView) convertView.findViewById(R.id.tv_cost);
		holder.tv_location          = (TextView) convertView.findViewById(R.id.tv_location);
		holder.tv_mode              = (TextView) convertView.findViewById(R.id.tv_mode);
		holder.tv_project           = (TextView) convertView.findViewById(R.id.tv_project);
		holder.tv_size              = (TextView) convertView.findViewById(R.id.tv_size);
		holder.tv_telephone         = (TextView) convertView.findViewById(R.id.tv_telephone);
	//	holder.tv_time              = (TextView) convertView.findViewById(R.id.tv_time);
		holder.tv_message           = (TextView) convertView.findViewById(R.id.tv_message);
		holder.ll                   = (LinearLayout) convertView.findViewById(R.id.ll);
		convertView.setTag(holder);
		
		return convertView;
		
	}
	private void initDialog(Context context) {
		if(dialog == null){
			dialog = new ZhaoBiaoDialog(context,context.getString(R.string.hint), context.getString(R.string.make_sure_tel));
			dialog.setOnDialogClickListener(new onDialogClickListener() {
				
				@Override
				public void onDialogOkClick() {
					ActivityUtils.goToDialActivity(QDZhuangXiuMessageBean.this.context, phone);
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
