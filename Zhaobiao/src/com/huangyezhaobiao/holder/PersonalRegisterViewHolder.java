package com.huangyezhaobiao.holder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public final class PersonalRegisterViewHolder {
	public View item;
	public ImageView grab_style;
	public ImageView iv_personalregister_type;
	public TextView title;
	public TextView time;
	public TextView grab_domesticregister_location; //注册区域
	public TextView tv_business_content;//经营业务
	public Button knock;

	//RD wangjianlong 2016.3.29 add for o2o 709
	public TextView tv_main_fee; //活动价
	public TextView tv_main_origin_fee; //原价
	//RD wangjianlong 2016.3.29 add for o2o 709 end

	public View view_personal_bottom;

}