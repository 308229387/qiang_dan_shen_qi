package com.huangyezhaobiao.vm;

import android.content.Context;
import android.text.TextUtils;

import com.huangye.commonlib.model.NetWorkModel;
import com.huangye.commonlib.utils.JsonUtils;
import com.huangye.commonlib.utils.NetBean;
import com.huangye.commonlib.vm.callback.ListNetWorkVMCallBack;
import com.huangyezhaobiao.bean.mylist.CleaningOrderListBean;
import com.huangyezhaobiao.bean.mylist.MessCenIACIndividualBean;
import com.huangyezhaobiao.bean.mylist.MessCenIACInnerCashBean;
import com.huangyezhaobiao.bean.mylist.NannyOrderListBean;
import com.huangyezhaobiao.bean.mylist.QDZhuangXiuMessageBean;
import com.huangyezhaobiao.lib.QDBaseBean;
import com.huangyezhaobiao.lib.ZBBaseListViewModel;
import com.huangyezhaobiao.model.CenterQiangDanListModel;
import com.huangyezhaobiao.utils.LogUtils;

import java.util.Map;
/**
 * 抢单信息展示的lvm
 * @author 58
 *
 */
public class QiangDanListViewModel extends ZBBaseListViewModel<QDBaseBean>{
	
	public QiangDanListViewModel(ListNetWorkVMCallBack callBack, Context context) {
		super(callBack, context);
	}

	@Override
	protected void initKey() {
		key = "displayType";
	}

	@Override
	protected void registerSourceDirs() {
		sourcesDir.put("1", QDZhuangXiuMessageBean.class);
		sourcesDir.put("2", MessCenIACIndividualBean.class);
		sourcesDir.put("3", MessCenIACInnerCashBean.class);
		//2015.8.18 add
//		sourcesDir.put("4", CenterAffiliateBean.class);
		//2015.12.7 add
		sourcesDir.put("5", NannyOrderListBean.class);
		sourcesDir.put("6", CleaningOrderListBean.class);
	}

	@Override
	protected NetWorkModel initListNetworkModel(Context context) {
		return new CenterQiangDanListModel(this, context);
	}



	@Override
	protected int getPageCount(NetBean bean) {
		if (TextUtils.isEmpty(bean.getOther())) {
			return 0;
		}
		LogUtils.LogE("ashenParse", "bean:" + bean.toString());
		Map<String, String> pageNum = JsonUtils.jsonToMap(bean.getOther());
		String pageCount = pageNum.get("pageCount");
		LogUtils.LogE("ashenPage", "pageCount:" + pageCount);
		return Integer.parseInt(pageCount);
	}
	
	
	



}
