package com.huangye.commonlib.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JsonUtils {


	private boolean isJSON(String json){
		try{
			JSON.parseObject(json);
		} catch(JSONException e){
			return false;
		}
		return true;
	}

	public static NetBean jsonToNetBean(String json) throws JSONException {


		JSONObject obj = JSON.parseObject(json);
		// NetBean netBean = (NetBean)JSONObject.toBean(obj, NetBean.class);
		NetBean netBean = new NetBean();

		netBean.setMsg(obj.getString("msg"));
		netBean.setStatus(Integer.parseInt(obj.getString("status")));

		JSONObject result = JSON.parseObject(obj.getString("result"));

		if (result != null) {
			netBean.setData(result.getString("data"));
			if (obj.getString("result").indexOf("other") != -1)
				netBean.setOther(result.getString("other"));
		}
		return netBean;

	}

	public static <T> List<T> jsonToObjectList(String json, Class<T> clazz) {
		try {
			return JSON.parseArray(json, clazz);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static <T> T jsonToObject(String json, Class<T> clazz) {

		return JSON.parseObject(json, clazz);

	}

	public static Map<String, String> jsonToMap(String json) {
		LogUtils.LogE("ashenParse", "json:" + json);
		JSONObject jsonObject = JSON.parseObject(json);
		LogUtils.LogE("ashenParse", "jsonObject:" + (jsonObject == null));
		Map<String, String> resultMap = new HashMap<String, String>();
		Set<String> set = jsonObject.keySet();
		Iterator<String> iter = set.iterator();
		String key = null;
		Object value = null;
		while (iter.hasNext()) {
			key = iter.next();
			value = jsonObject.get(key);
			resultMap.put(key,  value.toString());
		}
		return resultMap;
	}

	public static List<Map<String, String>> jsonToNewListMap(String json) {

		JSONArray jsonArray = JSON.parseArray(json);
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);

			Set<String> set = jsonObject.keySet();
			Iterator<String> iter = set.iterator();
			String key = null;
			Object value = null;
			while (iter.hasNext()) {
				key = iter.next();
				value = jsonObject.get(key);
				Map<String, String> resultMap = jsonToMap(value.toString());
				resultMap.put("newtype", key);
				list.add(resultMap);
			}
		}
		return list;
		//
		// JSONObject jsonObject = JSON.parseObject(json);
		//
		// List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		//
		// Set<String> set = jsonObject.keySet();
		// Iterator<String> iter = set.iterator();
		// String key = null;
		// Object value = null;
		// while (iter.hasNext()) {
		// key = iter.next();
		// value = jsonObject.get(key);
		// Map<String, String> resultMap = jsonToMap((String) value);
		// resultMap.put("newtype", key);
		//
		// list.add(resultMap);
		// }

		// return list;
	}

	public static List<Map<String, String>> jsonToListMap(String json) {

		JSONArray array = JSON.parseArray(json);
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		for (int i = 0; i < array.size(); i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			Map<String, String> resultMap = new HashMap<String, String>();
			Set<String> set = jsonObject.keySet();
			Iterator<String> iter = set.iterator();
			String key = null;
			Object value = null;
			while (iter.hasNext()) {
				key = iter.next();
				value = jsonObject.get(key);
				resultMap.put(key, (String) value);
			}
			list.add(resultMap);
			System.out.println(resultMap);
			/*
			 * 打印结果{map={"sex":"female","age":"23","name":"Alexia"},
			 * employ={"sex":"female","age":"24","name":"wjl"}}
			 */
		}
		return list;

	}

	public static void main(String[] a) {
		String haha = "{\"status\":0,\"msg\":\"ok\",\"result\":{\"data\":{\"orderId\":\"12312321\",\"time\":\"2015-05-15 18:49\",\"title\":\"住宅装修-二手房\",\"space\":\"56平米\",\"budget\":\"预算3-5万\",\"endTime\":\"2015年6月\",\"location\":\"朝阳区北苑\"}}}";

		String haha1 = "{" + "\"status\":0," + "\"msg\":\"ok\"," + "\"result\":{" + "\"data\":[" + "{"
				+ "\"orderId\":\"12312321\"," + "\"time\":\"2015-05-15 18:49\"," + "\"title\":\"住宅装修-二手房\","
				+ "\"space\":\"56平米\"," + "\"budget\":\"预算3-5万\"," + "\"endTime\":\"2015年6月\","
				+ "\"location\":\"朝阳区北苑\"" + "}," + "{" + "\"orderId\":\"12312321\"," + "\"time\":\"2015-05-15 18:49\","
				+ "\"title\":\"住宅装修-二手房\"," + "\"space\":\"56平米\"," + "\"budget\":\"预算3-5万\","
				+ "\"endTime\":\"2015年6月\"," + "\"location\":\"朝阳区北苑\"" + "}," + "{		" + "\"orderId\":\"12312321\","
				+ "\"time\":\"2015-05-15 18:49\"," + "\"title\":\"住宅装修-二手房\"," + "\"space\":\"56平米\","
				+ "\"budget\":\"预算3-5万\"," + "\"endTime\":\"2015年6月\"," + "\"location\":\"朝阳区北苑\"" + "}" + "]}}";
		NetBean netBean = JsonUtils.jsonToNetBean(haha1);

		// List<DecorationBean> decorationBean =
		// JsonUtils.jsonToObjectList(netBean.getData(), DecorationBean.class);

		System.out.print("haha");

	}

}
