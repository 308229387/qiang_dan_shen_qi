package com.huangyezhaobiao.holder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public final class RenovationViewHolder {
	public View item; //整个Item
	public ImageView grab_style;
	public ImageView iv_renovation_type;
	public TextView title; //标题 ---装修
	public TextView time; //时间
	public TextView space; //面积
	public TextView budget; //预算
	public TextView decorate_type; //方式
	public TextView distance;//距离
	public TextView location; //区域
	public Button knock; //抢单按钮

	// edited by chenguangming
	public TextView tvOriginalPrice; //原价
	public TextView tvActivePrice; //现价

	public View view_bottom;
}