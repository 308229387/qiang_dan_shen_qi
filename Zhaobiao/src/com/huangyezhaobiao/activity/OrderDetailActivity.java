package com.huangyezhaobiao.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huangye.commonlib.vm.callback.NetWorkVMCallBack;
import com.huangyezhaobiao.R;
import com.huangyezhaobiao.bean.popdetail.BottomViewBean;
import com.huangyezhaobiao.bean.popdetail.LogBean;
import com.huangyezhaobiao.bean.push.PushBean;
import com.huangyezhaobiao.bean.push.PushToPassBean;
import com.huangyezhaobiao.constans.AppConstants;
import com.huangyezhaobiao.enums.TitleBarType;
import com.huangyezhaobiao.inter.MDConstans;
import com.huangyezhaobiao.inter.OrderDetailCallBack;
import com.huangyezhaobiao.utils.BDEventConstans;
import com.huangyezhaobiao.utils.BDMob;
import com.huangyezhaobiao.utils.HYEventConstans;
import com.huangyezhaobiao.utils.HYMob;
import com.huangyezhaobiao.utils.MDUtils;
import com.huangyezhaobiao.utils.PushUtils;
import com.huangyezhaobiao.view.TitleMessageBarLayout;
import com.huangyezhaobiao.view.ZhaoBiaoDialog;
import com.huangyezhaobiao.view.ZhaoBiaoDialog.onDialogClickListener;
import com.huangyezhaobiao.vm.KnockViewModel;
import com.huangyezhaobiao.vm.QIangDanDetailViewModel;

import org.w3c.dom.Text;

import java.util.List;

/**
 * 可抢订单详情页
 * 
 * @author linyueyang
 * 
 */
public class OrderDetailActivity extends QBBaseActivity implements NetWorkVMCallBack, OrderDetailCallBack {
	private LinearLayout back_layout;
	private Button done;
	private ZhaoBiaoDialog zbdialog;
	private LinearLayout linear;// 可抢单详情页 用于addview
	private QIangDanDetailViewModel viewModel;
	private TextView fee;
	private TextView discountFee;
	private PushToPassBean popPass;
	private KnockViewModel knockViewModel;
	private TextView txt_head;
	private int bidState;
	private PushToPassBean receivePassBean;
	private View           rl_qd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_orderdetail);
		initView();
		initListener();
		Intent intent = this.getIntent(); 
		receivePassBean=(PushToPassBean)intent.getSerializableExtra("passBean");
		//long bidId = receivePassBean.getBidId();
		viewModel = new QIangDanDetailViewModel(this, this, this);
		viewModel.getdata(receivePassBean);
	}

	@Override
	public void initView() {
		rl_qd            = getView(R.id.rl_qd);
		discountFee      = getView(R.id.discountFee);
		layout_back_head = getView(R.id.layout_head);
		back_layout = (LinearLayout) this.findViewById(R.id.back_layout);
		done = (Button) this.findViewById(R.id.done);
		tbl = (TitleMessageBarLayout) findViewById(R.id.tbl);
		linear = (LinearLayout) findViewById(R.id.llll);
		fee = (TextView) findViewById(R.id.fee);
		txt_head = (TextView) findViewById(R.id.txt_head);
		txt_head.setText(R.string.details);
	}

	@SuppressLint("ResourceAsColor")
	@Override
	public void initListener() {
		back_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				OrderDetailActivity.this.finish();
			}
		});
	}

	@Override
	public void onNotificationCome(PushBean pushBean) {
		if (null != pushBean) {
			tbl.setPushBean(pushBean);
			tbl.setVisibility(View.VISIBLE);
			if(pushBean.getTag()==100){
				PushUtils.pushList.clear();
			}
		}
	}

	@Override
	public void onTitleBarClicked(TitleBarType type) {

	}

	@Override
	public void NetConnected() {
		if (tbl != null && tbl.getType() == TitleBarType.NETWORK_ERROR) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					tbl.setVisibility(View.GONE);
				}
			});
		}
	}

	@Override
	public void NetDisConnected() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (tbl != null) {
					tbl.showNetError();
					tbl.setVisibility(View.VISIBLE);
				}
			}
		});
	}

	@Override
	public void onLoadingStart() {
		startLoading();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onLoadingSuccess(Object t) {
		stopLoading();
		if (t instanceof Integer) {
			rl_qd.setVisibility(View.GONE);
			int status = (Integer) t;
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putSerializable("passBean", popPass);
			intent.putExtras(bundle);
			
			if(status==3){
				intent.setClass(OrderDetailActivity.this, BidSuccessActivity.class);
				startActivity(intent);
			}
			else if(status==1){
				intent.setClass(OrderDetailActivity.this, BidGoneActivity.class);
				startActivity(intent);
			}
			else if(status==2) {
				
				zbdialog = new ZhaoBiaoDialog(this, getString(R.string.hint), getString(R.string.not_enough_balance));
				zbdialog.setCancelButtonGone();
				zbdialog.setOnDialogClickListener(new onDialogClickListener() {
					
					@Override
					public void onDialogOkClick() {
						zbdialog.dismiss();
						
					}
					
					@Override
					public void onDialogCancelClick() {
						// TODO Auto-generated method stub
						
					}
				});
				zbdialog.show();
				MDUtils.YuENotEnough("", "");
			}
			else if(status==4){
				Toast.makeText(OrderDetailActivity.this,getString(R.string.bidding_already_bid), Toast.LENGTH_SHORT).show();
			}else if(status==5){
				Toast.makeText(OrderDetailActivity.this,"抢单并没有成功", Toast.LENGTH_SHORT).show();
				intent.setClass(OrderDetailActivity.this, BidFailureActivity.class);
				startActivity(intent);
			}
			else{
				Toast.makeText(OrderDetailActivity.this,getString(R.string.bidding_exception), Toast.LENGTH_SHORT).show();;
			}
		} else {

			// 这里把bean返回的List<View> 添加到对应位置的linearlayout里面
			// 当然addView是不能margin和padding的

			List<View> viewList = (List<View>) t;

			for (View v : viewList) {
				linear.addView(v);
			}
		}
	}

	@Override
	public void onLoadingError(String msg) {
		stopLoading();
		if(!TextUtils.isEmpty(msg))
			Toast.makeText(this, msg,Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onLoadingCancel() {
		stopLoading();
	}

	@Override
	public void onNoInterNetError() {

	}

	@Override
	public void onLoginInvalidate() {
		showExitDialog();
	}

	@SuppressLint("ResourceAsColor")
	@Override
	public void back(LogBean log, final BottomViewBean bottom) {
 
		popPass = log.toPopPassBean();
		bidState = log.getBidState();
		if (bidState == 0) {
			done.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
							try {
								BDMob.getBdMobInstance().onMobEvent(OrderDetailActivity.this, BDEventConstans.EVENT_ID_BIDDING_DETAIL_PAGE_BIDDING);

								HYMob.getDataListForQiangdan(OrderDetailActivity.this,HYEventConstans.EVENT_ID_BIDDING_DETAIL_PAGE_BIDDING, String.valueOf(popPass.getBidId()), "2");

								rl_qd.setVisibility(View.VISIBLE);
								knockViewModel = new KnockViewModel(OrderDetailActivity.this, OrderDetailActivity.this);
								knockViewModel.knock(popPass, AppConstants.BIDSOURCE_DETAIL);
								MDUtils.bidDetailsPageMD(OrderDetailActivity.this, "" + bidState,
										popPass.getCateId() + "", popPass.getBidId() + "", MDConstans.ACTION_QIANG_DAN);
							} catch (Exception e) {

					}
				}
			});
		} else {
			done.setBackgroundColor(getResources().getColor(R.color.whitedark));
			done.setText(R.string.bidding_finish);
		}
		//bottom.setPrevilage(bottom.getOriginFee());//for test
		//如果活动价与原价一样，那么就只显示原价不显示活动价，原价也不划横线
		if(TextUtils.equals(bottom.getPrevilage(),bottom.getOriginFee())){//活动价为空,不显示活动价
			discountFee.setText(bottom.getPrevilage());
			//discountFee的marginTop为15dp,ui好看 shenzhixin add 2016.3.28
			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) discountFee.getLayoutParams();
			params.topMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,15,getResources().getDisplayMetrics());
			discountFee.setLayoutParams(params);
			//shenzhixin add 2016.3.28 end
			fee.setVisibility(View.GONE);
		}else {
			//fee是原价
			fee.setText(bottom.getOriginFee());
			fee.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
			//discountFee是活动价
			discountFee.setText(bottom.getPrevilage());
		}
	}


	@Override
	protected void onStop() {
		super.onStop();
		HYMob.getBaseDataListForPage(OrderDetailActivity.this, HYEventConstans.PAGE_BINDING_DETAIL, stop_time - resume_time);
	}
}
