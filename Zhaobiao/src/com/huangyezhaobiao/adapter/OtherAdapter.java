package com.huangyezhaobiao.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.bean.push.PushToStorageBean;
import com.huangyezhaobiao.inter.Constans;
import com.huangyezhaobiao.utils.LogUtils;
import com.huangyezhaobiao.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

public class OtherAdapter extends BaseAdapter{
	private Context context;
	private LayoutInflater mInflater;
	private List<PushToStorageBean> beans = new ArrayList<PushToStorageBean>();
	public OtherAdapter(Context context){
		this.context = context;
		mInflater = LayoutInflater.from(context);
	}
	public void setDataSources(List<PushToStorageBean> beans){
		this.beans = beans;
		notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		return beans.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.layout_other_item, parent,false);
			holder.tv_bg_title = (TextView) convertView.findViewById(R.id.tv_bg_title);
			holder.tv_time     = (TextView) convertView.findViewById(R.id.tv_time);
			holder.tv_qd_result = (TextView) convertView.findViewById(R.id.tv_qd_result);
			holder.rl_more = (RelativeLayout) convertView.findViewById(R.id.rl_more);
			holder.tv_more = (TextView) convertView.findViewById(R.id.tv_more);
			holder.tv_order_number = (TextView) convertView.findViewById(R.id.tv_order_number);
			holder.tv_noouejd = (TextView) convertView.findViewById(R.id.tv_noouejd);
			holder.view   = convertView.findViewById(R.id.view);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		PushToStorageBean messageBean = beans.get(position);
		LogUtils.LogE("efefefefef", "messageBean:" + messageBean.toString());
		String fee = messageBean.getFee();
		LogUtils.LogE("efefefefef", "fee:" + fee);
		int status = messageBean.getStatus();
		int tag = messageBean.getTag();
		LogUtils.LogE("ashenhhhh", "tag:" + tag);
		switch (tag) {
		case Constans.QD_RESULT:
			if(status == 1){
				holder.tv_qd_result.setText(R.string.bidding_success);
				holder.view.setVisibility(View.VISIBLE);
				holder.rl_more.setVisibility(View.VISIBLE);
				holder.tv_noouejd.setVisibility(View.VISIBLE);
				holder.tv_noouejd.setText("订单编号  " + messageBean.getOrderid());
				holder.tv_order_number.setVisibility(View.VISIBLE);
				
			}else{
				holder.tv_qd_result.setText(R.string.bidding_failure);
				holder.view.setVisibility(View.GONE);
				holder.tv_noouejd.setVisibility(View.GONE);
				holder.rl_more.setVisibility(View.GONE);
				holder.tv_order_number.setVisibility(View.INVISIBLE);
			}
			//fix bug ui
			if(!TextUtils.isEmpty(fee)){
				holder.tv_order_number.setVisibility(View.VISIBLE);
				holder.tv_order_number.setText("订单价格  "  +fee +"元");
			}else{
				holder.tv_order_number.setVisibility(View.INVISIBLE);
			}
			//fix bug ui
			LogUtils.LogE("ashjhj", "status:" + status);
			holder.tv_time.setText(TimeUtils.formatDateTime(messageBean.getTime()));
			String mss = messageBean.getStr();
			LogUtils.LogE("post", "mss:" + mss);
			if(mss.length()>31)
				holder.tv_bg_title.setText("订单信息  " + mss.substring(0, 31));
			else {
				holder.tv_bg_title.setText("订单信息  " + mss);
			}
			break;
		case Constans.QD_DAOJISHI://倒计时
			holder.tv_time.setText(TimeUtils.formatDateTime(messageBean.getTime()));
			holder.tv_qd_result.setText(R.string.daojishi_hint);
			holder.tv_qd_result.setTextColor(Color.parseColor("#808080"));
			holder.tv_noouejd.setText("订单编号  " + messageBean.getOrderid());
			holder.tv_bg_title.setText("客户姓名  " + messageBean.getGuestName());
			holder.view.setVisibility(View.VISIBLE);
			holder.rl_more.setVisibility(View.VISIBLE);
			holder.tv_more.setText("查看详情");
			holder.tv_noouejd.setVisibility(View.VISIBLE);
			holder.tv_order_number.setVisibility(View.GONE);

			break;
		case Constans.SYS_NOTI://倒计时
			holder.tv_bg_title.setText(messageBean.getStr());
			holder.tv_noouejd.setText(context.getString(R.string.order_number)+messageBean.getOrderid());
			holder.view.setVisibility(View.GONE);
			holder.rl_more.setVisibility(View.GONE);
			holder.tv_noouejd.setVisibility(View.GONE);
			holder.tv_order_number.setVisibility(View.GONE);
			holder.tv_qd_result.setVisibility(View.GONE);
			holder.tv_time.setText(messageBean.getTime());
			break;

		}

		LogUtils.LogE("asasasas", "msg:" + messageBean.getStr());
		return convertView;
	}
	
	class ViewHolder{
		public TextView tv_time;
		public TextView tv_bg_title;
		public TextView tv_qd_result;
		public TextView tv_order_number;
		public TextView tv_noouejd;
		public RelativeLayout rl_more;
		public TextView tv_more;
		public View view;
	}
	
	

}
