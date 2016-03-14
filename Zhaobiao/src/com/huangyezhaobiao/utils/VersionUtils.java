package com.huangyezhaobiao.utils;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;

/**
 * 检查更新的工具
 * @author szx
 *
 */
public class VersionUtils {

	/**
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isUpdate(Context context){
		String name = "";
		try {
			name = getVersionName(context);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		//判断name和网络上获取的名字哪个大，比较
		
		
		return false;
	}
	
	
	/**
	 * 获得版本名
	 * @param context
	 * @return
	 * @throws NameNotFoundException 
	 */
	public static String getVersionCode(Context context) throws NameNotFoundException{
		String packageName = context.getPackageName();
		return ""+(context.getPackageManager().getPackageInfo(packageName, 0).versionCode);
	}
	/**
	 * 获得版本号
	 * @param context
	 * @return
	 * @throws NameNotFoundException 
	 */
	public static String getVersionName(Context context) throws NameNotFoundException{
		String packageName = context.getPackageName();
		return ""+(context.getPackageManager().getPackageInfo(packageName, 0).versionName);
	}
	
	
	
		/**
		 * 进行apk的安装
		 * @param context
		 * @param file 要安装的apk文件
		 */
		public static void installApk(Context context, File file) {
			Intent intent = new Intent();
			// 执行动作
			intent.setAction(Intent.ACTION_VIEW);
			// 执行的数据类型
			intent.setDataAndType(Uri.fromFile(file),
					"application/vnd.android.package-archive");
			context.startActivity(intent);
		}
}
