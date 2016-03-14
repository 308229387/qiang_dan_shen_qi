package com.huangyezhaobiao.inter;

import com.huangyezhaobiao.bean.popdetail.BottomViewBean;
import com.huangyezhaobiao.bean.popdetail.LogBean;

/**
 * 用于详情页的底部栏回调
 * @author linyueyang
 *
 */
public interface OrderDetailCallBack {

	 void back(LogBean log, BottomViewBean bottom);
	
}
