package com.huangyezhaobiao.utils;

import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;

/**
 * viewpager的帮助类，进行ui滑动效果的展示
 * @author 申志鑫
 *
 */
public class ViewPageHelper {

	/**
	 * view那个line的滑动
	 * @param view
	 * @param position
	 * @param positionOffset
	 * @param mCurrentIndex
	 */
	public static void goLine(View view,int position, float positionOffset,int view_width){
		RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
		layoutParams.leftMargin = (int) ((position + positionOffset) * view_width);
		view.setLayoutParams(layoutParams);
	}

	
	
}
