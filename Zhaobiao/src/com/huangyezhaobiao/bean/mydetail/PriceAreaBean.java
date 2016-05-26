package com.huangyezhaobiao.bean.mydetail;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.bean.popdetail.QDDetailBaseBean;
import com.huangyezhaobiao.eventbus.EventAction;
import com.huangyezhaobiao.eventbus.EventType;
import com.huangyezhaobiao.eventbus.EventbusAgent;
import com.huangyezhaobiao.utils.BDEventConstans;
import com.huangyezhaobiao.utils.BDMob;
import com.huangyezhaobiao.utils.HYEventConstans;
import com.huangyezhaobiao.utils.HYMob;

/**
 * 订单详情的价格区间的bean
 * @author shenzhixin
 *
 */
public class PriceAreaBean extends QDDetailBaseBean{
	private String name;
	private String orderNum;
	private String orderFee;
	private String originalFee;
	private String refundState;
	private TextView tv_last_cost_content;
	private TextView tv_last_number_content;
	private TextView tv_original_fee;
	private View     rl_tuidan;
	private Context  context;

	public String getOriginalFee() {
		return originalFee;
	}

	public void setOriginalFee(String originalFee) {
		this.originalFee = originalFee;
	}

	@Override
	public View initView(Context context) {
		this.context           = context;
		View view 			   = LayoutInflater.from(context).inflate(R.layout.layout_qiangdan_last, null);
		tv_last_cost_content   = (TextView) view.findViewById(R.id.tv_last_cost_content);
		tv_last_number_content = (TextView) view.findViewById(R.id.tv_last_number_content);
		rl_tuidan    		   = view.findViewById(R.id.rl_tuidan);
		tv_original_fee        = (TextView) view.findViewById(R.id.tv_original_fee);
		fillDatas();
		return view;
	}
	
	private void fillDatas()
	{
		Log.e("shenzhixinUUU", "state:" + refundState);
		//refundState = "1";//for test
		//orderFee = originalFee; for test
		tv_last_number_content.setText(orderNum);
		//orderFee是活动价，当原价与活动价相等时，显示活动价，隐藏原价
		tv_last_cost_content.setText(orderFee);
		if(!TextUtils.isEmpty(originalFee)) {
			tv_original_fee.setText(originalFee);
		}
		tv_original_fee.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		if(TextUtils.equals(orderFee,originalFee)){
			tv_original_fee.setVisibility(View.GONE);
		}
		if(refundState==null){
			rl_tuidan.setVisibility(View.GONE);
		}
		if(!TextUtils.isEmpty(refundState)){
		switch (refundState) {
			case "-1"://不能退单
			case "":
				rl_tuidan.setVisibility(View.GONE);
				break;
			case "0"://退单时间允许之外
				rl_tuidan.setVisibility(View.VISIBLE);
				break;
			case "1"://第一次提交
				rl_tuidan.setVisibility(View.VISIBLE);
				break;
			case "2"://补交
				rl_tuidan.setVisibility(View.VISIBLE);
				break;
			case "3"://退单结果
				rl_tuidan.setVisibility(View.VISIBLE);
				break;
		}
		}
		rl_tuidan.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//点击了退单入口按钮
				BDMob.getBdMobInstance().onMobEvent(context, BDEventConstans.EVENT_ID_ORDER_DETAIL_REFUND);

				HYMob.getDataListByRefund(context, HYEventConstans.EVENT_ID_ORDER_LIST_PHONE, String.valueOf(orderId));


				Log.e("shenzhixin","hahahah state:"+refundState);
				EventAction action = new EventAction(EventType.REGISTER_SUCCESS);
				//用EventBus导出去
				switch (refundState){
					case "0"://退单时间允许之外
						action.type = EventType.EVENT_TUIDAN_NOT_TIME;
						break;
					case "1"://第一次提交
						action.type = EventType.EVENT__FIRST_SUBMIT_TUIDAN;
						break;
					case "2"://补交
						action.type = EventType.EVENT_ADD_TUIDAN;
						break;
					case "3"://退单结果
						action.type = EventType.EVENT_TUIDAN_RESULT;
						break;
				}
				EventbusAgent.getInstance().post(action);
			}
		});
	}

	public String getRefundState() {
		return refundState;
	}

	public void setRefundState(String refundState) {
		this.refundState = refundState;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	public String getOrderFee() {
		return orderFee;
	}
	public void setOrderFee(String orderFee) {
		this.orderFee = orderFee;
	}
	public TextView getTv_last_cost_content() {
		return tv_last_cost_content;
	}
	public void setTv_last_cost_content(TextView tv_last_cost_content) {
		this.tv_last_cost_content = tv_last_cost_content;
	}
	public TextView getTv_last_number_content() {
		return tv_last_number_content;
	}
	public void setTv_last_number_content(TextView tv_last_number_content) {
		this.tv_last_number_content = tv_last_number_content;
	}

	
	public void setNewType(String newType){
		super.newtype = newType;
	}
}
