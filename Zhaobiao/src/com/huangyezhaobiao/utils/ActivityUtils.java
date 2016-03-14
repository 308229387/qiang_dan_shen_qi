package com.huangyezhaobiao.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.huangyezhaobiao.R;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class ActivityUtils {

	public static <T> void goToActivity(Context context, Class<T> clazz) {
		if (context == null || clazz == null) {
			throw new RuntimeException("参数不能为空");
		}
		Intent intent = new Intent(context
				, clazz);
		context.startActivity(intent);
		//	animEnter(context);
	}

/*	static void animEnter(Context context) {
		if (context instanceof Activity) {
			Activity activity = (Activity) context;
			activity.overridePendingTransition(R.anim.base_next_in, R.anim.base_next_out);
		}
	}*/

	public static <T> void goToActivityWithInteger(Context context, Class<T> clazz,
												   Map<String, Integer> maps) {
		if (context == null || clazz == null) {
			throw new RuntimeException("参数不能为空");
		}
		Intent intent = new Intent(context
				, clazz);
		Set<Entry<String, Integer>> entrySet = maps.entrySet();
		Iterator<Entry<String, Integer>> iterator = entrySet.iterator();
		while (iterator.hasNext()) {
			Entry<String, Integer> next = iterator.next();
			String key = next.getKey();
			Integer value = next.getValue();
			intent.putExtra(key, value);
		}
		context.startActivity(intent);
		//animEnter(context);
	}


	public static <T> void goToActivityWithString(Context context, Class<T> clazz,
												  Map<String, String> maps) {
		if (context == null || clazz == null) {
			throw new RuntimeException("参数不能为空");
		}
		Intent intent = new Intent(context
				, clazz);
		Set<Entry<String, String>> entrySet = maps.entrySet();
		Iterator<Entry<String, String>> iterator = entrySet.iterator();
		while (iterator.hasNext()) {
			Entry<String, String> next = iterator.next();
			String key = next.getKey();
			String value = next.getValue();
			intent.putExtra(key, value);
		}
		context.startActivity(intent);

	}


	public static void goToDialActivity(Context context, String tel) {

		//先判断有没有打电话的权限
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tel));
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		context.startActivity(intent);

	}
}
