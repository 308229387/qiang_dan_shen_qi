package com.huangyezhaobiao.view;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huangyezhaobiao.R;

public class ZhaoBiaoDialog extends Dialog implements View.OnClickListener{
	private Context context;
	private LayoutInflater mInflater;
	private View view;
	private TextView dialog_tv_title;
	private TextView dialog_tv_content;
	private RelativeLayout rl_cancel,rl_ok;
	private TextView tv_dialog_cancel,tv_dialog_ok;
	private onDialogClickListener listener;
	private String title;
	private String content;
	
	public void setOnDialogClickListener(onDialogClickListener listener){
		this.listener = listener;
	}

	public void setNagativeText(String nagative){
		tv_dialog_cancel.setText(nagative);
	}

	public void setPositiveText(String positive){
		tv_dialog_ok.setText(positive);
	}

	public ZhaoBiaoDialog(Context context,String title,String content) {
		 super(context,R.style.RequestDialog);
		 this.context = context;
		 mInflater = LayoutInflater.from(context);
		 this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题
		 getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
				WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
		 setCanceledOnTouchOutside(false);
		 Window window = getWindow();
		 window.setWindowAnimations(R.style.dialogWindowAnim);
		 this.title = title;
		 this.content = content;
		 initView();
		
	}
	
	private void initView() {
		view = mInflater.inflate(R.layout.dialog_zhaobiao, null);
		setContentView(view);
		dialog_tv_content = (TextView) view.findViewById(R.id.dialog_tv_content);
		dialog_tv_title   = (TextView) view.findViewById(R.id.dialog_tv_title);
		rl_cancel         = (RelativeLayout) view.findViewById(R.id.rl_cancel);
		rl_ok             = (RelativeLayout) view.findViewById(R.id.rl_ok);
		tv_dialog_cancel  = (TextView) view.findViewById(R.id.tv_dialog_cancel);
		tv_dialog_ok      = (TextView) view.findViewById(R.id.tv_dialog_ok);
		rl_ok.setOnClickListener(this);
		rl_cancel.setOnClickListener(this);
		if(TextUtils.isEmpty(content)){
			dialog_tv_content.setVisibility(View.GONE);
		}else{
			dialog_tv_content.setText(content);
		}
		
		if(TextUtils.isEmpty(title)){
			dialog_tv_title.setVisibility(View.GONE);
		}else {
			dialog_tv_title.setText(title);
		}

	}

	/**
	 * 设置标题
	 * @param title
	 */
	public void setTitle(String title){
		dialog_tv_title.setText(title);
	}
	
	/**
	 * 设置内容
	 * @param content
	 */
	public void setMessage(String content){
		dialog_tv_content.setText(content);
		if(TextUtils.isEmpty(content)){
			dialog_tv_content.setVisibility(View.GONE);
		}else{
			dialog_tv_content.setVisibility(View.VISIBLE);
			dialog_tv_content.setText(content);
		}
	}
	
	public void setCancelButtonGone(){
		rl_cancel.setVisibility(View.GONE);
	}
	
	@Override
	public void show() {
		super.show();
	}
	
	public interface onDialogClickListener{
		public void onDialogOkClick();
		public void onDialogCancelClick();
	}



	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_ok:
			if(listener!=null){
				listener.onDialogOkClick();
			}
			break;
		case R.id.rl_cancel:
			if(listener!=null){
				listener.onDialogCancelClick();
			}
			break;
		}
	}
}
