package com.huangyezhaobiao.bean.push.pop;

import android.content.Context;
import android.view.View;

import com.huangyezhaobiao.bean.push.PushBean;

/**
 * 需要弹窗的类别基类，需要完成和对应view的绑定
 * @author linyueyang
 *
 */
public abstract class PopBaseBean extends PushBean{

	public abstract View initView(Context context);

	public abstract int getCateId();
	
	
	
	public abstract String getVoice();
	
	public abstract String getFee();

	//2015.12.8 add discountFee
	public abstract String getOriginalFee();
	
}
