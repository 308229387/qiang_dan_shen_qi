package com.huangyezhaobiao.bean.poplist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.activity.OrderDetailActivity;
import com.huangyezhaobiao.bean.push.PushToPassBean;
import com.huangyezhaobiao.holder.AffiliateGrabHolder;
import com.huangyezhaobiao.inter.MDConstans;
import com.huangyezhaobiao.lib.QDBaseBean;
import com.huangyezhaobiao.lib.ZBBaseAdapter;
import com.huangyezhaobiao.utils.BDEventConstans;
import com.huangyezhaobiao.utils.BDMob;
import com.huangyezhaobiao.utils.HYEventConstans;
import com.huangyezhaobiao.utils.HYMob;
import com.huangyezhaobiao.utils.MDUtils;

/**
 * 主列表页的招商加盟Listbean
 * @author shenzhixin
 * {
				"pushId":"11112222",
				"pushTurn":"1",
				"cateId":"4065",
				"displayType":"4",
				"bidId":"12312321",	
				"title":"招商加盟-餐饮加盟-火锅",
				"time":"2015-05-15 18:49",
				"budget":"20万",
				"investKeywords":"有品牌，回本快", 	//投资意向关键词
				"bidState":"0"					//订单状态,如可抢\已经被抢
	}
 *
 */
public class AffiliatesListBean extends QDBaseBean{
	private long pushId;
	private int pushTurn;
	private int displayType;
	private long bidId;
	private int bidState;
	private String title;
	private String time;
	private String budget;
	private String investKeywords;
	private AffiliateGrabHolder holder;
	@Override
	public String getCateId() {
		return cateId;
	}
	
	

	@Override
	public String toString() {
		return "AffiliatesListBean [pushId=" + pushId + ", pushTurn="
				+ pushTurn + ", displayType=" + displayType + ", bidId="
				+ bidId + ", bidState=" + bidState + ", title=" + title
				+ ", time=" + time + ", budget=" + budget + ", investKeywords="
				+ investKeywords + "]";
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



	public long getBidId() {
		return bidId;
	}



	public void setBidId(long bidId) {
		this.bidId = bidId;
	}



	public int getBidState() {
		return bidState;
	}



	public void setBidState(int bidState) {
		this.bidState = bidState;
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



	public String getBudget() {
		return budget;
	}



	public void setBudget(String budget) {
		this.budget = budget;
	}



	public String getInvestKeywords() {
		return investKeywords;
	}



	public void setInvestKeywords(String investKeywords) {
		this.investKeywords = investKeywords;
	}



	@Override
	public int getDisplayType() {
		return displayType;
	}

	public void setDisplayType(int displayType) {
		this.displayType = displayType;
	}
	
	@Override
	public void setCateId(String cateId) {
		this.cateId = cateId;
	}

	@Override
	public int getLayoutId() {
		return R.layout.layout_affiliates_list_bean;
	}

	@Override
	public void fillDatas() {
		holder.grab_affiliates_title.setText(title);
		holder.grab_affiliates_time.setText(time);
		holder.grab_affiliates_investKeywords.setText(investKeywords);
		holder.grab_affiliates_budget.setText(budget);
		holder.affiliates_item.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
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
				MDUtils.servicePageMD(context, cateId, String.valueOf(bidId), MDConstans.ACTION_DETAILS);
			}
		});

		if (bidState == 1) {
			holder.grab_affiliates_knock.setImageResource(R.drawable.t_bid_gone);
			holder.grab_affiliates_knock.setClickable(false);
		} else {
			holder.grab_affiliates_knock.setImageResource(R.drawable.t_knock);
			holder.grab_affiliates_knock.setClickable(true);
			holder.grab_affiliates_knock.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					adapter.itemClicked(holder.grab_affiliates_knock.getId(), toPopPassBean());
					MDUtils.servicePageMD(AffiliatesListBean.this.context, cateId, String.valueOf(bidId),
							MDConstans.ACTION_QIANG_DAN);
				}
			});
		}
	}

	@Override
	public View initView(View convertView, LayoutInflater inflater,
			ViewGroup parent, Context context, ZBBaseAdapter<QDBaseBean> adapter) {
		this.context = context;
		this.adapter = adapter;
		convertView = inflater.inflate(getLayoutId(), parent, false);
		holder = new AffiliateGrabHolder();
		holder.affiliates_item = convertView.findViewById(R.id.affiliates_item);
		holder.grab_affiliates_budget = (TextView) convertView.findViewById(R.id.grab_affiliates_budget);
		holder.grab_affiliates_investKeywords = (TextView) convertView.findViewById(R.id.grab_affiliates_investKeywords);
		holder.grab_affiliates_knock = (ImageView) convertView.findViewById(R.id.grab_affiliates_knock);
		holder.grab_affiliates_time = (TextView) convertView.findViewById(R.id.grab_affiliates_time);
		holder.grab_affiliates_title = (TextView) convertView.findViewById(R.id.grab_affiliates_title);
		convertView.setTag(holder);
		return convertView;
	}

	@Override
	public void converseView(View convertView, Context context,
			ZBBaseAdapter<QDBaseBean> adapter) {
		this.context = context;
		this.adapter = adapter;
		holder = (AffiliateGrabHolder) convertView.getTag();

	}


	public PushToPassBean toPopPassBean() {
		PushToPassBean bean = new PushToPassBean();
		bean.setBidId(bidId);
		bean.setPushId(pushId);
		bean.setPushTurn(pushTurn);
		bean.setCateId(Integer.parseInt(cateId));
		return bean;
	}

}
