package wuba.zhaobiao.grab.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.huangyezhaobiao.bean.push.PushToPassBean;
import com.huangyezhaobiao.utils.LogUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * lib类
 * @author 58
 *
 * @param <T>
 */
public abstract class GrabBaseAdapter<T> extends BaseAdapter{
	protected Map<String, Integer> typeMap = new HashMap<String, Integer>();
	protected List<T> beans = new ArrayList<T>();
	private LayoutInflater mInflater;
	protected Context context;
	protected AdapterListener listener;
	public GrabBaseAdapter(Context context, AdapterListener listener) {
		this.context = context;
		mInflater = LayoutInflater.from(context);
		this.listener = listener;
		transferBeanTypeToAdapterType();
	}
	
	/**
	 * 把bean的类型(58网站的)转化成我们这个adapter的类型识别(系统要求的)
	 */
	public  abstract void transferBeanTypeToAdapterType();
	
	public void refreshSuccess(List<T> beans){
		this.beans = beans;
		LogUtils.LogE("asadffrf", "refresh size:" + this.beans.size());
		listener.onAdapterRefreshSuccess();
		
	}
	
	public void loadMoreSuccess(List<T> beans){
		LogUtils.LogE("asadffrf", "before load more size:" + this.beans.size());
		LogUtils.LogE("asadffrf", "beans size:" + beans.size());
		this.beans=beans;
		LogUtils.LogE("asadffrf", "load more size:" + this.beans.size());
		listener.onAdapterLoadMoreSuccess();
	}
	
	
	public void itemClicked(int id,PushToPassBean bean){
		listener.onAdapterViewClick(id,bean);
	}
	/**
	 * 返回总共有多少种业务类型，这个一定是个确定的值
	 * @return
	 */
	public abstract int getTotalTypeCount();
	
	@Override
	public int getCount() {
		return beans.size();
	}

	@Override
	public Object getItem(int position) {
		return beans.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//LogUtils.LogE("uiuiui", "size:"+this.beans.size());
		T t = initJavaBean(position);
		if(convertView==null){
			convertView = initView(t,convertView,parent,mInflater,context);
		}else{
			converseView(t,convertView,context);  
		}
		fillDatas(t);
		return convertView;
	}

	/**
	 * 初始化时convertView不为空
	 * @param t
	 * @param view
	 * @param parent
	 * @return
	 */
	public abstract View initView(T t,View view,ViewGroup parent,LayoutInflater  inflater,Context context);
	
	/**
	 * convertView复用时
	 * @param t
	 * @param view
	 */
	public abstract void converseView(T t,View view,Context context); 
	
	/**
	 * 填充数据
	 * @param t
	 */
	public abstract void fillDatas(T t);
	/**
	 * 初始化我们需要做的javaBean模型
	 */
	public abstract T initJavaBean(int position);
	
	
	@Override
	public int getItemViewType(int position) {
		return getAdapterItemType(position);
	}

	/**
	 * 得到对应位置的type的值
	 * @param position
	 * @return
	 */
	protected abstract int getAdapterItemType(int position);

	@Override
	public int getViewTypeCount() {
		return getTotalTypeCount();
	}
	
	
	public interface AdapterListener{

		public void onAdapterViewClick(int id, PushToPassBean bean);
	}
}
