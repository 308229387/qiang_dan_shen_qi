package com.huangyezhaobiao.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.adapter.CallClassifyAdapter;
import com.huangyezhaobiao.bean.mydetail.OrderDetail.CallClassifyEntity;
import com.huangyezhaobiao.callback.DialogCallback;
import com.huangyezhaobiao.utils.ToastUtils;
import com.lzy.okhttputils.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;
import okhttp3.Response;

public class CallClassifyDialog extends Dialog implements View.OnClickListener {
	private Context context;
	private LayoutInflater mInflater;
	private View view;
	private GridView gridView_call_classify;
	private CallClassifyAdapter adapter;
	private EditText et_business_situation;
	private LinearLayout ll_submit_call;

	private onDialogClickListener listener;

	private List<CallClassifyEntity> callList = new ArrayList<>();

	public CallClassifyDialog(Context context,boolean isFirst) {
		super(context, R.style.RequestDialog);
		this.context = context;
		mInflater = LayoutInflater.from(context);
		setCanceledOnTouchOutside(false);
		Window window = getWindow();
		window.setWindowAnimations(R.style.dialogWindowAnim);

		if(isFirst){
			initFirstData();
		}else{
			initAfterData();
		}
		initView(isFirst);
	}

	private void initView(boolean isFirst) {
		view = mInflater.inflate(R.layout.dialog_classify_call, null);
		setContentView(view);
		gridView_call_classify = (GridView) view.findViewById(R.id.gridView_call_classify);
		if (isFirst){
			gridView_call_classify.setNumColumns(3);
		}else{
			gridView_call_classify.setNumColumns(4);
		}
		adapter = new CallClassifyAdapter(context,callList);
		gridView_call_classify.setAdapter(adapter);
		et_business_situation = (EditText) view.findViewById(R.id.et_business_situation);
		ll_submit_call = (LinearLayout) view.findViewById(R.id.ll_submit_call);
		ll_submit_call.setOnClickListener(this);
	}

	public void setOnDialogClickListener(onDialogClickListener listener){
		this.listener = listener;
	}

		public interface onDialogClickListener {
		public void onDialogSubmitClick();

	}

	public String getInputMessage(){
		String message  = et_business_situation.getText().toString();
		if(!TextUtils.isEmpty(message)){
			return message;
		}
		return null;
	}


	@Override
	public void show() {
		super.show();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.ll_submit_call:
				if(listener!=null){
					listener.onDialogSubmitClick();
				}

				break;
		}
	}


	@Override
	public void dismiss() {
		super.dismiss();
	}

	private void initFirstData(){
		callList.clear();
		CallClassifyEntity entity1 = new CallClassifyEntity();
		entity1.setCallClassifyId("11");
		entity1.setCallClassifyImage(R.drawable.detail_not_connected);
		entity1.setCallClassifyName("未打通");
		callList.add(entity1);
		CallClassifyEntity entity2 = new CallClassifyEntity();
		entity2.setCallClassifyId("20");
		entity2.setCallClassifyImage(R.drawable.detail_can_follow_up);
		entity2.setCallClassifyName("可后续跟进");
		callList.add(entity2);
		CallClassifyEntity entity3 = new CallClassifyEntity();
		entity3.setCallClassifyId("90");
		entity3.setCallClassifyImage(R.drawable.detail_ad_and_other);
		entity3.setCallClassifyName("广告及其他");
		callList.add(entity3);
	}

	private void initAfterData(){
		callList.clear();
		CallClassifyEntity entity4 = new CallClassifyEntity();
		entity4.setCallClassifyId("1");
		entity4.setCallClassifyImage(R.drawable.detail_already_traded);
		entity4.setCallClassifyName("已成交");
		callList.add(entity4);
		CallClassifyEntity entity5 = new CallClassifyEntity();
		entity5.setCallClassifyId("2");
		entity5.setCallClassifyImage(R.drawable.detail_not_traded);
		entity5.setCallClassifyName("未成交");
		callList.add(entity5);
		CallClassifyEntity entity6 = new CallClassifyEntity();
		entity6.setCallClassifyId("21");
		entity6.setCallClassifyImage(R.drawable.detail_not_connected);
		entity6.setCallClassifyName("未打通");
		callList.add(entity6);
		CallClassifyEntity entity7 = new CallClassifyEntity();
		entity7.setCallClassifyId("90");
		entity7.setCallClassifyImage(R.drawable.detail_ad_and_other);
		entity7.setCallClassifyName("广告及其他");
		callList.add(entity7);
	}



}
