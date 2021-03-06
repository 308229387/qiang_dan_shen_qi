package com.huangyezhaobiao.bean.mydetail;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.bean.popdetail.QDDetailBaseBean;
import com.huangyezhaobiao.inter.MDConstans;
import com.huangyezhaobiao.utils.ActivityUtils;
import com.huangyezhaobiao.utils.DetailsLogBeanUtils;
import com.huangyezhaobiao.utils.LogUtils;
import com.huangyezhaobiao.utils.MDUtils;
import com.huangyezhaobiao.view.ZhaoBiaoDialog;
import com.huangyezhaobiao.view.ZhaoBiaoDialog.onDialogClickListener;
/**
 * 抢单详情页面的工商注册个人页面
 * @author shenzhixin
 *	registerLocation出问题了,主要是命名不对，反射时有错误
 */
public class IACIndividualDetailBean extends QDDetailBaseBean{
	private Context   mContext;
	private TextView  tv_type_content;
	private TextView  tv_lf_time_content;
	private TextView  tv_location_content;
	private TextView  tv_needs_content;
	private TextView  tv_ch_tel_content;
	private TextView  tv_business_content;
//	private ImageView iv_tels;
	private String name;
	private String registerType;
	private String registerTime;
	private String registerLocation;
	private String special;
	private String clientPhone;
	private String business;
	private ZhaoBiaoDialog dialog;
	@Override
	public View initView(Context context) {
		mContext = context; 
//		initDialog(mContext);
		View view = LayoutInflater.from(context).inflate(R.layout.layout_qiangdan_middle_getigs, null);
		//tv_ch_tel_content = (TextView) view.findViewById(R.id.tv_ch_tel_content);
		tv_lf_time_content = (TextView) view.findViewById(R.id.tv_lf_time_content);
		tv_location_content = (TextView) view.findViewById(R.id.tv_location_content);
		tv_needs_content = (TextView) view.findViewById(R.id.tv_needs_content);
		tv_type_content = (TextView) view.findViewById(R.id.tv_type_content);
//		iv_tels         = (ImageView) view.findViewById(R.id.iv_tels);
		tv_business_content = (TextView) view.findViewById(R.id.tv_business_content);
		/*.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//点击了打电话按钮
				BDMob.getBdMobInstance().onMobEvent(mContext, BDEventConstans.EVENT_ID_ORDER_DETAIL_PAGE_PHONE);
				initDialog(mContext);
				iv_telsdialog.show();
			}
		});*/
		fillDatas();
		return view;
	}
	
	private void fillDatas(){
		//tv_ch_tel_content.setText(clientPhone);
		tv_lf_time_content.setText(registerTime);
		tv_location_content.setText(registerLocation);
		tv_needs_content.setText(special);
		if(TextUtils.isEmpty(special)){
			tv_needs_content.setText("无");
		}
		tv_type_content.setText( registerType);
		if(!TextUtils.isEmpty(business)) {
			tv_business_content.setText(business);
		}else{
			tv_business_content.setText("无");
		}
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRegisterType() {
		return registerType;
	}
	public void setRegisterType(String registerType) {
		this.registerType = registerType;
	}
	public String getRegisterTime() {
		return registerTime;
	}
	public void setRegisterTime(String registerTime) {
		this.registerTime = registerTime;
	}

	public String getregisterLocation() {
		return registerLocation;
	}

	public void setregisterLocation(String registerLocation) {
		this.registerLocation = registerLocation;
	}
	public String getSpecial() {
		return special;
	}
	public void setSpecial(String special) {
		this.special = special;
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

	public String getBusiness() {
		return business;
	}

	public void setBusiness(String business) {
		this.business = business;
	}

//	private void initDialog(Context context) {
//		if(dialog == null){
//			dialog = new ZhaoBiaoDialog(context, "提示", "确定要拨打电话?");
//			dialog.setOnDialogClickListener(new onDialogClickListener() {
//
//				@Override
//				public void onDialogOkClick() {
//					LogUtils.LogE("assssshenaaa", "bidid:" + DetailsLogBeanUtils.bean.getBidID() + ",cateid:" + DetailsLogBeanUtils.bean.getCateID());
//					ActivityUtils.goToDialActivity(mContext, clientPhone);
//					MDUtils.OrderDetailsPageMD(QiangDanBaseFragment.orderState,DetailsLogBeanUtils.bean.getCateID()+"",orderId+"",MDConstans.ACTION_DOWN_TEL,clientPhone);
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
