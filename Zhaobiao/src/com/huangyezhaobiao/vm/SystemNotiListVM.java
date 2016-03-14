package com.huangyezhaobiao.vm;

import android.content.Context;
import android.text.TextUtils;

import com.huangye.commonlib.model.NetWorkModel;
import com.huangye.commonlib.utils.JsonUtils;
import com.huangye.commonlib.utils.NetBean;
import com.huangye.commonlib.vm.ListSourceViewModel;
import com.huangye.commonlib.vm.callback.ListNetWorkVMCallBack;
import com.huangyezhaobiao.bean.SysListBean;
import com.huangyezhaobiao.model.SystemNotiListModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by shenzhixin on 2015/12/15.
 * 获取系统信息列表页的vm层
 */
public class SystemNotiListVM extends ListSourceViewModel{

    public SystemNotiListVM(ListNetWorkVMCallBack callBack, Context context) {
        super(callBack, context);
    }

    @Override
    protected int getPageCount(NetBean bean) {
        if (TextUtils.isEmpty(bean.getOther())) {
            return 0;
        }
        Map<String, String> pageNum = JsonUtils.jsonToMap(bean.getOther());
        String pageCount = pageNum.get("pageCount");
        return Integer.parseInt(pageCount);
    }

    @Override
    protected List transferToListBean(String t) {
        List<SysListBean> sysListBeans = new ArrayList<SysListBean>();
        sysListBeans = JsonUtils.jsonToObjectList(t,SysListBean.class);
        return sysListBeans;
    }

    @Override
    protected NetWorkModel initListNetworkModel(Context context) {
        return new SystemNotiListModel(this,context);
    }
}
