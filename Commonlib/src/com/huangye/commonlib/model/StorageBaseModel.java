package com.huangye.commonlib.model;

import android.content.Context;

import com.huangye.commonlib.delegate.StorageCallBack;
import com.huangye.commonlib.file.SqlUtils;
import com.huangye.commonlib.model.callback.StorageModelCallBack;
import com.huangye.commonlib.utils.LogUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public class StorageBaseModel<T> extends HYBaseModel implements StorageCallBack{

	public SqlUtils sqlUtils;
	public StorageModelCallBack storageModelCallBack;
	Class<T> clazz;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public StorageBaseModel(Context context, StorageModelCallBack callback){
		LogUtils.LogE("shenzhixinDB","oncreate sbm");
		sqlUtils = SqlUtils.getInstance(context);
		storageModelCallBack = callback;
		Type modelType = getClass().getGenericSuperclass();
		if ((modelType instanceof ParameterizedType)) {
			Type[] modelTypes = ((ParameterizedType) modelType).getActualTypeArguments();
			clazz = ((Class) modelTypes[0]);
		}

	}
	
	/**
	 * 获取单个数据  
	 * @param key
	 * @param value
	 * @return
	 */
	public T getData(String key, String value){
		return sqlUtils.get(clazz,key,value,this);
	}
	
	/**
	 * 插入单个数据 若没建表会自动见表
	 * @param o
	 */
	public void putData(Object o){
		sqlUtils.save(o,this);
	}
	
	/**
	 * 删除符合条件的数据 和get相同
	 * @param key
	 * @param value
	 */
	public void delete(String key, String value){
		LogUtils.LogE("ashen", "delete.key:" + key + ",value:" + value);
		sqlUtils.delete(clazz, key, value, this);
	}

	@Override
	public void getDataSuccess(Object o) {
		storageModelCallBack.getDataSuccess(o);
	}

	@Override
	public void getDataFailure() {
		storageModelCallBack.getDataFailure();
	}

	@Override
	public void insertDataSuccess() {
		storageModelCallBack.insertDataSuccess();
		
	}

	@Override
	public void insertDataFailure() {
		storageModelCallBack.insertDataFailure();
		
	}

	@Override
	public void deleteDataSuccess() {
		LogUtils.LogE("ashen", "baseModel..delete..success");
		storageModelCallBack.deleteDataSuccess();
		
	}

	@Override
	public void deleteDataFailure() {
		storageModelCallBack.deleteDataFailure();
		
	}
	
	
	/**
	 * 根据key value 获取全部数据
	 * @param key
	 * @param value
	 * @return
	 */
	public List<T> getAll(String key, String value){
		return sqlUtils.getList(clazz, key, value, this);
	}
	/**
	 * 根据页数，页码获取数据
	 * @param orderBy
	 * @param pageSize
	 * @param pageNum
	 * @return
	 */
	public List<T> getData(String orderBy,int pageSize,int pageNum){
		return sqlUtils.getListPage(clazz, orderBy, pageSize, pageNum, this);
	}
	/**
	 * 插入List
	 * @param list
	 */
	public void putData(List<Object> list){
		sqlUtils.saveList(list,this);
	}

}
