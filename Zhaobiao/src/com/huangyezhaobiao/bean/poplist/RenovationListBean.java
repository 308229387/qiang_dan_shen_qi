package com.huangyezhaobiao.bean.poplist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
import com.huangyezhaobiao.utils.MDUtils;

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
	private String originalPrice; //原价
	private String activePrice;//活动价

	private int bidState;

	private RenovationViewHolder holder;

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

	public void setOriginalPrice(String originalPrice){
		this.originalPrice = originalPrice;
	}

	public String getOriginalPrice(){return originalPrice;}

	public void setActivePrice(String activePrice){this.activePrice = activePrice;}

	public String getActivePrice(){return activePrice;}

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
		return R.layout.adapter_item_order;
	}

	@Override
	public void fillDatas() {
		if (bidState == 1) {
			//holder.knock.setImageResource(R.drawable.t_bid_gone);
			holder.knock.setBackgroundColor(RenovationListBean.this.context.getResources().getColor(R.color.whitedark));
			holder.knock.setClickable(false);//避免setListener已经设置了,bean被复用了，导致不可抢时也可以点击
			holder.knock.setText("已抢完");
		}
		else {
			try {
				BDMob.getBdMobInstance().onMobEvent(RenovationListBean.this.context, BDEventConstans.EVENT_ID_BIDDING_DETAIL_PAGE_BIDDING);
				holder.knock.setClickable(true);
				holder.knock.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					RenovationListBean.this.adapter.itemClicked(holder.knock.getId(), toPopPassBean());
					MDUtils.servicePageMD(RenovationListBean.this.context, "" + cateId, "" + bidId, MDConstans.ACTION_QIANG_DAN);
				}
			});
			} catch (Exception e) {
			}
		}
		holder.title.setText(this.getTitle());
		holder.time.setText(this.getTime());
		
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
		Log.e("shenzhixin", "zhuangxiu,space:" + getSpace() + ",budget:" + getBudget() + ",type:" + getType() + ",endTime:" + getEndTime() + ",location:" + getLocation());
		holder.space.setText("面积:"+this.getSpace());
		holder.budget.setText("预算:"+this.getBudget());
		holder.decorate_type.setText("装修方式:"+this.getType());
		holder.location.setText("区域:"+this.getLocation());

		holder.item.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(bidState==1){//不可抢
					BDMob.getBdMobInstance().onMobEvent(context, BDEventConstans.EVENT_ID_BIDDING_LIST_TO_DETAIL_UNABLE_BIDDING);
				}else{
					BDMob.getBdMobInstance().onMobEvent(context, BDEventConstans.EVENT_ID_BIDDING_LIST_TO_DETAIL_ENABLE_BIDDING);
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
		holder.title = (TextView) convertView.findViewById(R.id.grab_renovation_title);
		holder.time = (TextView) convertView.findViewById(R.id.grab_renovation_time);
		holder.space = (TextView) convertView.findViewById(R.id.tv_grab_area_title);
		holder.budget = (TextView) convertView.findViewById(R.id.tv_grab_budget_title);
		holder.decorate_type = (TextView) convertView.findViewById(R.id.tv_grab_type_title);
		holder.location = (TextView) convertView.findViewById(R.id.tv_grab_location_title);
		holder.knock = (TextView) convertView.findViewById(R.id.grab_renovation_knock);
		holder.tvActivePrice = (TextView) convertView.findViewById(R.id.tv_grab_curprice);
		holder.tvOriginalPrice = (TextView) convertView.findViewById(R.id.tv_grab_oldprice);

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
