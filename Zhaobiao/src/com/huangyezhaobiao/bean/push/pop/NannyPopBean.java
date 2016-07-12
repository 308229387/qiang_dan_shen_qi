package com.huangyezhaobiao.bean.push.pop;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.bean.push.PushToPassBean;
import com.huangyezhaobiao.bean.push.PushToStorageBean;
import com.huangyezhaobiao.utils.LogUtils;

/**
 * 这个bean就是新订单和结果的共用的
 * 倒计时和系统通知用别的
 * Created by shenzhixin on 2015/12/7.
 * 保姆的弹窗和titleBar的bean
 *  "voice”:”规则由产品定，服务端拼接”,   //播报内容
    "info":{
     "cateId", "4063"
     "displayType":"5"
    "bidId", "12312321"
    "title", "保姆月嫂"
    “serviceType”:”保姆（住家）” //需求类型
    "employTime":"1个月",      //雇佣时间
    "age":"30-35岁",			  //年龄限制
       "experience":"不限经验", 	  //经验年限
    "location":"北京-朝阳-大山子", //区域
     "startTime":"2015-11-11",	//开始时间
     "fee", "300"
 */
public class NannyPopBean extends PopBaseBean{
    private String orderId;
    private int status;// 抢单结果通知，1代表失败，0代表成功 新订单时不用，结果用
    private int cateId;//":"4065",
    private int displayType;//":"4"
    private long bidId;//":"12312321",
    private String title;
    private String serviceType;
    private String employTime;
    private String age;
    private String experience;
    private String location;
    private String startTime;
    private String fee;
    private String voice;   //新订单时用，结果不用
    private String time;    //推送时间
    private String originalFee;

    private String guestName;

    @Override
    public String getOriginalFee() {
        return originalFee;
    }

    public void setOriginalFee(String originalFee) {
        this.originalFee = originalFee;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getStatus() {
        return status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
        Log.e("shenzhixinUUU","time:"+time);
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setCateId(int cateId) {
        this.cateId = cateId;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getEmployTime() {
        return employTime;
    }

    public void setEmployTime(String employTime) {
        this.employTime = employTime;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public void setVoice(String voice) {
        this.voice = voice;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    @Override
    public View initView(Context context) {
        if(location!=null && location.length()>20){
            location = location.substring(0,19)+"...";
        }
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_pop_nanny,null);
        ((TextView)view.findViewById(R.id.nanny_need_content)).setText(serviceType);
        ((TextView)view.findViewById(R.id.nanny_hire_time)).setText(employTime);
        ((TextView)view.findViewById(R.id.nanny_age)).setText(age);
        ((TextView)view.findViewById(R.id.nanny_experience)).setText(experience);
        ((TextView)view.findViewById(R.id.nanny_start_time)).setText(startTime);
        ((TextView)view.findViewById(R.id.nanny_location)).setText(location);
        return view;
    }

    @Override
    public int getCateId() {
        return cateId;
    }

    @Override
    public String getVoice() {
        return voice;
    }

    @Override
    public String getFee() {
        return fee;
    }

    @Override
    public PushToStorageBean toPushStorageBean() {//titleBar的展示
        PushToStorageBean bean = new PushToStorageBean();
        try{
            bean.setOrderid(Long.parseLong(orderId));
        }catch(Exception e){

        }
        bean.setTag(100);//没什么用,外面又赋值了
        bean.setTime(time);
        // 拼接消息字符串
        StringBuilder str = new StringBuilder();
//        if(title.contains("-")){
//            String[] titles = title.split("-");
//            if(titles.length==2){
//                LogUtils.LogE("asasasas", titles[0] + "," + titles[1]);
//                str.append(titles[0]+" ").append(age + " ").append(experience+" ").append(location + " ").append(titles[1]+" ");
//            }
//        }else{
//            str.append(title + "").append(age + "").append(experience+"").append(location + "");
//        }

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
        bean.setGuestName(guestName);
        return bean;
    }

    @Override
    public PushToPassBean toPushPassBean() {//只是为了埋点
        PushToPassBean bean = new PushToPassBean();
        bean.setBidId(bidId);
        bean.setPushId(pushId);
        bean.setPushTurn(pushTurn);
        bean.setCateId(cateId);
        return bean;
    }
}
