package com.huangyezhaobiao.vm;

import android.content.Context;

import com.huangye.commonlib.model.NetWorkModel;
import com.huangye.commonlib.utils.NetBean;
import com.huangye.commonlib.vm.SourceViewModel;
import com.huangye.commonlib.vm.callback.NetWorkVMCallBack;
import com.huangyezhaobiao.model.LogoutModel;

public class LogoutViewModel extends SourceViewModel {

	public LogoutViewModel(NetWorkVMCallBack callBack, Context context) {
		super(callBack, context);
	}

	@Override
	protected NetWorkModel initListNetworkModel(Context context) {
		return new LogoutModel(this, context);
	}
	
	public void logout(){
		t.getDatas();
	}
	
	@Override
	public void onLoadingSuccess(NetBean bean, NetWorkModel model) {
		//do nothing
	}

	@Override
	public void onVersionBack(String value) {
		//do nothing
	}
}
