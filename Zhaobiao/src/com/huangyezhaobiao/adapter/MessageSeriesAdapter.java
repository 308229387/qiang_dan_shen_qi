package com.huangyezhaobiao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.bean.push.PushToStorageBean;
import com.huangyezhaobiao.utils.TimeUtils;
import com.huangyezhaobiao.utils.UnreadUtils;

import java.util.ArrayList;
import java.util.List;
/**
 * 消息列表界面的adapter
 * @author 58
 *
 */
public class MessageSeriesAdapter extends BaseAdapter{
	private final static int MESSAGE_ITEM_COUNT = 4;
	private List<PushToStorageBean> messageBeans = new ArrayList<PushToStorageBean>();
	private Context context;
	private LayoutInflater mInflater;
	private int[] unread_counts = new int[MESSAGE_ITEM_COUNT-1];
	private int count;
	
	public void setUnreadCounts(int[] count){
		unread_counts = count;
		notifyDataSetChanged();
	}
	
	public MessageSeriesAdapter(Context context){
		this.context = context;
		mInflater = LayoutInflater.from(context);
	}

	public void setDataSources(List<PushToStorageBean> messageBeans){
		this.messageBeans = messageBeans;
		notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		return MESSAGE_ITEM_COUNT-1;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView==null){
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.layout_series_message,parent,false);
			holder.iv_message = (ImageView) convertView.findViewById(R.id.iv_message);
			holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
			holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
			holder.tv_unread = (TextView) convertView.findViewById(R.id.tv_unread);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		PushToStorageBean bean = messageBeans.get(position);
		holder.tv_time.setText(TimeUtils.formatDateTime(bean.getTime()));
		if(unread_counts[position]>0){
			holder.tv_unread.setVisibility(View.VISIBLE);
			holder.tv_unread.setText("" + unread_counts[position]);
			if (unread_counts[position] > 99) {
				holder.tv_unread.setText("99+");
			}
		}else{
			holder.tv_unread.setVisibility(View.INVISIBLE);
		}
		
		switch (position+1) {
		case 0:
			holder.tv_title.setText(R.string.toast_yue);
			break;
		case 1:
			holder.tv_title.setText(R.string.toast_bidding_result);
			holder.iv_message.setImageResource(R.drawable.qd_result);

			if(bean.getStr() == "暂无消息"){
				holder.tv_content.setText(bean.getStr());
			}else {
				if(bean.getStatus() == 1){
					holder.tv_content.setText("抢单成功!" + bean.getStr());
				}else {
					holder.tv_content.setText("抢单失败!" + bean.getStr());
				}
			}

			count = UnreadUtils.getQDResult(context);
			break;
		case 2:
			holder.tv_title.setText(R.string.toast_bidding_timeless);
			holder.iv_message.setImageResource(R.drawable.daojishi);
			holder.tv_content.setText(bean.getStr());
			count = UnreadUtils.getDaoJiShi(context);
			break;
		case 3:
			holder.tv_title.setText(R.string.bidding_sys_noti);
			holder.iv_message.setImageResource(R.drawable.sysnotifi);
			holder.tv_content.setText(bean.getStr());
			count = UnreadUtils.getSysNotiNum(context);
			break;
	
		}
		if(count==0){
			holder.tv_unread.setVisibility(View.GONE);
		}else{
			holder.tv_unread.setVisibility(View.VISIBLE);
			holder.tv_unread.setText(count+"");
		}
		return convertView;
	}
	
	class ViewHolder{
		public ImageView iv_message;
		public TextView  tv_title;
		public TextView  tv_time;
		public TextView  tv_content;
		private TextView tv_unread;
	}

	public void setEmpty(int i) {
		if(i<4){
			unread_counts[i-1]=0;
			notifyDataSetChanged();
		}
	}

}
