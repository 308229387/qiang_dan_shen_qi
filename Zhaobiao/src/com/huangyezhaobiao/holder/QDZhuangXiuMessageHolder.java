package com.huangyezhaobiao.holder;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 抢单装修信息的holder
 * @author shenzhixin
 *
 */
public class QDZhuangXiuMessageHolder {
	public TextView tv_project;//装修title
	public TextView tv_time_content; //抢单时间
	public TextView tv_location; //区域
	public TextView tv_decorate_address_content; //详细地址
	public TextView tv_customer_name_content; //客户姓名
	public ImageView iv_decorate_orderstate_line; //订单状态条
	public TextView tv_decotrate_order_state;//订单状态
	public LinearLayout ll; //整个Item

//	public TextView tv_size;//面积
//	public TextView tv_cost;//预算
//	public TextView tv_mode;//装修类型
	//public TextView tv_time;


//	public TextView tv_telephone;//客户电话
//	public TextView tv_message; //已结束
//	public ImageView   btn_alreadry_contact;//电话按钮
//	public ImageView   btn_tel_contact;
//	public RelativeLayout rl_maybe_not;//只有已结束才会出现

}
