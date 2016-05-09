package com.huangyezhaobiao.vm;

import android.content.Context;

import com.huangye.commonlib.model.NetWorkModel;
import com.huangye.commonlib.vm.SourceViewModel;
import com.huangye.commonlib.vm.callback.NetWorkVMCallBack;
import com.huangyezhaobiao.model.YuEModel;

public class YuEViewModel extends SourceViewModel{

	public YuEViewModel(NetWorkVMCallBack callBack,Context context) {
		super(callBack,context);
	}

	@Override
	protected NetWorkModel initListNetworkModel(Context context) {
		YuEModel model = new YuEModel(this,context);
		return model;
	}

	public void getBalance(){
		t.getDatas();
	}

	@Override
	public void onVersionBack(String value) {

	}
}
