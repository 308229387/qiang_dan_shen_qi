package com.huangyezhaobiao.vm;

import android.content.Context;

import com.huangye.commonlib.vm.ListStorageViewModel;
import com.huangye.commonlib.vm.callback.StorageVMCallBack;
import com.huangyezhaobiao.model.DetailMessageListStorageModel;
import com.huangyezhaobiao.utils.LogUtils;

/**
 * 具体的各个消息中心的item的列表页vm
 * @author shenzhixin
 *
 */
public class DetailMessageListStorageVM extends ListStorageViewModel{
	private String type;
	public DetailMessageListStorageVM(Context context,
			StorageVMCallBack callback) {
		super(context, callback);
	}

	@Override
	public void initModel(Context context) {
		listModel = new DetailMessageListStorageModel(context, this);
	}

	public void cleanAllDatas(){
		LogUtils.LogE("ashen", "clean datas");
		listModel.delete("tag",type);
	}
	
	@Override
	public Object getDate() {
		LogUtils.LogE("storage", "type:" + type);
		return 	listModel.getAll("tag", type);
	}

	public void setType(String type){
		this.type = type;
	}


	
	
}
