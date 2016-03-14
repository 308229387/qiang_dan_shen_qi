package com.huangyezhaobiao.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huangye.commonlib.vm.callback.StorageVMCallBack;
import com.huangyezhaobiao.R;
import com.huangyezhaobiao.adapter.OtherAdapter;
import com.huangyezhaobiao.bean.push.PushBean;
import com.huangyezhaobiao.bean.push.PushToStorageBean;
import com.huangyezhaobiao.db.DataBaseManager.TABLE_OTHER;
import com.huangyezhaobiao.inter.Constans;
import com.huangyezhaobiao.utils.ActivityUtils;
import com.huangyezhaobiao.utils.LogUtils;
import com.huangyezhaobiao.utils.UnreadUtils;
import com.huangyezhaobiao.view.ZhaoBiaoDialog;
import com.huangyezhaobiao.view.ZhaoBiaoDialog.onDialogClickListener;
import com.huangyezhaobiao.vm.DetailMessageListStorageVM;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * 其他三个读取数据库展示的列表页
 * 余额提醒，抢单提醒，倒计时提醒，系统通知的列表页
 * @author shenzhx
 *
 */
public class OtherDetailActivity extends QBBaseActivity implements OnClickListener,StorageVMCallBack, onDialogClickListener{
	private ListView lv_message_center;
	private OtherAdapter adapter;
	private Button btn_clean;
	public List<PushToStorageBean> beans = new ArrayList<PushToStorageBean>();
	private int type;
	private DetailMessageListStorageVM svm;
	private TextView tv_top_center;
	private ZhaoBiaoDialog dialog;
	private RelativeLayout rl_back;
	private RelativeLayout rl_no_unread;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_other_message);
		initView();
		dialog = new ZhaoBiaoDialog(this, getString(R.string.hint),getString(R.string.make_sure_clear_all_message));
		dialog.setOnDialogClickListener(this);
		tbl.setVisibility(View.GONE);
		svm = new DetailMessageListStorageVM(this, this);
		type = getIntent().getIntExtra("type", -1);
		configTypeTitle(type);
		svm.setType(String.valueOf(type));
		loadDatas();

	}

	private void configTypeTitle(int type) {
		String title = "";
		switch (type) {
		case TABLE_OTHER.KOUFEI:
			title = getString(R.string.bidding_result);
			break;
		case TABLE_OTHER.DAOJISHI:
			title = getString(R.string.bidding_timeless);
			break;
		case  TABLE_OTHER.SYSNOTIF:
			title = getString(R.string.bidding_sys_noti);
			break;

		}
		tv_top_center.setText(title);
	}

	public  void initView() {
		layout_back_head = getView(R.id.layout_head);
		tbl          = getView(R.id.tbl);
		rl_back      = getView(R.id.rl_back);
		tv_top_center = getView(R.id.tv_top_center);
		rl_no_unread  = getView(R.id.rl_no_unread);
		lv_message_center = getView(R.id.lv_message_center);
		adapter = new OtherAdapter(this);
		lv_message_center.setAdapter(adapter);
		btn_clean = getView(R.id.btn_clean);
		btn_clean.setOnClickListener(this);
		rl_back.setOnClickListener(this);
		lv_message_center.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				PushToStorageBean pushToStorageBean = beans.get(position);
				int state = pushToStorageBean.getStatus();
				String orderId = String.valueOf(pushToStorageBean.getOrderid());
				HashMap<String, String> map = new HashMap<String, String>();
				//跳转到不同的页面
				switch (type) {
					case TABLE_OTHER.DAOJISHI:
						map.put(Constans.ORDER_ID, orderId);
						ActivityUtils.goToActivityWithString(OtherDetailActivity.this, FetchDetailsActivity.class, map);
						break;
					case TABLE_OTHER.KOUFEI:
						if (state == 1) {
							map.put(Constans.ORDER_ID, orderId);
							ActivityUtils.goToActivityWithString(OtherDetailActivity.this, FetchDetailsActivity.class, map);
						}
						break;
					case TABLE_OTHER.SYSNOTIF:
						break;

				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_clean:
			dialog.show();
			break;
		case R.id.rl_back:
			onBackPressed();
			break;
		}
	}


	@Override
	public void getDataSuccess(final Object o) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				beans = (List<PushToStorageBean>) o;
				LogUtils.LogE("szxMessage", "beans:" + (beans == null));
				if (beans != null) {
					Collections.reverse(beans);
					LogUtils.LogE("szxMessage", "size:" + beans.size());
					adapter.setDataSources(beans);
					if (beans.size() == 0) {
						lv_message_center.setVisibility(View.GONE);
						rl_no_unread.setVisibility(View.VISIBLE);

					} else {
						lv_message_center.setVisibility(View.VISIBLE);
						rl_no_unread.setVisibility(View.GONE);
					}
				} else {
					lv_message_center.setVisibility(View.GONE);
					rl_no_unread.setVisibility(View.VISIBLE);
				}
			}
		});

	}

	@Override
	public void getDataFailure() {

	}

	@Override
	public void insertDataSuccess() {
	}

	@Override
	public void insertDataFailure() {
		Toast.makeText(this, getString(R.string.insert_data_failure), Toast.LENGTH_SHORT).show();
	}

	@Override
	public void deleteDataSuccess() {
		LogUtils.LogE("ashen", "deleteSuccess");
		loadDatas();
	}

	private void loadDatas() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				svm.getDate();
			}
		}).start();
	}

	@Override
	public void deleteDataFailure() {
		Toast.makeText(this, getString(R.string.clear_data_failure), Toast.LENGTH_SHORT).show();
	}

	@Override
	public void initListener() {
		
	}

	@Override
	public void onNotificationCome(PushBean pushBean) {
		if(null != pushBean){
			int push_type = pushBean.getTag();
			if(type == push_type){
				loadDatas();
				switch (type) {
				case TABLE_OTHER.DAOJISHI:
					UnreadUtils.clearDaoJiShiResult(this);
					break;
				case TABLE_OTHER.KOUFEI:
					UnreadUtils.clearQDResult(this);
					break;
				case TABLE_OTHER.SYSNOTIF:
					UnreadUtils.clearSysNotiNum(this);
					break;
				default:
					break;
				}
			}else{
				super.onNotificationCome(pushBean);
			}
		}
	
	}

	@Override
	public void onDialogOkClick() {
		if(exitDialog!=null && exitDialog.isShowing()){
			super.onDialogOkClick();
			return;
		}
		dialog.dismiss();
		svm.cleanAllDatas();
	}

	@Override
	public void onDialogCancelClick() {
		dialog.dismiss();
	}



}
