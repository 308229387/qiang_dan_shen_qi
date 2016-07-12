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
import com.huangyezhaobiao.holder.RenovationViewHolder;
import com.huangyezhaobiao.inter.MDConstans;
import com.huangyezhaobiao.lib.QDBaseBean;
import com.huangyezhaobiao.lib.ZBBaseAdapter;
import com.huangyezhaobiao.utils.BDEventConstans;
import com.huangyezhaobiao.utils.BDMob;
import com.huangyezhaobiao.utils.HYEventConstans;
import com.huangyezhaobiao.utils.HYMob;
import com.huangyezhaobiao.utils.MDUtils;
import com.huangyezhaobiao.utils.TimeUtils;

/**
 * 抢单信息的模型类----装修
 * 
 * @author linyueyang
 *
 */
public class RenovationListBean extends QDBaseBean {

	private long pushId;
	private int pushTurn;
	private int displayType;
	private long bidId;

	private String budget;
	private String orderState;
	private String title;
	private String time;
	private String location;
	private String type;
	private String endTime;
	private String space;

	// edited by guangming
	private String fee;//活动价
	private String originFee;//原价

	private String needNear; //距离

	private int bidState;

	private RenovationViewHolder holder;

	public String getNeedNear() {
		return needNear;
	}

	public void setNeedNear(String needNear) {
		this.needNear = needNear;
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

	public int getBidState() {
		return bidState;
	}

	public void setBidState(int bidState) {
		this.bidState = bidState;
	}

	public void setOriginFee(String originFee){
		this.originFee = originFee;
	}

	public String getOriginFee(){return originFee;}

	public void setFee(String fee){this.fee = fee;}

	public String getFee(){return fee;}

	public RenovationViewHolder getHolder() {
		return holder;
	}

	public void setHolder(RenovationViewHolder holder) {
		this.holder = holder;
	}

	@Override
	public String toString() {
		return "QiangDanBean [budget=" + budget + ", orderState=" + orderState + ", title=" + title + ", time=" + time
				+ ", location=" + location + ", type=" + type + ", endTime=" + endTime + ", space=" + space + ", bidId="
				+ bidId + "]";
	}

	@Override
	public int getLayoutId() {
		return 0;
	}

	@Override
	public void fillDatas() {

		holder.title.setText(this.getTitle());
		holder.time.setText(TimeUtils.formatDateTime(time));
		/**
		 * edited by chenguangming ==== start
		 * */
		holder.tvActivePrice.setText("￥" + fee);

		if(TextUtils.equals(fee, originFee)){
			holder.tvOriginalPrice.setVisibility(View.GONE);
		} else {
			holder.tvOriginalPrice.setVisibility(View.VISIBLE);
			holder.tvOriginalPrice.setText(originFee);
		}

		/**
		 * ============================ end
		 * */
		if(null!=space&&space.length()>11)
			space = space.substring(0,10)+"...";
		if(null!=budget&&budget.length()>11)
			budget = budget.substring(0,10)+"...";
		if(null!=type&&type.length()>11)
			type = type.substring(0,10)+"...";
		if(null!=endTime&&endTime.length()>11)
			endTime = endTime.substring(0,10)+"...";
		if(null!=location&&location.length()>10)
			location = location.substring(0,9)+"...";

		holder.space.setText("面积:"+this.getSpace());
		holder.budget.setText("预算:"+this.getBudget());
		holder.decorate_type.setText("方式:"+this.getType());
		holder.location.setText("区域:"+this.getLocation());

		if(!TextUtils.isEmpty(needNear)){
			holder.distance.setVisibility(View.VISIBLE);
			holder.distance.setText("距离:"+this.getNeedNear());
		}

		//Item跳转
		holder.item.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if(bidState==1){//不可抢 埋点
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
				MDUtils.servicePageMD(context, "" + cateId, "" + bidId, MDConstans.ACTION_DETAILS);
			}
		});

		/**
		 * edited by chenguangming ==== start
		 * */
		switch (bidState){
			case 1://不可抢
				holder.grab_style.setImageResource(R.drawable.type_back_grabbed);
				holder.iv_renovation_type.setImageResource(R.drawable.iv_renovation_brush);
				holder.view_bottom.setVisibility(View.GONE);
//				holder.knock.setBackgroundResource(R.drawable.t_not_bid_bg);
//				holder.knock.setText("已抢完");
//				holder.knock.setTextColor(context.getResources().getColor(R.color.transparent));
//				holder.knock.setClickable(false);
				break;
			default://可抢
				holder.grab_style.setImageResource(R.drawable.type_back_grab);
				holder.iv_renovation_type.setImageResource(R.drawable.iv_renovation_brush_knock);
				holder.view_bottom.setVisibility(View.VISIBLE);
				holder.knock.setBackgroundResource(R.drawable.bt_knock_button_selector);
				holder.knock.setTextColor(context.getResources().getColor(R.color.white));
				holder.knock.setText("抢单");
				holder.knock.setClickable(true);
				holder.knock.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						adapter.itemClicked(holder.knock.getId(), toPopPassBean());
						MDUtils.servicePageMD(RenovationListBean.this.context, cateId, String.valueOf(bidId),
								MDConstans.ACTION_QIANG_DAN);
					}
				});
				break;
		}
		/**
		 * ==============================end
		 * */

	}

	@SuppressLint("NewApi")
	@Override
	public View initView(View convertView, LayoutInflater inflater, ViewGroup parent, Context context,
			ZBBaseAdapter<QDBaseBean> adapter) {
		if (this.context == null)
			this.context = context;
		holder = new RenovationViewHolder();
		RenovationListBean.this.adapter = adapter;
		convertView = inflater.inflate(R.layout.order_item_renovation, null);
		holder.item =  convertView.findViewById(R.id.renovation_item);
		holder.grab_style= (ImageView) convertView.findViewById(R.id.grab_style);
		holder.iv_renovation_type = (ImageView) convertView.findViewById(R.id.iv_renovation_type);
		holder.title = (TextView) convertView.findViewById(R.id.grab_renovation_title);
		holder.time = (TextView) convertView.findViewById(R.id.grab_renovation_time);
		holder.space = (TextView) convertView.findViewById(R.id.tv_grab_area_title);
		holder.budget = (TextView) convertView.findViewById(R.id.tv_grab_budget_title);
		holder.decorate_type = (TextView) convertView.findViewById(R.id.tv_grab_type_title);
		holder.location = (TextView) convertView.findViewById(R.id.tv_grab_location_title);
        holder.distance = (TextView) convertView.findViewById(R.id.tv_grab_distance_title);

		holder.view_bottom = convertView.findViewById(R.id.view_bottom);
		holder.knock = (Button) convertView.findViewById(R.id.grab_main_knock);
		holder.tvActivePrice = (TextView) convertView.findViewById(R.id.tv_main_fee);
		holder.tvOriginalPrice = (TextView) convertView.findViewById(R.id.tv_main_origin_fee);
		holder.tvOriginalPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);


		convertView.setTag(holder);
		return convertView;
	}

	@Override
	public void converseView(View convertView, Context context,ZBBaseAdapter<QDBaseBean> adapter) {
		this.adapter = adapter;
		holder = (RenovationViewHolder) convertView.getTag();
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
	public String getCateId() {
		return cateId;
	}

}
