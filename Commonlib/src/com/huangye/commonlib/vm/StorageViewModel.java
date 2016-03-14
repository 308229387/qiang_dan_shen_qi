package com.huangye.commonlib.vm;

import com.huangye.commonlib.model.StorageBaseModel;
import com.huangye.commonlib.model.callback.StorageModelCallBack;
import com.huangye.commonlib.vm.callback.StorageVMCallBack;

import android.content.Context;

public abstract class StorageViewModel implements StorageModelCallBack {

	protected StorageBaseModel model ; 
	
	protected StorageVMCallBack callback;
	
	/**
	 * 继承该类的子类必须在该方法内初始化StorageBaseModel
	 * @param context
	 * @param callback
	 */
	public StorageViewModel(Context context,StorageVMCallBack callback){

		//model = new StorageBaseModel(context, this);
		this.callback = callback;
		initModel(context);
		

	}

	/**
	 * 初始化model
	 * @param context
	 */
	public abstract void initModel(Context context);
	
	/**
	 * 子类通过改方法拼接自己的查询条件，实现获取数据
	 * @return
	 */
	public abstract Object getDate();
	
	/**
	 * 在数据没有改变的时候直接调用，如果需要改变数据，请重写该方法
	 */
	@Override
	public void getDataSuccess(Object o) {
		callback.getDataSuccess(o);
		
	}

	/**
	 * 在数据没有改变的时候直接调用，如果需要改变数据，请重写该方法
	 */
	@Override
	public void getDataFailure() {
		callback.getDataFailure();
	}
	@Override
	public void insertDataSuccess() {
		callback.insertDataSuccess();
		
	}

	@Override
	public void insertDataFailure() {
		callback.insertDataFailure();
	}

	@Override
	public void deleteDataSuccess() {
		callback.deleteDataSuccess();
	}

	@Override
	public void deleteDataFailure() {
		callback.deleteDataFailure();
	}
	
	
}
