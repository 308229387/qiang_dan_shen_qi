package com.huangyezhaobiao.model;

import android.content.Context;

import com.huangye.commonlib.model.ListNetWorkModel;
import com.huangye.commonlib.model.callback.NetworkModelCallBack;
import com.huangyezhaobiao.fragment.QiangDanBaseFragment;
import com.huangyezhaobiao.request.ZhaoBiaoRequest;
import com.huangyezhaobiao.url.URLConstans;
import com.huangyezhaobiao.url.UrlSuffix;
import com.huangyezhaobiao.utils.LogUtils;

/**
 * Created by shenzhixin on 2015/12/15.
 */
public class SystemNotiListModel extends ListNetWorkModel{

    public SystemNotiListModel(NetworkModelCallBack baseSourceModelCallBack, Context context) {
        super(baseSourceModelCallBack, context);
    }

    @Override
    public void loadCache() {

    }

    @Override
    public void loadMore() {
        current_load_page++;
        LogUtils.LogE("ashen", "loadmore..page" + current_load_page);
        String url = URLConstans.URL_SYS_LIST + UrlSuffix.getSysListSuffix("" + current_load_page);
        setRequestURL(url);
        LogUtils.LogE("shenzhixintest", "loadmore url:" + url);
        getDatas();
    }

    @Override
    public void refresh() {
        //根据m
        current_load_page = 1;
        LogUtils.LogE("ashen", "refresh..page" + current_load_page);
        String url = URLConstans.URL_SYS_LIST + UrlSuffix.getSysListSuffix("1");
        LogUtils.LogE("shenzhixintest", "refresh url:" + url);
        setRequestURL(url);
        LogUtils.LogE("ashenTest", "refresh url:" + "http://192.168.118.41/app/order/orderlist?userid=24454277549826&orderstate=" + QiangDanBaseFragment.orderState + "&pageNum=1&token=1");
        getDatas();
    }

    @Override
    protected ZhaoBiaoRequest<String> createHttpRequest() {
        return new ZhaoBiaoRequest<>(ZhaoBiaoRequest.METHOD_GET,"",this);
    }
}
