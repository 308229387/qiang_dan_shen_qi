package com.huangyezhaobiao.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.huangyezhaobiao.R;

/**
 * 进行toast显示的Ui工具类
 * @author shenzhixin
 *
 */
public class ToastUtils extends Toast{

	public ToastUtils(Context context) {
		super(context);
	}

	
	
	/**
	 * 生成一个图文并存的Toast
	 * 
	 * @param context
	 *            // 上下文对象
	 * @param res_id
	 *            // 要显示的图片的资源id
	 * @param text
	 *            // 要显示的文字
	 * @param duration
	 *            // 显示时间
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static ToastUtils makeImgAndTextToast(Context context,
			String content, int res_id, int duration) {
		ToastUtils result = new ToastUtils(context);

		LayoutInflater inflate = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflate.inflate(R.layout.dialog_score, null);
		ImageView iv_toast_res      = (ImageView) v.findViewById(R.id.iv_toast_res);
		TextView  tv_toast_content  = (TextView) v.findViewById(R.id.tv_toast_content);
		iv_toast_res.setImageResource(res_id);
		tv_toast_content.setText(content);
     	result.setView(v);
		// setGravity方法用于设置位置，此处为垂直居中
		result.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		result.setDuration(duration);
		return result;
	}
}
