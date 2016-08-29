package com.huangyezhaobiao.netmodel;

import com.huangyezhaobiao.utils.LogUtils;

/**
 * 网络状态的管理者，用他来进行网络状态的管理
 * @author shenzhixin
 *
 */
public class NetStateManager {
	private INetStateChangedListener listener;
	private static NetStateManager manager ;
	private int net_state;
	public static final int NET_CONNTTED = 1;
	public static final int NET_DISCONNECTED = 2;
	private NetStateManager(){};
	
	public static NetStateManager getNetStateManagerInstance(){
		if (manager == null) {
			synchronized (NetStateManager.class){
				manager = new NetStateManager();
			}
		}
		return manager;
	}
	
	public void setNetState(int state){
		net_state = state;
		if(listener!=null){
			if(net_state==NET_CONNTTED){
				LogUtils.LogV("网络变化", "connected");
				listener.NetConnected();
			}else{
				LogUtils.LogV("网络变化", "not connected");
				listener.NetDisConnected();
			}
		}
		
	}
	public void setINetStateChangedListener(INetStateChangedListener listener){
		this.listener = listener;
	}

	public void removeINetStateChangedListener(){
		this.listener = null;
	}
}
