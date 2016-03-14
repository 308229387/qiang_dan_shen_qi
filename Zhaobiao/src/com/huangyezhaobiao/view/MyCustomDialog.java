package com.huangyezhaobiao.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.activity.MainActivity;
import com.huangyezhaobiao.bean.push.PushToPassBean;
import com.huangyezhaobiao.bean.push.pop.PopBaseBean;
import com.huangyezhaobiao.inter.MDConstans;
import com.huangyezhaobiao.utils.LogUtils;
import com.huangyezhaobiao.utils.MDUtils;
import com.huangyezhaobiao.utils.PushUtils;
import com.huangyezhaobiao.voice.VoiceManager;
import com.huangyezhaobiao.voice.VoiceManager.OnVoiceManagerPlayFinished;

/**
 * 用于弹窗的dialog
 * 单例的目的是抢单弹窗只能有一个，避免每次new Dialog 
 * state的作用是 判断是不是第一次弹窗 需不需要显示下一条按钮
 * @author Linyueyang
 *
 */
public class MyCustomDialog extends Dialog {
	private String yuyin_op = MDConstans.ACTION_CLOSE_VOLUMN;

	private VoiceManager voiceManager;
	private Context context;
	public static MyCustomDialog myDialog;

	private OnCustomDialogListener customDialogListener;

	private ImageView voice;
	private ImageView cancel;
	private Button knock;
	private Button next;
	private PopBaseBean bean;
	
	private LinearLayout dialog_linear;
	
	private TextView count;
	private TextView dialog_fee;
	
	private int countDown;

	public static MyCustomDialog getInstance(Context context,OnCustomDialogListener customDialogListener) {
		if (myDialog == null||myDialog.context!=context) {
			LogUtils.LogE("ashenDialog", "dialog create new...");
			myDialog = new MyCustomDialog(context);
		}
		myDialog.context = context;
		myDialog.customDialogListener = customDialogListener;
		return myDialog;
	}

	private MyCustomDialog(Context context) {
		super(context, R.style.MyDialog);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_orderpop);
		dialog_linear = (LinearLayout) findViewById(R.id.dialog_linear);
		cancel = (ImageView) findViewById(R.id.dialog_cancel);
		knock = (Button) findViewById(R.id.dialog_knock);
		next = (Button) findViewById(R.id.dialog_next);
		voice = (ImageView) findViewById(R.id.dialog_voice);
		dialog_fee = (TextView)findViewById(R.id.dialog_fee);
		//展示消息队列的第一条消息
		showFirst();
		cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// 关闭dialog,清空list
				MyCustomDialog.this.dismiss();
				PushUtils.pushList.clear();
				MDUtils.pushWindowPageMD(context, bean.getCateId() + "", bean.toPushPassBean().getBidId() + "", MDConstans.ACTION_MANUAL_CLOSE);

			}
		});
		
		voice.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				voiceManager.clickVolumeButton();
				MDUtils.pushWindowPageMD(context, bean.getCateId()+"", bean.toPushStorageBean()+"", yuyin_op);
				if(MDConstans.ACTION_CLOSE_VOLUMN.equals(yuyin_op)){
					yuyin_op = MDConstans.ACTION_OPEN_VOLUMN;
					voice.setImageResource(R.drawable.t_voice_close);
					
				}else{
					yuyin_op = MDConstans.ACTION_CLOSE_VOLUMN;
					voice.setImageResource(R.drawable.t_voice);
				}
			}
		});
		
		knock.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				customDialogListener.back(bean.toPushPassBean());
				voiceManager.clickQDButton();
				MDUtils.pushWindowPageMD(context, bean.toPushPassBean().getCateId()+"", bean.toPushPassBean().getBidId()+"", MDConstans.ACTION_QIANG_DAN);
			}
		});

	}


	

	//展示当前推送列表的第一条
	private void showFirst() {
		if (null != PushUtils.pushList && PushUtils.pushList.size() > 0) {
			bean = PushUtils.pushList.get(0);
			beanToView();
		} else
			MyCustomDialog.this.dismiss();
	}
	
	/**
	 * 需要实现的
	 */
	private void beanToView() {
		dialog_linear.removeAllViews();
		dialog_linear.addView(bean.initView(context));
		count = (TextView)findViewById(R.id.pop_count);
		countDown = 15;
		countdown();
		dialog_fee.setText("￥"+bean.getFee()+"元");
	}
	
	private void countdown(){
        handler.postDelayed(runnable, 1000);  
	}
	
	Handler handler = new Handler();  
    Runnable runnable = new Runnable() {  
        @Override  
        public void run() {  
        	countDown--;  
            if(countDown>=0){
            	count.setText("" + countDown);  
            	handler.postDelayed(this, 1000);  
            }
            else{
            	if(voiceManager.isSpeakFinish()){
            		handler.removeCallbacks(runnable);
            		showNext();
            	}
            }
        }  
    };  

	//展示下一个按钮
	public void showNextButton() {
		next.setVisibility(View.VISIBLE);
		next.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//这时要把runnable去掉
				handler.removeCallbacks(runnable);
				showNext();
				MDUtils.pushWindowPageMD(context,bean.toPushPassBean().getCateId() + "", bean.toPushPassBean().getBidId() + "",
						MDConstans.ACTION_MANUAL_NEXT_ORDER);
			}
		});
	}



	public void showNext() {
		if(PushUtils.pushList.size()>0) {
			PushUtils.pushList.remove(0);
		}
		showFirst();
		//LogUtils.LogE("hahah", ""+PushUtils.pushList.size());
		if (PushUtils.pushList.size() > 0) {
			if (PushUtils.pushList.size() <= 1)
				next.setVisibility(View.INVISIBLE);
				// 需要改成list的下一条
				voiceManager.addOrder(bean.getVoice());
				voiceManager.manaulToNextOrders();
		}

	}

	@Override
	public void show() {
		try {
			LogUtils.LogE("ashenDialog", "dialog show..213");
			//getInstance(context, customDialogListener);
			if (context == null /*|| !((Activity) context).isTaskRoot()*/){   
				LogUtils.LogE("ashenDialog", "dialog show..216");
				return;// 解决activity不在引起的 dialog异常
			}
			if (PushUtils.pushList.size() == 1) {
				if (null == voiceManager) {
					voiceManager = VoiceManager.getVoiceManagerInstance(context.getApplicationContext());
					voiceManager.setOnPlayFinishedListener(new OnVoiceManagerPlayFinished() {
						@Override
						public void onPlayFinished() {
							if (countDown <= 0)
								showNext();
						}
					});
				}
				LogUtils.LogE("ashenDialog", "dialog show....237");
				 super.show();
				LogUtils.LogE("ashenDialog", "dialog after show....239");
				// nextButtonState = 1;
				MDUtils.pushWindowPageMD(context, bean.toPushPassBean().getCateId() + "",
						bean.toPushPassBean().getBidId() + "", MDConstans.ACTION_PUSH);
				// 让dialog全屏显示
				WindowManager windowManager = ((Activity) context).getWindowManager();
				Display display = windowManager.getDefaultDisplay();
				WindowManager.LayoutParams lp = this.getWindow().getAttributes();
				lp.width = (int) (display.getWidth()); // 设置宽度
				lp.height = (int) (display.getHeight());
				this.getWindow().setAttributes(lp);

				String du = bean.getVoice();

				voiceManager.createOrdersDialog(du);

			} else if (PushUtils.pushList.size() == 2) {
				showNextButton();
			}

	 }catch(Exception e){
		 if(context!=null)
			 Toast.makeText(context, "出錯了.."+e.getMessage(), 0).show();
		 myDialog = null;
		 e.printStackTrace();
		 LogUtils.LogE("ashenDialog", "dialog is null");
	 }
	}


	@Override
	public void dismiss() {
		handler.removeCallbacks(runnable);
		super.dismiss();
		//nextButtonState = 0;
		if(voiceManager!=null){
			voiceManager.closeOrdersDialog();
		}
		voiceManager = null;
		myDialog = null;
		PushUtils.pushList.clear();
		if (context instanceof MainActivity) {
			((MainActivity) context).loadDatas();
		}
		context = null;//avoid memory leak
	}



	public interface OnCustomDialogListener{
		public void back(PushToPassBean bean);
	}

	// private View.OnClickListener clickListener = new View.OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// //customDialogListener.back(String.valueOf(etName.getText()));
	// MyCustomDialog.this.dismiss();
	// }
	// };

}
