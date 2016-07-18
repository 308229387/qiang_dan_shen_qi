package com.huangyezhaobiao.bean.push.pop;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.bean.push.PushToPassBean;
import com.huangyezhaobiao.bean.push.PushToStorageBean;
import com.huangyezhaobiao.utils.LogUtils;

import org.w3c.dom.Text;

/**
 * 装修类别推送bean
 * 
 * @author linyueyang
 *
 */
public class RenovationPopBean extends PopBaseBean {
	private String orderId;//订单的Id
	private int status;// 抢单成功还是失败
	private int cateId;// ", "4063"
	private int displayType;// ":"1"
	private long bidId;// ;//", "12312321"
	private String time;// ", "2015-05-15 18,49"
	private String title;// ", "住宅装修-二手房"
	private String space;// ", "56平米"
	private String budget;// ", "预算3-5万"
	private String endTime;// ", "2015年6月"
	private String location;// ", "朝阳区北苑"
	private String type;// ", "半包"
	private String fee;// ", "300"

	private String needNear; //距离

	private String voice;
	private String originalFee;

	@Override
	public String getOriginalFee() {
		return originalFee;
	}

	public void setOriginalFee(String originalFee) {
		this.originalFee = originalFee;
	}

	@Override
	public View initView(Context context) {
		if(location!=null && location.length()>20){
			location = location.substring(0,19)+"...";
		}
		View view = LayoutInflater.from(context).inflate(R.layout.dialog_pop_renovation, null);
		((TextView) view.findViewById(R.id.renovation_pop_title)).setText(title);
		((TextView) view.findViewById(R.id.renovation_pop_space)).setText(space);

		  LinearLayout ll_distance = ((LinearLayout) view.findViewById(R.id.ll_distance));
		if (!TextUtils.isEmpty(needNear)){
			ll_distance.setVisibility(View.VISIBLE);
			((TextView) view.findViewById(R.id.renovation_pop_distance)).setText(needNear);
		}else{
			ll_distance.setVisibility(View.GONE);
		}


		((TextView) view.findViewById(R.id.renovation_pop_budget)).setText(budget);
		((TextView) view.findViewById(R.id.renovation_pop_type)).setText(type);
		((TextView) view.findViewById(R.id.renovation_pop_time)).setText(endTime);
		((TextView) view.findViewById(R.id.renovation_pop_location)).setText(location);
		((TextView) view.findViewById(R.id.renovation_pop_location)).setText(location);
		return view;
	}

	@Override
	public PushToStorageBean toPushStorageBean() {
		PushToStorageBean bean = new PushToStorageBean();
		Log.e("shenzhixinUUU","orderId:"+orderId);
		try{
		bean.setOrderid(Long.parseLong(orderId));
		}catch(Exception e){
			
		}
		bean.setTag(100);//没什么用,外面又赋值了
		bean.setTime(time);
		LogUtils.LogE("shenzhixinUUU",
				"title:" + title + ",space:" + space + ",type:" + type + "time:" + time + "budget:" + budget + ",");
		// 拼接消息字符串
		StringBuilder str = new StringBuilder();
//		if(title.contains("-")){
//			String[] titles = title.split("-");
//			if(titles.length==2){
//				LogUtils.LogE("asasasas", titles[0] + "," + titles[1]);
//				str.append(titles[0]+" ").append(space + " ").append(budget+" ").append(type + " ").append(titles[1]+" ").append(time + "");
//			}
//		}else{
//			str.append(title + "").append(space + "").append(budget+"").append(type + "").append(time + "");
//		}

		if(location.contains("-")){
			String[] locations = location.split("-");
			if(locations.length == 3){
				str.append(locations[0]+"").append("-").append(locations[1]+"").append("-").append(title + "");
			}else{
				str.append(location+"").append("-").append(title + "");
			}
		}

		//if (status == 1) {
			bean.setFee(fee);
		//}

		bean.setStr(str.toString());
		bean.setStatus(status);
		return bean;
	}
	
	@Override
	public PushToPassBean toPushPassBean(){
		PushToPassBean bean = new PushToPassBean();
		bean.setBidId(bidId);
		bean.setPushId(pushId);
		bean.setPushTurn(pushTurn);
		bean.setCateId(cateId);
		
		
		
		return bean;
	}

	@Override
	public int getCateId() {
		return cateId;
	}
	

	public void setStatus(int status) {
		this.status = status;
	}

	public int getDisplayType() {
		return displayType;
	}

	public void setDisplayType(int displayType) {
		this.displayType = displayType;
	}

	public long getBidId() {
		return bidId;
	}

	public void setBidId(long bidId) {
		this.bidId = bidId;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSpace() {
		return space;
	}

	public void setSpace(String space) {
		this.space = space;
	}

	public String getBudget() {
		return budget;
	}

	public void setBudget(String budget) {
		this.budget = budget;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	public void setCateId(int cateId) {
		this.cateId = cateId;
	}

	public void setVoice(String voice) {
		this.voice = voice;
	}

	@Override
	public String getVoice() {
		return voice;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getNeedNear() {
		return needNear;
	}

	public void setNeedNear(String needNear) {
		this.needNear = needNear;
	}

}
