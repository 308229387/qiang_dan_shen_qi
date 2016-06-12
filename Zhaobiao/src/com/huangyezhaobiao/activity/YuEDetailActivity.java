package com.huangyezhaobiao.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.huangye.commonlib.vm.callback.StorageVMCallBack;
import com.huangyezhaobiao.R;
import com.huangyezhaobiao.bean.MessageBean;
import com.huangyezhaobiao.bean.push.PushBean;
import com.huangyezhaobiao.enums.TitleBarType;
import com.huangyezhaobiao.vm.DetailMessageListStorageVM;

import java.util.ArrayList;
import java.util.List;

/**
 * 余额提醒界面
 * @author shenzhixin
 *
 */
public class YuEDetailActivity extends QBBaseActivity implements OnClickListener, StorageVMCallBack{
	private Button   btn_clean;
	private ListView lv_message_center;
	private YuEAdapter adapter ;
	private List<MessageBean> beans = new ArrayList<MessageBean>();
	private DetailMessageListStorageVM vm;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_center_message);
		initView();
		initListener();
		vm = new DetailMessageListStorageVM(this, this);
		vm.setType("0");
		vm.getDate();
	}
	
	
	public void initListener(){
		btn_clean.setOnClickListener(this);
	}

	public void initView() {
		layout_back_head = getView(R.id.layout_head);
		tbl               = getView(R.id.tbl);
		btn_clean         = getView(R.id.btn_clean);
		lv_message_center = getView(R.id.lv_message_center);
		lv_message_center.setDivider(null);
		adapter = new YuEAdapter();
		lv_message_center.setAdapter(adapter);
		
	}
	
	
	
	 private class YuEAdapter extends BaseAdapter{
	    	private List<MessageBean> beans = new ArrayList<MessageBean>();
	    	public void setSources(List<MessageBean> beans){
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
				if(convertView==null){
					holder = new ViewHolder();
					convertView = LayoutInflater.from(YuEDetailActivity.this).inflate(R.layout.layout_db_item, parent,false);
					holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
					holder.tv_bg_title = (TextView) convertView.findViewById(R.id.tv_bg_title);
					holder.tv_bg_content = (TextView) convertView.findViewById(R.id.tv_bg_content);
					convertView.setTag(holder);
				}else{
					holder = (ViewHolder) convertView.getTag();
				}
				MessageBean bean = beans.get(position);
				holder.tv_bg_title.setText(bean.getMessage());
				holder.tv_time.setText(bean.getTime());
				return convertView;
			}
	    	
			
			class ViewHolder{
				public TextView tv_time,tv_bg_title,tv_bg_content;
			}
	    }



	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btn_clean:
			
			
			break;

		}
	}




	@Override
	public void getDataSuccess(Object o) {
		beans = (List<MessageBean>) o;
		if (beans != null && beans.size() > 0){
			adapter.setSources(beans);
		}

	}




	@Override
	public void getDataFailure() {
		
	}




	@Override
	public void insertDataSuccess() {
		
	}




	@Override
	public void insertDataFailure() {
		
	}




	@Override
	public void deleteDataSuccess() {
		
	}




	@Override
	public void deleteDataFailure() {
		
	}


	@Override
	public void onNotificationCome(PushBean pushBean) {
		
	}


	@Override
	public void onTitleBarClicked(TitleBarType type) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void NetConnected() {
		
	}


	@Override
	public void NetDisConnected() {
		
	}


	
}
