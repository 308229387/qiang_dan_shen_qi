package com.huangyezhaobiao.vm;

import java.util.List;

import android.content.Context;

import com.huangye.commonlib.vm.ListStorageViewModel;
import com.huangye.commonlib.vm.callback.StorageVMCallBack;
import com.huangyezhaobiao.bean.MessageBean;
import com.huangyezhaobiao.bean.push.PushToStorageBean;
import com.huangyezhaobiao.inter.Constans;
import com.huangyezhaobiao.model.CenterMessageStorageModel;
import com.huangyezhaobiao.utils.UserUtils;

public class CenterMessageStorageViewModel extends ListStorageViewModel{
	private String type;
	public CenterMessageStorageViewModel(Context context,
			StorageVMCallBack callback) {
		super(context, callback);
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

	@Override
	public void initModel(Context context) {
		model = new CenterMessageStorageModel(context, this);
	
	}
	
	
	@Override
	public Object getDate() {
		return model.getAll("userId", UserUtils.userId);//每次取的都是userId下的所有数据
	}
	
	public void insertAllDatas(List<MessageBean> list){
		model.putData(list);
	}

	public void insertData(MessageBean bean){
		model.putData(bean);
	}

	
	
}
