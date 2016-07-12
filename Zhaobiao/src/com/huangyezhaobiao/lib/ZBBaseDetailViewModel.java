package com.huangyezhaobiao.lib;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.huangye.commonlib.model.NetWorkModel;
import com.huangye.commonlib.utils.JsonUtils;
import com.huangye.commonlib.utils.NetBean;
import com.huangye.commonlib.vm.SourceViewModel;
import com.huangye.commonlib.vm.callback.NetWorkVMCallBack;
import com.huangyezhaobiao.utils.LogUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 针对详情页使用的ViewModel基类
 * @author linyueyang
 *
 * @param <T> 详情页各模块的基类bean
 */
public abstract class ZBBaseDetailViewModel<T> extends SourceViewModel{
	
	public String key;
	public long orderId;
	/**
	 * 注册的type与类型的对应 左侧是cateId,右侧是这个cateId对应的业务bean的类
	 */
	public Map<String, Class<? extends T>> sourcesDir = new HashMap<String, Class<? extends T>>();

	
	public Context context;
	
	/**
	 * 要转变成的listBean
	 */
	private List<T> beans = new ArrayList<T>();

	public ZBBaseDetailViewModel(NetWorkVMCallBack callBack, Context context) {
		super(callBack, context);
		this.context = context;
		registerSourceDirs();
		initKey();
	}
	
	/**
	 * 注册所有的业务资源
	 */
	protected abstract void registerSourceDirs();
	
	
	/**
	 * 成功的回调函数
	 */
	@Override
	public void onLoadingSuccess(NetBean bean, NetWorkModel model) {
		if(TextUtils.isEmpty(bean.getData())){
			callBack.onLoadingError("订单详情有问题");
			return;
		}
		List<Map<String, String>> a = JsonUtils.jsonToNewListMap(bean.getData());
		List<T> b =transferListMapToListBean(a);
		List<View>  c= transferListBeanToListView(b);
		callBack.onLoadingSuccess(c);
	}
	
	
	
	protected abstract List<View> transferListBeanToListView(List<T> list);
		
		
		
	
	protected List<T> transferListMapToListBean(List<Map<String, String>> t) {
		Log.e("shenzhixinUUU","size:"+t.size());
		beans.clear();
		//为测试不同的数据而使用
		for(Map<String, String> map:t){
			Class<? extends T> classz = sourcesDir.get(map.get(key));
			try {
				if(classz!=null){
					T order = toJavaBean(classz.newInstance(), map);
					beans.add(order);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return beans;
	}

	/**
	 * 将map转换成Javabean
	 * @param javabean
	 *            javaBean
	 * @param data
	 *            map数据
	 */
	private T toJavaBean(T javabean, Map<String, String> data) {
		Method[] methods = javabean.getClass().getDeclaredMethods();
		for (Method method : methods){
			try {
				if (method.getName().startsWith("set")) {
					String field = method.getName();
					field = field.substring(field.indexOf("set") + 3);
					field = field.toLowerCase().charAt(0) + field.substring(1);
					LogUtils.LogE("asdffff", "field:" + field + "aaa:" + data.get(field));
					method.invoke(javabean, new Object[] { data.get(field) });
				}
			}catch (Exception e) {
			}
		}
		return javabean;
	}

	protected abstract void initKey();
	
	

}
