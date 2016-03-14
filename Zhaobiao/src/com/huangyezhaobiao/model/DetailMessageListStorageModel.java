package com.huangyezhaobiao.model;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.huangye.commonlib.model.ListStorageBaseModel;
import com.huangye.commonlib.model.callback.StorageModelCallBack;
import com.huangye.commonlib.utils.ConditionBean;
import com.huangyezhaobiao.activity.LoginActivity;
import com.huangyezhaobiao.bean.MessageBean;
import com.huangyezhaobiao.bean.push.PushToStorageBean;
import com.huangyezhaobiao.inter.Constans;
import com.huangyezhaobiao.utils.UserUtils;

public class DetailMessageListStorageModel extends ListStorageBaseModel<PushToStorageBean>{
	private List<ConditionBean> conditionList = new ArrayList<ConditionBean>();
	public DetailMessageListStorageModel(Context context,
			StorageModelCallBack callback) {
		super(context, callback);
		initConditionList("userId",UserUtils.getUserId(context),Constans.DB_OPERATION_EQUAL);
	}

	/**
	 * 初始化
	 * @param columnName
	 * @param value
	 * @param operation
	 */
	private void initConditionList(String columnName,String value,String operation){
		ConditionBean bean = new ConditionBean();
		bean.setColumnName(columnName);
		bean.setValue(value);
		bean.setOp(operation);
		conditionList.add(bean);
	}
	
	
	@Override
	public List<PushToStorageBean> getAll(String key, String value) {
		initConditionList(key, value, Constans.DB_OPERATION_EQUAL);
		return sqlUtils.getListByCondition(PushToStorageBean.class, conditionList, this);
	}

	

	@Override
	public void delete(String key, String value) {
		initConditionList(key, value, Constans.DB_OPERATION_EQUAL);
		sqlUtils.deleteByCondition(PushToStorageBean.class, conditionList, this);
	}

	

	
	
	
	
}
