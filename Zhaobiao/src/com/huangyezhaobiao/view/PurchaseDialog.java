package com.huangyezhaobiao.view;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huangyezhaobiao.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PurchaseDialog extends Dialog implements View.OnClickListener{
	private Context context;
	private LayoutInflater mInflater;
	private View view;
	private TextView tv_purchase_content;
	private CheckBox cb_remind;
	private RelativeLayout rl_cancel,rl_ok;
	private TextView tv_dialog_cancel,tv_dialog_ok;
	private onDialogClickListener listener;
	private String content;


	public PurchaseDialog(Context context) {
		super(context,R.style.RequestDialog);
		this.context = context;
		mInflater = LayoutInflater.from(context);
		setCanceledOnTouchOutside(false);
		Window window = getWindow();
		window.setWindowAnimations(R.style.dialogWindowAnim);
		initView();

	}

	private void initView() {
		view = mInflater.inflate(R.layout.dialog_purchase, null);
		setContentView(view);
		tv_purchase_content = (TextView) view.findViewById(R.id.tv_purchase_content);

		cb_remind = (CheckBox) view.findViewById(R.id.cb_remind);

		rl_cancel         = (RelativeLayout) view.findViewById(R.id.rl_cancel);
		rl_ok             = (RelativeLayout) view.findViewById(R.id.rl_ok);
		tv_dialog_cancel  = (TextView) view.findViewById(R.id.tv_dialog_cancel);
		tv_dialog_ok      = (TextView) view.findViewById(R.id.tv_dialog_ok);
		rl_ok.setOnClickListener(this);
		rl_cancel.setOnClickListener(this);
		if(TextUtils.isEmpty(content)){
			tv_purchase_content.setVisibility(View.GONE);
		}else{
			tv_purchase_content.setText(content);
		}

	}

	public void setOnDialogClickListener(onDialogClickListener listener){
		this.listener = listener;
	}

	
	/**
	 * 设置内容
	 * @param content
	 */
	public void setMessage(String content){
		tv_purchase_content.setText(content);
		if(TextUtils.isEmpty(content)){
			tv_purchase_content.setVisibility(View.GONE);
		}else{
			tv_purchase_content.setVisibility(View.VISIBLE);
			tv_purchase_content.setText(content);
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
