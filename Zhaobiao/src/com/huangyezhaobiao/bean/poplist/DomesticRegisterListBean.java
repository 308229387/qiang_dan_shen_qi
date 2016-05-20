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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.activity.OrderDetailActivity;
import com.huangyezhaobiao.bean.push.PushToPassBean;
import com.huangyezhaobiao.holder.DomesticRegisterViewHolder;
import com.huangyezhaobiao.inter.MDConstans;
import com.huangyezhaobiao.lib.QDBaseBean;
import com.huangyezhaobiao.lib.ZBBaseAdapter;
import com.huangyezhaobiao.utils.BDEventConstans;
import com.huangyezhaobiao.utils.BDMob;
import com.huangyezhaobiao.utils.HYEventConstans;
import com.huangyezhaobiao.utils.HYMob;
import com.huangyezhaobiao.utils.MDUtils;

/**
 * 抢单信息的模型类----工商注册
 * 
 * @author linyueyang
 *
 */
public class DomesticRegisterListBean extends QDBaseBean {

	private long pushId;
	private int pushTurn;
	private int displayType;
	private int bidState;
	private long bidId;
	private String title;// 工商注册-个体
	private String time;
	private String endTime;
	private String location;

	private String agencyTime;// 3个月代理记账
	private String agencyLocation;// 代理注册区域
	//2015.10.21 add
	private String business;//经营业务
	//2015.10.21 add end

	//2016.03.29 add　for O2O-709
	private String fee;//活动价
	private String originFee; //原价



	private DomesticRegisterViewHolder holder;

	public String getBusiness() {
		return business;
	}

	public void setBusiness(String business) {
		this.business = business;
	}

	public int getDisplayType() {
		return displayType;
	}

	public void setDisplayType(int displayType) {
		this.displayType = displayType;
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

	public DomesticRegisterViewHolder getHolder() {
		return holder;
	}

	public void setHolder(DomesticRegisterViewHolder holder) {
		this.holder = holder;
	}

	public int getBidState() {
		return bidState;
	}

	public void setBidState(int bidState) {
		this.bidState = bidState;
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

	public String getAgencyTime() {
		return agencyTime;
	}

	public void setAgencyTime(String agencyTime) {
		this.agencyTime = agencyTime;
	}

	public String getAgencyLocation() {
		return agencyLocation;
	}

	public void setAgencyLocation(String agencyLocation) {
		this.agencyLocation = agencyLocation;
	}

	@Override
	public String getCateId() {
		return cateId;
	}

	@Override
	public int getLayoutId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void fillDatas() {
		if (bidState == 1) {
//			holder.knock.setImageResource(R.drawable.t_bid_gone);
			holder.knock.setBackgroundResource(R.drawable.t_not_bid_bg);
			holder.knock.setTextColor(context.getResources().getColor(R.color.transparent));
			holder.knock.setText(R.string.bidding_finish);
			holder.knock.setClickable(false);
		}
		else {
//			holder.knock.setImageResource(R.drawable.t_knock);
			holder.knock.setBackgroundResource(R.drawable.t_redbuttonselector);
			holder.knock.setTextColor(context.getResources().getColor(R.color.white));
			holder.knock.setText("抢单");
			holder.knock.setClickable(true);
			holder.knock.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					DomesticRegisterListBean.this.adapter.itemClicked(holder.knock.getId(), toPopPassBean());
					MDUtils.servicePageMD(DomesticRegisterListBean.this.context, cateId, String.valueOf(bidId),
							MDConstans.ACTION_QIANG_DAN);

					HYMob.getDataList(context, HYEventConstans.EVENT_ID_BIDDING_DETAIL_PAGE_BIDDING, String.valueOf(bidId), "2");
					String data= HYMob.dataBeanToJson(HYMob.dataList, "co","sl","modelState","grabOrderStyle", "sa", "cq");
					HYMob.createMap(context, data, "0") ; //0表示正常日志，1表示崩溃日志
				}
			});
		}
		Log.e("shenzhixin", DomesticRegisterListBean.this.toString());
		holder.title.setText(title);
		holder.time.setText(time);
		holder.grab_domesticregister_time_delegate.setText("代理记账:"+agencyTime);
		holder.grab_domesticregister_need_location_delegate.setText("代理地址:"+agencyLocation);
		holder.location.setText("注册区域:"+location);
		Log.e("shenzhixinHAHAHA", "business:" + business);
		holder.tv_business_content.setText("经营业务");
		if(!TextUtils.isEmpty(business)) {
			holder.tv_business_content.setText("经营业务"+business);
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
					String data= HYMob.dataBeanToJson(HYMob.dataList, "co","sl","grabOrderState", "sa", "cq");
					HYMob.createMap(context, data, "0") ; //0表示正常日志，1表示崩溃日志


				}else{
					BDMob.getBdMobInstance().onMobEvent(context, BDEventConstans.EVENT_ID_BIDDING_LIST_TO_DETAIL_ENABLE_BIDDING);

					HYMob.getDataListByState(context, HYEventConstans.EVENT_ID_BIDDING_LIST_TO_DETAIL_UNABLE_BIDDING, String.valueOf(bidId), "1");
					String data= HYMob.dataBeanToJson(HYMob.dataList, "co","sl","grabOrderState", "sa", "cq");
					HYMob.createMap(context, data, "0") ; //0表示正常日志，1表示崩溃日志
				}
				Intent intent = new Intent();
				intent.setClass(context, OrderDetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("passBean", toPopPassBean());
				intent.putExtras(bundle);
				context.startActivity(intent);
				MDUtils.servicePageMD(context, cateId, String.valueOf(bidId), MDConstans.ACTION_DETAILS);
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
		holder = new DomesticRegisterViewHolder();
		this.adapter = adapter;
		convertView = inflater.inflate(R.layout.order_item_domesticregister, null);
		holder.item =  convertView.findViewById(R.id.domesticregister_item);
		holder.title = (TextView) convertView.findViewById(R.id.grab_domesticregister_title);
		holder.time = (TextView) convertView.findViewById(R.id.grab_domesticregister_time);
		holder.grab_domesticregister_time_delegate = (TextView) convertView.findViewById(R.id.tv_delegate_budget);
		holder.grab_domesticregister_need_location_delegate = (TextView) convertView.findViewById(R.id.tv_delegate_address_title);
		holder.tv_business_content  = (TextView) convertView.findViewById(R.id.tv_business_title);
		holder.location = (TextView) convertView.findViewById(R.id.tv_location_title);

		holder.tv_main_fee                = (TextView) convertView.findViewById(R.id.tv_main_fee);
		holder.tv_main_origin_fee         = (TextView) convertView.findViewById(R.id.tv_main_origin_fee);
		holder.knock = (Button) convertView.findViewById(R.id.grab_main_knock);


		convertView.setTag(holder);
		
		return convertView;
	}

	@Override
	public void converseView(View convertView, Context context,ZBBaseAdapter<QDBaseBean> adapter) {
		this.adapter = adapter;
		holder = (DomesticRegisterViewHolder) convertView.getTag();
		if (this.context == null)
			this.context = context;

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

	@Override
	public String toString() {
		return "DomesticRegisterListBean{" +
				"business='" + business + '\'' +
				", pushId=" + pushId +
				", pushTurn=" + pushTurn +
				", displayType=" + displayType +
				", bidState=" + bidState +
				", bidId=" + bidId +
				", title='" + title + '\'' +
				", time='" + time + '\'' +
				", endTime='" + endTime + '\'' +
				", location='" + location + '\'' +
				", fee='" + fee + '\'' +
				", originFee='" + originFee + '\'' +
				", agencyTime='" + agencyTime + '\'' +
				", agencyLocation='" + agencyLocation + '\'' +
				'}';
	}
}
