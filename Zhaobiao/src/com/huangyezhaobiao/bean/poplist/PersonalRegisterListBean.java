package com.huangyezhaobiao.bean.poplist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.activity.OrderDetailActivity;
import com.huangyezhaobiao.bean.push.PushToPassBean;
import com.huangyezhaobiao.holder.PersonalRegisterViewHolder;
import com.huangyezhaobiao.inter.MDConstans;
import com.huangyezhaobiao.lib.QDBaseBean;
import com.huangyezhaobiao.lib.ZBBaseAdapter;
import com.huangyezhaobiao.utils.BDEventConstans;
import com.huangyezhaobiao.utils.BDMob;
import com.huangyezhaobiao.utils.HYEventConstans;
import com.huangyezhaobiao.utils.HYMob;
import com.huangyezhaobiao.utils.MDUtils;
import com.huangyezhaobiao.utils.TimeUtils;
import com.huangyezhaobiao.utils.UserUtils;

/**
 * 抢单信息的模型类----装修
 * 
 * @author linyueyang
 *
 */
public class PersonalRegisterListBean extends QDBaseBean {

	private long pushId;
	private int pushTurn;
	private int displayType;
	private long bidId;
	private String title;// 工商注册-个体
	private String time;
	private String endTime;
	private String location;
	private int bidState;
	//2015.10.21 add
	private String business;//经营业务
	//2015.10.21 add end

	//2016.03.29  wangjianlong add　for O2O-709
	private String fee;//活动价
	private String originFee; //原价
	//2016.03.29  wangjianlong add　for O2O-709 end


	public String getBusiness() {
		return business;
	}

	public void setBusiness(String business) {
		this.business = business;
	}

	public long getPushId() {
		return pushId;
	}

	public void setPushId(long pushId) {
		this.pushId = pushId;
	}

	public int getPushTurn() {
		return pushTurn;
	}

	public void setPushTurn(int pushTurn) {
		this.pushTurn = pushTurn;
	}

	public int getDisplayType() {
		return displayType;
	}

	public void setDisplayType(int displayType) {
		this.displayType = displayType;
	}

	public long getBidId() {
		return bidId;
	}

	public void setBidId(long bidId) {
		this.bidId = bidId;
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

	public int getBidState() {
		return bidState;
	}

	public void setBidState(int bidState) {
		this.bidState = bidState;
	}

	public PersonalRegisterViewHolder getHolder() {
		return holder;
	}

	public void setHolder(PersonalRegisterViewHolder holder) {
		this.holder = holder;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	public String getOriginFee() {
		return originFee;
	}

	public void setOriginFee(String originFee) {
		this.originFee = originFee;
	}

	private PersonalRegisterViewHolder holder;

	@Override
	public int getLayoutId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void fillDatas() {
		if (bidState == 1) { //不可抢
			holder.grab_style.setImageResource(R.drawable.type_back_grabbed);
			holder.iv_personalregister_type.setImageResource(R.drawable.iv_domesticregister_label);
			holder.view_personal_bottom.setVisibility(View.GONE);
//			holder.knock.setImageResource(R.drawable.t_bid_gone);
//			holder.knock.setBackgroundResource(R.drawable.t_not_bid_bg);
//			holder.knock.setText(R.string.bidding_finish);
//			holder.knock.setTextColor(context.getResources().getColor(R.color.transparent));
//			holder.knock.setClickable(false);
		}
		else {
			holder.grab_style.setImageResource(R.drawable.type_back_grab);
			holder.iv_personalregister_type.setImageResource(R.drawable.iv_domesticregister_label_knock);
			holder.view_personal_bottom.setVisibility(View.VISIBLE);

			String isSon = UserUtils.getIsSon(context);
			if (!TextUtils.isEmpty(isSon) && TextUtils.equals("1", isSon)) {
				String rbac = UserUtils.getRbac(context);
				if (!TextUtils.isEmpty(rbac)
						&& TextUtils.equals("1", rbac) || TextUtils.equals("5", rbac)) {
					holder.knock.setBackgroundResource(R.color.t_gray);
					holder.knock.setTextColor(context.getResources().getColor(R.color.white));
					holder.knock.setText("抢单");
					holder.knock.setClickable(false);
				}else{
					holder.knock.setBackgroundResource(R.drawable.bt_knock_button_selector);
					holder.knock.setTextColor(context.getResources().getColor(R.color.white));
					holder.knock.setText("抢单");
					holder.knock.setClickable(true);
					holder.knock.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Log.e("holder.knock", holder.knock.getId() + "");
							PersonalRegisterListBean.this.adapter.itemClicked(holder.knock.getId(), toPopPassBean());
							//Toast.makeText(PersonalRegisterListBean.this.context, "抢单", 0).show();
							MDUtils.servicePageMD(PersonalRegisterListBean.this.context, "" + cateId, String.valueOf(bidId),
									MDConstans.ACTION_QIANG_DAN);


						}
					});
				}

			} else {
//			holder.knock.setImageResource(R.drawable.t_knock);
				holder.knock.setBackgroundResource(R.drawable.bt_knock_button_selector);
				holder.knock.setTextColor(context.getResources().getColor(R.color.white));
				holder.knock.setText("抢单");
				holder.knock.setClickable(true);
				holder.knock.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Log.e("holder.knock", holder.knock.getId() + "");
						PersonalRegisterListBean.this.adapter.itemClicked(holder.knock.getId(), toPopPassBean());
						//Toast.makeText(PersonalRegisterListBean.this.context, "抢单", 0).show();
						MDUtils.servicePageMD(PersonalRegisterListBean.this.context, "" + cateId, String.valueOf(bidId),
								MDConstans.ACTION_QIANG_DAN);


					}
				});
			}
		}
		holder.title.setText(title);
		holder.time.setText(TimeUtils.formatDateTime(time));
		holder.grab_domesticregister_location.setText("注册区域   "+location);
		holder.tv_business_content.setText("经营业务   ");
		if(!TextUtils.isEmpty(business)) {
			holder.tv_business_content.setText("经营业务   "+business);
		}

		//2016.03.29 add for O2O-709
		if(TextUtils.equals(fee,originFee)){
			holder.tv_main_fee.setText("￥" + fee);
			holder.tv_main_origin_fee.setVisibility(View.INVISIBLE);

		}else {
			holder.tv_main_fee.setText("￥" + fee);
			holder.tv_main_origin_fee.setVisibility(View.VISIBLE);
			holder.tv_main_origin_fee.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
			holder.tv_main_origin_fee.setText(originFee);
		}
		holder.item.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(bidState==1){//不可抢
					BDMob.getBdMobInstance().onMobEvent(context, BDEventConstans.EVENT_ID_BIDDING_LIST_TO_DETAIL_UNABLE_BIDDING);

					HYMob.getDataListByState(context, HYEventConstans.EVENT_ID_BIDDING_LIST_TO_DETAIL_UNABLE_BIDDING, String.valueOf(bidId), "0");

				}else{
					BDMob.getBdMobInstance().onMobEvent(context, BDEventConstans.EVENT_ID_BIDDING_LIST_TO_DETAIL_ENABLE_BIDDING);

					HYMob.getDataListByState(context, HYEventConstans.EVENT_ID_BIDDING_LIST_TO_DETAIL_UNABLE_BIDDING, String.valueOf(bidId), "1");

				}
				Intent intent = new Intent();
				intent.setClass(context, OrderDetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("passBean", toPopPassBean());
				intent.putExtras(bundle);
				context.startActivity(intent);
				MDUtils.servicePageMD(context, "" + cateId, String.valueOf(bidId), MDConstans.ACTION_DETAILS);
			}
		});

	}

	@SuppressLint("NewApi")
	@Override
	public View initView(View convertView, LayoutInflater inflater, ViewGroup parent, Context context,
			ZBBaseAdapter<QDBaseBean> adapter) {
		if (this.context == null)
			this.context = context;
		// convertView = getLayoutView(inflater, parent);
		holder = new PersonalRegisterViewHolder();
		this.adapter = adapter;
		convertView = inflater.inflate(R.layout.order_item_personalregister, null);
		holder.item = convertView.findViewById(R.id.personalregister_item);
		holder.grab_style= (ImageView) convertView.findViewById(R.id.grab_style);
		holder.iv_personalregister_type = (ImageView) convertView.findViewById(R.id.iv_personalregister_type);
		holder.title = (TextView) convertView.findViewById(R.id.grab_personalregister_title);
		holder.time = (TextView) convertView.findViewById(R.id.grab_personalregister_time);
		holder.grab_domesticregister_location = (TextView) convertView.findViewById(R.id.tv_location_title);
		holder.tv_business_content = (TextView) convertView.findViewById(R.id.tv_business_title);

		holder.view_personal_bottom = convertView.findViewById(R.id.view_personal_bottom);

		holder.tv_main_fee                = (TextView) convertView.findViewById(R.id.tv_main_fee);
		holder.tv_main_origin_fee         = (TextView) convertView.findViewById(R.id.tv_main_origin_fee);
		holder.knock = (Button) convertView.findViewById(R.id.grab_main_knock);

		convertView.setTag(holder);
		
		return convertView;
	}

	@Override
	public void converseView(View convertView, Context context,ZBBaseAdapter<QDBaseBean> adapter) {
		this.adapter = adapter;
		holder = (PersonalRegisterViewHolder) convertView.getTag();
		if (this.context == null)
			this.context = context;

	}

	@Override
	public String getCateId() {
		return cateId;
	}

	public PushToPassBean toPopPassBean() {
		PushToPassBean bean = new PushToPassBean();
		bean.setBidId(bidId);
		bean.setPushId(pushId);
		bean.setPushTurn(pushTurn);
		bean.setCateId(Integer.parseInt(cateId));
		return bean;
	}

	@Override
	public void setCateId(String cateId) {
		this.cateId = cateId;

	}

}
