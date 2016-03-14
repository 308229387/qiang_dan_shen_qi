package com.huangye.commonlib.model;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FilterInputStream;

import android.content.Context;

import com.huangye.commonlib.listop.ListNetModelOp;
import com.huangye.commonlib.model.callback.NetworkModelCallBack;

public abstract class  ListNetWorkModel extends NetWorkModel implements ListNetModelOp{
	/**
	 * 当前加载到第几页
	 * 初始页为1
	 */
	protected int current_load_page = 1;
	public ListNetWorkModel(NetworkModelCallBack baseSourceModelCallBack,Context context) {
		super(baseSourceModelCallBack,context);
	}
	
	@Override
	public void loadMore() {
		current_load_page++;
	}
	
	@Override
	public void refresh() {
		current_load_page=1;
	}

	/**
	 * 返回当前的加载页数索引
	 * @return
	 */
	public int getCurrentPage(){
		return current_load_page;
	}
	
}
