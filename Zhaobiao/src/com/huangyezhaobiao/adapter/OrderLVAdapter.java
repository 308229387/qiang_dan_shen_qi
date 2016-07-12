package com.huangyezhaobiao.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huangyezhaobiao.lib.QDBaseBean;
import com.huangyezhaobiao.lib.ZBBaseAdapter;
/**
 * 四个fragment中lv共用的adapter
 * @author shenzhx
 *
 */
public class OrderLVAdapter extends ZBBaseAdapter<QDBaseBean>{
	public OrderLVAdapter(Context context,
			AdapterListener listener) {
		super(context, listener);
	
	}

	@Override
	public void transferBeanTypeToAdapterType() {
		typeMap.put("1", 0);
		typeMap.put("2", 1);
		typeMap.put("3", 2);
		//2015.8.18 add
		typeMap.put("4", 3);
		//2015.12.7 add
		typeMap.put("5",4);
		typeMap.put("6",5);
	}

	@Override
	public int getTotalTypeCount() {
		return typeMap.size();
	}

	
	
	@Override
	public View initView(QDBaseBean t, View view, ViewGroup parent,
			LayoutInflater inflater,Context context) {
		return t.initView(view, inflater, parent,context,this);
	}

	@Override
	public void converseView(QDBaseBean t, View view,Context context) {
		t.converseView(view,context,this);
	}

	@Override
	public void fillDatas(QDBaseBean t) {
		t.fillDatas();
	}

	@Override
	public QDBaseBean initJavaBean(int position) {
		return beans.get(position);
	}

	@Override
	protected int getAdapterItemType(int position) {
		return typeMap.get(""+beans.get(position).getDisplayType());
	}
	
}
