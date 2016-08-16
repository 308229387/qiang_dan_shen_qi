package wuba.zhaobiao.order.utils;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huangye.commonlib.utils.JsonUtils;
import com.huangyezhaobiao.bean.mylist.CleaningOrderListBean;
import com.huangyezhaobiao.bean.mylist.MessCenIACIndividualBean;
import com.huangyezhaobiao.bean.mylist.MessCenIACInnerCashBean;
import com.huangyezhaobiao.bean.mylist.NannyOrderListBean;
import com.huangyezhaobiao.bean.mylist.QDZhuangXiuMessageBean;
import com.huangyezhaobiao.utils.LogUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by SongYongmeng on 2016/8/7.
 */
public class OrderCachUtils<T> {
    protected Map<String, Class<? extends T>> sourcesDir = new HashMap<String, Class<? extends T>>();

    public OrderCachUtils() {
        registerSourceDirs();
    }


    public List<T> transferToListBean(String t) {
        // TODO Auto-generated method stub
        List<T> list = new ArrayList<T>();
        String key = "displayType";

        try {
            JSONArray jsonArray = JSON.parseArray(t);
            if (jsonArray == null) {
                return list;
            } else {
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Log.e("shenzhixin", "key:" + jsonObject.get(key));
                    if (sourcesDir.get(jsonObject.get(key) + "") != null) {//防止有新业务出现，但是app还没有升级到最新的版本
                        Log.e("shenzhixin", "hahaha");
                        Class<? extends T> classz = sourcesDir.get(jsonObject.get(key) + "");
                        LogUtils.LogE("shenzhixin", "key:" + key + ",className:" + classz + ",content:" + jsonObject.toString());
                        T object = JsonUtils.jsonToObject(jsonObject.toString(), classz);
                        list.add(object);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    public String transferToBean(String t) {
        String pageNum = "";
        try {
            JSONObject jsonObject = JSON.parseObject(t);
            if (jsonObject == null) {
                return null;
            } else {
                pageNum = jsonObject.getString("pageCount");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return pageNum;
    }


    protected void registerSourceDirs() {
        sourcesDir.put("1", (Class<? extends T>) QDZhuangXiuMessageBean.class);
        sourcesDir.put("2", (Class<? extends T>) MessCenIACIndividualBean.class);
        sourcesDir.put("3", (Class<? extends T>) MessCenIACInnerCashBean.class);
        //2015.8.18 add
//		sourcesDir.put("4", CenterAffiliateBean.class);
        //2015.12.7 add
        sourcesDir.put("5", (Class<? extends T>) NannyOrderListBean.class);
        sourcesDir.put("6", (Class<? extends T>) CleaningOrderListBean.class);
    }
}
