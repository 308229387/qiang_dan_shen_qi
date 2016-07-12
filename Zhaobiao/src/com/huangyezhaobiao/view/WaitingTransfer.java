package com.huangyezhaobiao.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.huangyezhaobiao.R;

public class WaitingTransfer extends Dialog {

	private Context context;
	private LayoutInflater mInflater;
	private View view;

	private ImageView mIVDailWaveAnim;
	private ImageView mIVDailAnim;
	private Animation waveAnim;
	private Animation dailAnim;

	public WaitingTransfer(Context context) {
		super(context, R.style.transfering);
		this.context = context;
		mInflater = LayoutInflater.from(context);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setCanceledOnTouchOutside(false);
		initView();
	}

	private void initView(){
		view = mInflater.inflate(R.layout.activity_waterwave, null);
		setContentView(view);
		mIVDailWaveAnim = (ImageView) view.findViewById(R.id.iv_dail_wave_anim);
		waveAnim = AnimationUtils.loadAnimation(context, R.anim.dail_wave_anim);

		// 启动外层动画
		mIVDailAnim = (ImageView) view.findViewById(R.id.iv_dail_anim);
		mIVDailWaveAnim.startAnimation(waveAnim);
		// 启动拨号动画
		dailAnim = AnimationUtils.loadAnimation(context, R.anim.dail_anim);
		mIVDailAnim.startAnimation(dailAnim);
	}


}
