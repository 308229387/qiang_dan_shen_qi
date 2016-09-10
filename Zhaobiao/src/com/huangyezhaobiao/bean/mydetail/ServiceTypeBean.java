package com.huangyezhaobiao.bean.mydetail;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.activity.BusinessDetailsActivity;
import com.huangyezhaobiao.bean.popdetail.QDDetailBaseBean;
import com.huangyezhaobiao.inter.Constans;
import com.huangyezhaobiao.view.ZhaoBiaoDialog;

/**
 * 订单详情的服务类型的bean
 * @author shenzhixin
 *
 */
public class ServiceTypeBean extends QDDetailBaseBean{
	private Context context;
	private String name; //订单状态
	private String leftTime;
	private String clientPhone;
	private String customerName;
	private String orderNum; //订单编号
	private String time;

//	private RelativeLayout rl_done_not_gone; //标题栏
	private TextView       tv_done_status;
//	private TextView       tv_tel_time;//剩余时间
//	private TextView       tv_tel_phone_content; //客户电话
	private TextView       tv_customer_name_content;
//	private ImageView      iv_tel;  //打电话



	private TextView tv_last_number_content;  //订单编号

	private ZhaoBiaoDialog dialog;

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	@Override
	public View initView(final Context context) {
		this.context = context;
//		initDialog(context);
		View view 			 = LayoutInflater.from(context).inflate(R.layout.layout_qiangdan_first, null);
		tv_last_number_content = (TextView) view.findViewById(R.id.tv_last_number_content);
		tv_done_status   	 = (TextView) view.findViewById(R.id.tv_done_status);
		tv_customer_name_content = (TextView) view.findViewById(R.id.tv_customer_name_content);

//		rl_done_not_gone 	 = (RelativeLayout) view.findViewById(R.id.rl_done_not_gone);
//		tv_tel_time      	 = (TextView) view.findViewById(R.id.tv_tel_time);
//		tv_tel_phone_content = (TextView) view.findViewById(R.id.tv_tel_phone_content);
//		iv_tel 			 	 = (ImageView) view.findViewById(R.id.iv_tel);
//		iv_tel.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				EventAction action = new EventAction(EventType.EVENT_TELEPHONE_FROM_DETAIL,new TelephoneBean(orderId+"",TelephoneBean.SOURCE_DETAIL));
//				EventbusAgent.getInstance().post(action);
//				//加埋点
//				BDMob.getBdMobInstance().onMobEvent(context, BDEventConstans.EVENT_ID_ORDER_DETAIL_PAGE_PHONE);
//
//				HYMob.getDataListByCall(context, HYEventConstans.EVENT_ID_ORDER_DETAIL_PAGE_PHONE, String.valueOf(orderId), "1");
//
//
//				initDialog(ServiceTypeBean.this.context);
//				dialog.show();
//			}
//		});
		fillDatas();
		return view;
	}
	private void fillDatas() {

		if(!TextUtils.isEmpty(clientPhone)){
			BusinessDetailsActivity.clientPhone = clientPhone;
		}

		if(!TextUtils.isEmpty(time)){
			BusinessDetailsActivity.time =time;
		}

		tv_last_number_content.setText(orderNum);

		if (TextUtils.equals(name,  Constans.READY_SERVICE))
			tv_done_status.setText(R.string.unservice);
		else if (TextUtils.equals(name, Constans.ON_SERVICE))
			tv_done_status.setText(R.string.servicing);
		else if (TextUtils.equals(name, Constans.DONE_FRAGMENT_FINISH))
			tv_done_status.setText(R.string.over_done_service);
		else if(TextUtils.equals(name,Constans.DONE_FRAGMENT_CANCEL))
					tv_done_status.setText(R.string.over_done_unservice);

//		tv_tel_phone_content.setText(clientPhone);
//		tv_tel_time.setText("剩余时间:    "+leftTime);
		tv_customer_name_content.setText(customerName);
	}
	public Context getContext() {
		return context;
	}
	public void setContext(Context context) {
		this.context = context;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLeftTime() {
		return leftTime;
	}
	public void setLeftTime(String leftTime) {
		this.leftTime = leftTime;
	}
	public String getClientPhone() {
		return clientPhone;
	}
	public void setClientPhone(String clientPhone) {
		this.clientPhone = clientPhone;
	}

	public void setNewType(String newType){
		super.newtype = newType;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	//	private void initDialog(Context context) {
//		if(dialog == null){
//			dialog = new ZhaoBiaoDialog(context, context.getString(R.string.hint), context.getString(R.string.make_sure_tel));
//			dialog.setOnDialogClickListener(new onDialogClickListener() {
//
//				@Override
//				public void onDialogOkClick() {
//					LogUtils.LogE("assssshenaaa", "bidid:" + DetailsLogBeanUtils.bean.getBidID() + ",cateid:" + DetailsLogBeanUtils.bean.getCateID());
//					ActivityUtils.goToDialActivity(ServiceTypeBean.this.context, clientPhone);
//					MDUtils.OrderDetailsPageMD(QiangDanBaseFragment.orderState, DetailsLogBeanUtils.bean.getCateID()+"",DetailsLogBeanUtils.bean.getBidID()+"", MDConstans.ACTION_UP_TEL,clientPhone);
//					dialog.dismiss();
//					dialog = null;
//				}
//
//				@Override
//				public void onDialogCancelClick() {
//					dialog.dismiss();
//					dialog = null;
//				}
//			});
//		}
//	}
}
