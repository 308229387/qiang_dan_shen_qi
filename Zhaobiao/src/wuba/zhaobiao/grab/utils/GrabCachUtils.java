package wuba.zhaobiao.grab.utils;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huangye.commonlib.utils.JsonUtils;
import com.huangyezhaobiao.bean.poplist.CleaningListBean;
import com.huangyezhaobiao.bean.poplist.DomesticRegisterListBean;
import com.huangyezhaobiao.bean.poplist.NannyListBean;
import com.huangyezhaobiao.bean.poplist.PersonalRegisterListBean;
import com.huangyezhaobiao.bean.poplist.RenovationListBean;
import com.huangyezhaobiao.utils.LogUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by SongYongmeng on 2016/8/7.
 */
public class GrabCachUtils<T> {
    protected Map<String, Class<? extends T>> sourcesDir = new HashMap<String, Class<? extends T>>();

    public GrabCachUtils() {
        registerSourceDirs();
    }


    public List<T> transferToListBean(String t) {
        // TODO Auto-generated method stub
        List<T> list = new ArrayList<T>();
        String key = "displayType";

        try {
            JSONArray jsonArray = null;
            try {
                jsonArray = JSON.parseArray(t);
            } catch (Exception e) {
                e.printStackTrace();
                jsonArray = null;
            }
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

    protected void registerSourceDirs() {
        sourcesDir.put("1", (Class<? extends T>) RenovationListBean.class);
        sourcesDir.put("2", (Class<? extends T>) PersonalRegisterListBean.class);
        sourcesDir.put("3", (Class<? extends T>) DomesticRegisterListBean.class);
        //2015.8.17 add
//		sourcesDir.put("4", AffiliatesListBean.class);
        //sourcesDir.put("1044", zhuche.class);
        //2015.12.4 add 保姆月嫂
        sourcesDir.put("5", (Class<? extends T>) NannyListBean.class);
        //2015.12.5 add 保洁清洗
        sourcesDir.put("6", (Class<? extends T>) CleaningListBean.class);
    }
}
