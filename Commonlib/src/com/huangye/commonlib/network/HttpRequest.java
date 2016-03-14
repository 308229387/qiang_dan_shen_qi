package com.huangye.commonlib.network;
import com.huangye.commonlib.delegate.HttpRequestCallBack;
import com.huangye.commonlib.utils.LogUtils;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
/**
 * 网络请求的封装
 * @author shenzhixin
 *
 */
public class HttpRequest<T> {
	private HashMap<String, String> map = new HashMap<String, String>();
	/**
	 * 以get方式进行请求
	 */
	public static final int METHOD_GET  = 0X00000001;
	/**
	 * 以post方式进行请求
	 */
	public static final int METHOD_POST = 0X00000002;
	private static final int DEFAULT_CONN_TIMEOUT = 0; //默认的连接超时时间
	
	//应该有一个缓存的策略对象----数据库----manager也得有自己的callBack，并且实现http的callBack---也得把结果转成Json
	//cache policy----
	private int timeout = DEFAULT_CONN_TIMEOUT;
	private int method;//请求的方式
	private String url;//请求的路径
	private RequestParams params;//post时请求传递的参数;
	private HTTPTools httpTools;//第三方的封装tools
	private HttpRequestCallBack callBack;//网络层的协议回调，回调到model层
	//超时时间
	private HttpHandler<String> handler;//网路请求返回的控制器，可以控制网络的取消
	public HttpRequest(int method,String url,HttpRequestCallBack callBack){
		this.method = method;
		this.url    = url;
		this.callBack = callBack;
		initEnv();
	}
	
	public void setRequestTimeOut(int timeout){
		this.timeout = timeout;
		httpTools.setRequestTimeOut(timeout);
	}
	
	public HttpRequest() {
		initEnv();
	}
	
	public void setCallBack(HttpRequestCallBack callBack) {
		this.callBack = callBack;
	}

	public void setMethod(int method) {
		this.method = method;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void configParams(HashMap<String, String> params_map){
		map = params_map;
		this.params = new RequestParams();
		Set<Entry<String, String>> entrySet = params_map.entrySet();
		Iterator<Entry<String, String>> iterator = entrySet.iterator();
		while (iterator.hasNext()) {
			  Entry<String, String> next = iterator.next();
			  String key   = next.getKey();
			  String value = next.getValue();
			  LogUtils.LogE("stestUrl", "key.." + key + ",value:" + value + "\n");
			  params.addBodyParameter(key, value);
		}
	}
	
	/**
	 * 对model层暴露的请求网络的方法
	 */
	public void getDatas(){
		//还要有一个判断是从哪获取，如果是从缓存获取，来缓存就行了
		if(method == METHOD_GET){
			get();
			LogUtils.LogE("stestUrl", "get....url:" + url);
		}
		else {
			post();
			LogUtils.LogE("stestUrl", "post....url:" + url);
			if(map!=null){
				Set<Entry<String, String>> entrySet = map.entrySet();
				Iterator<Entry<String, String>> iterator = entrySet.iterator();
				while (iterator.hasNext()) {
					  Entry<String, String> next = iterator.next();
					  String key   = next.getKey();
					  String value = next.getValue();
					  LogUtils.LogE("stestUrl", "key.." + key + ",value:" + value + "\n");
				}
			}
			
		}
		
	}
	
	
	/**
	 * 取消这一次的网络请求
	 */
	public void cancelWork(){
		if(handler!=null && handler.supportCancel())
				handler.cancel();
	}
	

	private void get(){
		handler =  httpTools.doGet(url, callBack);
	}
	
	private void post(){
		LogUtils.LogE("login", "toString:" + params.toString());
		handler =  httpTools.doPost(url, params, callBack);
	}
	
	/**
	 * 必须要初始化的东西
	 */
	private void initEnv() {
		this.params = new RequestParams();
		httpTools = HTTPTools.newHttpUtilsInstance();
		setRequestTimeOut(timeout);
	}
	
}
