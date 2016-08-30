package com.huangyezhaobiao.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huangye.commonlib.vm.callback.StorageVMCallBack;
import com.huangyezhaobiao.R;
import com.huangyezhaobiao.adapter.OtherAdapter;
import com.huangyezhaobiao.bean.push.PushBean;
import com.huangyezhaobiao.bean.push.PushToStorageBean;
import com.huangyezhaobiao.db.DataBaseManager;
import com.huangyezhaobiao.db.DataBaseManager.TABLE_OTHER;
import com.huangyezhaobiao.inter.Constans;
import com.huangyezhaobiao.utils.ActivityUtils;
import com.huangyezhaobiao.utils.HYEventConstans;
import com.huangyezhaobiao.utils.HYMob;
import com.huangyezhaobiao.utils.LogUtils;
import com.huangyezhaobiao.utils.UnreadUtils;
import com.huangyezhaobiao.utils.UserUtils;
import com.huangyezhaobiao.view.ZhaoBiaoDialog;
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
public class OtherDetailActivity extends QBBaseActivity implements OnClickListener,StorageVMCallBack{

	private View    back_layout;
	private TextView txt_head;
	private ListView lv_message_center;
	private OtherAdapter adapter;
//	private Button btn_clean;
	public List<PushToStorageBean> beans = new ArrayList<PushToStorageBean>();
	private int type;
	private DetailMessageListStorageVM svm;
//	private TextView tv_top_center;
//	private ZhaoBiaoDialog dialog;
//	private RelativeLayout rl_back;
	private RelativeLayout rl_no_unread;
	private ImageView iv_unread;
	private TextView tv_no_unread;

	private String Logger_Type; //埋点类型

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_other_message);
		initView();
//		dialog = new ZhaoBiaoDialog(this, getString(R.string.hint),getString(R.string.make_sure_clear_all_message));
//		dialog.setOnDialogClickListener(listener);
		tbl.setVisibility(View.GONE);
		svm = new DetailMessageListStorageVM(this, this);
		type = getIntent().getIntExtra("type", -1);
		configTypeTitle(type);
		svm.setType(String.valueOf(type));
		loadDatas();

	}

	private void configTypeTitle(int type) {
		String title = "";
		String message = "";
		switch (type) {
		case TABLE_OTHER.KOUFEI:
			title = getString(R.string.bidding_result);
			message = "您还没有收到抢单结果提醒哦~";
			iv_unread.setImageResource(R.drawable.result_hint);
			Logger_Type = HYEventConstans.PAGE_RESULT_ALERT;
			break;
		case TABLE_OTHER.DAOJISHI:
			title = getString(R.string.bidding_timeless);
			message = "您还没有收到客户联系提醒哦~";
			iv_unread.setImageResource(R.drawable.contact_hint);
			Logger_Type = HYEventConstans.PAGE_CONTACT_CLIENT;
			break;
		case  TABLE_OTHER.SYSNOTIF:
			title = getString(R.string.bidding_sys_noti);
			message = "您还没有收到通知资讯哦~";
			iv_unread.setImageResource(R.drawable.systeminfo_hint);
			break;

		}
		txt_head.setText(title);
//		tv_top_center.setText(title);
		tv_no_unread.setText(message);
	}

	public  void initView() {
		layout_back_head = getView(R.id.layout_head);
		back_layout      = getView(R.id.back_layout);
		back_layout.setVisibility(View.VISIBLE);
		txt_head         =  getView(R.id.txt_head);
		tbl          = getView(R.id.tbl);
//		tv_top_center = getView(R.id.tv_top_center);
		rl_no_unread  = getView(R.id.rl_no_unread);
		iv_unread = getView(R.id.iv_unread);
		tv_no_unread = getView(R.id.tv_no_unread);
		lv_message_center = getView(R.id.lv_message_center);
		adapter = new OtherAdapter(this);
		lv_message_center.setAdapter(adapter);
//		btn_clean = getView(R.id.btn_clean);
//		btn_clean.setOnClickListener(this);
		back_layout.setOnClickListener(this);

		String isSon = UserUtils.getIsSon(this);
		if (!TextUtils.isEmpty(isSon) && TextUtils.equals("1", isSon)) {
			String rbac = UserUtils.getRbac(this);
			if (!TextUtils.isEmpty(rbac)
					&& TextUtils.equals("1", rbac) || TextUtils.equals("3", rbac)) {

			}else{
				setLIstViewListener();
			}
		}else{
			setLIstViewListener();
		}

	}

	private void setLIstViewListener() {
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

						HYMob.getDataList(OtherDetailActivity.this, HYEventConstans.EVENT_ID_CHECK_ORDER);
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
//		case R.id.btn_clean:
//			dialog.show();
//			break;
		case R.id.back_layout:
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
				case TABLE_OTHER.DAOJISHI:  //客户联系提醒 102
					UnreadUtils.clearDaoJiShiResult(this);
					break;
				case TABLE_OTHER.KOUFEI:  //抢单结果 101
					UnreadUtils.clearQDResult(this);
					break;
				case TABLE_OTHER.SYSNOTIF: //系统消息 103
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
	protected void onStop() {
		super.onStop();

		HYMob.getBaseDataListForPage(this, Logger_Type, stop_time - resume_time);
	}

	//	ZhaoBiaoDialog.onDialogClickListener listener = new ZhaoBiaoDialog.onDialogClickListener() {
//		@Override
//		public void onDialogOkClick() {
//			if(dialog != null){
//				dialog.dismiss();
//			}
//
//			svm.cleanAllDatas();
//		}
//
//		@Override
//		public void onDialogCancelClick() {
//			if(dialog != null){
//				dialog.dismiss();
//			}
//		}
//	};

}
