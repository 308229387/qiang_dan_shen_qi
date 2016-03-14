package com.huangyezhaobiao.netmodel;
/**
 * 网络状态变化时的回调
 * @author 5shenzhixin
 *
 */
public interface INetStateChangedListener {
	public void NetConnected();
	public void NetDisConnected();
}
