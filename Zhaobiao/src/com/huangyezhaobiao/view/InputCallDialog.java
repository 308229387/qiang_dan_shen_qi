package com.huangyezhaobiao.view;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huangyezhaobiao.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputCallDialog extends Dialog implements View.OnClickListener{
	private Context context;
	private LayoutInflater mInflater;
	private View view;
//	private TextView dialog_tv_title;
	private TextView dialog_tv_content;
	private LinearLayout ll_input_dialog;
	private EditText et_number;
	private TextView dialog_tv_change;
	private RelativeLayout rl_cancel,rl_ok;
	private TextView tv_dialog_cancel,tv_dialog_ok;
	private onDialogClickListener listener;
	private String title;
	private String content;
	private TextView dialog_tv_alert;


	public InputCallDialog(Context context) {
		super(context,R.style.RequestDialog);
		this.context = context;
		mInflater = LayoutInflater.from(context);
//		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
//				WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
		setCanceledOnTouchOutside(false);
		Window window = getWindow();
		window.setWindowAnimations(R.style.dialogWindowAnim);
		initView();

	}

	private void initView() {
		view = mInflater.inflate(R.layout.dialog_call, null);
		setContentView(view);
		dialog_tv_content = (TextView) view.findViewById(R.id.dialog_tv_content);
		dialog_tv_change = (TextView) view.findViewById(R.id.dialog_tv_change);
		ll_input_dialog = (LinearLayout) view.findViewById(R.id.ll_input_dialog);
		et_number = (EditText) view.findViewById(R.id.et_number);
		dialog_tv_alert = (TextView) view.findViewById(R.id.dialog_tv_alert);
//		dialog_tv_title   = (TextView) view.findViewById(R.id.dialog_tv_title);
		rl_cancel         = (RelativeLayout) view.findViewById(R.id.rl_cancel);
		rl_ok             = (RelativeLayout) view.findViewById(R.id.rl_ok);
		tv_dialog_cancel  = (TextView) view.findViewById(R.id.tv_dialog_cancel);
		tv_dialog_ok      = (TextView) view.findViewById(R.id.tv_dialog_ok);
		rl_ok.setOnClickListener(this);
		rl_cancel.setOnClickListener(this);
		dialog_tv_change.setOnClickListener(this);
		if(TextUtils.isEmpty(content)){
			dialog_tv_content.setVisibility(View.GONE);
		}else{
			dialog_tv_content.setText(content);
		}
		
//		if(TextUtils.isEmpty(title)){
//			dialog_tv_title.setVisibility(View.GONE);
//		}else {
//			dialog_tv_title.setText(title);
//		}

	}

	public void setOnDialogClickListener(onDialogClickListener listener){
		this.listener = listener;
	}

//	/**
//	 * 设置标题
//	 * @param title
//	 */
//	public void setTitle(String title){
//		dialog_tv_title.setText(title);
//	}
	
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


	public void setNagativeText(String nagative){
		tv_dialog_cancel.setText(nagative);
	}

	public void setPositiveText(String positive){
		tv_dialog_ok.setText(positive);
	}
	
	public void setCancelButtonGone(){
		rl_cancel.setVisibility(View.GONE);
	}

	public void setnumberButtonGone(){
		dialog_tv_change.setVisibility(View.GONE);
	}

	public void setInputNumberVisible(){
		ll_input_dialog.setVisibility(View.VISIBLE);
	}

	public String  getInputNumber(){
		String phone = et_number.getText().toString();
			if(!TextUtils.isEmpty(phone)){
				return phone;
			}
		return null;
	}

	public boolean isValidate(){
		String phone = et_number.getText().toString();
		if(!isPhoneNumberValid(phone)){
			dialog_tv_alert.setVisibility(View.VISIBLE);
			return false;
		}else{
			dialog_tv_alert.setVisibility(View.GONE);
			return true;
		}
	}


	@Override
	public void show() {
		super.show();
	}
	
	public interface onDialogClickListener{
		public void onDialogOkClick();
		public void onDialogCancelClick();
		public void changeNumberClick();
	}



	private boolean isPhoneNumberValid(String phoneNumber) {
		boolean isValid = false;
	/*
	  * 可接受的电话格式有：
      */
		String expression = "^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{5})$";
	/*
      * 可接受的电话格式有：
      */
		String expression2 = "^\\(?(\\d{3})\\)?[- ]?(\\d{4})[- ]?(\\d{4})$";

		if(!TextUtils.isEmpty(phoneNumber)){
			CharSequence inputStr = phoneNumber;
			Pattern pattern = Pattern.compile(expression);
			Matcher matcher = pattern.matcher(inputStr);
			Pattern pattern2 = Pattern.compile(expression2);
			Matcher matcher2 = pattern2.matcher(inputStr);
			if (matcher.matches() || matcher2.matches()) {
				isValid = true;
			}
		}
		return isValid;

	}



	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_ok:
			if(listener!=null){
				if(isValidate())
				listener.onDialogOkClick();

			}


			break;
		case R.id.rl_cancel:
			if(listener!=null){
				listener.onDialogCancelClick();
			}
			break;
			case R.id.dialog_tv_change:
				if(listener!=null){
					listener.changeNumberClick();
				}
				break;
		}
	}
}
