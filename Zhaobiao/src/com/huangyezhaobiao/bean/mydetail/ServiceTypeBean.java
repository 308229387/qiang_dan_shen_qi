package com.huangyezhaobiao.bean.mydetail;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.activity.FetchDetailsActivity;
import com.huangyezhaobiao.bean.TelephoneBean;
import com.huangyezhaobiao.bean.popdetail.QDDetailBaseBean;
import com.huangyezhaobiao.eventbus.EventAction;
import com.huangyezhaobiao.eventbus.EventType;
import com.huangyezhaobiao.eventbus.EventbusAgent;
import com.huangyezhaobiao.fragment.QiangDanBaseFragment;
import com.huangyezhaobiao.inter.Constans;
import com.huangyezhaobiao.inter.MDConstans;
import com.huangyezhaobiao.utils.ActivityUtils;
import com.huangyezhaobiao.utils.DetailsLogBeanUtils;
import com.huangyezhaobiao.utils.LogUtils;
import com.huangyezhaobiao.utils.MDUtils;
import com.huangyezhaobiao.view.ZhaoBiaoDialog;
import com.huangyezhaobiao.view.ZhaoBiaoDialog.onDialogClickListener;

/**
 * 订单详情的服务类型的bean
 * @author shenzhixin
 *
 */
public class ServiceTypeBean extends QDDetailBaseBean{
	private Context context;
	private String name;
	private String leftTime;
	private String clientPhone;
	private String customerName;
	private RelativeLayout rl_done_not_gone;
	private TextView       tv_done_status;
	private TextView       tv_tel_time;
	private TextView       tv_tel_phone_content;
	private TextView       tv_customer_name_content;
	private ImageView      iv_tel;
	private ZhaoBiaoDialog dialog;

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	@Override
	public View initView(Context context) {
		this.context = context;
		initDialog(context);
		View view 			 = LayoutInflater.from(context).inflate(R.layout.layout_qiangdan_first, null);
		rl_done_not_gone 	 = (RelativeLayout) view.findViewById(R.id.rl_done_not_gone);
		tv_done_status   	 = (TextView) view.findViewById(R.id.tv_done_status);
		tv_tel_time      	 = (TextView) view.findViewById(R.id.tv_tel_time);
		tv_customer_name_content = (TextView) view.findViewById(R.id.tv_customer_name_content);
		tv_tel_phone_content = (TextView) view.findViewById(R.id.tv_tel_phone_content);
		iv_tel 			 	 = (ImageView) view.findViewById(R.id.iv_tel);
		iv_tel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				EventAction action = new EventAction(EventType.EVENT_TELEPHONE_FROM_DETAIL,new TelephoneBean(orderId+"",TelephoneBean.SOURCE_DETAIL));
				EventbusAgent.getInstance().post(action);
				initDialog(ServiceTypeBean.this.context);
				dialog.show();
			}
		});
		fillDatas();
		return view;
	}
	private void fillDatas() {
		Log.e("shenzhixin","fetch state:"+FetchDetailsActivity.orderState);
		if(TextUtils.equals(QiangDanBaseFragment.orderState,"1"))
			tv_done_status.setText(R.string.unservice);
		else if(TextUtils.equals(QiangDanBaseFragment.orderState,"2"))
			tv_done_status.setText(R.string.servicing);
		else if(TextUtils.equals(QiangDanBaseFragment.orderState,"3")){
			if(TextUtils.equals(FetchDetailsActivity.orderState,Constans.DONE_FRAGMENT_FINISH))
					tv_done_status.setText(R.string.over_done_service);
			else if(TextUtils.equals(FetchDetailsActivity.orderState,Constans.DONE_FRAGMENT_CANCEL)){
					tv_done_status.setText(R.string.over_done_unservice);
			}
		}else{
			tv_done_status.setText(R.string.order_details);
		}
		tv_tel_phone_content.setText(clientPhone);
		tv_tel_time.setText(leftTime);
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
	public RelativeLayout getRl_done_not_gone() {
		return rl_done_not_gone;
	}
	public void setRl_done_not_gone(RelativeLayout rl_done_not_gone) {
		this.rl_done_not_gone = rl_done_not_gone;
	}
	public TextView getTv_done_status() {
		return tv_done_status;
	}
	public void setTv_done_status(TextView tv_done_status) {
		this.tv_done_status = tv_done_status;
	}
	public TextView getTv_tel_time() {
		return tv_tel_time;
	}
	public void setTv_tel_time(TextView tv_tel_time) {
		this.tv_tel_time = tv_tel_time;
	}
	public TextView getTv_tel_phone_content() {
		return tv_tel_phone_content;
	}
	public void setTv_tel_phone_content(TextView tv_tel_phone_content) {
		this.tv_tel_phone_content = tv_tel_phone_content;
	}
	public ImageView getIv_tel() {
		return iv_tel;
	}
	public void setIv_tel(ImageView iv_tel) {
		this.iv_tel = iv_tel;
	}
	
	
	public void setNewType(String newType){
		super.newtype = newType;
	}
	
	
	private void initDialog(Context context) {
		if(dialog == null){
			dialog = new ZhaoBiaoDialog(context, context.getString(R.string.hint), context.getString(R.string.make_sure_tel));
			dialog.setOnDialogClickListener(new onDialogClickListener() {
				
				@Override
				public void onDialogOkClick() {
					LogUtils.LogE("assssshenaaa", "bidid:" + DetailsLogBeanUtils.bean.getBidID() + ",cateid:" + DetailsLogBeanUtils.bean.getCateID());
					ActivityUtils.goToDialActivity(ServiceTypeBean.this.context, clientPhone);
					MDUtils.OrderDetailsPageMD(QiangDanBaseFragment.orderState, DetailsLogBeanUtils.bean.getCateID()+"",DetailsLogBeanUtils.bean.getBidID()+"", MDConstans.ACTION_UP_TEL,clientPhone);
					dialog.dismiss();
					dialog = null;
				}
				
				@Override
				public void onDialogCancelClick() {
					dialog.dismiss();
					dialog = null;
				}
			});
		}
	}
}
