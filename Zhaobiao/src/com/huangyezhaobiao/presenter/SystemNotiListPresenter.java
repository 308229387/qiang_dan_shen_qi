package com.huangyezhaobiao.presenter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.BaseAdapter;

import com.huangyezhaobiao.activity.SystemActiveActivity;
import com.huangyezhaobiao.activity.SystemNotiDetailActivity;
import com.huangyezhaobiao.adapter.SystemNotiAdapter;
import com.huangyezhaobiao.bean.SysListBean;

import java.util.List;

/**
 * Created by shenzhixin on 2015/12/15.
 */
public class SystemNotiListPresenter {
    private Context context;
    private SystemNotiAdapter systemNotiAdapter;
    public SystemNotiListPresenter(Context context){
        this.context = context;
    }

    public void initNotiAdapter(List<SysListBean > beans){
        systemNotiAdapter = new SystemNotiAdapter(beans,context);
    }
    public BaseAdapter getSysNotiAdapter(){
        return systemNotiAdapter;
    }

    public void refreshDatas(List<SysListBean> beans){
        Log.e("shenzhixin","HAHA:"+beans.size());
        systemNotiAdapter.refreshDatas(beans);
    }

    public void notifyData(){
        systemNotiAdapter.notifyDataSetChanged();
    }


    /**
     * 处理点击事件，根据syslistbea的类型来判断跳转
     * @param listBean
     */
    public void handleClick(SysListBean listBean){
        if(TextUtils.isEmpty(listBean.getUrl())){
            //为空,直接把ListBean传递到detail界面
            Intent intent = SystemNotiDetailActivity.onNewIntent(context,listBean);
            context.startActivity(intent);
        }else{
            //把url传到h5页面
            Intent intent1 = SystemActiveActivity.onNewIntent(context,listBean.getUrl());
            context.startActivity(intent1);

        }
    }
}
