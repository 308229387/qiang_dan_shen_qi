package com.huangyezhaobiao.vm;

import android.content.Context;
import android.text.TextUtils;

import com.huangye.commonlib.model.NetWorkModel;
import com.huangye.commonlib.utils.JsonUtils;
import com.huangye.commonlib.utils.NetBean;
import com.huangye.commonlib.vm.ListSourceViewModel;
import com.huangye.commonlib.vm.callback.ListNetWorkVMCallBack;
import com.huangyezhaobiao.bean.ConsumeItemBean;
import com.huangyezhaobiao.model.ConsumeListModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by shenzhixin on 2015/12/12.
 */
public class ConsumeListVM extends ListSourceViewModel{
    public ConsumeListVM(ListNetWorkVMCallBack callBack, Context context) {
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
        List<ConsumeItemBean> consumeItemBeans = new ArrayList<ConsumeItemBean>();
        consumeItemBeans = JsonUtils.jsonToObjectList(t,ConsumeItemBean.class);
        return consumeItemBeans;
    }

    @Override
    protected NetWorkModel initListNetworkModel(Context context) {
        return new ConsumeListModel(this,context);
    }
}
