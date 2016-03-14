package com.huangyezhaobiao.inter;

import com.huangyezhaobiao.bean.push.PushBean;

/**
 * 透传消息来了时的回调接口
 * @author shenzhixin
 *
 */
public interface INotificationListener {
	/**
	 * 透传消息来临时给各个界面的回调函数
	 * @param pushBean 可能不是string,是javaBean，这个还不一定
	 */
	public void onNotificationCome(PushBean pushBean);
}
