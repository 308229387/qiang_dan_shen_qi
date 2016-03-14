package com.huangyezhaobiao.bean.popdetail;

import android.content.Context;
import android.view.View;

public abstract class QDDetailBaseBean {

	protected String newtype;
	protected long orderId;
	
	
	public abstract View initView(Context context);
	
	
	
}
