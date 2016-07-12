package com.huangyezhaobiao.voice;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.huangyezhaobiao.utils.LogUtils;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * 语音管理器，封装了科大讯飞的语音服务
 * 				1.语音的初始化
 * 				2.语音的播放或者不播放
 * 				3.语音的一条条播放(监听事件)
 *				4.语音的停止，停止后再次播放
 *				5.外观设计模式---有一个科大讯飞语音实例的引用,封装了科大讯飞的语音行为
 * @author shenzhixin
 *
 */
public class VoiceManager {
	private static final String APP_ID = "55bf5315";//申请的appId
	private static VoiceManager voiceManager;
	private Context context;
	private SpeechSynthesizer mTts;//语音说话器
	public static  boolean allowSpeak = true;//是否允许播报
	//线程安全的集合队列,存放
	private List<String> messages = Collections.synchronizedList(new ArrayList<String>());//总队列存放标地、消息

	private List<String> otherMessageList = Collections.synchronizedList(new ArrayList<String>()); //存放消息的队列

	private String message;
	private OnVoiceManagerPlayFinished listener;
	private VoiceManager(){};
	public static VoiceManager getVoiceManagerInstance(Context context){
		if(voiceManager==null){
			synchronized (VoiceManager.class) {
				if(voiceManager == null){
					voiceManager = new VoiceManager();
				}
			} 
		}
		voiceManager.context = context;
		return voiceManager;
	}
	
	/**
	 * 设置播放完毕的监听器
	 * @param listener
	 */
	public void setOnPlayFinishedListener(OnVoiceManagerPlayFinished listener){
		this.listener = listener;
	}
	
	
	/**
	 * 对语音进行初始化操作
	 */
	public void initVoiceManager(Context context){
		SpeechUtility.createUtility(context,SpeechConstant.APPID+"=55bf5315");
		//1.创建SpeechSynthesizer对象, 第二个参数：本地合成时传InitListener  
		if(mTts == null){
			LogUtils.LogE("demo", "init:");
			mTts = SpeechSynthesizer.createSynthesizer(context, null);  
		}
		//2.合成参数设置，详见《科大讯飞MSC API手册(Android)》SpeechSynthesizer 类  
		mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");//设置发音人  
		mTts.setParameter(SpeechConstant.SPEED, "50");//设置语速  
		mTts.setParameter(SpeechConstant.VOLUME, "80");//设置音量，范围0~100  
		mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD); //设置云端

	}
	
	public void speakLocalMessage(String msg){
		//if(mTts!=null && !mTts.isSpeaking()){
			mTts.startSpeaking(msg, null);
		//}
	}


	/**
	 * 说话
	 */
	private void speak(){
	//	mTts.startSpeaking("科大讯飞欢迎您", mSynListener);
		if(messages.size()>0){
			LogUtils.LogE("demo", "speak");
			message = messages.remove(0);
			LogUtils.LogE("demo", "message:" + message);
			mTts.startSpeaking(message, mSynListener);
		}else{
			Toast.makeText(context,"消息列表页:"+messages.size(),Toast.LENGTH_SHORT).show();
		}
	}

	private void respeak(){
		if(mTts!=null && !TextUtils.isEmpty(message)){
			mTts.startSpeaking(message, mSynListener);
		}
	}
	
	/**
	 * 停止说话
	 */
	private void stop(){
		if(mTts!=null && mTts.isSpeaking()){
			mTts.stopSpeaking();
		}
	}


	/**
	 * 创建批次的第一个新订单
	 * @param message
	 */
	public void createOrdersDialog(String message){
		addOrder(message);
		speak();
	}






	/**
	 * 订单窗还在时又来了一条新订单
	 */
	public void addOrder(String message){
		messages.add(message);
	}


	/**
	 * 点击了抢单按钮
	 */
	public void clickQDButton(){
		stop();
	}
	
	/**
	 * 销毁这一批次的订单播报
	 * 点击关闭按钮时
	 */
	public void closeOrdersDialog(){
		stop();
		messages.clear();
	}
	
	/**
	 * 手动到下一条
	 */
	public void manaulToNextOrders(){
		//先停止当前说的,然后播放下一条
		stop();
		speak();
	}
	
	/**
	 * 自动到下一条
	 */
	public void autoToNextOrders(){
		speak();
	}
	
	/**
	 * 点击了音量按钮
	 */
	public void clickVolumeButton(){
		allowSpeak = !allowSpeak;
		if(!allowSpeak){
			stop();
		}else{
			respeak();
		}
	}
	
	/**
	 * 判断有没有播放完毕的方法，完毕返回true,没有返回false
	 * @return
	 */
	public boolean isSpeakFinish(){
		if(mTts!=null) return !mTts.isSpeaking();
		return true;
	}
	
	/**
	 * 说话的监听器
	 */
	private SynthesizerListener mSynListener = new SynthesizerListener(){  
	    //会话结束回调接口，没有错误时，error为null  
	    public void onCompleted(SpeechError error) {
	    	//messages.remove(0);//播送完一条删除一条
	    	//speak();
	    	if(listener!=null)
	    		listener.onPlayFinished();
	    }  
	    //缓冲进度回调  
	    //percent为缓冲进度0~100，beginPos为缓冲音频在文本中开始位置，endPos表示缓冲音频在文本中结束位置，info为附加信息。  
	    public void onBufferProgress(int percent, int beginPos, int endPos, String info) {}  
	    //开始播放  
	    public void onSpeakBegin() {}  
	    //暂停播放  
	    public void onSpeakPaused() {}  
	    //播放进度回调  
	    //percent为播放进度0~100,beginPos为播放音频在文本中开始位置，endPos表示播放音频在文本中结束位置.  
	    public void onSpeakProgress(int percent, int beginPos, int endPos) {}  
	    
	    //恢复播放回调接口  
	    public void onSpeakResumed() {}  
	    //会话事件回调接口  
	    public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {}
	};
	
	public interface OnVoiceManagerPlayFinished{
		/**
		 * 播放完毕的回调方法
		 */
		public void onPlayFinished();
	}
}
