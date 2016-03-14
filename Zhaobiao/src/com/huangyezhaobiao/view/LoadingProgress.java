package com.huangyezhaobiao.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;

import com.huangyezhaobiao.R;

public class LoadingProgress extends Dialog {
	public LoadingProgress(Context context, int theme) {
		super(context, theme);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_loading);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {

		int action = ev.getAction();
		String TAG = "onTouchEvent";
		Log.e(TAG, "result");

		this.dismiss();

		return true;
	}

}
