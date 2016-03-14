package com.huangyezhaobiao.vm;

import android.content.Context;

import com.huangye.commonlib.model.NetWorkModel;
import com.huangye.commonlib.utils.NetBean;
import com.huangye.commonlib.vm.callback.ListNetWorkVMCallBack;
import com.huangyezhaobiao.bean.poplist.AffiliatesListBean;
import com.huangyezhaobiao.bean.poplist.CleaningListBean;
import com.huangyezhaobiao.bean.poplist.DomesticRegisterListBean;
import com.huangyezhaobiao.bean.poplist.NannyListBean;
import com.huangyezhaobiao.bean.poplist.PersonalRegisterListBean;
import com.huangyezhaobiao.bean.poplist.RenovationListBean;
import com.huangyezhaobiao.lib.QDBaseBean;
import com.huangyezhaobiao.lib.ZBBaseListViewModel;
import com.huangyezhaobiao.model.QiangDanListModel;

public class GrabListViewModel extends ZBBaseListViewModel<QDBaseBean>{

	public GrabListViewModel(ListNetWorkVMCallBack callBack, Context context) {
		super(callBack, context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void initKey() {
		key = "displayType";
	}

	@Override
	protected void registerSourceDirs() {
		sourcesDir.put("1", RenovationListBean.class);
		sourcesDir.put("2", PersonalRegisterListBean.class);
		sourcesDir.put("3", DomesticRegisterListBean.class);
		//2015.8.17 add
		sourcesDir.put("4", AffiliatesListBean.class);

		//sourcesDir.put("1044", zhuche.class);
		//2015.12.4 add 保姆月嫂
		sourcesDir.put("5", NannyListBean.class);
		//2015.12.5 add 保洁清洗
		sourcesDir.put("6", CleaningListBean.class);
	}
	
	
	

	@Override
	protected NetWorkModel initListNetworkModel(Context context) {
		return new QiangDanListModel(this, context);
	}

	@Override
	protected int getPageCount(NetBean bean) {
		return 5;
	}


}
