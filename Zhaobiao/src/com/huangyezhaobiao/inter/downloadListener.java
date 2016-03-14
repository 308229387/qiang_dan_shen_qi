package com.huangyezhaobiao.inter;

import java.io.File;

import com.lidroid.xutils.exception.HttpException;

public interface downloadListener{
	/**
	 * 失败时
	 * @param arg0
	 * @param arg1
	 */
	void onFailure(HttpException exce, String msg);

	/**
	 * 正在下载
	 * @param total 总数
	 * @param current 当前
	 */
	void onLoading(long total, long current);

	/**
	 * 取消
	 */
	void onCancelled();

	/**
	 * 成功时，安装文件
	 * @param file
	 */
	void onSuccess(File file);
	
}
