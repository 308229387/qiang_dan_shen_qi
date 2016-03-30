package com.huangyezhaobiao.vm;

import android.content.Context;

import com.huangye.commonlib.model.NetWorkModel;
import com.huangye.commonlib.model.NetWorkModel.TAG;
import com.huangye.commonlib.vm.SourceViewModel;
import com.huangye.commonlib.vm.callback.NetWorkVMCallBack;
import com.huangyezhaobiao.model.UpdateModel;

/**
 * 更新的viewModel
 * @author shenzhixin
 *
 */
public class UpdateViewModel extends SourceViewModel{

	public UpdateViewModel(NetWorkVMCallBack callBack, Context context) {
		super(callBack, context);
	}

	@Override
	protected NetWorkModel initListNetworkModel(Context context) {
		return new UpdateModel(this, context);
	}

	
	public void checkVersion(){
		t.type = TAG.CHECKVERSION;
		t.getDatas();
	}

	@Override
	public void onLoadingStart() {

	}

}
