package com.huangye.commonlib.file;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import com.huangye.commonlib.constans.ResponseConstans;
import com.huangye.commonlib.utils.LogUtils;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class SharedPreferencesUtils {

	private static final String NAME = "push_sp";

	public static String myLoginToken = "";
	public static String ReadDefault(Context context, String key) {

		SharedPreferences preferences = context.getSharedPreferences("default", Context.MODE_PRIVATE);
		String value = preferences.getString(key, "0");
		return value;

	}

	public static void WriteDefault(Context context, String key, String value) {
		SharedPreferences preferences = context.getSharedPreferences("default", Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static String Read(Context context, String name, String key) {
		SharedPreferences preferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
		String value = preferences.getString(key, "0");
		return value;

	}
	
	public static int ReadInt(Context context, String name, String key) {
		SharedPreferences preferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
		int value = preferences.getInt(key, 0);
		return value;

	}

	public static void Write(Context context, String name, String key, String value) {
		SharedPreferences preferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static Map<String,?> ReadAll(Context context, String name, String key) {
		SharedPreferences preferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
		Map<String, ?> map = preferences.getAll();
		return map;

	}

	public static void WriteAll(Context context, String name, Map<String, String> map) {
		SharedPreferences preferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();

		Collection<String> c = map.values();
		Iterator<String> it = c.iterator();
		for (; it.hasNext();) {
			editor.putString((String) it.next(), map.get(it.next()));
		}
		editor.commit();
	}

	/**
	 * @param context
	 * @param spName
	 * @param token
	 */
	public static void saveUserToken(Context context,String spName,String token){
		SharedPreferences sharedPreferences = context.getSharedPreferences(spName,Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		myLoginToken = token;
		if(!TextUtils.isEmpty(token)){
			editor.putString(ResponseConstans.KEY_LOGIN_TOKEN,token);
		}
		editor.commit();
	}


	/**
	 * ��õ�¼ʱ��userToken
	 * @param context
	 * @return
	 */
	public static String getUserToken(Context context)
	{
		SharedPreferences sharedPreferences = context.getSharedPreferences(ResponseConstans.SP_NAME,Context.MODE_PRIVATE);
		String loginToken = sharedPreferences.getString(ResponseConstans.KEY_LOGIN_TOKEN, "");
		return loginToken;

	}
	/**
	 *
	 *
	 *
	 * @param context
	 * @param validateToken
	 * @return
	 */
	public static boolean isLoginValidate(Context context,String spName,String validateToken){
		SharedPreferences sharedPreferences = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
		String myToken = sharedPreferences.getString(ResponseConstans.KEY_LOGIN_TOKEN, "");
		LogUtils.LogE("shenzhixin","validateToken:"+validateToken+",myToken:"+myToken+",myLoginToken:"+myLoginToken+",TextUtils.isEmpty(validateToken):"+TextUtils.isEmpty(validateToken));
		if(TextUtils.isEmpty(myToken)){
			myToken = myLoginToken;
		}
		return TextUtils.equals(validateToken,myToken) || TextUtils.isEmpty(validateToken) || TextUtils.equals(validateToken,"null");
	}

	/**
	 * clear login info
	 */
	public static void clearLoginToken(Context context){
		SharedPreferences sharedPreferences = context.getSharedPreferences(ResponseConstans.SP_NAME,Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putString(ResponseConstans.KEY_LOGIN_TOKEN,"");
		editor.commit();
	}
}
